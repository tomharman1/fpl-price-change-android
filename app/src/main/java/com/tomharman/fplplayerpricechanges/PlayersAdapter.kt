package com.tomharman.fplplayerpricechanges

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

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
        this.notifyDataSetChanged()
    }

    class PlayerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val nameTv: TextView = view.findViewById(R.id.playerName)

        fun bind(player: Player) {
            nameTv.text = player.name
        }
    }

    data class Player(val name: String)
}
