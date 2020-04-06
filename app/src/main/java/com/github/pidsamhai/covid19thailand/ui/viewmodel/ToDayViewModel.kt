package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.pidsamhai.covid19thailand.repository.CoVidRepository
import com.github.pidsamhai.covid19thailand.network.response.Today

class ToDayViewModel(private val coVidRepository: CoVidRepository) : ViewModel(){
    val today:LiveData<Today> = coVidRepository.getToDay()
}