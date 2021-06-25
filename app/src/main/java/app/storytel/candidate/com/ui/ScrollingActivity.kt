package app.storytel.candidate.com.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.ui.feature.main.MainContract
import app.storytel.candidate.com.ui.feature.main.MainPresenter
import app.storytel.candidate.com.R
import app.storytel.candidate.com.model.PostAndImages
import app.storytel.candidate.com.ui.adapters.PostAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ScrollingActivity: AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter

    private lateinit var mRecyclerView:RecyclerView
    private lateinit var mPostAdapter: PostAdapter
    private lateinit var mProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mProgressBar =  findViewById(R.id.loading_pb)

        mPostAdapter = PostAdapter(this)

        mRecyclerView = findViewById(R.id.recycler_view)
        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mPostAdapter
        }

        presenter = MainPresenter(this)
        presenter.attach(this)
        presenter.getPostsAndPhotos()
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
        mProgressBar.visibility = View.GONE
    }

    override fun onError(message: String) {
        Log.e("Error", message)
        mProgressBar.visibility = View.GONE
        showErrorDialog( message)
    }

    /**
     * Show an alert dialog with error message
     * On neutral button click, dismiss dialog and refresh data
     */
    private fun showErrorDialog( message: String) {

        val dialog = MaterialAlertDialogBuilder(this).apply {
            setTitle(R.string.error_title)
            setMessage(message)

            setNeutralButton(R.string.error_retry_button) { dialog, _ ->
                dialog.dismiss()
                presenter.getPostsAndPhotos()
                mProgressBar.visibility = View.VISIBLE
            }
        }.create()
        dialog.show()
    }

}