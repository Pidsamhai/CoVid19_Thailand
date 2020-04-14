package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pidsamhai.covid19thailand.repository.RapidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WorldWideModel(
    private val rapidRepository: RapidRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    val countries = rapidRepository.countries
    val history = rapidRepository.histories

    var cachePosition: Int?
        get() = stateHandle.get<Int>(STATE_POSITION)
        set(value) {
            stateHandle.set(STATE_POSITION, value)
        }

    var cacheDatas: String?
        get() = stateHandle.get(STATE_DATA)
        set(value) {
            stateHandle.set(STATE_DATA, value)
        }
    var cacheCountries: List<String>?
        get() = stateHandle.get(STATE_COUNTRIES)
        set(value) {
            stateHandle.set(STATE_COUNTRIES, value)
        }

    fun refresh() {
        rapidRepository.fetchStaticsData()
    }

    suspend fun getStatic(country: String) = rapidRepository.getStatic(country)

    suspend fun getHistory(country: String) = rapidRepository.getHistory(country)

    companion object {
        private const val STATE_POSITION = "spinner_position"
        private const val STATE_DATA = "data"
        private const val STATE_COUNTRIES = "countries"
    }
}