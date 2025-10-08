package de.janaja.playlistpurger.features.vote_result.di

import de.janaja.playlistpurger.features.vote_result.domain.usecase.GetVoteResultsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val voteResultModule = module {
    factoryOf(::GetVoteResultsUseCase)
}