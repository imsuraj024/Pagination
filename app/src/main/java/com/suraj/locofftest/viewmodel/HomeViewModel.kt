package com.suraj.locofftest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suraj.locofftest.data.repository.DataRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private var dataRepository: DataRepository? = DataRepository.getInstance(application)

    private var _usersFirstPageResponse = MutableLiveData<Any>()
    private var _usersNextPageResponse = MutableLiveData<Any>()

    fun requestFirstPageUsers(page : Int) {
        dataRepository!!.loadPage(_usersFirstPageResponse , page)
    }

    fun requestFirstNextPageUsers(page : Int) {
        dataRepository!!.loadPage(_usersNextPageResponse , page)
    }

    val usersFirstPageResponse : LiveData<Any>
        get() = _usersFirstPageResponse
    val usersNextPageResponse : LiveData<Any>
        get() = _usersNextPageResponse

}