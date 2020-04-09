package com.github.pidsamhai.covid19thailand.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.pidsamhai.covid19thailand.network.response.Today
import com.github.pidsamhai.covid19thailand.repository.CoVidRepository

class ToDayViewModel(
    private val coVidRepository: CoVidRepository,
    private val state: SavedStateHandle
) : ViewModel() {
    var cache: String?
        get() = state.get(STATE)
        set(value) {
            state.set(STATE, value)
        }

    val today: LiveData<Today> = coVidRepository.getToDay()

    fun refresh() {
        coVidRepository.refreshToday()
    }

    companion object {
        private const val STATE = "string"
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("onCleared: ", "Good bye")
    }
}