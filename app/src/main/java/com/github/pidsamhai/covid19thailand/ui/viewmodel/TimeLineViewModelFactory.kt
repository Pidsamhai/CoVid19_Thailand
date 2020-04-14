package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.pidsamhai.covid19thailand.repository.CoVidDDCRepository

@Suppress("UNCHECKED_CAST")
class TimeLineViewModelFactory (private val coVidDDCRepository: CoVidDDCRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TimeLineViewModel::class.java)){
            TimeLineViewModel(coVidDDCRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}