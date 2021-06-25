package app.storytel.candidate.com.ui.feature.main

import androidx.fragment.app.FragmentActivity
import app.storytel.candidate.com.model.PostAndImages

interface MainContract {

    interface Presenter {

        /**
         * Configure lifecycle owner
         */
        fun attach(activity: FragmentActivity)

        /**
         * Get posts and photos.
         */
        fun getPostsAndPhotos()

    }

    interface View {
        /**
         * Callback for received post list.
         */
        fun onPostsAndPhotosReceived(data: PostAndImages)


        /**
         * An abstract error received.
         */
        fun onError(message: String)
    }
}