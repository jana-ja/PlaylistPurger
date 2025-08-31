package de.janaja.playlistpurger.features.auth.di

import de.janaja.playlistpurger.features.auth.domain.usecase.InitiateThirdPartyAuthUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.LoginWithCodeUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.LogoutUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.ObserveThirdPartyAuthResultUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.ObserveUserLoginStateUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


val authModule = module {
    factoryOf(::InitiateThirdPartyAuthUseCase)
    factoryOf(::LoginWithCodeUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::ObserveThirdPartyAuthResultUseCase)
    factoryOf(::ObserveUserLoginStateUseCase)
}