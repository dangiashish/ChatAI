package com.codebyashish.chatai.networking

import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface apiinterface  {

    @FormUrlEncoded
    @POST("chat/completions")
    fun sendRequest (
        @Field("model") model : String,
        @Field("messages") message : JSONArray
    ) : Call<String>

}