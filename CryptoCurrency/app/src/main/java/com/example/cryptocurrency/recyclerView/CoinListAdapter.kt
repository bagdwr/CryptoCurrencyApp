package com.example.cryptocurrency.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency.R
import com.example.cryptocurrency.models.Coin

class CoinListAdapter(
    val coins:ArrayList<Coin> = arrayListOf()
):RecyclerView.Adapter<CoinListVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinListVH {
        return CoinListVH(
            LayoutInflater.from(parent.context).inflate(R.layout.coin_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: CoinListVH, position: Int) {
        holder.bind(coins[position])
    }

    override fun getItemCount(): Int = coins.size

    fun refreshItems(newCoins:ArrayList<Coin>){
        this.coins.clear()
        this.coins.addAll(newCoins)
        notifyDataSetChanged()
    }
    fun addItems(newCoins:ArrayList<Coin>){
        this.coins.addAll(newCoins)
        notifyDataSetChanged()
    }
}