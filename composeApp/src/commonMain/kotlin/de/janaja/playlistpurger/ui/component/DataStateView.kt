package de.janaja.playlistpurger.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.ui.DataState

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