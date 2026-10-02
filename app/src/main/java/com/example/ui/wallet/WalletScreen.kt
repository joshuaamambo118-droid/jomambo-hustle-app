package com.jomambo.app.ui.wallet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jomambo.app.data.model.Transaction
import com.jomambo.app.data.model.User
import com.jomambo.app.ui.theme.JomamboGoldDark
import com.jomambo.app.ui.theme.JomamboGoldLight
import com.jomambo.app.ui.theme.JomamboGoldSecondary
import com.jomambo.app.ui.theme.JomamboGreenEarn
import com.jomambo.app.ui.theme.JomamboPurpleDark
import com.jomambo.app.ui.theme.JomamboPurplePrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen(
    user: User?,
    transactions: List<Transaction>,
    onOpenWithdraw: () -> Unit,
    onOpenCreateAd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    var selectedFilter by remember { mutableStateOf("All") }

    val filteredTransactions = transactions.filter {
        when (selectedFilter) {
            "Earn" -> it.type == "earn"
            "Withdraw" -> it.type == "withdraw"
            else -> true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("wallet_balance_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = JomamboPurpleDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(JomamboPurpleDark, JomamboPurplePrimary)
                            )
                        )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AVAILABLE REWARD BALANCE",
                                color = JomamboGoldLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            if (user?.verified == true) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = JomamboGreenEarn.copy(alpha = 0.2f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = null,
                                            tint = JomamboGreenEarn,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "Phone Verified",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${user?.coins ?: 0}",
                                color = Color.White,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Coins",
                                color = JomamboGoldSecondary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        // Using Senior Dev Upgrade from User.kt
                        Text(
                            text = "≈ ${user?.getFormattedNaira() ?: "₦0.00"} (Rate: 100 Coins = ₦10)",
                            color = JomamboGoldLight,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onOpenWithdraw,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("withdraw_action_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = JomamboGoldSecondary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Withdraw",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Button(
                                onClick = onOpenCreateAd,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("create_ad_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Launch Ad",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Invite Friends & Earn +50 Coins",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Your Referral Code: ${user?.referralCode ?: "JM0000"}",
                            fontSize = 12.sp,
                            color = JomamboPurplePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(user?.referralCode ?: "JM0000"))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Referral Code",
                            tint = JomamboPurplePrimary
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transaction History",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("All", "Earn", "Withdraw").forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = JomamboPurplePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        if (filteredTransactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No transactions found", fontWeight = FontWeight.Bold)
                        Text(
                            "Start watching videos or doing tasks to earn!",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredTransactions) { tx ->
                TransactionItemRow(tx = tx)
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TransactionItemRow(tx: Transaction) {
    val isEarn = tx.type == "earn"
    val dateFormatter = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(tx.timestamp) { dateFormatter.format(Date(tx.timestamp)) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            if (isEarn) JomamboGreenEarn.copy(alpha = 0.15f) else Color.Red.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isEarn) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                        contentDescription = null,
                        tint = if (isEarn) JomamboGreenEarn else Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = tx.description.ifEmpty { tx.method },
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "$formattedDate • ${tx.status.replaceFirstChar { it.uppercase() }}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (isEarn) "+" else "-"}${tx.amount} Coins",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = if (isEarn) JomamboGreenEarn else Color.Red
                )
                Text(
                    text = "₦${"%.1f".format(tx.nairaAmount)}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
