package de.janaja.playlistpurger.core.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchTextField(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchImeAction: () -> Unit,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = modifier,
        label = {
            Text("Search")
        },
        trailingIcon = {
            IconButton(onClick = onSearchImeAction) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchImeAction()
            }
        ),
        singleLine = true,
        shape = RoundedCornerShape(20.dp)
    )

}