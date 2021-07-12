package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.github.pidsamhai.covid19thailand.repository.Repository

class TimeLineViewModel(repository: Repository) : ViewModel() {
    val timeline = repository.getTimeLineLiveData()
    val datas = repository.getTimeLineDataLiveData()
}