package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.model.Vote

interface VoteRepo {

    fun getVotesForPlaylist(playlistId: String, userId: String): List<Vote>
}