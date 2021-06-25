package app.storytel.candidate.com.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.databinding.PostItemBinding
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post
import app.storytel.candidate.com.model.PostAndImages
import app.storytel.candidate.com.ui.adapters.PostAdapter.PostViewHolder
import coil.load
import java.util.*

class PostAdapter(private val onSelectItem: (Post, Photo) -> Unit) :
    RecyclerView.Adapter<PostViewHolder>() {

    private var mData: PostAndImages? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        holder.title.text = mData!!.mPosts[position].title
        holder.body.text = mData!!.mPosts[position].body

        // Select a random image
        val index = Random().nextInt(mData!!.mPhotos.size - 1)
        val imageUrl = mData!!.mPhotos[index].thumbnailUrl
        holder.image.load(imageUrl)

        holder.body.setOnClickListener {
            onSelectItem(mData!!.mPosts[position], mData!!.mPhotos[index] )
        }
    }

    fun setData(data: PostAndImages?) {
        mData = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mData == null) 0 else mData!!.mPosts.size
    }

    inner class PostViewHolder constructor(
        vm: PostItemBinding
    ) : RecyclerView.ViewHolder(vm.root) {

        val title: TextView = vm.title
        val body: TextView = vm.body
        val image: ImageView = vm.image
    }
}