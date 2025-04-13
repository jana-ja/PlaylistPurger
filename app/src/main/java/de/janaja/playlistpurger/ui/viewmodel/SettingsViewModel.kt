package de.janaja.playlistpurger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.domain.repository.AuthService
import de.janaja.playlistpurger.domain.repository.SettingsRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepo: SettingsRepo,
    private val authService: AuthService,
    ): ViewModel() {

    val showSwipeFirst = settingsRepo.showSwipeFirstFlow
        .map { it ?: true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = true
        )

    fun updateShowSwipeFirst(newValue: Boolean){
        viewModelScope.launch {
            settingsRepo.updateShowSwipeFirst(newValue)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authService.logout()
        }
    }

}