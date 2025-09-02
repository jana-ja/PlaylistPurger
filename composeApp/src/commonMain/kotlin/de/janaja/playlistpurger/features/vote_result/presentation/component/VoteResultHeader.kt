package de.janaja.playlistpurger.features.vote_result.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.core.ui.component.CircleUserImage
import de.janaja.playlistpurger.shared.domain.model.PlaylistVoteResults
import de.janaja.playlistpurger.shared.domain.model.VoteOption

@Composable
fun VoteResultHeader(
    playlistVoteResults: PlaylistVoteResults,
    modifier: Modifier = Modifier
) {

    // TODO so eine map gibt es jeweils schon im TrackListRepo, zu PlaylistVoteResults hinzufügen?
    val votesByVoteOption = playlistVoteResults.tracksWithVotes.map { it.second }.groupBy { it.resultOption }

        Column(
            modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(playlistVoteResults.collaborators) { user ->
                    CircleUserImage(
                        user,
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                VoteOptionResultOverview(
                    votesByVoteOption[VoteOption.KEEP]?.size ?: 0,
                    VoteOption.KEEP
                )
                VoteOptionResultOverview(
                    votesByVoteOption[VoteOption.DONT_CARE]?.size ?: 0,
                    VoteOption.DONT_CARE
                )
                VoteOptionResultOverview(
                    votesByVoteOption[VoteOption.REMOVE]?.size ?: 0,
                    VoteOption.REMOVE
                )
                VoteOptionResultOverview(
                    votesByVoteOption[null]?.size ?: 0,
                    null
                )
            }
        }
}
