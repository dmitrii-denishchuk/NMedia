//package ru.netology.nmedia.data
//
//import android.app.Application
//import android.content.Context
//import androidx.core.content.edit
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.MutableLiveData
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import ru.netology.nmedia.dto.Post
//import ru.netology.nmedia.viewModel.PostViewModel
//import java.math.RoundingMode
//import kotlin.properties.Delegates
//
//class InFilePostRepository(
//    private val application: Application
//) : PostRepository {
//
//    private val gson = Gson()
//    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
//
//    private val prefs = application.getSharedPreferences(
//        "repo", Context.MODE_PRIVATE
//    )
//
//    private var nextId by Delegates.observable(
//        prefs.getInt(NEXT_ID_PREFS_KEY, 0)
//    ) { _, _, newValue ->
//        prefs.edit {
//            putInt(NEXT_ID_PREFS_KEY, newValue)
//        }
//    }
//
//    override val data: MutableLiveData<List<Post>>
//
//    private var posts
//        get() = checkNotNull(data.value)
//        set(value) {
//            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
//                .bufferedWriter().use {
//                    it.write(gson.toJson(value))
//                }
//            data.value = value
//        }
//
//    init {
//        val postFile = application.filesDir.resolve(FILE_NAME)
//        val posts: List<Post> = if (postFile.exists()) {
//            val inputStream = application.openFileInput(FILE_NAME)
//            val reader = inputStream.bufferedReader()
//            reader.use { gson.fromJson(it, type) }
//        } else emptyList()
//        data = MutableLiveData(posts)
//    }
//
//    override fun likeById(id: Int) {
//        posts = posts.map { post ->
//            if (post.id == id)
//                post.copy(
//                    isLiked = !post.isLiked,
//                    likes = if (post.isLiked) post.likes - 1 else post.likes + 1
//                )
//            else post
//        }
//    }
//
//    override fun shareById(id: Int) {
//        posts = posts.map { post ->
//            if (post.id == id) post.copy(shares = post.shares + 1)
//            else post
//        }
//    }
//
//    override fun removeById(id: Int) {
//        posts = posts.filter { it.id != id }
//    }
//
//    override fun save(post: Post) {
//        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
//    }
//
//    override fun views(id: Int) {
//        posts = posts.map { post ->
//            if (post.id == id) post.copy(views = post.views + 1)
//            else post
//        }
//    }
//
//    fun insert(post: Post) {
//        posts = listOf(post.copy(id = ++nextId)) + posts
//    }
//
//    fun update(post: Post) {
//        posts = posts.map {
//            if (it.id == post.id) post else it
//        }
//    }
//
//    private companion object {
//        const val NEXT_ID_PREFS_KEY = "posts"
//        const val FILE_NAME = "posts.json"
//    }
//}
//
//fun slicer(count: Int): String {
//    return when (count) {
//        in 1_000..999_999 ->
//            if ((count.toDouble() / 1_000).toBigDecimal().setScale(1, RoundingMode.DOWN)
//                    .toDouble() * 10 % 10 == 0.0
//            )
//                (count / 1_000).toString() + "K"
//            else ((count.toDouble() / 1_000).toBigDecimal()
//                .setScale(1, RoundingMode.DOWN)).toString() + "K"
//        in 1_000_000..999_999_999 ->
//            if ((count.toDouble() / 1_000_000).toBigDecimal().setScale(1, RoundingMode.DOWN)
//                    .toDouble() * 10 % 10 == 0.0
//            )
//                (count / 1_000_000).toString() + "M"
//            else ((count.toDouble() / 1_000_000).toBigDecimal()
//                .setScale(1, RoundingMode.DOWN)).toString() + "M"
//        else -> count.toString()
//    }
//}