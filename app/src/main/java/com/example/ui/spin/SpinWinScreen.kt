package com.example.ui.spin

import android.graphics.Paint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.ui.theme.JomamboGoldDark
import com.example.ui.theme.JomamboGoldLight
import com.example.ui.theme.JomamboGoldSecondary
import com.example.ui.theme.JomamboGreenEarn
import com.example.ui.theme.JomamboPurpleDark
import com.example.ui.theme.JomamboPurplePrimary

@Composable
fun SpinWinScreen(
    user: User?,
    isSpinning: Boolean,
    spinRotationAngle: Float,
    rewardWon: Long?,
    onTriggerSpin: () -> Unit,
    onDismissReward: () -> Unit,
    onOpenVipModal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedAngle by animateFloatAsState(
        targetValue = spinRotationAngle,
        animationSpec = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
        label = "wheel_spin_animation"
    )

    val segments = listOf(
        Pair("2", Color(0xFF6200EE)),
        Pair("5", Color(0xFFFFC107)),
        Pair("10", Color(0xFF00C853)),
        Pair("15", Color(0xFFFF5722)),
        Pair("20", Color(0xFF9C27B0)),
        Pair("50★", Color(0xFFFFD700)),
        Pair("8", Color(0xFF00BCD4)),
        Pair("12", Color(0xFFE91E63))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = JomamboPurpleDark)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Daily Lucky Spin & Win",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Spin the wheel for random 2 to 50 Coins daily!",
                        color = JomamboGoldSecondary,
                        fontSize = 12.sp
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = JomamboGoldSecondary
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Spins Counter Badge
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = if ((user?.dailySpinsLeft ?: 0) > 0) JomamboGoldSecondary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = null,
                    tint = if ((user?.dailySpinsLeft ?: 0) > 0) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Daily Spins Left: ${user?.dailySpinsLeft ?: 0} / 5",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if ((user?.dailySpinsLeft ?: 0) > 0) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Animated Wheel Canvas Box
        Box(
            modifier = Modifier
                .size(310.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer golden ring shadow
            Surface(
                modifier = Modifier
                    .size(290.dp)
                    .shadow(12.dp, CircleShape),
                shape = CircleShape,
                color = JomamboGoldSecondary
            ) {}

            // The Spinning Wheel
            Canvas(
                modifier = Modifier
                    .size(276.dp)
                    .rotate(animatedAngle)
            ) {
                val canvasSize = size.minDimension
                val radius = canvasSize / 2f
                val center = Offset(size.width / 2f, size.height / 2f)
                val sweepAngle = 360f / segments.size

                segments.forEachIndexed { index, (label, color) ->
                    val startAngle = index * sweepAngle

                    // Draw Sector Arc
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(canvasSize, canvasSize)
                    )

                    // Sector border line
                    drawArc(
                        color = Color.White.copy(alpha = 0.4f),
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(canvasSize, canvasSize),
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // Draw text label on canvas
                    val angleRad = Math.toRadians((startAngle + sweepAngle / 2.0))
                    val textDist = radius * 0.65f
                    val textX = (center.x + textDist * Math.cos(angleRad)).toFloat()
                    val textY = (center.y + textDist * Math.sin(angleRad)).toFloat()

                    drawContext.canvas.nativeCanvas.apply {
                        val paint = Paint().apply {
                            this.color = android.graphics.Color.WHITE
                            this.textSize = 34f
                            this.isFakeBoldText = true
                            this.textAlign = Paint.Align.CENTER
                        }
                        drawText(label, textX, textY + 12f, paint)
                    }
                }

                // Inner circle hub
                drawCircle(
                    color = Color.White,
                    radius = 28.dp.toPx(),
                    center = center
                )
                drawCircle(
                    color = JomamboPurplePrimary,
                    radius = 22.dp.toPx(),
                    center = center
                )
            }

            // Top Pointer Pin
            Canvas(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.TopCenter)
            ) {
                val path = Path().apply {
                    moveTo(size.width / 2f, size.height)
                    lineTo(0f, 0f)
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(path, color = Color.Red, style = Fill)
                drawPath(path, color = Color.White, style = Stroke(width = 2.dp.toPx()))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Spin Button
        val spinsLeft = user?.dailySpinsLeft ?: 0
        Button(
            onClick = onTriggerSpin,
            enabled = spinsLeft > 0 && !isSpinning,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(52.dp)
                .testTag("spin_wheel_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = JomamboGoldSecondary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = null,
                    tint = if (spinsLeft > 0) Color.Black else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isSpinning) "Spinning Lucky Wheel..." else "SPIN TO WIN (Interstitial Ad)",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = if (spinsLeft > 0) Color.Black else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // VIP Bonus promo
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
                        text = "Want 2x Spin Payouts?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "VIP Hustlers double all spin prizes automatically.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = onOpenVipModal,
                    colors = ButtonDefaults.buttonColors(containerColor = JomamboPurplePrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Go VIP", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    // Win Announcement Dialog
    if (rewardWon != null) {
        AlertDialog(
            onDismissRequest = onDismissReward,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Celebration,
                        contentDescription = null,
                        tint = JomamboGoldSecondary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Lucky Spin Winner!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("The wheel stopped on your lucky slot!")
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = JomamboGoldSecondary.copy(alpha = 0.2f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "+$rewardWon COINS",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = JomamboGoldDark
                            )
                            if (user?.isVip == true) {
                                Text(
                                    text = "★ 2X VIP MULTIPLIER APPLIED ★",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = JomamboPurplePrimary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Added to your JOMAMBO wallet immediately.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onDismissReward,
                    colors = ButtonDefaults.buttonColors(containerColor = JomamboPurplePrimary)
                ) {
                    Text("Awesome!", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
