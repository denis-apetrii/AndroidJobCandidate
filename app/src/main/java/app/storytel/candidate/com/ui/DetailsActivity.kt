package app.storytel.candidate.com.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import app.storytel.candidate.com.R
import app.storytel.candidate.com.databinding.ActivityDetailsBinding
import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post
import app.storytel.candidate.com.ui.feature.details.PostDetailContract
import app.storytel.candidate.com.ui.feature.details.PostDetailsPresenter
import coil.load

class DetailsActivity : AppCompatActivity(), PostDetailContract.View {
    companion object {
        private const val ARGS_POST = "post"
        private const val ARGS_PHOTO = "photo"

        fun newIntent(context: Context, post: Post, photo: Photo): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(ARGS_POST, post)
                putExtra(ARGS_PHOTO, photo)
            }
        }
    }

    private var mPost: Post? = null
    private var mPhoto: Photo? = null

    private lateinit var mPresenter: PostDetailContract.Presenter

    private lateinit var  vm: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(vm.root)

        setSupportActionBar(vm.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Get extras
        mPost = intent.extras?.getParcelable(ARGS_POST)
        mPhoto = intent.extras?.getParcelable(ARGS_PHOTO)

        // Display data
        title = mPost?.title
        vm.backdrop.load(mPhoto?.thumbnailUrl)
        vm.details.text = mPost?.body

        mPresenter = PostDetailsPresenter(this)
        mPresenter.attach(this)
        mPost?.let { mPresenter.getComments(it.id) }
    }

    override fun onCommentsReceived(data: List<Comment>) {
        vm.progressBar.loadingPb.visibility = View.GONE

        val cards: Array<Triple<CardView, TextView, TextView>> = arrayOf(
            Triple(vm.comment1, vm.title1, vm.description1),
            Triple(vm.comment2, vm.title2, vm.description2),
            Triple(vm.comment3, vm.title3, vm.description3)
        )

        // For each card set visible and display info
        data.take(3).forEachIndexed { index, comment ->
            cards[index].first.visibility = View.VISIBLE
            cards[index].second.text = comment.name
            cards[index].third.text = comment.body
        }
    }

    override fun onError(message: String) {
        Log.e("Error", message)
        vm.progressBar.loadingPb.visibility = View.GONE
        showErrorDialog( this, message, R.string.error_ok_button) { }
    }
}