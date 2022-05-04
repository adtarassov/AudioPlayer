package com.adtarassov.audioplayer.data.api

import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path


interface AudioBackendApi {

  @Headers("Accept: application/json")
  @FormUrlEncoded
  @POST("api/token/")
  suspend fun postAuthorizeUser(
    @Field("username")
    username: String,

    @Field("password")
    password: String,
  ): Response<TokenModel>

  @Headers("Accept: application/json")
  @FormUrlEncoded
  @POST("api/users/")
  suspend fun postRegisterUser(
    @Field("username")
    username: String,

    @Field("password")
    password: String,
  ): Response<TokenModel>

  @GET("api/audio/recommendation")
  suspend fun getAudioRecommendation(): Response<List<AudioResponseModel>>

  @GET("api/{account_name}/audio")
  suspend fun getAudioProfile(
    @Path("account_name")
    accountName: String,
  ): Response<List<AudioResponseModel>>


  @Multipart
  @POST("api/audio/")
  suspend fun uploadAudioFile(
    @Header("Authorization")
    token: String,
    @retrofit2.http.Part("name")
    audioName: RequestBody,
    @retrofit2.http.Part("description")
    audioDescription: RequestBody,
    @retrofit2.http.Part
    file: Part?,
  ): Response<Any>

  companion object {
    const val BASE_URL = "http://185.233.82.123:8000/"
  }
}