package de.janaja.playlistpurger.features.player.data.model

import de.janaja.playlistpurger.shared.data.model.TrackDto
import de.janaja.playlistpurger.features.player.domain.model.PlayerState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerStateDto(
    val device: DeviceDto,
    @SerialName("progress_ms")
    val progressMs: Long,
    @SerialName("is_playing")
    val isPlaying: Boolean,
    val item: TrackDto, // TODO right type? // TODO could also be episode, would crash
)

fun PlayerStateDto.toPlayerState(): PlayerState {
    return PlayerState(
        device = this.device.toDevice(),
        progressMs = this.progressMs,
        isPlaying = this.isPlaying,
        trackId = this.item.id
    )
}


