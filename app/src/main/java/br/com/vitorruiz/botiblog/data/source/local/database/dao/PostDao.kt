package br.com.vitorruiz.botiblog.data.source.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.vitorruiz.botiblog.data.source.local.database.entity.PostEntity
import br.com.vitorruiz.botiblog.data.source.local.database.entity.PostWithUser

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Update
    suspend fun update(post: PostEntity)

    @Delete
    suspend fun delete(post: PostEntity)

    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun findById(id: Long): PostEntity?

    @Transaction
    @Query("SELECT * FROM posts")
    fun findAllLiveData(): LiveData<List<PostWithUser>>
}