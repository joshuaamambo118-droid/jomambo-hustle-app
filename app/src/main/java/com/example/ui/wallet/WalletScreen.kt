package com.example.ui.wallet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Transaction
import com.example.data.model.User
import com.example.ui.theme.JomamboGoldDark
import com.example.ui.theme.JomamboGoldLight
import com.example.ui.theme.JomamboGoldSecondary
import com.example.ui.theme.JomamboGreenEarn
import com.example.ui.theme.JomamboPurpleDark
import com.example.ui.theme.JomamboPurplePrimary
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
    var selectedFilter by remember { mutableStateOf("All") } // "All", "Earn", "Withdraw"

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

            // Main Wallet Card
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

                        // Large Coins
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

                        // Naira Equivalent (100 coins = 10 Naira)
                        val naira = (user?.coins ?: 0L) * 0.10
                        Text(
                            text = "≈ ₦${"%.2f".format(naira)} (Rate: 100 Coins = ₦10)",
                            color = JomamboGoldLight,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Action Buttons: Withdraw & Create Ad
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

        // Referral Card
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

        // Transactions Header & Filters
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
                        Text("Start watching videos or doing tasks to earn!", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    val isWithdraw = tx.type == "withdraw"
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
                        text = tx.description,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "$formattedDate • ${tx.status.capitalize(Locale.ROOT)}",
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

// WITHDRAW DIALOG (VTU AIRTIME & BANK)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawDialog(
    user: User?,
    isLoading: Boolean,
    onAirtimeWithdraw: (String, String, Long) -> Unit,
    onBankWithdraw: (String, String, String, Long) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedMethod by remember { mutableIntStateOf(0) } // 0 = VTU Airtime, 1 = Bank Transfer

    // Airtime State
    var airtimePhone by remember { mutableStateOf(user?.emailOrPhone ?: "") }
    var airtimeNetwork by remember { mutableStateOf("MTN Nigeria") }
    var airtimeAmount by remember { mutableLongStateOf(1000L) } // ₦1,000 face value (cost 970 coins, profit 30 retained)

    // Bank State
    val nigerianBanks = listOf("OPay", "PalmPay", "Kuda Microfinance Bank", "Zenith Bank", "GTBank (Guaranty Trust)", "Access Bank", "First Bank of Nigeria", "United Bank for Africa (UBA)")
    var selectedBank by remember { mutableStateOf(nigerianBanks.first()) }
    var bankDropdownExpanded by remember { mutableStateOf(false) }
    var accountNumber by remember { mutableStateOf("") }
    var accountName by remember { mutableStateOf("Joshua Amambo") }
    var withdrawCoins by remember { mutableStateOf("1000") } // Min 1000 coins (₦100)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = null,
                    tint = JomamboPurplePrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Withdraw Rewards", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(
                    selectedTabIndex = selectedMethod,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                ) {
                    Tab(
                        selected = selectedMethod == 0,
                        onClick = { selectedMethod = 0 },
                        text = { Text("VTU Airtime", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedMethod == 1,
                        onClick = { selectedMethod = 1 },
                        text = { Text("Bank Transfer", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (selectedMethod == 0) {
                    // AIRTIME VTU FORM
                    Text(
                        text = "Instant VTU Airtime Recharge",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Face Value: ₦$airtimeAmount Airtime (App cost ₦970, user gets full ₦1,000)",
                        fontSize = 11.sp,
                        color = JomamboGreenEarn,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = airtimePhone,
                        onValueChange = { airtimePhone = it },
                        label = { Text("11-digit Phone Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Select Network:", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("MTN", "Airtel", "Glo", "9mobile").forEach { net ->
                            val isSelected = airtimeNetwork.startsWith(net)
                            FilterChip(
                                selected = isSelected,
                                onClick = { airtimeNetwork = "$net Nigeria" },
                                label = { Text(net, fontSize = 11.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Amount (Naira):", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(200L, 500L, 1000L).forEach { amt ->
                            val isSelected = airtimeAmount == amt
                            FilterChip(
                                selected = isSelected,
                                onClick = { airtimeAmount = amt },
                                label = { Text("₦$amt", fontSize = 11.sp) }
                            )
                        }
                    }
                } else {
                    // BANK TRANSFER FORM
                    Text(
                        text = "Direct Nigerian Bank Transfer",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Min withdrawal: 1,000 Coins (₦100). Zero transfer fees.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ExposedDropdownMenuBox(
                        expanded = bankDropdownExpanded,
                        onExpandedChange = { bankDropdownExpanded = !bankDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedBank,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Bank") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bankDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = bankDropdownExpanded,
                            onDismissRequest = { bankDropdownExpanded = false }
                        ) {
                            nigerianBanks.forEach { bank ->
                                DropdownMenuItem(
                                    text = { Text(bank, fontSize = 13.sp) },
                                    onClick = {
                                        selectedBank = bank
                                        bankDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = accountNumber,
                        onValueChange = {
                            if (it.length <= 10) accountNumber = it
                            if (it.length == 10) accountName = "Verified: " + (user?.fullName ?: "Joshua Amambo")
                        },
                        label = { Text("10-Digit NUBAN Account Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (accountNumber.length == 10) {
                        Text(
                            text = accountName,
                            fontSize = 11.sp,
                            color = JomamboGreenEarn,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = withdrawCoins,
                        onValueChange = { withdrawCoins = it },
                        label = { Text("Coins to Withdraw") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    val coinsNum = withdrawCoins.toLongOrNull() ?: 0L
                    Text(
                        text = "You will receive: ₦${"%.1f".format(coinsNum * 0.10)}",
                        fontSize = 11.sp,
                        color = JomamboPurplePrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedMethod == 0) {
                        onAirtimeWithdraw(airtimePhone, airtimeNetwork, airtimeAmount)
                    } else {
                        val coins = withdrawCoins.toLongOrNull() ?: 1000L
                        onBankWithdraw(selectedBank, accountNumber, accountName, coins)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = JomamboPurplePrimary),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                } else {
                    Text("Confirm Withdraw", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// PHONE VERIFICATION OTP MODAL
@Composable
fun PhoneVerifyDialog(
    isLoading: Boolean,
    onSubmitOtp: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var otpInput by remember { mutableStateOf("") }
    val demoOtp = "739201"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = JomamboPurplePrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Phone Verification Required", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            Column {
                Text(
                    text = "To comply with anti-fraud regulations, please verify your phone number before making withdrawals.",
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = JomamboPurplePrimary.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Demo SMS OTP sent: $demoOtp",
                        fontWeight = FontWeight.Bold,
                        color = JomamboPurplePrimary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = otpInput,
                    onValueChange = { if (it.length <= 6) otpInput = it },
                    label = { Text("Enter 6-digit OTP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmitOtp(otpInput) },
                colors = ButtonDefaults.buttonColors(containerColor = JomamboPurplePrimary),
                enabled = otpInput.length == 6 && !isLoading
            ) {
                Text("Verify & Continue", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// DIRECT ADS CREATION MODAL
@Composable
fun CreateAdDialog(
    user: User?,
    isLoading: Boolean,
    onSubmitAd: (String, String, String, Int, String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var targetUrl by remember { mutableStateOf("") }
    var viewsCount by remember { mutableIntStateOf(500) }
    var category by remember { mutableStateOf("Tech") }

    val costInNaira = viewsCount * 5.0 // ₦5 per view (60% platform profit retained)
    val costInCoins = viewsCount * 50L

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = null,
                    tint = JomamboPurplePrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Launch Direct Ad Campaign", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            Column {
                Text(
                    text = "Promote your YouTube video, Instagram profile, app or website to 100,000+ active Nigerian hustlers.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Campaign Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = targetUrl,
                    onValueChange = { targetUrl = it },
                    label = { Text("Target URL (https://...)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Brief Description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Target Views: $viewsCount Views", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(200, 500, 1000).forEach { v ->
                        FilterChip(
                            selected = viewsCount == v,
                            onClick = { viewsCount = v },
                            label = { Text("$v views", fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = JomamboGoldSecondary.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Campaign Cost: ₦${costInNaira.toInt()} or $costInCoins Coins",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = JomamboGoldDark
                        )
                        Text(
                            text = "User pays first before task goes live (Platform retains 60% margin)",
                            fontSize = 10.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmitAd(title, desc, targetUrl, viewsCount, category) },
                colors = ButtonDefaults.buttonColors(containerColor = JomamboPurplePrimary),
                enabled = !isLoading && title.isNotBlank() && targetUrl.isNotBlank()
            ) {
                Text("Pay & Launch Ad", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
