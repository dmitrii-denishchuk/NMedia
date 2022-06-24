package ru.netology.nmedia.data

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode
import java.util.*

class InMemoryPostRepository : PostRepository {

    private var post = Post(
        id = 1,
        postHeader = "Нетология. Университет интернет-профессий",
        date = Date().toString(),
        isLiked = false,
        likes = 999,
        shares = 995,
        views = 13
    )

    override val data = MutableLiveData(post)

    override fun like(): String {
        post = post.copy(isLiked = !post.isLiked)
        if (post.isLiked) post.likes++ else post.likes--
        data.value = post
        return slicer(post.likes)
    }

    override fun share(): String {
        post.shares++
        return slicer(post.shares)
    }
}

fun slicer(count: Int): String {
    return when (count) {
        in 1_000..999_999 ->
            if ((count.toDouble() / 1_000).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble() * 10 % 10 == 0.0)
                (count / 1_000).toString() + "K"
            else ((count.toDouble() / 1_000).toBigDecimal().setScale(1, RoundingMode.DOWN)).toString() + "K"
        in 1_000_000..999_999_999 ->
            if ((count.toDouble() / 1_000_000).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble() * 10 % 10 == 0.0)
                (count / 1_000_000).toString() + "M"
            else ((count.toDouble() / 1_000_000).toBigDecimal().setScale(1, RoundingMode.DOWN)).toString() + "M"
        else -> count.toString()
    }
}