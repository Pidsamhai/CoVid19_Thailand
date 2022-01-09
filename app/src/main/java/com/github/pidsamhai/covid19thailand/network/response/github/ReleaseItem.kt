package com.github.pidsamhai.covid19thailand.network.response.github


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.github.pidsamhai.covid19thailand.BuildConfig
import com.navelplace.jsemver.Version
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ReleaseItem(
    @SerializedName("assets")
    val assets: List<Asset>? = null,
    @SerializedName("assets_url")
    val assetsUrl: String? = null,
    @SerializedName("author")
    val author: Author? = null,
    @SerializedName("body")
    val body: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("draft")
    val draft: Boolean? = null,
    @SerializedName("html_url")
    val htmlUrl: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("node_id")
    val nodeId: String? = null,
    @SerializedName("prerelease")
    val prerelease: Boolean? = null,
    @SerializedName("published_at")
    val publishedAt: String? = null,
    @SerializedName("tag_name")
    val tagName: String? = null,
    @SerializedName("tarball_url")
    val tarballUrl: String? = null,
    @SerializedName("target_commitish")
    val targetCommitish: String? = null,
    @SerializedName("upload_url")
    val uploadUrl: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("zipball_url")
    val zipballUrl: String? = null
) : Parcelable {
    @IgnoredOnParcel
    private val currentVersion = Version(BuildConfig.VERSION_NAME)
    @IgnoredOnParcel
    val isNewVersion: Boolean
        get() = currentVersion.olderThan(Version(tagName!!))
}