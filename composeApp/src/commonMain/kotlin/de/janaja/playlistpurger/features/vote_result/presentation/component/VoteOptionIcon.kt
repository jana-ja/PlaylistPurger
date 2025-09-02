package de.janaja.playlistpurger.features.vote_result.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import org.jetbrains.compose.resources.painterResource
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.outline_question_mark_24

@Composable
fun VoteOptionIcon(
    voteOption: VoteOption?,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(
            voteOption?.resource
                ?: Res.drawable.outline_question_mark_24
        ),
        contentDescription = voteOption?.contentDescription
            ?: "no final result yet",
        modifier = modifier.size(42.dp)
            .border(1.dp, Color.Black, CircleShape).padding(8.dp)
    )
}
