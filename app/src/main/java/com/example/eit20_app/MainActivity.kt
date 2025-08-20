package com.example.eit20_app

import android.Manifest
import android.bluetooth.*
import android.content.*
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.eit20_app.ui.theme.EIT20_AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.*

val selectedIndex = mutableStateOf(2)  // -1 = none selected

val flightDisplay = 1440
val ft = 1400
val kts = 35
val inhg = 16.0
val boxWidth = 326
// Conversion
val ftScale = (((ft+ 9999).toDouble() / 109998) * boxWidth)-22  // -9999 to 99999 or 0 to 109998
val ktsScale = (((kts - 20).toDouble() / 130) * boxWidth)-22     // 20 to 150 or 0 to 130
val inhgScale = ((inhg.toDouble() / 32) * boxWidth)-22         // 0 to 32
val TARGET_DEVICE_MAC = "84:C6:92:56:0B:BD"
val TARGET_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // common SPP UUID

val connectionStatus = mutableStateOf("Not connected")
fun checkSppStatus(): Boolean = connectionStatus.value == "Connected"

class MainActivity : ComponentActivity() {

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private var connectThread: ConnectThread? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private val TAG = "CAN2BT"

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        if (it.address == TARGET_DEVICE_MAC) {
                            if (ActivityCompat.checkSelfPermission(
                                    this@MainActivity,
                                    Manifest.permission.BLUETOOTH_SCAN
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                bluetoothAdapter?.cancelDiscovery()
                            } else {
                                ActivityCompat.requestPermissions(
                                    this@MainActivity,
                                    arrayOf(
                                        Manifest.permission.BLUETOOTH_SCAN,
                                        Manifest.permission.BLUETOOTH_CONNECT
                                    ),
                                    1002
                                )
                            }
                            connectToDevice(it)
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Toast.makeText(this@MainActivity, "Scan finished", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val btStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val state = intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            when(state) {
                BluetoothAdapter.STATE_OFF -> {
                    connectionStatus.value = "Disconnected"
                    connectThread?.cancel()
                }
                BluetoothAdapter.STATE_ON -> {
                    // Bluetooth is back on — reconnect immediately
                    connectThread?.cancel() // cancel any stale connection
                    if (ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val pairedDevice = bluetoothAdapter?.bondedDevices
                            ?.find { it.address == TARGET_DEVICE_MAC }

                        pairedDevice?.let {
                            connectToDevice(it)
                        } ?: startBluetoothScan()
                    }
                }
            }
        }
    }



    override fun onStart() {
        super.onStart()
        registerReceiver(btStateReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    override fun onStop() {
        super.onStop()
        try { unregisterReceiver(btStateReceiver) } catch (_: Exception) {}
    }

    private fun startBluetoothScan() {
        if (bluetoothAdapter == null) return
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_SCAN),
                1002
            )
            return
        }
        if (bluetoothAdapter!!.isDiscovering) bluetoothAdapter!!.cancelDiscovery()
        registerReceiver(bluetoothReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        registerReceiver(bluetoothReceiver, IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
        bluetoothAdapter!!.startDiscovery()
        Toast.makeText(this, "Scanning for devices…", Toast.LENGTH_SHORT).show()
    }

    private fun connectToDevice(device: BluetoothDevice) {
        connectThread = ConnectThread(device)
        connectThread?.start()
    }

    private inner class ConnectThread(private val device: BluetoothDevice) : Thread() {
        private var socket: BluetoothSocket? = null

        override fun run() {
            try {
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    runOnUiThread { connectionStatus.value = "Permission missing" }
                    return
                }

                socket = device.createRfcommSocketToServiceRecord(TARGET_UUID)
                bluetoothAdapter?.cancelDiscovery()

                // Set status to "Connecting" before attempting connection
                runOnUiThread { connectionStatus.value = "Connecting" }

                socket?.connect()

                inputStream = socket?.inputStream
                outputStream = socket?.outputStream

                runOnUiThread { connectionStatus.value = "Connected" }

                startReadingData()
                monitorConnection() // start monitoring socket health
            } catch (e: IOException) {
                runOnUiThread { connectionStatus.value = "Connection failed" }
                Log.e(TAG, "Could not connect", e)
                try { socket?.close() } catch (_: IOException) {}
            }
        }

        fun cancel() {
            try { socket?.close() } catch (e: IOException) { Log.e(TAG, "Could not close socket", e) }
        }
    }

    var canFrame = mutableStateListOf<String>()  // this will hold every CAN frame
    private val frames = listOf(
        ":S7FFN0001000F41CCE148;",
        ":S7FFN0004398271DF7FFF;",
        ":S7FFN00057FFFFFFF7FFF;",
        ":S7FFN000641EEACF741ED;",
        ":S7FFN000741ED9F8A7FFF;",
        ":S7FFN00087FFFFFFF3982;",
        ":S7FFN00097FFFFFFF0000;",
        ":S7FFN000A7FFFFFFF434A;",
        ":S7FFN0015434A96264037;",
        ":S7FFN000B4037819A7FFF;",
        ":S7FFN000C0000000F41CC;",
        ":S7FFN000D00000000BCA0;",
        ":S7FFN000EBCA000003F7F;",
        ":S7FFN000F3F7F00007FFF;",
        ":S7FFN00107FFFFFFF7FFF;",
        ":S7FFN00117FFFFFFF017C;",
        ":S7FFN00254128286C6A93;",
        ":S7FFN0024014000010008;",
        ":S7FFN0002495500160620;",
        ":S7FFN0003062020250001;",
        ":S7FFN00167FFFFFFF7FFF;",
        ":S7FFN00177FFFFFFF00AE;",
        ":S7FFN00137FFFFFFF0000;",
        ":S7FFN00147FFFFFFF7FFF;",
        ":S7FFN001841CCE1480000;"
    )

    fun sendFrames() {
        Thread {
            try {
                frames.forEach { frame ->
                    outputStream?.write(frame.toByteArray(Charsets.UTF_8))
                    outputStream?.flush()
                    Log.d(TAG, "Sent frame: $frame")

                    // Append frame to the Compose-friendly list
                    runOnUiThread {
                        canFrame.add(frame)
                    }

                    Thread.sleep(50) // small delay between frames
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error sending frames", e)
            }
        }.start()
    }

    private fun startReadingData() {
        Thread {
            try {
                val reader = BufferedReader(InputStreamReader(inputStream!!, Charsets.UTF_8))

                // First line from device — likely "CAN-BT-APPL"
                val firstLine = reader.readLine()
                firstLine?.takeIf { it.isNotBlank() }?.let { line ->
                    runOnUiThread { canFrame.add(line) }
                }

                // Now start sending frames after greeting received
                sendFrames()

                // Read the rest of incoming frames
                while (true) {
                    val line = reader.readLine() ?: break
                    line.split(";").forEach { frame ->
                        val trimmedFrame = frame.trim()
                        if (trimmedFrame.isNotEmpty()) {
                            runOnUiThread { canFrame.add(trimmedFrame) }
                        }
                    }
                }

            } catch (e: IOException) {
                runOnUiThread { connectionStatus.value = "Disconnected" }
            }
        }.start()
    }

    private fun monitorConnection() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    outputStream?.write(byteArrayOf(0)) // heartbeat
                    kotlinx.coroutines.delay(2000)
                } catch (e: IOException) {
                    connectionStatus.value = "Disconnected"
                    break
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported.", Toast.LENGTH_SHORT).show()
            return
        }

        enableEdgeToEdge()

        setContent {
            EIT20_AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .background(Color.Black)
                            .fillMaxSize()
                            .padding(
                                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                                top = innerPadding.calculateTopPadding(),
                                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                                bottom = 0.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val status by connectionStatus
                        Text(
                            text = "SPP Status: $status",
                            color = when(status) {
                                "Connected" -> Color.Green
                                "Connecting" -> Color.Yellow
                                else -> Color.Red
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )


                        Spacer(modifier = Modifier.height(5.dp))
                        ButtonRow()
                        Spacer(modifier = Modifier.height(15.dp))

                        val scrollState = rememberScrollState()

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(30.dp)
                                .verticalScroll(scrollState)
                                .border(1.dp, Color.White),
                        ) {
                            Column {
                                canFrame.forEach { frame ->
                                    Text(
                                        text = frame.trim(),         // remove any leading/trailing spaces
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        modifier = Modifier.fillMaxWidth() // make text span full width
                                    )
                                }
                            }
                        }

                        ChosenHeaderColumn()
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                                    .height(90.dp),
                            ) {
                                SettingBtn()
                                Box(
                                    modifier = Modifier
                                        .width(140.dp)
                                        .height(LocalConfiguration.current.screenWidthDp.dp / 3)
                                        .border(BorderStroke(2.dp, Color.White)),
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Column {
                                            Text(
                                                "FRZ",
                                                fontSize = 30.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                "SLIP",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Red
                                            )
                                            Text(
                                                "EHI",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Red
                                            )
                                        }
                                    }
                                }
                                SupplementalBtn()
                            }
                        }
                    }

                    CheckSppConnection()
                }

                // Attempt connection in the background
                LaunchedEffect(Unit) {
                    if (ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val pairedDevice = try {
                            bluetoothAdapter?.bondedDevices?.find { it.address == TARGET_DEVICE_MAC }
                        } catch (e: SecurityException) {
                            null
                        }

                        if (pairedDevice != null) {
                            connectToDevice(pairedDevice)
                        } else {
                            startBluetoothScan()
                        }
                    } else {
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                            1001
                        )
                        connectionStatus.value = "Permission required"
                    }
                }
            }
        }
    }

    @Composable
    private fun CheckSppConnection() {
        val status by connectionStatus
        if (status != "Connected" && status != "Connecting") {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("SPP Connection Failed") },
                text = { Text("You must pair and connect to the CAN-BT via Bluetooth") },
                confirmButton = {}
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try { unregisterReceiver(bluetoothReceiver) } catch (_: Exception) {}
        connectThread?.cancel()
    }
}