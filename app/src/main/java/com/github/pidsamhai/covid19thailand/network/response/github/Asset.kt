package com.github.pidsamhai.covid19thailand.network.response.github


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Asset(
    @SerializedName("browser_download_url")
    val browserDownloadUrl: String? = null,
    @SerializedName("content_type")
    val contentType: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("download_count")
    val downloadCount: Int? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("label")
    val label: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("node_id")
    val nodeId: String? = null,
    @SerializedName("size")
    val size: Int? = null,
    @SerializedName("state")
    val state: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("uploader")
    val uploader: Uploader? = null,
    @SerializedName("url")
    val url: String? = null
) : Parcelable