package de.janaja.playlistpurger.data.remote.spotify.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenRequestResponseDto(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("token_type")
    val tokenType: String, // always "Bearer"
    val scope: String,
    @SerialName("expires_in")
    val expiresInSec: Int,
    @SerialName("refresh_token")
    val refreshToken: String = ""
)
// swap code for token
/*
access_token	string	An access token that can be provided in subsequent calls, for example to Spotify Web API services.
token_type	string	How the access token may be used: always "Bearer".
scope	string	A space-separated list of scopes which have been granted for this access_token
expires_in	int	The time period (in seconds) for which the access token is valid.
refresh_token	string	See refreshing tokens.
 */

// refresh
/*
{
  access_token: 'BQBLuPRYBQ...BP8stIv5xr-Iwaf4l8eg',
  token_type: 'Bearer',
  expires_in: 3600,
  refresh_token: 'AQAQfyEFmJJuCvAFh...cG_m-2KTgNDaDMQqjrOa3', // OPTIONAL
  scope: 'user-read-email user-read-private'
}
 */
