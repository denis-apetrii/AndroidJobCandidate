package app.storytel.candidate.com.repository

import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*



/**
 * Remote service.
 */
interface RetrofitService {

    companion object {
        // Base URL of service.
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

        /**
         * Create service for jsonplaceholder API.
         * @param debugNetworkInterceptor
         * @param isNetworkAvailable Higher-order function for determining if network is available.
         * @param noNetworkErrorMessage Optional message for missing network connection.
         * @return Configured client.
         */
        fun createOkHttpClient(
            debugNetworkInterceptor: Interceptor?,
            isNetworkAvailable: () -> Boolean,
            noNetworkErrorMessage: String? = "Network connection not available"
        ): OkHttpClient.Builder {
            return OkHttpClient.Builder().apply {

                // Interceptor for checking if network connection is available.
                addInterceptor  {  chain ->
                    if (!isNetworkAvailable()) {
                        throw UnknownHostException(noNetworkErrorMessage)
                    }
                    chain.proceed(chain.request())
                }

                // Add interceptor for debugging tool.
                if (debugNetworkInterceptor != null) {
                    addInterceptor(debugNetworkInterceptor)
                }

                // Timeouts for long running requests.
                connectTimeout(60, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
            }
        }

        /**
         * Create service for API.
         * @param okHttpClient Client on which to perform requests on base URL.
         * @return service.
         */
        fun createService(okHttpClient: OkHttpClient): RetrofitService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RetrofitService::class.java)
        }
    }


    /**
     * GET request for retrieving posts.
     */
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

    /**
     * GET request for retrieving photos.
     */
    @GET("photos")
    suspend fun getPhotos(): Response<List<Photo>>
}