package com.example.cryptocurrency

import com.example.cryptocurrency.models.Coin
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinRetrofit {
    //api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false
    @GET("api/v3/coins/markets")
    fun getCoins(
        @Query("vs_currency") vs_currency:String,
        @Query("order") order:String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int,
        @Query("sparkline") sparkline:Boolean
    ): List<Coin>
}