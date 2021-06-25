package app.storytel.candidate.com.model

data class Comment(
    var postId: Int,
    var id: Int,
    var name: String,
    var email: String,
    var body: String
)