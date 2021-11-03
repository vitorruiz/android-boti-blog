package br.com.vitorruiz.botiblog.data.source.local.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var text: String,
    val date: Long,
    var edited: Boolean = false,
    val ownerEmail: String
)

data class PostWithUser(
    @Embedded val post: PostEntity,
    @Relation(
        parentColumn = "ownerEmail",
        entityColumn = "email"
    )
    val user: UserEntity
)