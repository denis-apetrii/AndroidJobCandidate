package app.storytel.candidate.com

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission
import app.storytel.candidate.com.repository.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

/**
 * Execute request safely while catching any thrown exception or error.
 * @param request Request that can throw exceptions when executed.
 * @param onSuccess Callback for successful response.
 * @param onFailed Callback for failed/error response.
 * @param onException Callback for caught exceptions.
 */
suspend fun <T, R> executeSafely(
    request: suspend () -> Response<T>,
    onSuccess: (T?) -> Resource<R>,
    onFailed: (errorMessage: String?, T?) -> Resource<R>,
    onException: (error: Exception) -> Resource<R>
): Resource<R> {
    return try {
        // Perform request.
        val response = request()
        // Check if request was successful.
        if (response.isSuccessful) {
            // Return response body.
            onSuccess(response.body())
        } else {
            // Get message from error response without blocking main thread.
            val errorMessage = withContext(Dispatchers.IO) {
                // Get JSON error response.
                response.errorBody()?.string()?.let { rawJsonError ->
                    // Extract error message from error response.
                    // This needs to be adjusted to how each API handles error messages, if any.
                    JSONObject(rawJsonError).getString("message")
                }
            }
            // Return error message.
            onFailed(errorMessage ?: "Unsuccessful network request", response.body())
        }
    } catch (error: Exception) {
        // Return caught exception, e.g. from interceptors.
        onException(error)
    }
}

/**
 * Check is network is available.
 */
@Suppress("DEPRECATION")
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isNetworkAvailable(): Boolean {
    var result = false
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connectivityManager?.run {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
    } else {
        connectivityManager?.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    else -> false
                }
            }
        }
    }
    return result
}