package de.janaja.playlistpurger.core.di

import de.janaja.playlistpurger.features.auth.data.helper.SpotifyOAuthResponseHelper
import org.koin.mp.KoinPlatform.getKoin

fun provideLoginResponseHelper(): SpotifyOAuthResponseHelper = getKoin().get()