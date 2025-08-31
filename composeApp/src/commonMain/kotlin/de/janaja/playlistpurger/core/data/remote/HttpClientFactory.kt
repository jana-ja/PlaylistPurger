package de.janaja.playlistpurger.core.data.remote

import de.janaja.playlistpurger.features.auth.domain.repo.TokenRepo
import de.janaja.playlistpurger.features.playlist_overview.domain.model.TokenState
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun createGeneralClient(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            applyCommonPlugins()
        }
    }

    // TODO third client for account api with basic auth

    fun createAuthClient(engine: HttpClientEngine, tokenRepo: TokenRepo): HttpClient {
        return HttpClient(engine) {
            applyCommonPlugins()

            defaultRequest {
                url("https://api.spotify.com/v1/")
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        println("Ktor Auth: loadTokens called.")

                        // Wait for the token state to be definitive (not Loading)
                        val definitiveTokenState = tokenRepo.accessTokenFlow.first {
                            it !is TokenState.Loading
                        }
                        if (definitiveTokenState is TokenState.HasToken) {
                            BearerTokens(
                                definitiveTokenState.token,
                                null // refresh token not needed here
                            )
                        } else {
                            null
                        }
                    }

                    refreshTokens {
                        val bla = tokenRepo.refreshAccessToken()
                        bla.fold(
                            onSuccess = {
                                BearerTokens(
                                    accessToken = it,
                                    refreshToken = null // refresh token not needed here
                                )
                            },
                            onFailure = {
                                // when refresh definitively fails its handled by tokenRepo -> clearTokens
                                // errors that get here might be temporarily, throw and let error handling handle this
                                throw it
                            }
                        )
                    }

                    sendWithoutRequest { request ->
                        val currentTokenState = tokenRepo.accessTokenFlow.value
                        val attemptAuth = currentTokenState is TokenState.HasToken
                        println("Auth: sendWithoutRequest currentTokenState: $currentTokenState, attemptAuth: $attemptAuth")
                        attemptAuth
                        // sends unauthenticated requests when user is not logged in (no token)
                        // when tokenState is loaded -> return true -> wants to make authenticated request -> calls loadTokens to get tokens
                        // => initial loadTokens happens only when there actually is a token to load
                    }
                }
            }
        }
    }

    private fun HttpClientConfig<*>.applyCommonPlugins() {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 20_000L
            requestTimeoutMillis = 20_000L
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = LogLevel.INFO
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
}