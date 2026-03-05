package com.campus.territory.ui.map

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.campus.territory.core.CameraBounds
import com.campus.territory.core.MapCoordinateTransformer
import com.campus.territory.data.MapRepository
import com.campus.territory.domain.model.MapCamera
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.min

class MapViewModel(
    private val repository: MapRepository = MapRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        val (mapWidth, mapHeight) = repository.getMapDimensions()
        _uiState.value = MapUiState(
            mapBitmap = repository.getMapBitmap(),
            mapWidthPx = mapWidth,
            mapHeightPx = mapHeight,
            pins = repository.getSamplePins(),
            playerWorld = repository.getPlayerSpawn()
        )
    }

    fun onViewportChanged(widthPx: Int, heightPx: Int) {
        if (widthPx <= 0 || heightPx <= 0) return

        val current = _uiState.value
        val firstViewportPass = current.viewportWidthPx == 0 || current.viewportHeightPx == 0

        var nextState = current.copy(
            viewportWidthPx = widthPx,
            viewportHeightPx = heightPx
        )

        if (firstViewportPass) {
            val initialZoom = computeInitialZoom(widthPx, heightPx, current.mapWidthPx, current.mapHeightPx)
            val initialPanX = (widthPx - current.mapWidthPx * initialZoom) * 0.5f
            val initialPanY = (heightPx - current.mapHeightPx * initialZoom) * 0.5f

            nextState = nextState.copy(
                camera = MapCamera(
                    zoom = initialZoom,
                    panX = initialPanX,
                    panY = initialPanY
                )
            )
        }

        val clampedCamera = CameraBounds.clamp(
            camera = nextState.camera,
            mapWidthPx = nextState.mapWidthPx,
            mapHeightPx = nextState.mapHeightPx,
            viewportWidthPx = nextState.viewportWidthPx,
            viewportHeightPx = nextState.viewportHeightPx
        )

        _uiState.value = nextState.copy(camera = clampedCamera)
    }

    fun onTransformGesture(centroid: Offset, pan: Offset, zoomChange: Float) {
        val current = _uiState.value
        val transformedCamera = MapCoordinateTransformer.applyGestureTransform(
            camera = current.camera,
            centroidX = centroid.x,
            centroidY = centroid.y,
            panX = pan.x,
            panY = pan.y,
            zoomChange = zoomChange,
            minZoom = MIN_ZOOM,
            maxZoom = MAX_ZOOM
        )

        val clampedCamera = CameraBounds.clamp(
            camera = transformedCamera,
            mapWidthPx = current.mapWidthPx,
            mapHeightPx = current.mapHeightPx,
            viewportWidthPx = current.viewportWidthPx,
            viewportHeightPx = current.viewportHeightPx
        )

        _uiState.value = current.copy(camera = clampedCamera)
    }

    fun onGridToggle(enabled: Boolean) {
        _uiState.update { it.copy(gridEnabled = enabled) }
    }

    fun onMapTap(screenTap: Offset) {
        val current = _uiState.value
        val tapWorld = MapCoordinateTransformer.screenToWorld(
            screenX = screenTap.x,
            screenY = screenTap.y,
            camera = current.camera
        )

        val worldHitRadius = 20f / current.camera.zoom.coerceAtLeast(MIN_ZOOM)
        val hitRadiusSquared = worldHitRadius * worldHitRadius

        val selectedPin = current.pins.firstOrNull { pin ->
            pin.worldPosition.distanceSquaredTo(tapWorld) <= hitRadiusSquared
        }

        _uiState.value = current.copy(
            lastTapWorld = tapWorld,
            selectedPin = selectedPin
        )
    }

    fun dismissPinSheet() {
        _uiState.update { it.copy(selectedPin = null) }
    }

    private fun computeInitialZoom(
        viewportWidthPx: Int,
        viewportHeightPx: Int,
        mapWidthPx: Int,
        mapHeightPx: Int
    ): Float {
        val fitX = viewportWidthPx.toFloat() / mapWidthPx.toFloat()
        val fitY = viewportHeightPx.toFloat() / mapHeightPx.toFloat()
        return min(fitX, fitY).coerceIn(MIN_ZOOM, 1.3f)
    }

    companion object {
        const val GRID_SIZE_PX = 32
        private const val MIN_ZOOM = 0.5f
        private const val MAX_ZOOM = 8f
    }
}
