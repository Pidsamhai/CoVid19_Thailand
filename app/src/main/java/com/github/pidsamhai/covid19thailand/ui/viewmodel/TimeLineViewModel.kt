package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.github.pidsamhai.covid19thailand.repository.CoVidRepository

class TimeLineViewModel(private val coVidRepository: CoVidRepository) : ViewModel() {
    val timeline = coVidRepository.getTimeLine()
    val datas = coVidRepository.timeLineDatas
}