package de.janaja.playlistpurger.features.player.data.model

import de.janaja.playlistpurger.features.player.domain.model.Device
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val id: String,
    val name: String,
    @SerialName("is_active")
    val isActive: Boolean,
)

fun DeviceDto.toDevice(): Device {
    return Device(
        id = this.id,
        name = this.name,
        isActive = this.isActive
    )
}