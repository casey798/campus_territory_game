package com.campus.territory.domain.model

data class WorldPoint(
    val x: Float,
    val y: Float
) {
    fun distanceSquaredTo(other: WorldPoint): Float {
        val dx = x - other.x
        val dy = y - other.y
        return dx * dx + dy * dy
    }
}
