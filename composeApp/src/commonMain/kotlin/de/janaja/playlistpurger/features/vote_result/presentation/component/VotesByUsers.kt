package de.janaja.playlistpurger.features.vote_result.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.shared.domain.model.User
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import org.jetbrains.compose.resources.painterResource

@Composable
fun VotesByUsers(
    usersByVoteOption: Map<VoteOption, List<User?>>,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {

        VoteOption.entries.forEach { vote ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(vote.resource),
                    contentDescription = vote.contentDescription,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .scale(scaling),
//                    tint = backgroundColor
                )
                usersByVoteOption[vote]?.forEach { user ->
                    // TODO replace with image later
                    Text(
                        user?.name?.split(" ")?.joinToString { it.first().toString().uppercase() } ?: "UU",
                        modifier = Modifier.size(24.dp).clip(CircleShape).background(Color.Cyan),
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
}