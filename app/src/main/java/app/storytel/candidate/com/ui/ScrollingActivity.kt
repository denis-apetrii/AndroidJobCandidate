package app.storytel.candidate.com.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.storytel.candidate.com.R
import app.storytel.candidate.com.databinding.ActivityScrollingBinding
import app.storytel.candidate.com.model.PostAndImages
import app.storytel.candidate.com.ui.adapters.PostAdapter
import app.storytel.candidate.com.ui.feature.main.MainContract
import app.storytel.candidate.com.ui.feature.main.MainPresenter

class ScrollingActivity: AppCompatActivity(), MainContract.View {

    private lateinit var mPresenter: MainContract.Presenter

    private lateinit var mPostAdapter: PostAdapter

    private lateinit var  vm: ActivityScrollingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(vm.root)

        setSupportActionBar(vm.toolbar)

        mPostAdapter = PostAdapter {
            post, photo -> startActivity(DetailsActivity.newIntent(this, post, photo))
        }

        vm.contentView.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mPostAdapter
        }

        mPresenter = MainPresenter(this)
        mPresenter.attach(this)
        mPresenter.getPostsAndPhotos()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_settings -> {
                Toast.makeText(this, R.string.settings_click_message, Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPostsAndPhotosReceived(data: PostAndImages) {
        mPostAdapter.setData(data)
        vm.progressBar.loadingPb.visibility = View.GONE
    }

    override fun onError(message: String) {
        Log.e("Error", message)
        vm.progressBar.loadingPb.visibility = View.GONE
        showErrorDialog( this, message, R.string.error_retry_button) {
            mPresenter.getPostsAndPhotos()
            vm.progressBar.loadingPb.visibility = View.VISIBLE }
    }
}