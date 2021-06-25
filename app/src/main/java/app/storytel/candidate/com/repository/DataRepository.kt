package app.storytel.candidate.com.repository

import androidx.lifecycle.LiveData
import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.repository.model.Resource
import app.storytel.candidate.com.model.PostAndImages

interface DataRepository {

    /**
     * Fetch posts and images and return life data with both data
     */
    fun fetchPostAndPhotos(): LiveData<Resource<PostAndImages>>

    /**
     * Fetch comments for a given post id
     */
    fun fetchComments(postId: Int): LiveData<Resource<List<Comment>>>
}