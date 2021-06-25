package app.storytel.candidate.com.ui.feature.details

import androidx.fragment.app.FragmentActivity
import app.storytel.candidate.com.model.Comment

interface PostDetailContract {

    interface Presenter {

        /**
         * Configure lifecycle owner
         */
        fun attach(activity: FragmentActivity)

        /**
         * Get comments for post.
         */
        fun getComments(postId: Int)

    }

    interface View {
        /**
         * Callback for received comments list.
         */
        fun onCommentsReceived(data: List<Comment>)


        /**
         * An abstract error received.
         */
        fun onError(message: String)
    }
}