package com.suraj.locofftest.data.retrofit

import com.suraj.locofftest.data.response.ApiResponse
import com.suraj.locofftest.utils.GET_USERS
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(GET_USERS)
    fun getAllUsers(@Query("page") pageIndex: Int):Call<ApiResponse>


}