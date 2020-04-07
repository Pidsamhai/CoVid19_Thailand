package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.pidsamhai.covid19thailand.network.response.TimeLine
import com.github.pidsamhai.covid19thailand.repository.CoVidRepository

class TimeLineViewModel(private val coVidRepository: CoVidRepository) : ViewModel(){
    val timeline = coVidRepository.getTimeLine()
}