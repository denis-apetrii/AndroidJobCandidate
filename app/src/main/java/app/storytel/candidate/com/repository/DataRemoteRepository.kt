package app.storytel.candidate.com.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.storytel.candidate.com.R
import app.storytel.candidate.com.repository.model.Resource
import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.model.PostAndImages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Implementation of data repository for remote data access.
 * @param context Application context.
 * @param dataService Service, where we can perform requests.
 */
class DataRemoteRepository(
    private val context: Context,
    private val dataService: RetrofitService
) : DataRepository {

    override fun fetchPostAndPhotos(): LiveData<Resource<PostAndImages>> {
        val mutableLiveData = MutableLiveData<Resource<PostAndImages>>()

        CoroutineScope(Dispatchers.Main).launch {

            val postsResponse = executeSafely(
                request = {
                    dataService.getPosts()
                },
                onSuccess = { data ->
                    Resource.success(data)
                },
                onFailed = { errorMessage, data ->
                    Resource.error(
                        errorMessage ?: context.getString(R.string.error_request_posts),
                        data
                    )
                },
                onException = { error ->
                    Resource.error(
                        error.localizedMessage ?: context.getString(R.string.error_request_posts)
                    )
                })

            val photoResponse = executeSafely(
                request = {
                    dataService.getPhotos()
                },
                onSuccess = { data ->
                    Resource.success(data)
                },
                onFailed = { errorMessage, data ->
                    Resource.error(
                        errorMessage ?: context.getString(R.string.error_request_photos),
                        data
                    )
                },
                onException = { error ->
                    Resource.error(
                        error.localizedMessage ?: context.getString(R.string.error_request_photos)
                    )
                }
            )

            if (postsResponse.status == Resource.Status.SUCCESS && photoResponse.status == Resource.Status.SUCCESS) {
                mutableLiveData.value = Resource.success(
                    PostAndImages(
                        postsResponse.data ?: mutableListOf(),
                        photoResponse.data ?: mutableListOf()
                    )
                )
            } else {
                if (postsResponse.status == Resource.Status.ERROR) {
                    mutableLiveData.value = Resource.error(postsResponse.errorMessage!!)
                } else {
                    mutableLiveData.value = Resource.error(photoResponse.errorMessage!!)
                }
            }

        }

        return mutableLiveData

    }

    override fun fetchComments(postId: Int): LiveData<Resource<List<Comment>>> {
        val mutableLiveData = MutableLiveData<Resource<List<Comment>>>()

        CoroutineScope(Dispatchers.Main).launch {
            mutableLiveData.value = executeSafely(
                request = {
                    dataService.getComments(postId)
                },
                onSuccess = { data ->
                    Resource.success(data)
                },
                onFailed = { errorMessage, data ->
                    Resource.error(
                        errorMessage ?: context.getString(R.string.error_request_comments),
                        data
                    )
                },
                onException = { error ->
                    Resource.error(
                        error.localizedMessage ?: context.getString(R.string.error_request_comments)
                    )
                }
            )
        }

        return mutableLiveData
    }
}
