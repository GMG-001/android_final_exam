package com.example.finalexam.api

import com.google.gson.annotations.SerializedName

data class Photos(
    val photos: List<Photo>
)

data class Photo(
    val id: Long,
    val width: Int,
    val height: Int,
    val url: String?,
    val photographer: String?,
    @SerializedName("photographer_url")
    val photographerUrl: String?,
    @SerializedName("photographer_id")
    val photographerId: Long,
    @SerializedName("avg_color")
    val avgColor: String,
    val src: Src,
    val alt: String?
)

data class Src(
    val original: String?,
    val large2x: String?,
    val large: String?,
    val medium: String?,
    val small: String?,
    val portrait: String?,
    val landscape: String?,
    val tiny: String?
)
