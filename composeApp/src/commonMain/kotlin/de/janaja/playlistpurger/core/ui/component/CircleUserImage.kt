package de.janaja.playlistpurger.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.core.util.ColorGenerator.generatePastelColorFromSeed
import de.janaja.playlistpurger.shared.domain.model.UserDetails
import org.jetbrains.compose.resources.painterResource
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.outline_question_mark_24

@Composable
fun CircleUserImage(
    user: UserDetails,
    modifier: Modifier = Modifier.size(24.dp)
) {

    if (user is UserDetails.Full) {
        AsyncImage(
            model = user.thumbnailUrl,
            contentDescription = "Profile Picture of ${user.name}",
            placeholder = painterResource(Res.drawable.outline_question_mark_24),
            contentScale = ContentScale.Crop,
            modifier = modifier.clip(CircleShape)
        )
    } else {
        Box(
            modifier = modifier.clip(CircleShape).background(generatePastelColorFromSeed(user.id)),
            contentAlignment = Alignment.Center
        ) {
            val text = if (user is UserDetails.Partial) user.name.firstOrNull()?.lowercase()
                ?: "-" else "?"
            Text(
                text = text,
                textAlign = TextAlign.Center
            )
        }
    }
}



