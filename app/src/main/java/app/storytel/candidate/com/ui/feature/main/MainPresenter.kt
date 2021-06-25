package app.storytel.candidate.com.ui.feature.main

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import app.storytel.candidate.com.ui.DataViewModel
import app.storytel.candidate.com.R
import app.storytel.candidate.com.repository.model.Resource

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    private lateinit var context: Context
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var dataViewModel: DataViewModel

    override fun attach(activity: FragmentActivity) {
        context = activity
        lifecycleOwner = activity

        dataViewModel = ViewModelProvider(activity).get(DataViewModel::class.java)
    }

    override fun getPostsAndPhotos() {
        dataViewModel.getPostsAndPhotos().observe(lifecycleOwner, Observer { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    val data = resource.data
                    if (data != null) {
                        view.onPostsAndPhotosReceived(data)
                    } else {
                        view.onError(context.getString(R.string.error_request_posts))
                    }
                }
                Resource.Status.ERROR -> {
                    view.onError(
                        resource.errorMessage ?: context.getString(R.string.error_request_posts)
                    )
                }
            }
        })
    }
}