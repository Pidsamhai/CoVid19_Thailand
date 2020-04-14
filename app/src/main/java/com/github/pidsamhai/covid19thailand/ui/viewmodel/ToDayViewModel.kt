package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.repository.CoVidDDCRepository
import timber.log.Timber

class ToDayViewModel(
    private val coVidDDCRepository: CoVidDDCRepository,
    private val state: SavedStateHandle
) : ViewModel() {
    var cache: String?
        get() = state.get(STATE)
        set(value) {
            state.set(STATE, value)
        }

    val today: MutableLiveData<Today> = coVidDDCRepository.today

    fun refresh() {
        coVidDDCRepository.fetchTodayData()
    }

    companion object {
        private const val STATE = "string"
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("Good bye")
    }
}