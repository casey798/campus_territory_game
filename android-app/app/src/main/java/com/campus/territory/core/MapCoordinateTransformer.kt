package com.campus.territory.core

import com.campus.territory.domain.model.MapCamera
import com.campus.territory.domain.model.WorldPoint

object MapCoordinateTransformer {

    fun worldToScreen(world: WorldPoint, camera: MapCamera): WorldPoint {
        return WorldPoint(
            x = world.x * camera.zoom + camera.panX,
            y = world.y * camera.zoom + camera.panY
        )
    }

    fun screenToWorld(screenX: Float, screenY: Float, camera: MapCamera): WorldPoint {
        val inverseZoom = 1f / camera.zoom.coerceAtLeast(0.0001f)
        return WorldPoint(
            x = (screenX - camera.panX) * inverseZoom,
            y = (screenY - camera.panY) * inverseZoom
        )
    }

    fun applyGestureTransform(
        camera: MapCamera,
        centroidX: Float,
        centroidY: Float,
        panX: Float,
        panY: Float,
        zoomChange: Float,
        minZoom: Float,
        maxZoom: Float
    ): MapCamera {
        val oldZoom = camera.zoom.coerceAtLeast(0.0001f)
        val newZoom = (oldZoom * zoomChange).coerceIn(minZoom, maxZoom)
        val scaleRatio = newZoom / oldZoom

        val updatedPanX = centroidX - (centroidX - camera.panX) * scaleRatio + panX
        val updatedPanY = centroidY - (centroidY - camera.panY) * scaleRatio + panY

        return MapCamera(
            zoom = newZoom,
            panX = updatedPanX,
            panY = updatedPanY
        )
    }
}
