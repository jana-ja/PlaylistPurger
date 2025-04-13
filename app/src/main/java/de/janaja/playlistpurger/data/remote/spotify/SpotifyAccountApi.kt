package de.janaja.playlistpurger.data.remote.spotify

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.janaja.playlistpurger.data.remote.spotify.model.TokenRequestResponseDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

private const val BASE_URL = "https://accounts.spotify.com/"

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

interface SpotifyAccountApiServiceRetrofit {

    @FormUrlEncoded
    @POST("api/token")
    suspend fun getToken(
        @Header("Authorization") client: String,
        @Header("Content-Type") type: String = "application/x-www-form-urlencoded",
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("grant_type") grantType: String = "authorization_code",

    ): TokenRequestResponseDto

    @FormUrlEncoded
    @POST("api/token")
    suspend fun refreshToken(
        @Header("Authorization") client: String,
        @Header("Content-Type") type: String = "application/x-www-form-urlencoded",
        @Field("refresh_token") refreshToken: String,
//        @Field("redirect_uri") redirectUri: String,
        @Field("grant_type") grantType: String = "refresh_token",

        ): TokenRequestResponseDto
}

object SpotifyAccountApi {
    val retrofitService: SpotifyAccountApiServiceRetrofit by lazy { retrofit.create(SpotifyAccountApiServiceRetrofit::class.java) }
}
