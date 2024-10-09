package com.akirachix.totosteps

import android.content.SharedPreferences
import com.akirachix.totosteps.api.ApiInterface
import com.akirachix.totosteps.api.ApiService
import com.akirachix.totosteps.models.Child


class ChildrenRepository constructor(
    private val apiService: ApiInterface,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun getChildren(): Result<List<Child>> {
        val parentId = sharedPreferences.getInt("USER_ID", -1)
        if (parentId == -1) return Result.failure(Exception("Parent ID not found"))

        return try {
            val parentResponse = apiService.getParentData(parentId)
            Result.success(parentResponse.children)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }}
