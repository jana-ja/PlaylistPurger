package de.janaja.playlistpurger.data.model

import de.janaja.playlistpurger.R

enum class VoteOption(val imgResId: Int, val contentDescription: String) {
    KEEP(imgResId = R.drawable.baseline_check_24, contentDescription = "vote keep track"),
    DONT_CARE(imgResId = R.drawable.outline_circle_24, contentDescription = "vote dont care"),
    REMOVE(imgResId = R.drawable.baseline_close_24, contentDescription = "vote remove song")
}