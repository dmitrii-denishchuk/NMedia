package ru.netology.nmedia.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
class PostEntity(
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "postHeader")
    val postHeader: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "isLiked")
    val isLiked: Boolean,

    @ColumnInfo(name = "likes")
    val likes: Int,

    @ColumnInfo(name = "shares")
    val shares: Int,

    @ColumnInfo(name = "views")
    val views: Int,

    @ColumnInfo(name = "video")
    val video: String,
)