package com.example.codenation.editor

import com.example.codenation.model.JDoodleRequest
import com.example.codenation.model.JDoodleResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface JdoodleApiService {
    @POST("execute")
    fun executeCode(@Body request: JDoodleRequest): Call<JDoodleResponse>
}
