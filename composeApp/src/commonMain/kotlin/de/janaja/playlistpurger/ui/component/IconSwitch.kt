package de.janaja.playlistpurger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.R

@Composable
fun IconSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(5.dp)
) {

    val checkedColor = MaterialTheme.colorScheme.onPrimary
    val checkedBackground = MaterialTheme.colorScheme.primary

    val uncheckedColor = MaterialTheme.colorScheme.onSurface
    val uncheckedBackground = MaterialTheme.colorScheme.surfaceDim

    val size = 24.dp

    val selectedModifier = Modifier
        .clip(shape)
        .background(color = checkedBackground)
        .padding(4.dp)
        .padding(horizontal = 2.dp)
        .size(size)
    val unselectedModifier = Modifier
        .padding(4.dp)
        .size(size)

    Row(
        modifier
            .height(IntrinsicSize.Min)
//            .border(width = Dp.Hairline, color = MaterialTheme.colorScheme.outline, shape = shape)
            .clip(shape)
            .background(color = uncheckedBackground)
    ) {

        // unchecked
        Icon(
            Icons.Default.Menu,
            contentDescription = "",
            modifier = (if (checked) unselectedModifier else selectedModifier)
                .clickable { onCheckedChange(false) }
            ,
            tint = if (checked) uncheckedColor else checkedColor
        )


//        VerticalDivider()
        // checked
        Icon(
            painterResource(R.drawable.round_rectangle_24),
            "",
            modifier = (if (checked) selectedModifier else unselectedModifier)
                .clickable { onCheckedChange(true) }
            ,
            tint = if (checked) checkedColor else uncheckedColor
        )
    }

}
