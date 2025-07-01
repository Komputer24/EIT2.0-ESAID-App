package com.example.eit20_app

import android.app.AlertDialog
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eit20_app.ui.theme.EIT20_AppTheme

val showDevelopmentAlert = mutableStateOf(false)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EIT20_AppTheme{
                AlertBox()

                val flightDisplay = 1440
                val ft = 1400
                val kts = 35
                val inhg = 16.0

                // Conversion
                val ftScale = (((ft+ 9999).toDouble() / 109998) * 364)-22  // -9999 to 99999 or 0 to 109998
                val ktsScale = (((kts - 20).toDouble() / 130) * 364)-22     // 20 to 150 or 0 to 130
                val inhgScale = ((inhg / 32) * 364)-22         // 0 to 32

//                val interactionSource = remember { MutableInteractionSource() }
//                val isPressed by interactionSource.collectIsPressedAsState()
//
//                val backgroundColor = if (isPressed) Color.Cyan else Color.Blue

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
                            Row(
//                                modifier = Modifier
//                                    .background(Color.Red),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if(flightDisplay < 100000 && flightDisplay > -10000) "${flightDisplay}" else "OOB", // (-9999 FT - 99999 FT)
                                    fontSize = 130.sp,
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
                        }
                        // BODY
                        Column(
                            modifier = Modifier
//                                .background(Color.Yellow)
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Bottom
                        ){
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
                                            painterResource(id = R.drawable.pointer_icon),
                                            contentDescription = "Pointer",
                                            modifier = Modifier
                                                .offset(x = (ftScale).dp) // Control where pointer points from 0 to 364
                                        )
                                    }
                                    Indicator(181,236,261,0, "ft") // Out of 362
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
                                                text = if(ft< 100000 && ft> -10000) "${ft}" else "OOB", // (-9999 FT - 99999 FT)
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
                                            painterResource(id = R.drawable.pointer_icon),
                                            contentDescription = "Pointer",
                                            modifier = Modifier
                                                .offset(x = (ktsScale).dp) // Control where pointer points from 0 to 364
                                        )
                                    }
                                    Indicator(272,0,0, 240, "kts") // Out of 362
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
                                                text = if(kts < 151 && kts > 19) "${kts}" else "OOB", // (20 KTS - 150 KTS)
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
                                            painterResource(id = R.drawable.pointer_icon),
                                            contentDescription = "Pointer",
                                            modifier = Modifier
                                                .offset(x = (inhgScale).dp) // Control where pointer points from 0 to 364
                                        )
                                    }
                                    Indicator(260,0,300, 0, "inhg") // Out of 362
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
                                                text = if(inhg < 33.0 && inhg >= 0.0) "${inhg}" else "OOB", //  (0 IN-HG - 32 IN-HG)
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
                        // FOOTER
                        Row(
                            modifier = Modifier
                                .fillMaxWidth() // Make the Column fill the entire screen
                                .padding(horizontal = 8.dp)
                                .height(90.dp),
//                                .background(Color.Red),
                            verticalAlignment = Alignment.Bottom // Push the Box to the bottom
                        ){
                            OutlinedButton(
                                onClick = { selectedBtn() },
                                modifier = Modifier
                                    .size(width = 115.dp, height = 90.dp)
                                    .padding(end = 4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =Color.Blue,
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
                            OutlinedButton(
                                onClick = { selectedBtn() },
                                modifier = Modifier
                                    .size(width = 115.dp, height = 90.dp)
                                    .padding(start = 4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =Color.Blue,
                                    contentColor = Color.White,
                                ),
                                border = BorderStroke(2.dp, Color.White),
                                shape = CutCornerShape(7.dp),
                            ) {
//                                Image(
//                                    painterResource(id = R.drawable.info_icon),
//                                    contentDescription = "Info"
//                                )
                                Text(
                                    "i",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 50.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun selectedBtn() {
        showDevelopmentAlert.value = true // 1.2.2, 1.3.2
    }
}

@Composable
fun ButtonRow() {
    val selectedIndex = remember { mutableStateOf(1) } // -1 = none selected
    val buttonLabels = listOf("INFO", "FLIGHT", "RAD-ALT")

    Row {
        buttonLabels.forEachIndexed { index, label ->
            OutlinedButton(
                onClick = { selectedIndex.value = index },
                modifier = Modifier
                    .size(width = 115.dp, height = 60.dp)
                    .padding(end = if (index == buttonLabels.size - 1) 0.dp else 4.dp),
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
fun Indicator(greenMark: Int, yellowHatchedMark: Int, yellowMark: Int, redHatchedMark: Int, type: String){
    var greenWidth = 0
    var yellowHatchedWidth = 0
    var yellowWidth = 0
    var redHatchedPos = 0
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
                greenWidth = greenMark
                yellowHatchedWidth = yellowHatchedMark - greenMark
                yellowWidth = if (yellowHatchedMark > 0) yellowMark - yellowHatchedMark else yellowMark - greenMark
            } else if (type == "kts") {
                greenWidth = greenMark
                yellowHatchedWidth = yellowHatchedMark - greenMark
                yellowWidth = if (yellowHatchedMark > 0) yellowMark - yellowHatchedMark else yellowMark - greenMark
            } else if (type == "inhg") {
                greenWidth = greenMark
                yellowHatchedWidth = yellowHatchedMark - greenMark
                yellowWidth = if (yellowHatchedMark > 0) yellowMark - yellowHatchedMark else yellowMark - greenMark
            }
            Box(
                modifier = Modifier
                    .width(greenWidth.dp)
                    .height(42.dp)
                    .background(Color.Green),
            ) {}
            Image(
                painterResource(id = R.drawable.yellow_hatched),
                contentScale = ContentScale.FillBounds,
                contentDescription = "YellowHatched",
                modifier = Modifier
                    .width(yellowHatchedWidth.dp)
                    .height(42.dp)
            )
            Box(
                modifier = Modifier
                    .width(yellowWidth.dp)
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
        if (redHatchedMark != 0) {
            Image(
                painterResource(id = R.drawable.red_hatched),
                contentScale = ContentScale.FillBounds,
                contentDescription = "RedHatched",
                modifier = Modifier
                    .width(6.dp)
                    .height(42.dp)
                    .offset(x = redHatchedMark.dp)
            )
        }
    }
}

@Composable
fun AlertBox() { // Replace with your actual Composable name
    // Conditionally display the AlertDialog based on the state
    if (showDevelopmentAlert.value) { // 1.2.1, 1.3.1
        AlertDialog( // 1.3.2, 1.1.4
            onDismissRequest = {
                showDevelopmentAlert.value = false // Dismiss the dialog
            },
            title = {
                Text(text = "Feature in Development")
            },
            text = {
                Text(text = "This feature is currently in development.")
            },
            confirmButton = { // 1.3.2, 1.6.1
                TextButton(
                    onClick = {
                        showDevelopmentAlert.value = false // Dismiss on confirm
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}