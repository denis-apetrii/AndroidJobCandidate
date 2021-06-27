package app.storytel.candidate.com.ui.feature.details

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.storytel.candidate.com.R
import app.storytel.candidate.com.repository.model.Resource
import app.storytel.candidate.com.ui.DataViewModel

class PostDetailsPresenter (private val view: PostDetailContract.View) : PostDetailContract.Presenter {

    private lateinit var context: Context
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var dataViewModel: DataViewModel

    override fun attach(activity: FragmentActivity) {
        context = activity
        lifecycleOwner = activity

        dataViewModel = ViewModelProvider(activity).get(DataViewModel::class.java)
    }

    override fun getComments(postId: Int) {
        dataViewModel.getComments(postId).observe(lifecycleOwner, Observer { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    val data = resource.data
                    if (data != null) {
                        view.onCommentsReceived(data)
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