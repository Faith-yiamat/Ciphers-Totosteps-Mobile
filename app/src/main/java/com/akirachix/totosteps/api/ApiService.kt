package com.akirachix.totosteps.api

import com.akirachix.totosteps.models.Child
import com.akirachix.totosteps.models.ParentResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("https://totosteps-29a482165136.herokuapp.com/api/parent/{parentId}/")
    suspend fun getParentData(@Path("parentId") parentId: Int): ParentResponse
}