package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.*
import com.github.pidsamhai.covid19thailand.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ToDayViewModel(
    private val repository: Repository,
    private val state: SavedStateHandle
) : ViewModel() {
    private val refreshKey = MutableLiveData(System.currentTimeMillis())

    val today: LiveData<Result<Today>> = refreshKey.switchMap {
        repository.getTodayLiveData().asLiveData()
    }

    fun refresh() = viewModelScope.launch { refreshKey.value = System.currentTimeMillis() }
}