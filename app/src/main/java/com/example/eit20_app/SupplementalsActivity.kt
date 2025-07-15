package com.example.eit20_app

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eit20_app.ui.theme.EIT20_AppTheme

class SupplementalsActivity : ComponentActivity() {
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
            EIT20_AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        // HEADER
                        Row {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                                        top = innerPadding.calculateTopPadding(),
                                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                                        bottom = 0.dp
                                    )
                            ) {
                                Spacer(modifier = Modifier.height(25.dp))
                                Text(
                                    "Supplemental Info",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 35.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                        // BODY
                        Row() {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                val borderWidth = 2.dp
                                val borderColor = Color.White
                                Box(
                                    modifier = Modifier
                                        .height(90.dp)
                                        .width(200.dp)
                                        .drawBehind { // Use drawBehind to custom draw
                                            // Convert Dp to Px for drawing
                                            val borderWidthPx = borderWidth.toPx()

                                            // Draw the bottom border
                                            drawLine(
                                                color = borderColor,
                                                start = Offset(0f, size.height - borderWidthPx / 2),
                                                end = Offset(
                                                    size.width,
                                                    size.height - borderWidthPx / 2
                                                ),
                                                strokeWidth = borderWidthPx
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Active Alerts",
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxHeight()
//                                    .width(2.dp)
//                                    .background(Color.White)
//                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                val borderWidth = 2.dp
                                val borderColor = Color.White
                                Box(
                                    modifier = Modifier
                                        .height(90.dp)
                                        .width(200.dp)
                                        .drawBehind { // Use drawBehind for custom drawing
                                            val borderWidthPx = borderWidth.toPx()

                                            // Draw the bottom border
                                            drawLine(
                                                color = borderColor,
                                                start = Offset(0f, size.height - borderWidthPx / 2),
                                                end = Offset(
                                                    size.width,
                                                    size.height - borderWidthPx / 2
                                                ),
                                                strokeWidth = borderWidthPx
                                            )
                                        },
                                    contentAlignment = Alignment.Center // Center the content vertically and horizontally
                                ) {
                                    Text(
                                        "Exceedances",
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
//                        Column(
//                            modifier = Modifier.height(90.dp),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                        ) {
//                            Text(
//                                "Active Alerts",
//                                fontFamily = FontFamily.Serif,
//                                fontSize = 25.sp
//                            )
//                            Column(
//                                modifier = Modifier.height(90.dp),
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                            ) {
//                                Text(
//                                    "GWT: GROSS WEIGHT",
//                                    fontFamily = FontFamily.Serif,
//                                    fontSize = 15.sp
//                                )
//                                Text(
//                                    "UNCONFIRMED OR OUT",
//                                    fontFamily = FontFamily.Serif,
//                                    fontSize = 15.sp
//                                )
//                                Text(
//                                    "OF RANGE",
//                                    fontFamily = FontFamily.Serif,
//                                    fontSize = 15.sp
//                                )
//                            }
//                        }

                        // FOOTER
                        Column(

                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth() // Make the Column fill the entire screen
                                    .padding(horizontal = 8.dp)
                                    .height(90.dp),
                                //                                .background(Color.Red),
                                horizontalArrangement = Arrangement.Center, // Center items horizontally
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Settings
                                returnHome()

                                Spacer(modifier = Modifier.width(18.dp))
                                Box(
                                    modifier = Modifier
                                        .width(240.dp)  // 384 for my screen
                                        .height(100.dp)
                                        .border(BorderStroke(2.dp, Color.White)),
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Column {
                                            Text(
                                                "GWT",
                                                fontSize = 30.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Yellow
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}