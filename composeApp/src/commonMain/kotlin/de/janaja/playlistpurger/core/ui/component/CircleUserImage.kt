package de.janaja.playlistpurger.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.shared.domain.model.UserDetails
import de.janaja.playlistpurger.shared.domain.model.UserWithName
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.outline_question_mark_24

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircleUserImage(
    user: UserDetails,
    modifier: Modifier = Modifier.size(24.dp)
) {
    val tooltipState = rememberTooltipState(isPersistent = false)
    val scope = rememberCoroutineScope()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                val text = if (user is UserWithName) {
                    user.name
                } else {
                    "Unknown"
                }
                Text(text)
            }
        },
        state = tooltipState
    ) {
        Box(
            Modifier.clickable {
                scope.launch {
                    tooltipState.show()
                }
            }
        ) {
            if (user is UserDetails.Full && user.thumbnailUrl != null) {
                AsyncImage(
                    model = user.thumbnailUrl,
                    contentDescription = "Profile Picture of ${user.name}",
                    placeholder = painterResource(Res.drawable.outline_question_mark_24),
                    contentScale = ContentScale.Crop,
                    modifier = modifier.clip(CircleShape)
                )
            } else {
                Box(
                    modifier = modifier.clip(CircleShape).background(user.color),
                    contentAlignment = Alignment.Center
                ) {
                    val text = if (user is UserWithName) user.name.firstOrNull()?.lowercase()
                        ?: "-" else "?"
                    Text(
                        text = text,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}



