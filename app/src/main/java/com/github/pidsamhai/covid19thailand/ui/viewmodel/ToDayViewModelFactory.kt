package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.pidsamhai.covid19thailand.repository.CoVidRepository

@Suppress("UNCHECKED_CAST")
class ToDayViewModelFactory(
    private val coVidRepository: CoVidRepository,
    private val state: SavedStateHandle
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ToDayViewModel::class.java)) {
            ToDayViewModel(coVidRepository,state) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}