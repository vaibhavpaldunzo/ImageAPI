package com.example.imageapi

import androidx.room.*

@Entity
data class ImageList(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val word:String,
    val urlList:List<String>
)


@Dao
interface ImageListDao {

    @Query("select urlList from ImageList where word = :queryWord")
    fun findByWord(queryWord:String) : List<String>

    @Insert
    fun insert(instance : ImageList)
}