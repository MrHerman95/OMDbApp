package com.example.sdk.ads.provider

sealed interface AdResult<out T> {
    data class Success<out T>(val ad: T) : AdResult<T>
    data class Failure(val error: AdError) : AdResult<Nothing>
}

enum class AdError {
    NO_FILL,
    NETWORK_ERROR,
    INVALID_REQUEST,
    NOT_CONFIGURED,
    INTERNAL_ERROR,
    UNKNOWN
}
