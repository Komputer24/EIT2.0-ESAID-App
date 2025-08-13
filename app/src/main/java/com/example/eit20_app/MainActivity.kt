package com.example.eit20_app

import android.Manifest
import android.bluetooth.*
import android.content.*
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.eit20_app.ui.theme.EIT20_AppTheme
import java.io.IOException
import java.util.*

val selectedIndex = mutableStateOf(1)  // -1 = none selected

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

val bluetoothEnabled = mutableStateOf(
    BluetoothAdapter.getDefaultAdapter()?.isEnabled == true
)

class MainActivity : ComponentActivity() {

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private var connectThread: ConnectThread? = null

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        if (it.address == TARGET_DEVICE_MAC) {
                            // Stop discovery once found
                            if (ActivityCompat.checkSelfPermission(
                                    this@MainActivity,
                                    Manifest.permission.BLUETOOTH_SCAN
                                ) == PackageManager.PERMISSION_GRANTED
                                || ActivityCompat.checkSelfPermission(
                                    this@MainActivity,
                                    Manifest.permission.BLUETOOTH_CONNECT
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                bluetoothAdapter?.cancelDiscovery()
                            } else {
                                // Request permission or handle lack of permission gracefully
                                ActivityCompat.requestPermissions(
                                    this@MainActivity,
                                    arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT),
                                    1002
                                )
                            }
                            Toast.makeText(
                                this@MainActivity,
                                "Found target device: ${it.name} (${it.address})",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Connect to the target device
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

    private fun startBluetoothScan() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            return
        }
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

        registerReceiver(
            bluetoothReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )
        registerReceiver(
            bluetoothReceiver,
            IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        )

        bluetoothAdapter!!.startDiscovery()
        Toast.makeText(this, "Scanning for devices…", Toast.LENGTH_SHORT).show()
    }

    private fun connectToDevice(device: BluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                1003
            )
            return
        }

        connectThread = ConnectThread(device)
        connectThread?.start()
    }

    private inner class ConnectThread(private val device: BluetoothDevice) : Thread() {

        private var socket: BluetoothSocket? = null

        override fun run() {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted, abort connection or request permission elsewhere before starting this thread
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Bluetooth permission required", Toast.LENGTH_SHORT).show()
                }
                return
            }

            // Safe to create socket now
            socket = device.createRfcommSocketToServiceRecord(TARGET_UUID)

            // Safe to cancel discovery now
            bluetoothAdapter?.cancelDiscovery()

            try {
                socket?.connect()
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Connected to device ${device.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // TODO: Manage connected socket (read/write data)
            } catch (e: IOException) {
                Log.e("Bluetooth", "Could not connect to device", e)
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Connection failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                try {
                    socket?.close()
                } catch (closeException: IOException) {
                    Log.e("Bluetooth", "Could not close socket", closeException)
                }
            }
        }

        fun cancel() {
            try {
                socket?.close()
            } catch (e: IOException) {
                Log.e("Bluetooth", "Could not close socket", e)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported on this device.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val prefs = getSharedPreferences("slider_prefs", MODE_PRIVATE)
        val savedBrightness = prefs.getFloat("brightness_slider", 0.5f)
        val savedVolume = prefs.getFloat("volume_slider", 0.5f)

        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val calculatedVolume = (savedVolume * maxVolume).toInt()

        val layoutParams = window.attributes
        layoutParams.screenBrightness = savedBrightness
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, calculatedVolume, 0)
        window.attributes = layoutParams

        enableEdgeToEdge()
        setContent {
            EIT20_AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .background(Color.Black)
                            .fillMaxSize(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                                    top = innerPadding.calculateTopPadding(),
                                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                                    bottom = 0.dp
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))
                            ButtonRow()
                            Spacer(modifier = Modifier.height(15.dp))
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
                }
                BluetoothStateListener()
                launchBluetoothScan(bluetoothEnabled)
            }
        }
    }

    @Composable
    private fun launchBluetoothScan(bluetooth: MutableState<Boolean>) {
        if (bluetooth.value) {
            startBluetoothScan()
        } else {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Bluetooth Required") },
                text = { Text("You must enable Bluetooth to continue") },
                confirmButton = {}
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(bluetoothReceiver)
        } catch (_: Exception) {}
        connectThread?.cancel()
    }
}

@Composable
fun BluetoothStateListener() {
    val context = LocalContext.current
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    DisposableEffect(Unit) {
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val state = intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                bluetoothEnabled.value = state == BluetoothAdapter.STATE_ON
            }
        }
        context.registerReceiver(receiver, filter)
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
}
