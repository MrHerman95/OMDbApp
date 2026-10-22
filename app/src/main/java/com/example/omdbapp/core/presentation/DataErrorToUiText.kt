package com.example.omdbapp.core.presentation

import com.example.omdbapp.R
import com.example.omdbapp.core.domain.util.DataError

fun DataError.toUiText(): UiText {
    val id = when (this) {
        DataError.Network.NO_INTERNET -> R.string.error_no_internet
        DataError.Network.REQUEST_TIMEOUT -> R.string.error_request_timeout
        DataError.Network.UNAUTHORIZED -> R.string.error_unauthorized
        DataError.Network.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        DataError.Network.NOT_FOUND -> R.string.error_not_found
        DataError.Network.SERVER_ERROR,
        DataError.Network.SERVICE_UNAVAILABLE -> R.string.error_server
        DataError.Network.SERIALIZATION -> R.string.error_serialization
        DataError.Local.DISK_FULL -> R.string.error_disk_full
        DataError.Local.NOT_FOUND -> R.string.error_not_found
        else -> R.string.error_unknown
    }
    return UiText.StringResource(id)
}
