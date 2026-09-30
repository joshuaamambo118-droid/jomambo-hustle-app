package com.example.ui.leaderboard

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LeaderboardEntry
import com.example.data.model.User
import com.example.ui.theme.JomamboGoldDark
import com.example.ui.theme.JomamboGoldSecondary
import com.example.ui.theme.JomamboGreenEarn
import com.example.ui.theme.JomamboPurpleDark
import com.example.ui.theme.JomamboPurplePrimary

@Composable
fun LeaderboardScreen(
    user: User?,
    leaderboard: List<LeaderboardEntry>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
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
                            text = "Weekly Top Earners",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Top 3 hustlers receive ₦5,000 cash bonus every Sunday!",
                            color = JomamboGoldSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = JomamboGoldSecondary
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp)
                        )
                    }
                }
            }
        }

        // Top 3 Podium
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                val top1 = leaderboard.getOrNull(0)
                val top2 = leaderboard.getOrNull(1)
                val top3 = leaderboard.getOrNull(2)

                // 2nd Place
                if (top2 != null) {
                    PodiumColumn(entry = top2, rank = 2, heightDp = 100, badgeColor = Color(0xFFC0C0C0))
                }
                // 1st Place (Gold)
                if (top1 != null) {
                    PodiumColumn(entry = top1, rank = 1, heightDp = 130, badgeColor = JomamboGoldSecondary)
                }
                // 3rd Place (Bronze)
                if (top3 != null) {
                    PodiumColumn(entry = top3, rank = 3, heightDp = 85, badgeColor = Color(0xFFCD7F32))
                }
            }
        }

        item {
            Text(
                text = "All Rankings",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        items(leaderboard) { entry ->
            val isCurrentUser = entry.name.contains("Joshua")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrentUser) JomamboPurplePrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = when (entry.rank) {
                                1 -> JomamboGoldSecondary
                                2 -> Color(0xFFC0C0C0)
                                3 -> Color(0xFFCD7F32)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${entry.rank}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (entry.rank <= 3) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = entry.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                if (entry.isVip) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = JomamboGoldSecondary
                                    ) {
                                        Text(
                                            text = "VIP",
                                            color = Color.Black,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Earned ₦${"%.0f".format(entry.naira)} this week",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = JomamboGreenEarn.copy(alpha = 0.12f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = JomamboGreenEarn,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${entry.coins} C",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = JomamboGreenEarn
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PodiumColumn(
    entry: LeaderboardEntry,
    rank: Int,
    heightDp: Int,
    badgeColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = badgeColor,
            modifier = Modifier.size(42.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = entry.avatarLetter,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = entry.name.split(" ").firstOrNull() ?: entry.name,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            maxLines = 1
        )

        Text(
            text = "${entry.coins} C",
            fontSize = 10.sp,
            color = JomamboGoldDark,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .width(80.dp)
                .height(heightDp.dp)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(badgeColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#$rank",
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        }
    }
}
