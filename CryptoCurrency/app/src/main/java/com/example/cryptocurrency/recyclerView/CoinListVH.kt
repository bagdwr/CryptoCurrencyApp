package com.example.cryptocurrency.recyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.R
import com.example.cryptocurrency.models.Coin

class CoinListVH(
    private val view:View
    ):RecyclerView.ViewHolder(view){
            fun bind(coin:Coin){
                with(view){
                    val ivCoin=findViewById<ImageView>(R.id.iv_item)
                    val tvCoinName=findViewById<TextView>(R.id.tvCoinName)
                    val tvCoinPrice=findViewById<TextView>(R.id.tvCoinPrice)
                    val tvMarketCap=findViewById<TextView>(R.id.tvMarketCap)
                    val tvCoinVolume=findViewById<TextView>(R.id.tvCoinVolume)

                    tvCoinName.text = coin.name.toString()
                    tvCoinPrice.text = "$"+coin.current_price.toString()
                    tvMarketCap.text="$"+coin.market_cap.toString()
                    tvCoinVolume.text="$"+coin.total_volume.toString()
                    Glide.with(this).load(coin.image).into(ivCoin)
                }
            }
     }