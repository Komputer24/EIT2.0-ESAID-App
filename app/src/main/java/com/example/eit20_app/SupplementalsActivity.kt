package com.example.eit20_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eit20_app.ui.theme.EIT20_AppTheme

class SupplementalsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EIT20_AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                    ) {
                        Text(
                            "Supplemental",
                            fontFamily = FontFamily.Serif,
                            fontSize = 50.sp
                        )
                    }
                }
            }
        }
    }
}