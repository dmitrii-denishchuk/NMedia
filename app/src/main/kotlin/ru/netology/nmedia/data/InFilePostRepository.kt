package ru.netology.nmedia.data

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode
import java.util.*
import kotlin.properties.Delegates

class InFilePostRepository(
    private val application: Application
) : PostRepository {

    val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    private var nextId by Delegates.observable(
        prefs.getInt(NEXT_ID_PREFS_KEY, 0)
    ) { _, _, newValue ->
        prefs.edit {
            putInt(NEXT_ID_PREFS_KEY, newValue)
        }
    }

    override val data: MutableLiveData<List<Post>>

    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
                .bufferedWriter().use {
                    it.write(gson.toJson(value))
                }
            data.value = value
        }

    init {
        val postFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use { gson.fromJson(it, type) }
        } else emptyList()
        data = MutableLiveData(posts)
    }

//    init {
//        val initialPost = List(GENERATED_POST_AMOUNT) { index ->
//            Post(
//                id = index + 1,
//                postHeader = "Нетология. Университет интернет-профессий",
//                date = Date().toString(),
//                message = "Тестовое сообщение № ${(1..100).random()}",
//                isLiked = false,
//                likes = 999999,
//                shares = 995,
//                views = (0..100).random(),
//                video = listOf("https://youtu.be/hBTNyJ33LWI", "").random()
//            )
//        }
//        data = MutableLiveData(initialPost)
//    }

    override fun likeById(id: Int) {
        posts = posts.map { post ->
            if (post.id == id)
                post.copy(
                    isLiked = !post.isLiked,
                    likes = if (post.isLiked) post.likes - 1 else post.likes + 1
                )
            else post
        }
    }

    override fun shareById(id: Int) {
        posts = posts.map { post ->
            if (post.id == id) post.copy(shares = post.shares + 1)
            else post
        }
    }

    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id }
    }

    override fun createPost(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    override fun views(id: Int) {
        posts = posts.map { post ->
            if (post.id == id) post.copy(views = post.views + 1)
            else post
        }
    }

    private fun insert(post: Post) {
        posts = listOf(post.copy(id = ++nextId)) + posts
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private companion object {
        const val GENERATED_POST_AMOUNT = 50
        const val POST_PREFS_KEY = "posts"
        const val NEXT_ID_PREFS_KEY = "posts"
        const val FILE_NAME = "posts.json"
    }
}

fun slicer(count: Int): String {
    return when (count) {
        in 1_000..999_999 ->
            if ((count.toDouble() / 1_000).toBigDecimal().setScale(1, RoundingMode.DOWN)
                    .toDouble() * 10 % 10 == 0.0
            )
                (count / 1_000).toString() + "K"
            else ((count.toDouble() / 1_000).toBigDecimal()
                .setScale(1, RoundingMode.DOWN)).toString() + "K"
        in 1_000_000..999_999_999 ->
            if ((count.toDouble() / 1_000_000).toBigDecimal().setScale(1, RoundingMode.DOWN)
                    .toDouble() * 10 % 10 == 0.0
            )
                (count / 1_000_000).toString() + "M"
            else ((count.toDouble() / 1_000_000).toBigDecimal()
                .setScale(1, RoundingMode.DOWN)).toString() + "M"
        else -> count.toString()
    }
}