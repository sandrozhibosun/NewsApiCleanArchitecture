package com.example.newsappcleanarchitecture.utils.network

import retrofit2.Response

/**
 * A sealed class that encapsulates data with its loading state.
 *
 * The `Resource` class is used to represent the state of a data request,
 * which can be in one of the three states: Loading, Success, or Failure.
 * This approach simplifies handling data states and corresponding UI updates.
 *
 * @param T the type of data being requested.
 */
sealed class Resource<out T> {

    /**
     * Represents the loading state before the data is available.
     * Used to indicate that the data fetching process is ongoing.
     */
    data object Loading : Resource<Nothing>()

    /**
     * Represents the success state with the resulting data.
     *
     * @param value The data successfully fetched.
     */
    data class Success<out T>(val value: T) : Resource<T>()

    /**
     * Represents the failure state of a data request.
     *
     * @param isNetworkException Indicates whether the failure was due to a network exception.
     * @param errorCode The HTTP error code associated with the failure, if applicable.
     * @param errorDescription A human-readable description of the error.
     */
    data class Failure(
        val isNetworkException: Boolean,
        val errorCode: Int? = null,
        val errorDescription: String? = null
    ) : Resource<Nothing>()
}

/**
 * Converts a Retrofit [Response] to a [Resource] instance.
 *
 * This extension function is designed to simplify the process of converting
 * Retrofit's [Response] object into a [Resource] object, which is more suitable
 * for handling within the UI layer. It automatically handles success, error,
 * and exception scenarios.
 *
 * @param T The type of the response body.
 * @return A [Resource] object representing the result of the conversion.
 *         It returns [Resource.Success] with the body if the response is successful and not null,
 *         [Resource.Failure] with the error code and message if the response is not successful,
 *         or [Resource.Failure] with throwable details if an exception occurs during the process.
 */
fun <T> Response<T>.toResource(): Resource<T> {
    return try {
        if (this.isSuccessful && this.body() != null) {
            Resource.Success(this.body()!!)
        } else {
            Resource.Failure(false, this.code(), this.message())
        }
    } catch (throwable: Throwable) {
        Resource.Failure(true, null, throwable.message)
    }
}