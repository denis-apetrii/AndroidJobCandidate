package app.storytel.candidate.com.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.storytel.candidate.com.BookyfeverApp
import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.model.PostAndImages
import app.storytel.candidate.com.repository.model.Resource

class DataViewModel: ViewModel() {

    private val repository = BookyfeverApp.remoteRepository

    /**
     * Fetch posts and photos form backend
     */
    fun getPostsAndPhotos(): LiveData<Resource<PostAndImages>> = repository.fetchPostAndPhotos()

    /**
     * Fetch comments for post form backend
     */
    fun getComments(postId: Int): LiveData<Resource<List<Comment>>> = repository.fetchComments(postId)
}