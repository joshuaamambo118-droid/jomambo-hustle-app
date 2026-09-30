package com.example.ui.tasks

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SocialTask
import com.example.data.model.User
import com.example.ui.components.ShimmerCard
import com.example.ui.theme.JomamboGoldDark
import com.example.ui.theme.JomamboGoldSecondary
import com.example.ui.theme.JomamboGreenEarn
import com.example.ui.theme.JomamboPurpleDark
import com.example.ui.theme.JomamboPurplePrimary

@Composable
fun TasksScreen(
    user: User?,
    tasks: List<SocialTask>,
    verifyingTaskId: String?,
    taskCountdown: Int,
    isLoading: Boolean,
    onStartTask: (SocialTask) -> Unit,
    onVerifyAndClaim: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Instagram", "YouTube", "Apps", "Telegram", "Twitter/X")

    val filteredTasks = tasks.filter {
        when (selectedCategory) {
            "Instagram" -> it.type == "ig_follow"
            "YouTube" -> it.type == "youtube_sub"
            "Apps" -> it.type == "app_install"
            "Telegram" -> it.type == "telegram_join"
            "Twitter/X" -> it.type == "x_repost"
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
            // Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = JomamboPurpleDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Social Hustle Tasks",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Follow, Subscribe, Install & Earn instant coins",
                                color = JomamboGoldSecondary,
                                fontSize = 12.sp
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = JomamboGoldSecondary
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
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
        }

        // Active Verification Banner if running
        if (verifyingTaskId != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (taskCountdown == 0) JomamboGreenEarn.copy(alpha = 0.15f) else JomamboGoldSecondary.copy(alpha = 0.15f)
                    ),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (taskCountdown == 0) Icons.Default.CheckCircle else Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = if (taskCountdown == 0) JomamboGreenEarn else JomamboGoldDark,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (taskCountdown > 0) "Verifying Task: ${taskCountdown}s" else "Verification Completed!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = if (taskCountdown > 0) "Please stay on partner page for 10s" else "Tap below to credit your coins!",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (taskCountdown == 0) {
                            Button(
                                onClick = { onVerifyAndClaim(verifyingTaskId) },
                                colors = ButtonDefaults.buttonColors(containerColor = JomamboGreenEarn),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("verify_and_claim_button")
                            ) {
                                Text("Claim", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = JomamboPurplePrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        if (filteredTasks.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No active tasks in this category", fontWeight = FontWeight.Bold)
                        Text("Check back soon for new sponsor slots.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            items(filteredTasks) { task ->
                val isVerifyingThis = verifyingTaskId == task.id
                TaskItemCard(
                    task = task,
                    isVerifying = isVerifyingThis,
                    taskCountdown = taskCountdown,
                    isVip = user?.isVip == true,
                    onOpenTask = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(task.url))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Intent fallback
                        }
                        onStartTask(task)
                    },
                    onClaim = {
                        onVerifyAndClaim(task.id)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TaskItemCard(
    task: SocialTask,
    isVerifying: Boolean,
    taskCountdown: Int,
    isVip: Boolean,
    onOpenTask: () -> Unit,
    onClaim: () -> Unit
) {
    val (icon, iconColor) = when (task.type) {
        "ig_follow" -> Pair(Icons.Default.CameraAlt, Color(0xFFE1306C))
        "youtube_sub" -> Pair(Icons.Default.PlayArrow, Color(0xFFFF0000))
        "app_install" -> Pair(Icons.Default.Download, Color(0xFF00C853))
        "telegram_join" -> Pair(Icons.Default.Send, Color(0xFF0088CC))
        else -> Pair(Icons.Default.People, JomamboPurplePrimary)
    }

    val displayPrice = if (isVip) task.price * 2 else task.price

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("task_item_${task.id}"),
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
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(iconColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = task.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${task.remaining} / ${task.totalSlots} slots remaining",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Coin reward badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = JomamboGoldSecondary.copy(alpha = 0.2f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = JomamboGoldDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+$displayPrice Coins",
                            color = JomamboGoldDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = task.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (isVerifying && taskCountdown == 0) {
                    Button(
                        onClick = onClaim,
                        colors = ButtonDefaults.buttonColors(containerColor = JomamboGreenEarn),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Claim +$displayPrice Coins", fontWeight = FontWeight.Bold)
                    }
                } else if (isVerifying) {
                    Button(
                        onClick = { /* Wait for countdown */ },
                        enabled = false,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Verifying (${taskCountdown}s)", fontSize = 12.sp)
                    }
                } else {
                    Button(
                        onClick = onOpenTask,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = JomamboPurplePrimary)
                    ) {
                        Text("Start Task", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}
