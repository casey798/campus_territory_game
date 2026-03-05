package com.campus.territory.ui.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import com.campus.territory.domain.model.Pin
import com.campus.territory.domain.model.WorldPoint
import kotlin.math.sin

@Composable
fun MapRenderer(
    state: MapUiState,
    playerBobPhase: Float,
    onViewportChanged: (Int, Int) -> Unit,
    onTransformGesture: (Offset, Offset, Float) -> Unit,
    onMapTap: (Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .onSizeChanged { onViewportChanged(it.width, it.height) }
            .pointerInput(state.camera.zoom, state.camera.panX, state.camera.panY) {
                detectTapGestures(onTap = onMapTap)
            }
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    onTransformGesture(centroid, pan, zoom)
                }
            }
    ) {
        val mapBitmap = state.mapBitmap ?: return@Canvas

        withTransform({
            translate(state.camera.panX, state.camera.panY)
            scale(state.camera.zoom, state.camera.zoom, pivot = Offset.Zero)
        }) {
            drawImage(
                image = mapBitmap,
                filterQuality = FilterQuality.None
            )

            if (state.gridEnabled) {
                drawGrid(
                    mapWidthPx = state.mapWidthPx,
                    mapHeightPx = state.mapHeightPx,
                    cameraZoom = state.camera.zoom
                )
            }

            drawPins(state.pins, state.camera.zoom)
            drawPlayerMarker(state.playerWorld, playerBobPhase, state.camera.zoom)
        }
    }
}

private fun DrawScope.drawGrid(
    mapWidthPx: Int,
    mapHeightPx: Int,
    cameraZoom: Float
) {
    val stroke = (1f / cameraZoom).coerceAtLeast(0.5f)
    val color = Color(0x66FFFFFF)

    var x = 0
    while (x <= mapWidthPx) {
        val xPos = x.toFloat()
        drawLine(
            color = color,
            start = Offset(xPos, 0f),
            end = Offset(xPos, mapHeightPx.toFloat()),
            strokeWidth = stroke
        )
        x += MapViewModel.GRID_SIZE_PX
    }

    var y = 0
    while (y <= mapHeightPx) {
        val yPos = y.toFloat()
        drawLine(
            color = color,
            start = Offset(0f, yPos),
            end = Offset(mapWidthPx.toFloat(), yPos),
            strokeWidth = stroke
        )
        y += MapViewModel.GRID_SIZE_PX
    }
}

private fun DrawScope.drawPins(pins: List<Pin>, cameraZoom: Float) {
    val pinRadius = 11f
    val stroke = (2f / cameraZoom).coerceAtLeast(1f)

    for (pin in pins) {
        val center = Offset(pin.worldPosition.x, pin.worldPosition.y)
        drawCircle(
            color = Color(0xFFD74D4D),
            radius = pinRadius,
            center = center
        )
        drawCircle(
            color = Color(0xFF2A1010),
            radius = pinRadius,
            center = center,
            style = Stroke(stroke)
        )
        drawCircle(
            color = Color(0xFFFFE08A),
            radius = 3.5f,
            center = center
        )
    }
}

private fun DrawScope.drawPlayerMarker(
    playerWorld: WorldPoint,
    playerBobPhase: Float,
    cameraZoom: Float
) {
    val bobOffset = sin(playerBobPhase.toDouble()).toFloat() * 4f
    val center = Offset(playerWorld.x, playerWorld.y + bobOffset)
    val outline = (2.2f / cameraZoom).coerceAtLeast(1f)

    drawCircle(
        color = Color(0xFF69D47C),
        radius = 14f,
        center = center
    )
    drawCircle(
        color = Color(0xFF14341D),
        radius = 14f,
        center = center,
        style = Stroke(outline)
    )
    drawCircle(
        color = Color(0xFFE8FFED),
        radius = 4f,
        center = Offset(center.x - 2f, center.y - 2f)
    )
}
