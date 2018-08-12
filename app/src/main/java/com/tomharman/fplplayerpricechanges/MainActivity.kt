package com.tomharman.fplplayerpricechanges

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.tomharman.fplplayerpricechanges.io.IPlayerService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private val playersAdapter = PlayersAdapter()

    val playerService by lazy {
        IPlayerService.create()
    }

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        recyclerView.apply {
            adapter = playersAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        fetchPlayersFromService()
//        addSamplePlayers()
    }

    private fun fetchPlayersFromService() {
        val successObserver = { result: List<IPlayerService.Player> ->
            Log.i("", "Success: Players loaded")
            result.forEach { player ->
                with(player) {
                    playersAdapter.add(PlayersAdapter.Player(
                        name = player_name,
                        teamName = short_team_name,
                        price =  price_formatted,
                        netTransfers = net_transfers_in.toString(),
                        netTransferPercentage = target_percentage_formatted))
                }
            }
            playersAdapter.notifyDataSetChanged()
        }
        val errorObserver = { t: Throwable ->
            Log.e("", "Error:- fetching players", t)
            Unit
        }
        playerService
            .getPlayers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(successObserver, errorObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addSamplePlayers() {
        playersAdapter.add(PlayersAdapter.Player(name = "Lacazette", teamName = "ARS", price = "10m", netTransfers = "100,000", netTransferPercentage = "150%"))
        playersAdapter.add(PlayersAdapter.Player(name = "Henry", teamName = "ARS", price = "10m", netTransfers = "100,000", netTransferPercentage = "150%"))
        playersAdapter.add(PlayersAdapter.Player(name = "Smith", teamName = "ARS", price = "10m", netTransfers = "100,000", netTransferPercentage = "150%"))
        playersAdapter.add(PlayersAdapter.Player(name = "Anelka", teamName = "ARS", price = "10m", netTransfers = "100,000", netTransferPercentage = "150%"))
        playersAdapter.add(PlayersAdapter.Player(name = "Wright", teamName = "ARS", price = "10m", netTransfers = "100,000", netTransferPercentage = "150%"))
    }
}
