package de.janaja.playlistpurger.core.ui.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.janaja.playlistpurger.core.ui.model.DataState

@Composable
fun <T> DataStateView(
    dataState: DataState<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    when(val state = dataState) {
        is DataState.Error -> {
            Text(state.message.asString())
        }
        DataState.Loading -> {
            CircularProgressIndicator()
        }
        is DataState.Ready -> {
            content(state.data)
        }
    }
}