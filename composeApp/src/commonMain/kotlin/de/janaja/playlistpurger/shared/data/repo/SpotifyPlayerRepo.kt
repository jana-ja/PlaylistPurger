package de.janaja.playlistpurger.shared.data.repo

import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.shared.domain.repository.PlayerRepo

class SpotifyPlayerRepo(
    private val webApi: SpotifyWebApi
) : PlayerRepo {


    override suspend fun play(playlistId: String, trackId: String): Result<Unit> {
        return webApi.playTrack(
            playlistId = "spotify:playlist:${playlistId}",
            trackId = "spotify:track:${trackId}"
        ).map { }
    }

    override suspend fun pause(): Result<Unit> {
        return webApi.pauseTrack()
            .map { }
    }

}