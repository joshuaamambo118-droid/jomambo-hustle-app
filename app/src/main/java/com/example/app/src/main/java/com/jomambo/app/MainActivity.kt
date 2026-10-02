package com.jomambo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.jomambo.app.ui.theme.MyApplicationTheme
import com.jomambo.app.ui.wallet.WalletScreen
import com.jomambo.app.data.model.User
import com.jomambo.app.data.model.Transaction

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Dummy data for testing - change later
                    WalletScreen(
                        user = User(coins = 5000, verified = true),
                        transactions = emptyList(),
                        onOpenWithdraw = {},
                        onOpenCreateAd = {}
                    )
                }
            }
        }
    }
}
