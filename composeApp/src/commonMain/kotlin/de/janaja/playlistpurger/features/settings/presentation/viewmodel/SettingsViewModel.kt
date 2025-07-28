package de.janaja.playlistpurger.features.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.features.auth.domain.usecase.LogoutUseCase
import de.janaja.playlistpurger.features.settings.domain.Settings
import de.janaja.playlistpurger.features.settings.domain.usecase.ObserveSettingsUseCase
import de.janaja.playlistpurger.features.settings.domain.usecase.UpdateShowSwipeFirstSettingUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// use case and Settings data class
// new client secret!
class SettingsViewModel(
    observeSettingsUseCase: ObserveSettingsUseCase,
    private val updateShowSwipeFirstSettingUseCase: UpdateShowSwipeFirstSettingUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val settings = observeSettingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Settings()
        )


    fun updateShowSwipeFirst(newValue: Boolean) {
        viewModelScope.launch {
            updateShowSwipeFirstSettingUseCase(newValue)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

}