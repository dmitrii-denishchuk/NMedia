package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.dto.Post

fun Cursor.toPost() = Post(
    id = getInt(getColumnIndexOrThrow(PostsTable.Column.ID.columnName)),
    postHeader = getString(getColumnIndexOrThrow(PostsTable.Column.POST_HEADER.columnName)),
    date = getString(getColumnIndexOrThrow(PostsTable.Column.DATE.columnName)),
    message = getString(getColumnIndexOrThrow(PostsTable.Column.MESSAGE.columnName)),
    isLiked = getInt(getColumnIndexOrThrow(PostsTable.Column.IS_LIKED.columnName)) != 0,
    likes = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKES.columnName)),
    shares = getInt(getColumnIndexOrThrow(PostsTable.Column.SHARES.columnName)),
    views = getInt(getColumnIndexOrThrow(PostsTable.Column.VIEWS.columnName)),
    video = getString(getColumnIndexOrThrow(PostsTable.Column.VIDEO.columnName))
)