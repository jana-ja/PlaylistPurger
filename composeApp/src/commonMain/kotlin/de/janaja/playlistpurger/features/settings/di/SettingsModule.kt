package de.janaja.playlistpurger.features.settings.di

import de.janaja.playlistpurger.features.settings.data.repo.DataStoreSettingsRepo
import de.janaja.playlistpurger.features.settings.domain.repo.SettingsRepo
import de.janaja.playlistpurger.features.settings.domain.usecase.ObserveSettingsUseCase
import de.janaja.playlistpurger.features.settings.domain.usecase.UpdateShowSwipeFirstSettingUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val settingsModule = module {
    single<SettingsRepo> {
        DataStoreSettingsRepo(get())
    }

    factoryOf(::ObserveSettingsUseCase)
    factoryOf(::UpdateShowSwipeFirstSettingUseCase)
}