package com.example.omdbapp.core.data.networking

import com.example.omdbapp.core.domain.util.DataError
import com.example.omdbapp.core.domain.util.Result
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <T> safeCall(
    execute: suspend () -> Response<T>
): Result<T, DataError.Network> {
    val response = try {
        execute()
    } catch (e: UnknownHostException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: ConnectException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: SocketTimeoutException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.REQUEST_TIMEOUT)
    } catch (e: SerializationException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        e.printStackTrace()
        return Result.Error(DataError.Network.UNKNOWN)
    }

    return responseToResult(response)
}

fun <T> responseToResult(
    response: Response<T>
): Result<T, DataError.Network> {
    return when (response.code()) {
        in 200..299 -> {
            val body = response.body()
            if (body != null) {
                Result.Success(body)
            } else {
                Result.Error(DataError.Network.SERIALIZATION)
            }
        }
        400 -> Result.Error(DataError.Network.BAD_REQUEST)
        401 -> Result.Error(DataError.Network.UNAUTHORIZED)
        403 -> Result.Error(DataError.Network.FORBIDDEN)
        404 -> Result.Error(DataError.Network.NOT_FOUND)
        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
        409 -> Result.Error(DataError.Network.CONFLICT)
        413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
        429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
        503 -> Result.Error(DataError.Network.SERVICE_UNAVAILABLE)
        in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
        else -> Result.Error(DataError.Network.UNKNOWN)
    }
}
