package de.janaja.playlistpurger.features.auth.di

import de.janaja.playlistpurger.features.auth.domain.usecase.LoginWithCodeUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.LogoutUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.ObserveLoginResponseResultUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.ObserveUserLoginStateUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.RefreshTokenUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


val authModule = module {
    factoryOf(::LoginWithCodeUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::ObserveLoginResponseResultUseCase)
    factoryOf(::ObserveUserLoginStateUseCase)
    factoryOf(::RefreshTokenUseCase)
}