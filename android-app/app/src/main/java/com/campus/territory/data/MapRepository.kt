package com.campus.territory.data

import androidx.compose.ui.graphics.ImageBitmap
import com.campus.territory.domain.model.Pin
import com.campus.territory.domain.model.WorldPoint

class MapRepository {

    companion object {
        const val MAP_WIDTH_PX = 1536
        const val MAP_HEIGHT_PX = 1024
    }

    private val mapBitmap: ImageBitmap by lazy {
        PlaceholderMapGenerator.generate(MAP_WIDTH_PX, MAP_HEIGHT_PX)
    }

    private val samplePins = listOf(
        Pin(id = "PIN-01", worldPosition = WorldPoint(230f, 220f)),
        Pin(id = "PIN-02", worldPosition = WorldPoint(450f, 320f)),
        Pin(id = "PIN-03", worldPosition = WorldPoint(810f, 220f)),
        Pin(id = "PIN-04", worldPosition = WorldPoint(1180f, 520f)),
        Pin(id = "PIN-05", worldPosition = WorldPoint(320f, 720f)),
        Pin(id = "PIN-06", worldPosition = WorldPoint(690f, 820f))
    )

    private val playerSpawn = WorldPoint(780f, 500f)

    fun getMapBitmap(): ImageBitmap = mapBitmap

    fun getMapDimensions(): Pair<Int, Int> = MAP_WIDTH_PX to MAP_HEIGHT_PX

    fun getSamplePins(): List<Pin> = samplePins

    fun getPlayerSpawn(): WorldPoint = playerSpawn
}
