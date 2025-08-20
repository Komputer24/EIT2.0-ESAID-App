package com.example.eit20_app

import android.app.Activity
import android.content.ContextWrapper
import android.media.MediaPlayer
import android.view.WindowManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.mutableStateOf
import java.time.format.TextStyle
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect // Import LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun ButtonRow() {
    val buttonLabels = listOf("INFO", "RAD-ALT", "FLIGHT")

    Row {
        buttonLabels.forEachIndexed { index, label ->
            OutlinedButton(
                onClick = { selectedIndex.value = index },
                modifier = Modifier
                    .size(width = 115.dp, height = 60.dp)
                    .padding(end = if (index == buttonLabels.size - 2) 0.dp else 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedIndex.value == index) Color.White else Color.Blue,
                    contentColor = if (selectedIndex.value == index) Color.Blue else Color.White,
                ),
                border = if (selectedIndex.value == index) BorderStroke(2.dp, Color.Blue) else BorderStroke(2.dp, Color.White),
                shape = CutCornerShape(10.dp),
            ) {
                Text(text = label, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ChosenHeaderColumn(){
    if(selectedIndex.value == 0){
        Column(
            modifier = Modifier.height(543.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfoInfo(if(checkSppStatus()) {25.0} else {0}, "°C", "OAT")
            InfoInfo(if(checkSppStatus()) {2400} else {0}, "LB", "GWT")
            InfoInfo(if(checkSppStatus()) {1400} else {0}, "FT", "DA")

            Divider(
                color = Color.White,
                thickness = 3.dp,
                modifier = Modifier.padding(vertical = 5.dp)
            )

            InfoInfo(if(checkSppStatus()) {120} else {0}, "KTS", "VNE")
            InfoInfo(if(checkSppStatus()) {22.9} else {0}, "IN-HG", "MCP")
            InfoInfo(if(checkSppStatus()) {6900} else {0}, "DA-FT", "OGE")
            InfoInfo(if(checkSppStatus()) {25.7} else {0}, "IN-HG", "SMP")
            InfoInfo(if(checkSppStatus()) {11300} else {0}, "DA-FT", "IGE")
        }
    }
    else if(selectedIndex.value == 1){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(543.dp), // Only this part gets 566.dp height
            contentAlignment = Alignment.Center // Centers children vertically and horizontally by default
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (flightDisplay < 100000 && flightDisplay > -10000 && checkSppStatus()) "$flightDisplay" else "ERR",
                        fontSize = 130.sp,
                    )
                    Column {
                        Text(
                            text = "FT",
                            fontSize = 15.sp,
                        )
                        Text(
                            text = "RA",
                            fontSize = 20.sp,
                        )
                    }
                }
//                Row(){
//                    var username by remember { mutableStateOf("") }
//
//                    TextField(
//                        value = username,
//                        onValueChange = { username = it },
//                        placeholder = { Text("Email or username" ) },
//                        shape = RoundedCornerShape(25.dp),
//                        colors = TextFieldDefaults.colors( // Use the colors parameter
//                            focusedContainerColor = Color(0xFF85AAB3), // Use this for when the TextField is focused
//                            unfocusedContainerColor = Color(0xFF85AAB3), // Use this for when the TextField is not focused
//                            focusedTextColor = Color.White, // Use this for the focused text color
//                            unfocusedTextColor = Color.White, // Use this for the unfocused text color
//                            focusedIndicatorColor = Color.Transparent, // Remove underline when focused
//                            unfocusedIndicatorColor = Color.Transparent, // Remove underline when unfocused
//                            disabledIndicatorColor = Color.Transparent,
//                            focusedPlaceholderColor = Color.White, // Set placeholder color when focused
//                            unfocusedPlaceholderColor = Color.White
//                        ),
//                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp)
//                    )
//                }
            }
        }
    }
    else if(selectedIndex.value == 2) {
        Column(
            modifier = Modifier
//                              .background(Color.Yellow)
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Row(
//                                modifier = Modifier
//                                    .background(Color.Red),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (flightDisplay < 100000 && flightDisplay > -10000 && checkSppStatus()) "${flightDisplay}" else "ERR",
                    fontSize = 80.sp,
                )
                Column(
                    modifier = Modifier
//                                        .background(Color.Cyan),
                ){
                    Text(
                        text = "FT",
                        fontSize = 15.sp,
                    )
                    Text(
                        text = "RA",
                        fontSize = 20.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Box(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp)  // 384 for my screen
                    .height(LocalConfiguration.current.screenWidthDp.dp / 3)
                    .border(BorderStroke(2.dp, Color.White))
                    .background(Color.Black),
            ) {
                Column{
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .clip(RectangleShape)
                    ){
                        Image(
                            painterResource(id = R.drawable.indicator_icon),
                            contentDescription = "indicator",
                            modifier = Modifier
                                .offset(x = (ftScale).dp) // Control where indicator points from 0 to 364
                        )
                    }
                    //  Scale(181, 236, 261, 0, "ft") // Out of 362
                    Scale(45000,61709,69308,-9999, "ft") // -9999 to 99999 or 0 to 109998
                    //Scale(300,0,0,0, "ft") // -9999 to 99999 or 0 to 109998
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ){
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if(ft< 100000 && ft> -10000 && checkSppStatus()) "${ft}" else "ERR", // (-9999 FT - 99999 FT)
                                fontSize = 30.sp,
                            )
                            Text(
                                text = "FT",
                                fontSize = 15.sp,
                            )
                            VerticalDivider(
                                color = Color.White,
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Text(
                                text = "DA",
                                fontSize = 20.sp,
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp)  // 384 for my screen
                    .height(LocalConfiguration.current.screenWidthDp.dp / 3)
                    .border(BorderStroke(2.dp, Color.White))
                    .clip(RectangleShape)
                    .background(Color.Black),
            ) {
                Column{
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                    ){
                        Image(
                            painterResource(id = R.drawable.indicator_icon),
                            contentDescription = "indicator",
                            modifier = Modifier
                                .offset(x = (ktsScale).dp) // Control where indicator points from 0 to 364
                        )
                    }
                    // Scale(272,0,0, 240, "kts")106
                    Scale(118,0,0, 106, "kts") // 20 to 150 or 0 to 130
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ){
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if(kts < 151 && kts > 19 && checkSppStatus()) "${kts}" else "ERR", // (20 KTS - 150 KTS)
                                fontSize = 30.sp,
                            )
                            Text(
                                text = "KTS",
                                fontSize = 15.sp,
                            )
                            VerticalDivider(
                                color = Color.White,
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Text(
                                text = "IAS",
                                fontSize = 20.sp,
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp)  // 384 for my screen
                    .height(LocalConfiguration.current.screenWidthDp.dp / 3)
                    .border(BorderStroke(2.dp, Color.White))
                    .clip(RectangleShape)
                    .background(Color.Black),
            ) {
                Column{
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                    ){
                        Image(
                            painterResource(id = R.drawable.indicator_icon),
                            contentDescription = "Indicator",
                            modifier = Modifier
                                .offset(x = (inhgScale).dp) // Control where indicator points from 0 to 364
                        )
                    }
                    //Scale(260,0,300, 0, "inhg") // Out of 362
                    Scale(24,0,26, 0, "inhg") // 0 to 32
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ){
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if(inhg < 33.0 && inhg >= 0.0 && checkSppStatus()) "${inhg}" else "ERR", //  (0 IN-HG - 32 IN-HG)
                                fontSize = 30.sp,
                            )
                            Text(
                                text = "IN-HG",
                                fontSize = 15.sp,
                            )
                            VerticalDivider(
                                color = Color.White,
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Text(
                                text = "MAP",
                                fontSize = 20.sp,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
fun Scale(greenMark: Number, yellowHatchedMark: Number, yellowMark: Number, redHatchedMark: Number, type: String){
    var greenWidth: Number = 0
    var yellowHatchedWidth: Number = 0
    var yellowWidth: Number = 0
    var redHatchedPos: Number = 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
    ) {
        Row{
            //Marker
            //        val markerValues = listOf(greenMark, yellowHatchedMark, yellowMark, redHatchedMark)
            //        markerValues.forEach { marker ->
            //            if (marker != 0) {
            //                VerticalDivider(
            //                    color = Color.Blue,
            //                    thickness = 1.dp,
            //                    modifier = Modifier
            //                        .height(42.dp)
            //                        .offset(x = (marker + 2).dp)
            //                )
            //            }
            //        }
            if (type == "ft") {
                // Conversion
                // FT Conversion: -9999 to 99999 or 0 to 109998
                val greenMarkFT = if (greenMark.toDouble() >= 0) ((greenMark.toDouble() + 9999) / 303.8) else ((9999 + (greenMark.toDouble())) / 303.8)
                val yellowHatchedMarkFT = if (yellowHatchedMark.toDouble() >= 0) ((yellowHatchedMark.toDouble() + 9999) / 303.8) else ((9999 + (yellowHatchedMark.toDouble())) / 303.8)
                val yellowMarkFT = if (yellowMark.toDouble() >= 0) ((yellowMark.toDouble() + 9999) / 303.8) else ((9999 + (yellowMark.toDouble())) / 303.8)

                greenWidth = greenMarkFT
                yellowHatchedWidth = (yellowHatchedMarkFT - greenMarkFT)
                yellowWidth = if (yellowHatchedMarkFT > 0) (yellowMarkFT - yellowHatchedMarkFT) else (yellowMarkFT - greenMarkFT)
            } else if (type == "kts") {
                // Conversion
                // KTS Conversion: 20 to 150 or 0 to 130
                val greenMarkKTS = (((greenMark.toDouble() - 20) / 130 ) * 362)
                val yellowHatchedMarkKTS = (((yellowHatchedMark.toDouble() - 20) / 130 ) * 362)
                val yellowMarkKTS = (((yellowMark.toDouble() - 20) / 130 ) * 362)
                val redHatchedPos = redHatchedMark.toDouble()

                greenWidth = (greenMarkKTS)
                yellowHatchedWidth = (yellowHatchedMarkKTS - greenMarkKTS)
                yellowWidth = if (yellowHatchedMarkKTS > 0) (yellowMarkKTS - yellowHatchedMarkKTS) else (yellowMarkKTS - greenMarkKTS)
            } else if (type == "inhg") {
                // Conversion
                // INHG Conversion: 0 to 32
                val greenMarkINHG = (greenMark.toDouble() * 11.43)
                val yellowHatchedMarkINHG = (yellowHatchedMark.toDouble() * 11.43)
                val yellowMarkINHG = (yellowMark.toDouble() * 11.43)

                greenWidth = (greenMarkINHG)
                yellowHatchedWidth = (yellowHatchedMarkINHG - greenMarkINHG)
                yellowWidth = if (yellowHatchedMarkINHG > 0) (yellowMarkINHG - yellowHatchedMarkINHG) else (yellowMarkINHG - greenMarkINHG)
            }
            Box(
                modifier = Modifier
                    .width(if (type == "ft") (greenWidth.toDouble()).dp else if(type == "kts") (greenWidth.toDouble()).dp else if (type == "inhg") (greenWidth.toDouble()).dp else 0.dp)
                    .height(42.dp)
                    .background(Color.Green),
            ) {}
            Image(
                painterResource(id = R.drawable.yellow_hatched),
                contentScale = ContentScale.FillBounds,
                contentDescription = "YellowHatched",
                modifier = Modifier
                    .width(if (type == "ft") (yellowHatchedWidth.toDouble()).dp else if(type == "kts") (yellowHatchedWidth.toDouble()).dp else if (type == "inhg") (yellowHatchedWidth.toDouble()).dp else 0.dp)
                    .height(42.dp)
            )
            Box(
                modifier = Modifier
                    .width(if (type == "ft") (yellowWidth.toDouble()).dp else if(type == "kts") (yellowWidth.toDouble()).dp else if (type == "inhg") (yellowWidth.toDouble()).dp else 0.dp)
                    .height(42.dp)
                    .background(Color.Yellow),
            ) {}
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .background(Color.Red),
            ) {}
        }
        if (redHatchedMark.toInt() != 0) {
            val redHatchedPos = (((redHatchedMark.toDouble() - 20) / 130 ) * 362)
            Image(
                painterResource(id = R.drawable.red_hatched),
                contentScale = ContentScale.FillBounds,
                contentDescription = "RedHatched",
                modifier = Modifier
                    .width(6.dp)
                    .height(42.dp)
                    .offset(x = redHatchedPos.dp)
            )
        }
    }
}

@Composable
fun SettingBtn() {
    val context = LocalContext.current
    val activity = context as? Activity

    OutlinedButton(
        onClick = {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
            activity?.overridePendingTransition(0, 0)
        },
        modifier = Modifier
            .size(width = 115.dp, height = 90.dp)
            .padding(end = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White,
        ),
        border = BorderStroke(2.dp, Color.White),
        shape = CutCornerShape(7.dp),
    ) {
        Image(
            painterResource(id = R.drawable.setting_icon),
            contentDescription = "Settings"
        )
    }
}

@Composable
fun SettingHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            "Settings",
            fontFamily = FontFamily.Serif,
            fontSize = 60.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            var sliderPosition by remember {
                mutableFloatStateOf(loadSliderValue(context, "brightness_slider", 0.5f))
            }
            Slider(
                value = sliderPosition,
                steps = 0,
                onValueChange = { newBrightness ->
                    sliderPosition = newBrightness
                    saveSliderValue(context, "brightness_slider", newBrightness)
                    setWindowBrightness(context, newBrightness)
                },
                valueRange = 0f..1f,
                modifier = Modifier.width(250.dp)
            )

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
                modifier = Modifier.offset(x = 10.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.brightness_icon),
                    contentDescription = "Brightness",
                    modifier = Modifier
                        .size(width = 40.dp, height = 50.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            var sliderPosition by remember { mutableFloatStateOf(loadSliderValue(context, "volume_slider", 0.5f)) }
            val mediaPlayer = remember { MediaPlayer.create(context, R.raw.ding_sfx) }

            LaunchedEffect(sliderPosition) {
                mediaPlayer.setVolume(sliderPosition, sliderPosition)
                saveSliderValue(context, "volume_slider", sliderPosition)
            }

            Slider(
                value = sliderPosition,
                steps = 0,
                onValueChange = { newPosition -> sliderPosition = newPosition },
                valueRange = 0f..1f,
                modifier = Modifier.width(250.dp)
            )

            OutlinedButton(
                onClick = {
                    mediaPlayer.start()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White,
                ),
                border = BorderStroke(2.dp, Color.White),
                shape = CutCornerShape(7.dp),
                modifier = Modifier.offset(x = 10.dp)
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painterResource(id = R.drawable.volume_icon),
                        contentDescription = "Volume",
                        modifier = Modifier
                            .size(width = 30.dp, height = 40.dp)
                    )
                    Text(
                        "TEST",
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun InfoInfo(number: Number, unit: String, measure: String){
    Box(
        modifier = Modifier
            .width(350.dp)
            .background(Color.Black)
            .padding(4.dp)
            .border(1.dp, Color.White),
        contentAlignment = Alignment.CenterEnd // Align Row to the right inside the Box
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = number.toString(),
                fontSize = 38.sp,
                color = Color.White,
                modifier = Modifier.padding(end = 4.dp)
            )

            Column(
                modifier = Modifier
                    .border(1.dp, Color.White)
                    .padding(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = unit,
                        fontSize = 15.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(2.dp)
                        .background(Color.White)
                )

                Text(
                    text = measure,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SupplementalBtn(){
    val context = LocalContext.current
    val activity = context as? Activity
    OutlinedButton(
        onClick = {
            val intent = Intent(context, SupplementalsActivity::class.java)
            context.startActivity(intent)
            activity?.overridePendingTransition(0, 0)
        },
        modifier = Modifier
            .size(width = 115.dp, height = 90.dp)
            .padding(start = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White,
        ),
        border = BorderStroke(2.dp, Color.White),
        shape = CutCornerShape(7.dp),
    ) {
        //  Image(
        //      painterResource(id = R.drawable.info_icon),
        //      contentDescription = "Info"
        //  )
        Text(
            "i",
            fontFamily = FontFamily.Serif,
            fontSize = 50.sp
        )
    }
}

@Composable
fun ReturnFooter(){
    Column{
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
            ReturnHome()

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

@Composable
fun ReturnHome(){
    val context = LocalContext.current
    val activity = context as? Activity // Safely cast context to an Activity

    OutlinedButton(
        onClick = {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            activity?.overridePendingTransition(0, 0) // Disable the enter animation for MainActivity
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White,
        ),
        border = BorderStroke(2.dp, Color.White),
        shape = CutCornerShape(7.dp),
    ) {
        Image(
            painterResource(id = R.drawable.returnhome_icon),
            contentDescription = "Return Home", // Changed contentDescription for clarity
            modifier = Modifier
                .size(width = 50.dp, height = 70.dp)
        )
    }
}


