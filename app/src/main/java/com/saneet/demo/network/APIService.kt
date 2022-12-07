package com.saneet.demo.network

import com.saneet.demo.models.Model
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

class APIService {
    interface CoroutineService {
        @GET("endPoint")
        suspend fun getModel(@Query("arg1") arg: Int): List<Model>
    }

    interface RxService {
        @GET("endPoint")
        suspend fun getModel(@Query("arg1") arg: Int): Single<List<Model>>
    }
}