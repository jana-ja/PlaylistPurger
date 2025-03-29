package de.janaja.playlistpurger.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.janaja.playlistpurger.data.model.PlaylistResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val BASE_URL = "https://api.spotify.com/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// neu für logging:
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    // Logging Levels: BODY, BASIC, NONE, HEADERS
    level = HttpLoggingInterceptor.Level.BODY
}
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface SpotifyApiService {


    @GET("me/playlists")
    suspend fun getCatImagesWithHeader(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 5
    ): PlaylistResponse


}

object SpotifyApi {
    val retrofitService: SpotifyApiService by lazy { retrofit.create(SpotifyApiService::class.java) }
}
