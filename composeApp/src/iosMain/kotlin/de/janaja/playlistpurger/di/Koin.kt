package de.janaja.playlistpurger.di

import de.janaja.playlistpurger.util.LoginResponseHelper
import org.koin.mp.KoinPlatform.getKoin

fun provideLoginResponseHelper(): LoginResponseHelper = getKoin().get()