package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pidsamhai.covid19thailand.db.LastFetch
import com.github.pidsamhai.covid19thailand.network.Download
import com.github.pidsamhai.covid19thailand.network.response.github.Asset
import com.github.pidsamhai.covid19thailand.network.response.github.ReleaseItem
import com.github.pidsamhai.covid19thailand.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class DownloadDialogVM(
    private val savedStateHandle: SavedStateHandle,
    private val githubRepository: GithubRepository,
    private val lastFetch: LastFetch
) : ViewModel() {

    val releaseItem: ReleaseItem = requireNotNull(lastFetch.getReleaseItem())
    private val asset: Asset = requireNotNull(releaseItem.assets?.firstOrNull())
    private val _download = MutableStateFlow<Download?>(null)
    val download: Flow<Download?> = _download
    private var call: Call? = null

    fun dlFile(file: File) {
        call = githubRepository.dlFile(
            tagName = releaseItem.tagName!!,
            fileName = asset.name!!,
            file = file
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                _download.emit(it)
            }
        }
    }

    fun cancel() = call?.cancel()

    fun removeReleaseItem() = lastFetch.removeReleaseItem()
}