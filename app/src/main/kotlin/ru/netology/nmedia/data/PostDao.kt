package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.netology.nmedia.db.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE posts SET message = :message WHERE id = :id")
    fun update(id: Int, message: String)

    fun save(post: PostEntity) =
        if (post.id == 0) insert(post) else update(post.id, post.message)

    @Query(
        """
        UPDATE posts SET
        likes = likes + CASE WHEN isLiked THEN -1 ELSE 1 END,
        isLiked = CASE WHEN isLiked THEN 0 ELSE 1 END
        WHERE id = :id;
        """
    )
    fun likeById(id: Int)

    @Query("DELETE FROM posts WHERE id = :id")
    fun removeById(id: Int)

    @Query(
        """
        UPDATE posts SET
        shares = shares + 1 
        WHERE id = :id;
        """
    )
    fun shareById(id: Int)

    @Query(
        """
        UPDATE posts SET
        views = views + 1 
        WHERE id = :id;
        """
    )
    fun views(id: Int)

//    @Delete
//    fun delete(post: PostEntity)

//    @Update(onConflict = OnConflictStrategy.ABORT)
//    fun update(post: PostEntity)
}