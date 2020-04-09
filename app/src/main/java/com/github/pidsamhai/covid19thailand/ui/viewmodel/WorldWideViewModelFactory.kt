package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.pidsamhai.covid19thailand.repository.CoVidRepository
import com.github.pidsamhai.covid19thailand.repository.RapidRepository

@Suppress("UNCHECKED_CAST")
class WorldWideViewModelFactory (private val rapidRepository: RapidRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TimeLineViewModel::class.java)){
            WorldWideModel(rapidRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}