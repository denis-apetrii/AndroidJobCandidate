package app.storytel.candidate.com

import android.app.Application
import app.storytel.candidate.com.repository.DataRemoteRepository
import app.storytel.candidate.com.repository.RetrofitService
import app.storytel.candidate.com.repository.isNetworkAvailable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class BookyfeverApp : Application() {

    companion object {
        // Getter for instance of application class.
        lateinit var instance: BookyfeverApp
            private set

        private lateinit var client: OkHttpClient
        lateinit var remoteRepository: DataRemoteRepository
    }

    override fun onCreate() {
        super.onCreate()

        // Set instance, so we can access it from companion object.
        instance = this

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Create OkHttp client
        client = RetrofitService.createOkHttpClient(
            interceptor,
            { this.isNetworkAvailable() },
            getString(R.string.error_no_network_connection)
        ).build()

        // Create remote item repository.
        remoteRepository = DataRemoteRepository(
            applicationContext,
            RetrofitService.createService(client)
        )
    }
}