package com.campus.territory.domain.model

data class Pin(
    val id: String,
    val worldPosition: WorldPoint,
    val state: PinState = PinState.AVAILABLE
)
