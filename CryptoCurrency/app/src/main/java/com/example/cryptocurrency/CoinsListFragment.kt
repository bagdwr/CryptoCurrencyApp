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
import com.jaredsburrows.retrofit2.adapter.synchronous.SynchronousCallAdapterFactory
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CoinsListFragment:Fragment() {
    //https://api.coingecko.com/
    private var coinRetrofit:CoinRetrofit?=null
    private val coinAdapter:CoinListAdapter= CoinListAdapter()
    private var retrofit:Retrofit?=null
    private var progress:ProgressBar?=null
    private val compositeDisposable=CompositeDisposable()
    private var pageNumber:Int=1

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
        retrofit=Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/")
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30,TimeUnit.SECONDS)
                    .writeTimeout(30,TimeUnit.SECONDS)
                    .readTimeout(30,TimeUnit.SECONDS)
                    .build()
            )
            .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        coinRetrofit=retrofit?.create(CoinRetrofit::class.java)
        getOneList(pageNumber)
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    fun setupRV(){
        val rvCoins=view?.findViewById<RecyclerView>(R.id.rvCoinList)
        val linearLayoutManager=LinearLayoutManager(context)
        rvCoins?.layoutManager=linearLayoutManager
        rvCoins?.adapter=coinAdapter
        rvCoins?.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                linearLayoutManager?.let {
                    val visibleCoinCount=it.childCount
                    val totalItemCount=it.itemCount
                    val firstVisibleItemPosition=it.findFirstVisibleItemPosition()
                    if (visibleCoinCount+firstVisibleItemPosition>=totalItemCount && firstVisibleItemPosition>=0){
                        getOneList(pageNumber)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }
    fun getOneList(page:Int){
        val disposable=Single.fromCallable{
                coinRetrofit?.getCoins(
                "usd",
                "market_cap_desc",
                12,
                page,
                false
            )
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    nullableCoins->nullableCoins?.let{
                    coinAdapter.addItems(it)
                    pageNumber++
            }
                    progress?.visibility = View.GONE
            },{
                Log.i("MyCoinsListFragment:","$it")
                it.printStackTrace()
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
                progress?.visibility = View.GONE
            })
        compositeDisposable.add(disposable)
    }
}