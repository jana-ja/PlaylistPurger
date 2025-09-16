package de.janaja.playlistpurger.features.player.data.repo

import de.janaja.playlistpurger.features.player.data.model.toDevice
import de.janaja.playlistpurger.features.player.data.model.toPlayerState
import de.janaja.playlistpurger.features.player.domain.model.Device
import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.features.player.domain.model.PlayerState
import de.janaja.playlistpurger.features.player.domain.repo.PlayerRepo

class SpotifyPlayerRepo(
    private val webApi: SpotifyWebApi
) : PlayerRepo {

    override suspend fun getAvailableDevices(): Result<List<Device>> {
        return webApi.getAvailableDevices().map { response ->
            response.devices.map { it.toDevice() }
        }
    }

    override suspend fun play(playlistId: String, trackId: String, deviceId: String): Result<Unit> {
        return webApi.playTrack(
            contextUri = "spotify:playlist:${playlistId}",
            trackUri = "spotify:track:${trackId}",
            deviceId = deviceId
        ).map { }
    }

    override suspend fun pause(): Result<Unit> {
        return webApi.pauseTrack()
            .map { }
    }

    override suspend fun seekPosition(newPosMs: Long): Result<Unit> {
        return webApi.seekPosition(newPosMs)
            .map { }
    }

    override suspend fun getPlayerState(): Result<PlayerState> {
        return webApi.getPlayerState()
            .map { it.toPlayerState() }
    }

}