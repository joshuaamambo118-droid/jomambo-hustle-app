package com.example.ui.offerwall

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.JomamboGoldDark
import com.example.ui.theme.JomamboGoldSecondary
import com.example.ui.theme.JomamboGreenEarn
import com.example.ui.theme.JomamboPurpleDark
import com.example.ui.theme.JomamboPurplePrimary

data class OfferwallItem(
    val id: String,
    val provider: String, // Tapjoy, IronSource, CPAlead
    val title: String,
    val description: String,
    val coins: Long, // 700 coins ≈ $0.70
    val usdValue: String,
    val difficulty: String,
    val category: String
)

data class SurveyItem(
    val id: String,
    val provider: String, // Pollfish, Prime Surveys
    val title: String,
    val durationMinutes: Int,
    val coins: Long,
    val matchRate: String
)

@Composable
fun OfferwallScreen(
    onCompleteOffer: (String, Long) -> Unit,
    onCompleteSurvey: (String, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Offerwalls, 1 = Surveys
    var activeOfferDialog by remember { mutableStateOf<OfferwallItem?>(null) }
    var activeSurveyDialog by remember { mutableStateOf<SurveyItem?>(null) }

    val offerwallItems = listOf(
        OfferwallItem(
            id = "off_1",
            provider = "Tapjoy",
            title = "Chipper Cash - Install & Send ₦500",
            description = "Download Chipper Cash app, verify BVN and make your first transfer.",
            coins = 700L,
            usdValue = "$0.70",
            difficulty = "Easy",
            category = "Fintech"
        ),
        OfferwallItem(
            id = "off_2",
            provider = "IronSource",
            title = "Candy Rush Saga - Reach Level 15",
            description = "Play and complete 15 stages in the match-3 puzzle game within 7 days.",
            coins = 700L,
            usdValue = "$0.70",
            difficulty = "Medium",
            category = "Gaming"
        ),
        OfferwallItem(
            id = "off_3",
            provider = "CPAlead",
            title = "Binance Africa - First Deposit",
            description = "Sign up on Binance through CPAlead link and deposit at least ₦2,000.",
            coins = 1200L,
            usdValue = "$1.20",
            difficulty = "Hard",
            category = "Crypto"
        ),
        OfferwallItem(
            id = "off_4",
            provider = "Tapjoy",
            title = "PiggyVest - Lock ₦1,000 for 30 Days",
            description = "Create a Safelock on PiggyVest and receive high instant bonus rewards.",
            coins = 850L,
            usdValue = "$0.85",
            difficulty = "Medium",
            category = "Investment"
        )
    )

    val surveyItems = listOf(
        SurveyItem(
            id = "surv_1",
            provider = "Pollfish",
            title = "Nigerian Consumer Mobile Network Survey",
            durationMinutes = 5,
            coins = 300L,
            matchRate = "98% Match"
        ),
        SurveyItem(
            id = "surv_2",
            provider = "Prime Surveys",
            title = "Fintech & Banking Habits in West Africa",
            durationMinutes = 8,
            coins = 450L,
            matchRate = "94% Match"
        ),
        SurveyItem(
            id = "surv_3",
            provider = "Pollfish",
            title = "Online Shopping Trends (Jumia/Konga)",
            durationMinutes = 3,
            coins = 200L,
            matchRate = "99% Match"
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = JomamboPurpleDark)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "High-Payout Offer Hub",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "Earn up to 700+ Coins ($0.70) per app installation & market research",
                        color = JomamboGoldSecondary,
                        fontSize = 12.sp
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = JomamboGoldSecondary
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tabs: Offerwall vs Surveys
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Public, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Offerwall ($0.70)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Poll, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Surveys (Pollfish)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (selectedTab == 0) {
                items(offerwallItems) { offer ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { activeOfferDialog = offer },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = JomamboPurplePrimary.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = offer.provider,
                                        color = JomamboPurplePrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = JomamboGreenEarn.copy(alpha = 0.15f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MonetizationOn,
                                            contentDescription = null,
                                            tint = JomamboGreenEarn,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "+${offer.coins} Coins (${offer.usdValue})",
                                            color = JomamboGreenEarn,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = offer.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = offer.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Difficulty: ${offer.difficulty} • ${offer.category}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Button(
                                    onClick = { activeOfferDialog = offer },
                                    colors = ButtonDefaults.buttonColors(containerColor = JomamboPurplePrimary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Open Offer", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            } else {
                items(surveyItems) { survey ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { activeSurveyDialog = survey },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = JomamboGoldSecondary.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = survey.provider,
                                        color = JomamboGoldDark,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                Text(
                                    text = survey.matchRate,
                                    color = JomamboGreenEarn,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = survey.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${survey.durationMinutes} mins",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Button(
                                    onClick = { activeSurveyDialog = survey },
                                    colors = ButtonDefaults.buttonColors(containerColor = JomamboGoldSecondary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        "+${survey.coins} Coins",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Offerwall Detail Simulation Modal
    if (activeOfferDialog != null) {
        val offer = activeOfferDialog!!
        var offerStep by remember { mutableIntStateOf(1) } // 1 = start, 2 = completing

        AlertDialog(
            onDismissRequest = { activeOfferDialog = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = JomamboPurplePrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${offer.provider} Offerwall")
                }
            },
            text = {
                Column {
                    Text(text = offer.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = offer.description, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Instructions:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = JomamboPurplePrimary
                            )
                            Text(
                                text = "1. Click 'Start Offer' to launch simulator\n2. Complete requested action\n3. Tap 'Check Completion' to claim +${offer.coins} Coins (${offer.usdValue})",
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCompleteOffer(offer.title, offer.coins)
                        activeOfferDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = JomamboGreenEarn)
                ) {
                    Text("Check Completion & Claim", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { activeOfferDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Survey Simulation Modal
    if (activeSurveyDialog != null) {
        val survey = activeSurveyDialog!!
        var surveyStep by remember { mutableIntStateOf(1) }
        var selectedAnswer by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { activeSurveyDialog = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Poll, contentDescription = null, tint = JomamboGoldDark)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${survey.provider} Survey (Step $surveyStep/3)")
                }
            },
            text = {
                Column {
                    when (surveyStep) {
                        1 -> {
                            Text("What is your primary mobile telecom network in Nigeria?", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            listOf("MTN Nigeria", "Airtel Nigeria", "Glo Mobile", "9mobile").forEach { option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedAnswer = option }
                                        .padding(vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (selectedAnswer == option) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = JomamboPurplePrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(option, fontSize = 13.sp)
                                }
                            }
                        }
                        2 -> {
                            Text("How frequently do you purchase airtime or data bundles online?", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            listOf("Daily", "2-3 times per week", "Once a month", "Rarely").forEach { option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedAnswer = option }
                                        .padding(vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (selectedAnswer == option) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = JomamboPurplePrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(option, fontSize = 13.sp)
                                }
                            }
                        }
                        3 -> {
                            Text("Which reward payout method do you prefer most on JOMAMBO?", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            listOf("Direct Bank Transfer to OPay/Kuda", "VTU Airtime to Phone", "Crypto (USDT)").forEach { option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedAnswer = option }
                                        .padding(vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (selectedAnswer == option) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = JomamboPurplePrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(option, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (surveyStep < 3) {
                            surveyStep += 1
                            selectedAnswer = null
                        } else {
                            onCompleteSurvey(survey.title, survey.coins)
                            activeSurveyDialog = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = JomamboPurplePrimary)
                ) {
                    Text(if (surveyStep == 3) "Submit & Claim +${survey.coins} Coins" else "Next Question")
                }
            },
            dismissButton = {
                TextButton(onClick = { activeSurveyDialog = null }) {
                    Text("Exit Survey")
                }
            }
        )
    }
}
