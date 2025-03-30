package de.janaja.playlistpurger.data.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepo {
    val tokenFlow: Flow<String?>
}