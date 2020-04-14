package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.github.pidsamhai.covid19thailand.repository.CoVidDDCRepository

class TimeLineViewModel(private val coVidDDCRepository: CoVidDDCRepository) : ViewModel() {
    val timeline = coVidDDCRepository.timeLine
    val datas = coVidDDCRepository.timeLineData
}