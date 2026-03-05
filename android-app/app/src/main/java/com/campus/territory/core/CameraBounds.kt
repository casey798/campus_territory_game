package com.campus.territory.core

import com.campus.territory.domain.model.MapCamera

object CameraBounds {

    fun clamp(
        camera: MapCamera,
        mapWidthPx: Int,
        mapHeightPx: Int,
        viewportWidthPx: Int,
        viewportHeightPx: Int
    ): MapCamera {
        if (mapWidthPx <= 0 || mapHeightPx <= 0 || viewportWidthPx <= 0 || viewportHeightPx <= 0) {
            return camera
        }

        val scaledWidth = mapWidthPx * camera.zoom
        val scaledHeight = mapHeightPx * camera.zoom

        val clampedPanX = clampAxis(camera.panX, scaledWidth, viewportWidthPx.toFloat())
        val clampedPanY = clampAxis(camera.panY, scaledHeight, viewportHeightPx.toFloat())

        return camera.copy(
            panX = clampedPanX,
            panY = clampedPanY
        )
    }

    private fun clampAxis(pan: Float, scaledSize: Float, viewportSize: Float): Float {
        return if (scaledSize <= viewportSize) {
            (viewportSize - scaledSize) * 0.5f
        } else {
            pan.coerceIn(viewportSize - scaledSize, 0f)
        }
    }
}
