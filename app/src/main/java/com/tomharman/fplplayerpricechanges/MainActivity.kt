package com.tomharman.fplplayerpricechanges

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import com.tomharman.fplplayerpricechanges.io.IPlayerService
import com.tomharman.fplplayerpricechanges.io.PlayerServiceInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private val playersAdapter = PlayersAdapter()

    private val playerServiceInteractor by lazy {
        PlayerServiceInteractor()
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_sort)

        recyclerView.apply {
            adapter = playersAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        progressBar.visibility = VISIBLE
    }

    override fun onResume() {
        super.onResume()
        fetchPlayersFromService()
        // addSamplePlayers()
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
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
            R.id.sort_net_transfers_in_descending -> {
                playersAdapter.sort(PlayersAdapter.SortTypeEnum.SORT_TYPE_NTI_DESCENDING)
                true
            }
            R.id.sort_net_transfers_in_ascending -> {
                playersAdapter.sort(PlayersAdapter.SortTypeEnum.SORT_TYPE_NTI_ASCENDING)
                true
            }
            R.id.sort_percentage_descending -> {
                playersAdapter.sort(PlayersAdapter.SortTypeEnum.SORT_TYPE_PERCENTAGE_DESCENDING)
                true
            }
            R.id.sort_percentage_ascending -> {
                playersAdapter.sort(PlayersAdapter.SortTypeEnum.SORT_TYPE_PERCENTAGE_ASCENDING)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchPlayersFromService() {
        val successObserver = { result: List<IPlayerService.Player> ->
            progressBar.visibility = GONE
            Log.i("", "Success: Players loaded")
            result.forEach { player ->
                with(player) {
                    playersAdapter.add(PlayersAdapter.Player(
                        name = player_name,
                        teamName = short_team_name,
                        price =  price_formatted,
                        netTransfers = net_transfers_in,
                        netTransfersFormatted = net_transfers_in.toString(),
                        netTransfersPercentage = target_percentage,
                        netTransferPercentageFormatted = target_percentage_formatted))
                }
            }
            playersAdapter.notifyDataSetChanged()
        }
        val errorObserver = { t: Throwable ->
            Log.e("", "Error:- fetching players", t)
            Unit
        }

        val disposable = playerServiceInteractor
            .getPlayers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(successObserver, errorObserver)
        compositeDisposable.add(disposable)
    }

    private fun addSamplePlayers() {
        playersAdapter.add(PlayersAdapter.Player(name = "Lacazette", teamName = "ARS", price = "10m", netTransfers = 10, netTransfersFormatted = "100,000", netTransfersPercentage = 150.0, netTransferPercentageFormatted = "150%"))
        playersAdapter.add(PlayersAdapter.Player(name = "Henry", teamName = "ARS", price = "10m", netTransfers = 10, netTransfersFormatted = "100,000", netTransfersPercentage = 150.0, netTransferPercentageFormatted = "150%"))
        playersAdapter.add(PlayersAdapter.Player(name = "Smith", teamName = "ARS", price = "10m", netTransfers = 10, netTransfersFormatted = "100,000", netTransfersPercentage = 150.0, netTransferPercentageFormatted = "150%"))
        playersAdapter.add(PlayersAdapter.Player(name = "Anelka", teamName = "ARS", price = "10m", netTransfers = 10, netTransfersFormatted = "100,000", netTransfersPercentage = 150.0, netTransferPercentageFormatted = "150%"))
        playersAdapter.add(PlayersAdapter.Player(name = "Wright", teamName = "ARS", price = "10m", netTransfers = 10, netTransfersFormatted = "100,000", netTransfersPercentage = 150.0, netTransferPercentageFormatted = "150%"))
    }
}
