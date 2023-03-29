package com.bignerdranch.android.basicgame.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.basicgame.viewmodel.ResultViewModel

class ResultViewModelFactory(private val finalResult: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java))
        return ResultViewModel(finalResult) as T
        throw IllegalStateException("Unknown ViewModel")
    }
}