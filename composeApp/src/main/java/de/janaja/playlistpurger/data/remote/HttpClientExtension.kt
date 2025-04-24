package de.janaja.playlistpurger.data.remote

import de.janaja.playlistpurger.domain.exception.DataException
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T> {
    val response = try {
        execute()
    } catch(e: SocketTimeoutException) {
        return Result.failure(DataException.Remote.RequestTimeout)
    } catch(e: UnresolvedAddressException) {
        return Result.failure(DataException.Remote.NoInternet)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.failure(DataException.Remote.Unknown)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                Result.success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                Result.failure(DataException.Remote.Serialization)
            }
        }
        401 -> Result.failure(DataException.Remote.InvalidAccessToken)
        408 -> Result.failure(DataException.Remote.RequestTimeout)
        429 -> Result.failure(DataException.Remote.TooManyRequests)
        in 500..599 -> Result.failure(DataException.Remote.Server)
        else -> Result.failure(DataException.Remote.Unknown)
    }
}