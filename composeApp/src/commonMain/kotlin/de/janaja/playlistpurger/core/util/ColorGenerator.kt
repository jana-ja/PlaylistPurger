package de.janaja.playlistpurger.core.util

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

object ColorGenerator {
    fun generatePastelColorFromSeed(seed: String): Color {
        val random = Random(seed.hashCode().toLong())

        // Hue: 0-360 degrees. Full range for variety.
        val hue = random.nextFloat() * 360f

        // Saturation: 0-1 (0% to 100%).
        // For pastels, keep saturation moderate to low.
        val saturation = random.nextFloat() * 0.3f + 0.4f // e.g., 0.4 to 0.7 (40% to 70%)

        // Lightness (for HSL) or Value (for HSV): 0-1 (0% to 100%).
        // For pastels, keep lightness high.
        val lightness = random.nextFloat() * 0.2f + 0.75f // e.g., 0.75 to 0.95 (75% to 95%)

        return Color.hsl(hue, saturation, lightness)
    }


    // A slightly different take focusing on vibrant but not too dark/light colors
    fun generateVibrantColorFromSeed(seed: String): Color {
        val random = Random(seed.hashCode().toLong())

        val hue = random.nextFloat() * 360f
        val saturation =
            random.nextFloat() * 0.5f + 0.5f // Saturation from 50% to 100% (more vibrant)
        val value =
            random.nextFloat() * 0.4f + 0.6f     // Value/Brightness from 60% to 100% (avoid too dark)

        return Color.hsv(hue, saturation, value)
    }
}