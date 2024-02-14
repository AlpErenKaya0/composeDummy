package com.alperen.composedummy

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alperen.composedummy.ui.theme.Red
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                JetpackComposeExample()
            }
        }
    }

    @Composable
    fun JetpackComposeExample() {
        var showDialog by rememberSaveable { mutableStateOf(false) }
        var showFirstButton by rememberSaveable { mutableStateOf(true) }
        var showSecondButton by rememberSaveable { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        var secondDialog by remember { mutableStateOf(false) }
        var isButtonClickable by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier.fillMaxSize(),
            content = {
                if (showFirstButton) {
                    Button(
                        onClick = {
                            isButtonClickable = false // Buton tıklanamaz yap
                            coroutineScope.launch {
                                delay(6000) // 6 saniye gecikme
                                isButtonClickable = true // Buton tıklanabilir yap
                                showDialog = true
                            }
                        },
                        enabled = isButtonClickable, // Butonun tıklanabilirlik durumuna göre kontrol et
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(if (isButtonClickable) "Popup'ı Aç" else "Biraz Bekleyeceksin...")
                    }
                }
                if (showSecondButton) {

                    Button(
                        onClick = {
                            secondDialog = true
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Sakın Tıklama")
                    }
                }
                if (secondDialog) {
                    var message by remember { mutableStateOf("Pencereden Dışarı Bak") }
                    var buttonsEnabled by remember { mutableStateOf(true) }

                    AlertDialog(
                        onDismissRequest = {
                            secondDialog = false
                            
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    buttonsEnabled = false
                                    message = "Sana söylemiştim..."
                                    object : CountDownTimer(6000, 2000) {
                                        var tickCount = 0
                                        val messages = listOf("Sana söylemiştim...", "Mutlu musun?", "Herkes yok oldu")

                                        override fun onTick(millisUntilFinished: Long) {
                                            message = messages[tickCount % messages.size]
                                            tickCount++
                                        }

                                        override fun onFinish() {
                                            // Süre dolduğunda popup kapanacak
                                            showDialog = false
                                            secondDialog = false
                                            buttonsEnabled = true
                                        }
                                    }.start()
                                },
                                enabled = buttonsEnabled
                            ) {
                                Text("Tamam")
                            }
                        },
                        text = {
                            Text(message)
                        }
                    )
                }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDialog = false
                                    showSecondButton = true
                                    showFirstButton = false
                                }
                                ,  colors = ButtonDefaults.buttonColors(Color.Red)
                            ) {
                                Text("Kırmızı Buton")
                            }
                        },
                        dismissButton = {
                            var buttonColor by remember { mutableStateOf(Color.Unspecified) }
                            var isButtonClicked by remember { mutableStateOf(false) }

                            Button(
                                onClick = {


                                    buttonColor = Color.Green
                                    isButtonClicked = true


                                    object : CountDownTimer(6000, 1000) {
                                        override fun onTick(millisUntilFinished: Long) {

                                        }

                                        override fun onFinish() {

                                            isButtonClicked = false
                                            showDialog = false

                                        }
                                    }.start()
                                },
                                colors = ButtonDefaults.buttonColors( if (isButtonClicked) Color.Green else Color.Gray),
                            ) {
                                Text("İptal")
                            }
                        },
                        text = {
                            Text("Kırmızı butona basarsan dünya yok olacak")
                        }
                    )
                }
            }
        )
    }
}


