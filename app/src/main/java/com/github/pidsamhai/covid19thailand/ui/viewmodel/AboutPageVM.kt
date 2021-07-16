package com.github.pidsamhai.covid19thailand.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pidsamhai.covid19thailand.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AboutPageVM(
    private val repository: Repository
) : ViewModel() {
    fun clearDatabase(complete: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        repository.clearDataBase()
        withContext(Dispatchers.Main) {
            complete()
        }
    }
}