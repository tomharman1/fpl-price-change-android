package com.tomharman.fplplayerpricechanges

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_player_item.view.*

class PlayersAdapter: RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder>() {

    private val players = mutableListOf<Player>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_player_item, parent, false)
        return PlayerViewHolder(view)
    }

    override fun getItemCount(): Int = players.count()

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(players[position])
    }

    fun add(player: Player) {
        players.add(player)
    }

    fun sort(sortTypeEnum: SortTypeEnum) {
        when(sortTypeEnum) {
            PlayersAdapter.SortTypeEnum.SORT_TYPE_NTI_ASCENDING ->
                players.sortBy { it.netTransfers }
            PlayersAdapter.SortTypeEnum.SORT_TYPE_NTI_DESCENDING ->
                players.sortByDescending { it.netTransfers }
            PlayersAdapter.SortTypeEnum.SORT_TYPE_PERCENTAGE_ASCENDING ->
                players.sortBy { it.netTransfersPercentage }
            PlayersAdapter.SortTypeEnum.SORT_TYPE_PERCENTAGE_DESCENDING ->
                players.sortByDescending { it.netTransfersPercentage }
        }
        notifyDataSetChanged()
    }

    class PlayerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(player: Player) {
            with(player) {
                view.playerName.text = name
                view.teamName.text = teamName
                view.playerPrice.text = price
                view.netTransfers.text = netTransfersFormatted
                view.percentageChange.text = netTransferPercentageFormatted
            }
        }
    }

    data class Player(
        val name: String,
        val teamName: String,
        val price: String,
        val netTransfers: Long,
        val netTransfersFormatted: String,
        val netTransfersPercentage: Double,
        val netTransferPercentageFormatted: String
    )

    enum class SortTypeEnum {
        SORT_TYPE_NTI_ASCENDING,
        SORT_TYPE_NTI_DESCENDING,
        SORT_TYPE_PERCENTAGE_ASCENDING,
        SORT_TYPE_PERCENTAGE_DESCENDING
    }
}
