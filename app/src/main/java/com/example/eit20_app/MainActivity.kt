package com.example.eit20_app

import android.app.AlertDialog
import android.content.Intent
import androidx.compose.material3.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eit20_app.ui.theme.EIT20_AppTheme
import android.content.Context
import android.media.AudioManager

val showDevelopmentAlert = mutableStateOf(false)
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔁 Reapply saved brightness here
        val prefs = getSharedPreferences("slider_prefs", Context.MODE_PRIVATE)
        val savedBrightness = prefs.getFloat("brightness_slider", 0.5f)
        val savedVolume = prefs.getFloat("volume_slider", 0.5f)

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val calculatedVolume = (savedVolume * maxVolume).toInt()

        val layoutParams = window.attributes
        layoutParams.screenBrightness = savedBrightness
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, calculatedVolume, 0)
        window.attributes = layoutParams
        
        enableEdgeToEdge()
        setContent {
            EIT20_AppTheme{
                AlertBox()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .background(Color.Black)
                            .fillMaxSize(),
                    ) {
                        // HEADER
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                                    top = innerPadding.calculateTopPadding(),
                                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                                    bottom = 0.dp
                                ),
//                                .background(Color.Green),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Spacer(modifier = Modifier.height(15.dp))
                            ButtonRow()
                            Spacer(modifier = Modifier.height(15.dp))
                        }

                        // BODY
                        ChosenHeaderColumn()
                        // FOOTER
                        Column(

                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth() // Make the Column fill the entire screen
                                    .padding(horizontal = 8.dp)
                                    .height(90.dp),
//                                .background(Color.Red),
                            ){
                                // Settings
                                SettingBtn()

                                Box(
                                    modifier = Modifier
                                        .width(140.dp)  // 384 for my screen
                                        .height(LocalConfiguration.current.screenWidthDp.dp / 3)
                                        .border(BorderStroke(2.dp, Color.White)),
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        Column{
                                            Text(
                                                "FRZ",
                                                fontSize = 30.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ){
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
                                // Info Supplemental
                                SupplementalBtn()
                            }
                        }
                    }
                }
            }
        }
    }

//    private fun selectedBtn() {
//        showDevelopmentAlert.value = true // 1.2.2, 1.3.2
//    }
}