package com.example.konwnow.data.remote.dto


import com.google.gson.annotations.SerializedName

data class WordId(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("meanings")
    val meanings: List<String>,
    @SerializedName("phonics")
    val phonics: String,
    @SerializedName("pronounceVoicePath")
    val pronounceVoicePath: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("__v")
    val v: Int,
    @SerializedName("word")
    val word: String,
    @SerializedName("wordClasses")
    val wordClasses: List<String>
)