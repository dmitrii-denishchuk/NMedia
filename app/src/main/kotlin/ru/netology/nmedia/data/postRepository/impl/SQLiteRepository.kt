package ru.netology.nmedia.data.postRepository.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.postRepository.PostRepository
import ru.netology.nmedia.data.postDao.PostDao
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode

class SQLiteRepository(private val dao: PostDao) : PostRepository {

//    private val posts
//        get() = checkNotNull(data.value)

    override val data = MutableLiveData(dao.getAll())

    private var nextId = GENERATED_POST_AMOUNT

    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            data.value = value
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
        dao.likeById(id)
        data.value = posts.map { post ->
            if (post.id == id)
                post.copy(
                    isLiked = !post.isLiked,
                    likes = if (post.isLiked) post.likes - 1 else post.likes + 1
                )
            else post
        }
    }

    override fun shareById(id: Int) {
        dao.shareById(id)
        data.value = posts.map { post ->
            if (post.id == id) post.copy(shares = post.shares + 1)
            else post
        }
    }

    override fun removeById(id: Int) {
        dao.removeById(id)
        data.value = posts.filter { it.id != id }
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        data.value = if (id == 0) {
            listOf(saved) + posts
        } else posts.map {
            if (it.id != id) it else saved
        }
    }

    override fun views(id: Int) {
        dao.views(id)
        data.value = posts.map { post ->
            if (post.id == id) post.copy(views = post.views + 1)
            else post
        }
    }

    fun insert(post: Post) {
        data.value = listOf(post.copy(id = post.id + 1)) + posts
    }

    fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private companion object {
        const val GENERATED_POST_AMOUNT = 50
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