package com.bilmsoft.httpserver.data.model

enum class HttpRequestType(val value: String) {
    GET("GET"),
    POST("POST");

    override fun toString(): String = value

}