package de.janaja.playlistpurger.features.vote_result.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.janaja.playlistpurger.shared.domain.model.VoteOption

@Composable
fun VoteOptionResultOverview(
    voteCount: Int,
    voteOption: VoteOption?,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        VoteOptionIcon(voteOption)
        Text("$voteCount")
    }
}
