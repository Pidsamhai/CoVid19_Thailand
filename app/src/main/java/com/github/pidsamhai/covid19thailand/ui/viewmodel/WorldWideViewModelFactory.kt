package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.pidsamhai.covid19thailand.repository.RapidRepository

@Suppress("UNCHECKED_CAST")
class WorldWideViewModelFactory(
    private val rapidRepository: RapidRepository,
    private val stateHandle: SavedStateHandle
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TimeLineViewModel::class.java)) {
            WorldWideModel(rapidRepository, stateHandle) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}