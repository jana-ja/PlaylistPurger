package de.janaja.playlistpurger.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.features.vote_result.presentation.component.makePastelByMixing
import de.janaja.playlistpurger.shared.domain.model.User
import org.jetbrains.compose.resources.painterResource
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.outline_question_mark_24

@Composable
fun CircleUserImage(
    user: User,
    modifier: Modifier = Modifier.size(24.dp)
) {

    if (user.thumbnailImage != null) {
        AsyncImage(
            model = user.thumbnailImage,
            contentDescription = "Profile Picture of ${user.name}",
            placeholder = painterResource(Res.drawable.outline_question_mark_24),
            modifier = modifier.clip(CircleShape)
        )
    } else {
        Text(
            text = user.name.firstOrNull()?.lowercase() ?: "",
            modifier = modifier.clip(CircleShape).background(Color.Cyan.makePastelByMixing()),
            textAlign = TextAlign.Center
        )
    }
}