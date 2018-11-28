package hk.edu.hkbu.comp.groupd.fashiontap

import hk.edu.hkbu.comp.groupd.fashiontap.json.YoutubeChannelResponse
import retrofit2.Call

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeService {
    companion object {
        val instance: YoutubeService by lazy {
            Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(YoutubeService::class.java)
        }
    }

    @GET("/youtube/v3/channels")
    fun getYoutubeChannel(@Query("part") part: String=("snippet,contentDetails,statistics"),@Query("id") id: String = "UC3T5fmgL4Kvk3kG1kF6JFeA",@Query("key") key: String = "AIzaSyBfemQ9-xiY5eA0X6tKKFpaOa_ZXuLUZ5g"): Call<YoutubeChannelResponse>

}