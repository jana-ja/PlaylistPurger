package de.janaja.playlistpurger.shared.data.model

import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.TrackAdder
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackWrapperDto(
    val track: TrackDto,
    @SerialName("added_by")
    val addedBy: UserRefDto,
)

fun TrackWrapperDto.toTrack(voteOption: VoteOption?, addedBy: TrackAdder): Track {
    return Track(
        id = this.track.id,
        name = this.track.name,
        durationMillis = this.track.durationMillis,
        album = this.track.album.toAlbum(),
        artists = this.track.artists.map { it.toArtist() },
        vote = voteOption,
        addedBy = addedBy
    )
}
