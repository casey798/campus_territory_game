package com.campus.territory.ui.map

import androidx.compose.ui.graphics.ImageBitmap
import com.campus.territory.domain.model.MapCamera
import com.campus.territory.domain.model.Pin
import com.campus.territory.domain.model.WorldPoint

data class MapUiState(
    val mapBitmap: ImageBitmap? = null,
    val mapWidthPx: Int = 0,
    val mapHeightPx: Int = 0,
    val viewportWidthPx: Int = 0,
    val viewportHeightPx: Int = 0,
    val camera: MapCamera = MapCamera(),
    val gridEnabled: Boolean = true,
    val pins: List<Pin> = emptyList(),
    val playerWorld: WorldPoint = WorldPoint(0f, 0f),
    val lastTapWorld: WorldPoint? = null,
    val selectedPin: Pin? = null
)
