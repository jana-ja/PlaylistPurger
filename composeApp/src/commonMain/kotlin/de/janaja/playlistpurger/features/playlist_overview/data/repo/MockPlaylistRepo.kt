package de.janaja.playlistpurger.features.playlist_overview.data.repo

import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.features.playlist_overview.domain.repo.PlaylistRepo
import de.janaja.playlistpurger.shared.domain.model.UserDetails

class MockPlaylistRepo(
    private val isSuccessFul: Boolean
) : PlaylistRepo {

    private val playlists = (1..5).map { number ->
        Playlist(
            id = "$number",
            name = "Test Playlist $number",
            description = "Test Description",
            collaborative = true,
            public = true,
            imageUrl = "",
            trackCount = 5,
            owner = UserDetails.Full(
                "0",
                name = "Test User",
                thumbnailUrl = "https://i.scdn.co/image/ab67757000003b82f63072e4fad4e5170c1fda52"
            )
        )

    }

    override suspend fun getPlaylists(): Result<List<Playlist>> {
        return if (isSuccessFul) {
            Result.success(
                playlists
            )
        } else {
            Result.failure(Exception("some exception"))
        }
    }
}