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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
import javax.inject.Inject

class CoinsListFragment:Fragment() {
    //region di vars
    @Inject
    lateinit var coinRetrofit: CoinRetrofit

    //endregion
    //https://api.coingecko.com/
    private val coinAdapter:CoinListAdapter= CoinListAdapter()
    private var swipeLayout: SwipeRefreshLayout?=null
    private val compositeDisposable=CompositeDisposable()
    private var pageNumber:Int=1
    private var isLoading:Boolean=false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.coinlist_layout, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity().application as App).appComponent.provideInj(this)
        swipeLayout=view.findViewById(R.id.swipeRefresh)
        swipeLayout?.isRefreshing=true
        swipeLayout?.setOnRefreshListener {
            refresh()
        }
        setupRV()
        getOneList(pageNumber,false)
    }

    private fun refresh(){
        pageNumber=1
        getOneList(1,true)
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
                        getOneList(pageNumber,false)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }
    fun getOneList(page:Int, isRefresh:Boolean){
        if (isLoading){
            return
        }
        isLoading=true
        val disposable=Single.fromCallable{
                coinRetrofit?.getCoins(
                "usd",
                "market_cap_desc",
                15,
                page,
                false
            )
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    nullableCoins->nullableCoins?.let{
                    if (isRefresh){
                        coinAdapter.refreshItems(it)
                    }else{
                        coinAdapter.addItems(it)
                    }
                    pageNumber++
                    isLoading=false

            }
                    swipeLayout?.isRefreshing = false
            },{
                isLoading=false
                Log.i("MyCoinsListFragment:","$it")
                it.printStackTrace()
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
                swipeLayout?.isRefreshing =false
            })
        compositeDisposable.add(disposable)
    }
}