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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eit20_app.ui.theme.EIT20_AppTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔁 Reapply saved brightness here
        val prefs = getSharedPreferences("slider_prefs", Context.MODE_PRIVATE)
        val savedBrightness = prefs.getFloat("brightness_slider", 0.5f)
        val savedVolume = prefs.getFloat("volume_slider", 0.5f)

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val calculatedVolume = (savedVolume * maxVolume).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, calculatedVolume, 0)

        val layoutParams = window.attributes
        layoutParams.screenBrightness = savedBrightness
        window.attributes = layoutParams

        enableEdgeToEdge()
        setContent {
            EIT20_AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // SLIDER HEADER
                        SettingHeader()

                        // BODY SETTING BUTTONS
                        Column(

                        ){
                            Spacer(modifier = Modifier.height(25.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        // Nothing
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Blue,
                                        contentColor = Color.White,
                                    ),
                                    border = BorderStroke(2.dp, Color.White),
                                    shape = CutCornerShape(7.dp),
                                    modifier = Modifier.size(width = 70.dp, height = 70.dp)
                                ) {
                                }
                                Text(
                                    "DOOR(S) OFF",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .offset(x = 10.dp)
                                        .width(90.dp)
                                )
                                Spacer(modifier = Modifier.width(18.dp))
                                OutlinedButton(
                                    onClick = {
                                        // Nothing
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Blue,
                                        contentColor = Color.White,
                                    ),
                                    border = BorderStroke(2.dp, Color.White),
                                    shape = CutCornerShape(7.dp),
                                    modifier = Modifier
                                        .size(width = 70.dp, height = 70.dp)
                                ) {
                                }
                                Text(
                                    "FIXED FLOATS",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .offset(x = 10.dp)
                                        .width(90.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(25.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        // Nothing
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Blue,
                                        contentColor = Color.White,
                                    ),
                                    border = BorderStroke(2.dp, Color.White),
                                    shape = CutCornerShape(7.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.size(width = 70.dp, height = 70.dp)
                                ) {
                                    Image(
                                        painterResource(id = R.drawable.weight_icon),
                                        contentDescription = "Volume",
                                        modifier = Modifier
                                            .size(width = 35.dp, height = 35.dp)
                                    )
                                }
                                Text(
                                    "GWT 2400 LB",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .offset(x = 10.dp)
                                        .width(90.dp)
                                )
                                Spacer(modifier = Modifier.width(18.dp))
                                OutlinedButton(
                                    onClick = {
                                        // Nothing
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Blue,
                                        contentColor = Color.White,
                                    ),
                                    border = BorderStroke(2.dp, Color.White),
                                    shape = CutCornerShape(7.dp),
                                    modifier = Modifier
                                        .size(width = 70.dp, height = 70.dp)
                                ) {
                                }
                                Text(
                                    "OPERATION ON WATER",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .offset(x = 10.dp)
                                        .width(100.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(25.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        // Nothing
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Blue,
                                        contentColor = Color.White,
                                    ),
                                    border = BorderStroke(2.dp, Color.White),
                                    shape = CutCornerShape(7.dp),
                                    modifier = Modifier.size(width = 70.dp, height = 70.dp)
                                ) {
                                }
                                Text(
                                    "POPOUTS DEPLOYED",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .offset(x = 10.dp)
                                        .width(90.dp)
                                )
                                Spacer(modifier = Modifier.width(18.dp))
                                OutlinedButton(
                                    onClick = {
                                        // Nothing
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Blue,
                                        contentColor = Color.White,
                                    ),
                                    border = BorderStroke(2.dp, Color.White),
                                    shape = CutCornerShape(7.dp),
                                    modifier = Modifier
                                        .size(width = 70.dp, height = 70.dp)
                                ) {
                                }
                                Text(
                                    "POPOUTS ARMED",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .offset(x = 10.dp)
                                        .width(100.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(90.dp))

                        // FOOTER
                        Column(

                        ){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth() // Make the Column fill the entire screen
                                    .padding(horizontal = 8.dp)
                                    .height(90.dp),
//                                .background(Color.Red),
                                horizontalArrangement = Arrangement.Center, // Center items horizontally
                                verticalAlignment = Alignment.CenterVertically
                            ){
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
                                    ){
                                        Column{
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