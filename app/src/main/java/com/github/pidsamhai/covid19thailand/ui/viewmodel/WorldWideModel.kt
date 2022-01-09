package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.*
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class WorldWideModel(
    private val stateHandle: SavedStateHandle,
    repository: Repository
) : ViewModel() {
    private val refreshKey = MutableLiveData(System.currentTimeMillis())
    private val query = MutableStateFlow(System.currentTimeMillis())
    val country = MutableLiveData<String?>(null)
    val countries: Flow<Result<List<String>>> = query.flatMapLatest { repository.getCountries() }
    val static: LiveData<Result<Static>> = country.switchMap { repository.getStatic(it ?: return@switchMap liveData {  }).asLiveData() }

    fun refresh() = refreshKey.postValue(System.currentTimeMillis())

    fun getCountry(country: String) = this.country.postValue(country)
}