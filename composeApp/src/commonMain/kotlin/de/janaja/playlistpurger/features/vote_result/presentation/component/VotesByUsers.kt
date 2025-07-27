package de.janaja.playlistpurger.features.vote_result.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.core.ui.component.CircleAsyncImage
import de.janaja.playlistpurger.shared.domain.model.User
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import org.jetbrains.compose.resources.painterResource

@Composable
fun VotesByUsers(
    usersByVoteOption: Map<VoteOption, List<User?>>,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        VoteOption.entries.forEach { vote ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    painter = painterResource(vote.resource),
                    contentDescription = vote.contentDescription,
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(Modifier.width(4.dp))
                usersByVoteOption[vote]?.forEach { user ->
                    user?.let { user ->
                        CircleAsyncImage(
                            url = user.thumbnailImage ?: "", // TODO
                            contentDescription = user.name,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}