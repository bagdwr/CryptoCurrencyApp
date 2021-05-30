package com.example.cryptocurrency

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency.models.Coin
import com.example.cryptocurrency.recyclerView.CoinListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoinsListFragment:Fragment() {
    //https://api.coingecko.com/
    private var coinRetrofit:CoinRetrofit?=null
    private val coinAdapter:CoinListAdapter= CoinListAdapter()
    private var retrofit:Retrofit?=null
    private var progress:ProgressBar?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.coinlist_layout, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progress=view.findViewById<ProgressBar>(R.id.progressBar)
        progress?.visibility=View.VISIBLE
        setupRV()
        getOneList(1)
    }

    fun setupRV(){
        val rvCoins=view?.findViewById<RecyclerView>(R.id.rvCoinList)
        rvCoins?.layoutManager=LinearLayoutManager(context)
        rvCoins?.adapter=coinAdapter
        retrofit=Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        coinRetrofit=retrofit?.create(CoinRetrofit::class.java)

    }
    fun getOneList(page:Int){
        val call=coinRetrofit?.getCoins(
            "usd",
            "market_cap_desc",
            70,
            page,
            false
        )
        call?.enqueue(object:Callback<List<Coin>>{
            override fun onResponse(call: Call<List<Coin>>, response: Response<List<Coin>>) {
               response.body()?.let {
                   coinAdapter.addItems(it as ArrayList<Coin>)
               }
                progress?.visibility=View.GONE
            }

            override fun onFailure(call: Call<List<Coin>>, t: Throwable) {
                Log.i("MyCoinsListFragment:","$t")
                t.printStackTrace()
                Toast.makeText(requireContext(), "Error: $t", Toast.LENGTH_LONG).show()
                progress?.visibility = View.GONE
            }

        })
    }
}