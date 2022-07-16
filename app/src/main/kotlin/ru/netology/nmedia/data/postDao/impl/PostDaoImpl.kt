package ru.netology.nmedia.data.postDao.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.data.postDao.PostDao
import ru.netology.nmedia.db.PostsTable
import ru.netology.nmedia.db.toPost
import ru.netology.nmedia.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {

    override fun getAll() = db.query(
        PostsTable.NAME,
        PostsTable.ALL_COLUMNS_NAMES,
        null, null, null, null,
        "${PostsTable.Column.ID.columnName} DESC"
    ).use { cursor ->
        List(cursor.count) {
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostsTable.Column.POST_HEADER.columnName, post.postHeader)
            put(PostsTable.Column.MESSAGE.columnName, post.message)
            put(PostsTable.Column.DATE.columnName, post.date)
            put(PostsTable.Column.VIDEO.columnName, post.video)
        }
        val id = if (post.id != 0) {
            db.update(
                PostsTable.NAME,
                values,
                "${PostsTable.Column.ID.columnName} = ?",
                arrayOf(post.id.toString())
            )
            post.id
        } else {
            db.insert(
                PostsTable.NAME,
                null,
                values
            )
        }
        return db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAMES,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString()),
            null, null, null,
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun removeById(id: Int) {
        db.delete(
            PostsTable.NAME,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString())
        )
    }

    override fun likeById(id: Int) {
        db.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                ${PostsTable.Column.LIKES.columnName} = ${PostsTable.Column.LIKES.columnName} + CASE WHEN ${PostsTable.Column.IS_LIKED.columnName} THEN -1 ELSE 1 END,
                ${PostsTable.Column.IS_LIKED.columnName} = CASE WHEN ${PostsTable.Column.IS_LIKED.columnName} THEN 0 ELSE 1 END
                WHERE ${PostsTable.Column.ID.columnName} = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun shareById(id: Int) {
        db.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                ${PostsTable.Column.SHARES.columnName} = ${PostsTable.Column.SHARES.columnName} + 1
                WHERE ${PostsTable.Column.ID.columnName} = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun views(id: Int) {
        db.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                ${PostsTable.Column.VIEWS.columnName} = ${PostsTable.Column.VIEWS.columnName} + 1
                WHERE ${PostsTable.Column.ID.columnName} = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }
}