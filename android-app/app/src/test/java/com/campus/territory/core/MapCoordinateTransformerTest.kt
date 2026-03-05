package com.campus.territory.core

import com.campus.territory.domain.model.MapCamera
import com.campus.territory.domain.model.WorldPoint
import org.junit.Assert.assertEquals
import org.junit.Test

class MapCoordinateTransformerTest {

    @Test
    fun worldToScreenAndBack_returnsOriginalPoint() {
        val camera = MapCamera(zoom = 2.5f, panX = -120f, panY = 88f)
        val world = WorldPoint(x = 300f, y = 512f)

        val screen = MapCoordinateTransformer.worldToScreen(world, camera)
        val recovered = MapCoordinateTransformer.screenToWorld(screen.x, screen.y, camera)

        assertEquals(world.x, recovered.x, 0.0001f)
        assertEquals(world.y, recovered.y, 0.0001f)
    }

    @Test
    fun applyGestureTransform_scalesAroundCentroid() {
        val camera = MapCamera(zoom = 1f, panX = 0f, panY = 0f)

        val transformed = MapCoordinateTransformer.applyGestureTransform(
            camera = camera,
            centroidX = 100f,
            centroidY = 50f,
            panX = 0f,
            panY = 0f,
            zoomChange = 2f,
            minZoom = 0.5f,
            maxZoom = 8f
        )

        assertEquals(2f, transformed.zoom, 0.0001f)
        assertEquals(-100f, transformed.panX, 0.0001f)
        assertEquals(-50f, transformed.panY, 0.0001f)
    }
}
