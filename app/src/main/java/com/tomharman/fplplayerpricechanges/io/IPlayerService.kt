package com.tomharman.fplplayerpricechanges.io

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface IPlayerService {

    @GET("price-changes")
    fun getPlayers(): Observable<List<Player>>

    companion object {
        fun create(): IPlayerService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://fierce-depths-10880.herokuapp.com/")
                .build()

            return retrofit.create(IPlayerService::class.java)
        }
    }

    data class Player(
        val player_name: String,
        val net_transfers_in: Long,
        val player_name_short: String,
        val position: String,
        val price: Double,
        val price_formatted: String,
        val short_team_name: String,
        val target_percentage: Double,
        val target_percentage_formatted: String,
        val team: String
    )
}
