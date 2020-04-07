package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.pidsamhai.covid19thailand.repository.CoVidRepository

@Suppress("UNCHECKED_CAST")
class TimeLineViewModelFactory (private val coVidRepository: CoVidRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TimeLineViewModel::class.java)){
            TimeLineViewModel(coVidRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}