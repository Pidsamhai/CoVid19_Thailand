package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pidsamhai.covid19thailand.repository.RapidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WorldWideModel(private val rapidRepository: RapidRepository) : ViewModel() {
    val static = rapidRepository.static
    val countries = rapidRepository.countries

    init {
        initData()
    }

    fun initData() {
        viewModelScope.launch(Dispatchers.IO) {
            rapidRepository.fetchCountries()
        }
    }

    fun search(country: String) {
        rapidRepository.getStatic(country)
    }

    suspend fun getStatic(country: String) = rapidRepository.getStatic2(country)
}