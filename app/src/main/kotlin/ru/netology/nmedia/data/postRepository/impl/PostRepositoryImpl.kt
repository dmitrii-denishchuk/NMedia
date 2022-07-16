package ru.netology.nmedia.data.postRepository.impl

import androidx.lifecycle.map
import ru.netology.nmedia.data.PostDao
import ru.netology.nmedia.data.postRepository.PostRepository
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toPost
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode

class PostRepositoryImpl(private val dao: PostDao) :
    PostRepository {

    override val data = dao.getAll().map { entities ->
        entities.map { it.toPost() }
    }

    private var nextId = GENERATED_POST_AMOUNT


    override fun likeById(id: Int) {
        dao.likeById(id)
    }

    override fun shareById(id: Int) {
        dao.shareById(id)
    }

    override fun removeById(id: Int) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        dao.save(post.toEntity())
    }

    override fun views(id: Int) {
        dao.views(id)
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