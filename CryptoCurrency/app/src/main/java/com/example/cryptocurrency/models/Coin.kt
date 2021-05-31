package com.example.cryptocurrency.models

import com.google.gson.annotations.SerializedName

class Coin(
    @SerializedName("id")
    val id:String,
    @SerializedName("name")
    val name:String,
    @SerializedName("current_price")
    val current_price:Double,
    @SerializedName("market_cap")
    val market_cap:Long,
    @SerializedName("image")
    val image:String,
    @SerializedName("total_volume")
    val total_volume:Double
)