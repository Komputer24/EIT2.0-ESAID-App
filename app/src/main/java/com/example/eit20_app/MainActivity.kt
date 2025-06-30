package com.example.eit20_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eit20_app.ui.theme.EIT20_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EIT20_AppTheme{
//                val interactionSource = remember { MutableInteractionSource() }
//                val isPressed by interactionSource.collectIsPressedAsState()
//
//                val backgroundColor = if (isPressed) Color.Cyan else Color.Blue

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues = innerPadding),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Spacer(modifier = Modifier.height(15.dp))
                            ButtonRow()

                            Row(
//                                modifier = Modifier
//                                    .background(Color.Red),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "1440",
                                    fontSize = 130.sp,
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(paddingValues = innerPadding),
                                        //.background(Color.Cyan),
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
                    }
                }
            }
        }
    }

    private fun selectedBtn() {
        // Empty
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
