package app.storytel.candidate.com.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.R
import app.storytel.candidate.com.model.PostAndImages
import app.storytel.candidate.com.ui.DetailsActivity
import app.storytel.candidate.com.ui.adapters.PostAdapter.PostViewHolder
import coil.load
import java.util.*

class PostAdapter(private val context: Context) :
    RecyclerView.Adapter<PostViewHolder>() {

    private var mData: PostAndImages? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        holder.title.text = mData!!.mPosts[position].title
        holder.body.text = mData!!.mPosts[position].body

        // Select a random image
        val index = Random().nextInt(mData!!.mPhotos.size - 1)
        val imageUrl = mData!!.mPhotos[index].thumbnailUrl
        holder.image.load(imageUrl)

        holder.body.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    DetailsActivity::class.java
                )
            )
        }
    }

    fun setData(data: PostAndImages?) {
        mData = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mData == null) 0 else mData!!.mPosts.size
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.title)
        var body: TextView = itemView.findViewById(R.id.body)
        var image: ImageView = itemView.findViewById(R.id.image)
    }
}