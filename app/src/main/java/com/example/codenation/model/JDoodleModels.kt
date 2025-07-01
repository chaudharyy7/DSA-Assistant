package com.example.codenation.model

data class JDoodleRequest(
    val clientId: String,
    val clientSecret: String,
    val script: String,
    val language: String,
    val versionIndex: String = "0",
    val stdin: String? = null
)

data class JDoodleResponse(
    val output: String,
    val statusCode: Int,
    val memory: String?,
    val cpuTime: String?,
    val error: String? = null
)
