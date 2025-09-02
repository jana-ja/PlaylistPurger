package de.janaja.playlistpurger.core.ui.component

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

/*
val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
Scaffold gets Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
TopAppBar get scrollBehavior = scrollBehavior
=> connection between scrolling inside Scaffold and TopAppBar
- app bar consumes some of the scroll delta to change its own heightOffset (making it collapse or expand), remaining delta is then passed content to scroll

ScrollBehavior has state
- state.heightOffset => vertical scroll offset of the app bar from its fully expanded position
    -> 0 = fully expanded top bar
    -> -X = top bar X pixels scrolled/moved up
- state.collapsedFraction => between 0.0 and 1.0, how collapsed is the top bar
    collapsedFraction = (heightOffset / heightOffsetLimit).coerceIn(0f, 1f)
    for animation
- state.contentOffset: Float: (ignore for now)


scrollbehavior.state must now the maxHeight and minHeight to calculate collapsedFraction and heightOffset
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsableTopAppBar(
    title: @Composable (() -> Unit),
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit) = { },
    actions: @Composable (RowScope.() -> Unit) = {},
    colors: TopAppBarColors = TopAppBarDefaults.mediumTopAppBarColors(),
    contentMaxHeight: Dp = 80.dp,
    content: @Composable (() -> Unit)
) {
    val density = LocalDensity.current

    val windowInsetsPadding = TopAppBarDefaults.windowInsets.asPaddingValues(density)

    val topInset = windowInsetsPadding.calculateTopPadding()
    // min size of TopBar
    val collapsedHeight = TopAppBarDefaults.TopAppBarExpandedHeight + topInset
    // max size of TopBar
    val expandedHeight = collapsedHeight + contentMaxHeight


    val expandedHeightPx: Float
    val collapsedHeightPx: Float
//    val titleBottomPaddingPx: Int
    density.run {
        expandedHeightPx = expandedHeight.toPx()
        collapsedHeightPx = collapsedHeight.toPx()
//        titleBottomPaddingPx = titleBottomPadding.roundToPx()
    }

    // Sets the app bar's height offset limit to hide just the bottom title area and keep top title
    // visible when collapsed.
    SideEffect {
        if (scrollBehavior.state.heightOffsetLimit != collapsedHeightPx - expandedHeightPx) {
            scrollBehavior.state.heightOffsetLimit = collapsedHeightPx - expandedHeightPx
        }
    }

    // size animations
    val currentAppBarHeight = lerp(expandedHeight, collapsedHeight, scrollBehavior.state.collapsedFraction)
    val currentFilterUiHeight = lerp(contentMaxHeight, 0.dp, scrollBehavior.state.collapsedFraction)

    // color
    val color = androidx.compose.ui.graphics.lerp(
        colors.containerColor,
        colors.scrolledContainerColor,
        FastOutLinearInEasing.transform(scrollBehavior.state.collapsedFraction)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(currentAppBarHeight),

        color = color,
//        tonalElevation = if (scrollBehavior.state.collapsedFraction < 1f) 2.dp else 0.dp // Example
    ) {
        Column {
            TopAppBar(
                navigationIcon = navigationIcon,
                title = title,
                actions = actions,
                colors = TopAppBarDefaults.mediumTopAppBarColors()
                    .copy(containerColor = color, scrolledContainerColor = color)
            )
            /*
            inset of TopAppBar
            navIcon has 4.dp horizontal padding, nothing more
            title has 4.dp horizontal padding + max(width of navIcon, TopAppBarTitleInset = 16.dp - TopAppBarHorizontalPadding = 12.dp)
             */
            if (currentFilterUiHeight > 0.dp) { // Only compose if there's space
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .graphicsLayer {
                            alpha = 1f - scrollBehavior.state.collapsedFraction
                            translationY = - (expandedHeightPx - collapsedHeightPx) * scrollBehavior.state.collapsedFraction / 2
                        }
                        .clipToBounds()
                ) {
                    // for fixed height
                    Box(
                        modifier = Modifier.requiredHeight(expandedHeight-collapsedHeight)
                    ) {
                        content()
                    }
                }
            }

        }
    }
}