package com.akirachix.totosteps.api

import com.akirachix.totosteps.ChildrenDataClass
import com.akirachix.totosteps.models.Answer
import com.akirachix.totosteps.models.AutismResultResponse
import com.akirachix.totosteps.models.AutismTestResult
import com.akirachix.totosteps.models.ChildData
import com.akirachix.totosteps.models.ChildResponse
import com.akirachix.totosteps.models.LoginRequest
import com.akirachix.totosteps.models.LoginResponse
import com.akirachix.totosteps.models.Milestone
import com.akirachix.totosteps.models.ParentResponse
import com.akirachix.totosteps.models.Question
import com.akirachix.totosteps.models.RegistrationResponse
import com.akirachix.totosteps.models.UserRegistration
import com.akirachix.totosteps.resources
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import okhttp3.MultipartBody

import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiInterface {

    @POST("/auth/login/")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/api/register/")

    fun registerUser(@Body user: UserRegistration): Call<RegistrationResponse>

    @GET("/api/questions/category/{category}/")
    suspend fun getQuestions(@Path("category") category: String): List<Question>


    @GET("/api/resources")
    suspend fun getResources(): Response<List<resources>>

    @GET("api/milestones/")
    fun getMilestones(): Call<List<Milestone>>

    @Multipart
    @POST("api/results/")
    suspend fun uploadImage(@Query("child_id") childId: Int, @Part image: MultipartBody.Part): Response<AutismTestResult>

    @POST("/api/children/")
    suspend fun createChild(@Body childData: ChildData): Response<ChildResponse>

    @GET("/api/parent/{parentId}/")
//    suspend fun getChildrenByParent(@Path("parentId") parentId: Int): Response<List<ParentResponse>>
    suspend fun getChildrenByParent(@Path("parentId") parentId: Int): ChildResponse

    @GET("api/parent/{parentId}/")
    suspend fun getParentData(@Path("parentId") parentId: Int): ParentResponse

}
