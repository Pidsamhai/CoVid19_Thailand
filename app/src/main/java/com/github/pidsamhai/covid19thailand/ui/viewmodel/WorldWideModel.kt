package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.*
import com.github.pidsamhai.covid19thailand.Result
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.repository.Repository


class WorldWideModel(
    private val stateHandle: SavedStateHandle,
    repository: Repository
) : ViewModel() {
    private val refreshKey = MutableLiveData(System.currentTimeMillis())
    val country = MutableLiveData<String?>(null)
    val countriesX: LiveData<Result<List<String>>> = refreshKey.switchMap { repository.getCountryLiveData() }
    val static: LiveData<Result<Static>> = country.switchMap { repository.getStatic(it ?: return@switchMap liveData {  }) }

    fun refresh() = refreshKey.postValue(System.currentTimeMillis())

    fun getCountry(country: String) = this.country.postValue(country)
}