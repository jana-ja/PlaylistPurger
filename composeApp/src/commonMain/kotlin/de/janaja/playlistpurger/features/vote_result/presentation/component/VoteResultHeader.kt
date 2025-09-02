package de.janaja.playlistpurger.features.vote_result.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.M
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

    val gridPadding = remember { 4 }
    val userImgSize = remember { 32 }

        Column(
            modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f).height((2*userImgSize + gridPadding).dp),
                    verticalArrangement = Arrangement.spacedBy(gridPadding.dp),
                    horizontalArrangement = Arrangement.spacedBy(gridPadding.dp)
                ) {
                    items(playlistVoteResults.collaborators) { user ->
//                        Text(user.id.first().toString())
//                        Box(Modifier.size(4.dp).background(Color.Red).padding(2.dp)) {}
                        CircleUserImage(
                            user,
                            modifier = Modifier
                                .size(userImgSize.dp)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
}
