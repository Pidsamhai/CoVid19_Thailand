package com.github.pidsamhai.covid19thailand.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pidsamhai.covid19thailand.BuildConfig
import com.github.pidsamhai.covid19thailand.db.LastFetch
import com.github.pidsamhai.covid19thailand.network.ApiResponse
import com.github.pidsamhai.covid19thailand.network.Download
import com.github.pidsamhai.covid19thailand.network.response.github.ReleaseItem
import com.github.pidsamhai.covid19thailand.repository.GithubRepository
import com.github.pidsamhai.covid19thailand.ui.dialog.UPDATE_CHANNEL_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File

data class DownloadParams(
    val file: File, val tagName: String, val fileName: String
)

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateDialogVM(
    private val githubRepository: GithubRepository,
    private val lastFetch: LastFetch,
    private val pref: SharedPreferences
) : ViewModel() {
    private val isDevChannel: Boolean
        get() = pref.getBoolean(UPDATE_CHANNEL_KEY, false)
    private val _releaseItem = MutableLiveData<ReleaseItem>()
    val releaseItem: LiveData<ReleaseItem> = _releaseItem

    private val _download = MutableStateFlow<Download?>(null)
    val download: Flow<Download?> = _download

    fun getRelease() = viewModelScope.launch(Dispatchers.IO) {
        githubRepository.getRelease().also {
            when (it) {
                is ApiResponse.Error -> {
                }
                is ApiResponse.Success -> {
                    if (it.data.size > 0) {
                        var release = it.data.first()
                        if (release.prerelease == true && !isDevChannel) {
                            release = release.copy(
                                tagName = BuildConfig.VERSION_NAME
                            )
                        }
                        _releaseItem.postValue(release)
                    }
                }
            }
        }
    }

    fun saveReleaseItem() {
        lastFetch.saveReleaseItem(releaseItem.value!!)
    }
}