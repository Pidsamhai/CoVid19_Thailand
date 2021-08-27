package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.*
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.repository.Repository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ToDayViewModel(
    private val state: SavedStateHandle,
    private val repository: Repository,
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : ViewModel() {
    private val refreshKey = MutableLiveData(System.currentTimeMillis())

    val today: LiveData<Result<Today>> = refreshKey.switchMap {
        repository.getTodayFlow().asLiveData()
    }

    fun refresh() = viewModelScope.launch { refreshKey.value = System.currentTimeMillis() }

    val notification: String = firebaseRemoteConfig.getString("notification")

    fun refreshNotification() {
        firebaseRemoteConfig.fetchAndActivate()
    }
}