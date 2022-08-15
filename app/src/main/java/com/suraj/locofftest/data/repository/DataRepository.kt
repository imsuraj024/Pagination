package com.suraj.locofftest.data.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.suraj.locofftest.data.response.ApiResponse
import com.suraj.locofftest.data.retrofit.ApiClient
import com.suraj.locofftest.data.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataRepository private constructor (app: Application){

    private var instanceApi: ApiInterface

    init {
        ApiClient.init(app)
        instanceApi= ApiClient.instance
    }

    companion object{
        private var dataRepository: DataRepository?=null
        @Synchronized
        fun getInstance(app:Application): DataRepository? {
            if (dataRepository == null) {
                dataRepository = DataRepository(app)
            }
            return dataRepository
        }
    }

    fun loadPage(allUsersResponse: MutableLiveData<Any>, page : Int) {
        instanceApi.getAllUsers(pageIndex = page).enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                allUsersResponse.value = t
            }

            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {

                if (response.isSuccessful){
                    allUsersResponse.value = response.body()
                }else{
                    allUsersResponse.value = "error"
                }
            }
        })
    }


}