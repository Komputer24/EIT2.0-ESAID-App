package com.example.eit20_app

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                    Column (
                        modifier = Modifier
                            .background(Color.Black)
                            .fillMaxSize(),
                    ){
                        // HEADER
                        Column {
                            Row {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = innerPadding.calculateStartPadding(
                                                LayoutDirection.Ltr
                                            ),
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
                            Row (){
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
                                                    start = Offset(
                                                        0f,
                                                        size.height - borderWidthPx / 2
                                                    ),
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
                                Box(
                                    modifier = Modifier
                                        .height(90.dp)
                                        .width(2.dp)
                                        .background(Color.White)
                                )
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
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
                                                    start = Offset(
                                                        0f,
                                                        size.height - borderWidthPx / 2
                                                    ),
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
                        }

                        // BODY
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row {
                                // First Column with scroll
                                Box(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .heightIn(max = 500.dp), // Limit height to enable scrolling inside
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .width(180.dp)
                                            .padding(vertical = 10.dp)
                                            .verticalScroll(rememberScrollState()) // Scroll inside this column only
                                    ) {
                                        // TEXT HERE
                                        Text("GWT: GROSS WEIGHT", fontFamily = FontFamily.Serif, fontSize = 13.sp, textAlign = TextAlign.Center)
                                        Text("UNCONFIRMED OR OUT", fontFamily = FontFamily.Serif, fontSize = 13.sp, textAlign = TextAlign.Center)
                                        Text("OF RANGE", fontFamily = FontFamily.Serif, fontSize = 13.sp, textAlign = TextAlign.Center)
//                                        repeat(25){
//                                            Text(
//                                                "XXXXXXXXXXXXXXXX",
//                                                fontFamily = FontFamily.Serif,
//                                                fontSize = 13.sp,
//                                                textAlign = TextAlign.Center
//                                            )
//                                        }
                                    }
                                }

                                // Vertical divider
                                Box(
                                    modifier = Modifier
                                        .height(500.dp) // Match height with scrollable box
                                        .width(2.dp)
                                        .background(Color.White)
                                )

                                // Second Column with scroll
                                // Second Column with fixed button and scrollable content above it
                                Box(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .heightIn(max = 500.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .width(180.dp)
                                            .padding(vertical = 10.dp)
                                            .fillMaxHeight(),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        // Scrollable content
                                        Column(
                                            modifier = Modifier
                                                .weight(1f) // Take up all remaining space
                                                .verticalScroll(rememberScrollState())
                                                .fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
//                                            repeat(25) {
//                                                Text(
//                                                    "XXXXXXXXXXXXXXXX",
//                                                    fontFamily = FontFamily.Serif,
//                                                    fontSize = 13.sp,
//                                                    textAlign = TextAlign.Center
//                                                )
//                                            }
                                        }

                                        // Fixed bottom-left button
                                        OutlinedButton(
                                            onClick = {
                                                // XXX
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Blue,
                                                contentColor = Color.White,
                                            ),
                                            border = BorderStroke(2.dp, Color.White),
                                            shape = CutCornerShape(7.dp),
                                            modifier = Modifier
                                                .padding(start = 8.dp, top = 8.dp) // some spacing from scrollable content
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    "CLR",
                                                    fontFamily = FontFamily.Serif,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    "EXC",
                                                    fontFamily = FontFamily.Serif,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }


                            }
                        }

                        // FOOTER
                        ReturnFooter()
                    }
                }
            }
        }
    }
}