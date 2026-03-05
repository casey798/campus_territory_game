package com.campus.territory.ui.map

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale
import kotlin.math.PI
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MapViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val transition = rememberInfiniteTransition(label = "player-bob")
    val playerBobPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "player-bob-phase"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1914))
    ) {
        MapRenderer(
            state = uiState,
            playerBobPhase = playerBobPhase,
            onViewportChanged = viewModel::onViewportChanged,
            onTransformGesture = viewModel::onTransformGesture,
            onMapTap = viewModel::onMapTap,
            modifier = Modifier.fillMaxSize()
        )

        DebugPanel(
            uiState = uiState,
            onGridToggle = viewModel::onGridToggle,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
        )
    }

    val selectedPin = uiState.selectedPin
    if (selectedPin != null) {
        ModalBottomSheet(onDismissRequest = viewModel::dismissPinSheet) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = selectedPin.id,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
                Text(text = "State: AVAILABLE")
            }
        }
    }
}

@Composable
private fun DebugPanel(
    uiState: MapUiState,
    onGridToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val lastTapLabel = remember(uiState.lastTapWorld) {
        uiState.lastTapWorld?.let {
            "${it.x.roundToInt()}, ${it.y.roundToInt()}"
        } ?: "--"
    }

    Surface(
        modifier = modifier,
        color = Color(0xD41A2A22),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Grid", color = Color.White)
                Switch(
                    checked = uiState.gridEnabled,
                    onCheckedChange = onGridToggle
                )
            }

            Text(
                text = "Zoom: ${formatFloat(uiState.camera.zoom)}x",
                color = Color.White
            )
            Text(
                text = "Pan: (${formatFloat(uiState.camera.panX)}, ${formatFloat(uiState.camera.panY)})",
                color = Color.White
            )
            Text(
                text = "Last tap world: $lastTapLabel",
                color = Color.White
            )
        }
    }
}

private fun formatFloat(value: Float): String {
    return String.format(Locale.US, "%.2f", value)
}
