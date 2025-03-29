package de.janaja.playlistpurger.data.model

data class TracksResponse(
    val total: Int,
    val items: List<TrackWrapper>
)
/*
{
  "href": "https://api.spotify.com/v1/me/shows?offset=0&limit=20",
  "limit": 20,
  "next": "https://api.spotify.com/v1/me/shows?offset=1&limit=1",
  "offset": 0,
  "previous": "https://api.spotify.com/v1/me/shows?offset=1&limit=1",
  "total": 4,
  "items":
 */