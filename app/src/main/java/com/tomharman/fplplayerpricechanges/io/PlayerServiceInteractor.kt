package com.tomharman.fplplayerpricechanges.io

class PlayerServiceInteractor {

    private val playerService by lazy {
        IPlayerService.create()
    }

    fun getPlayers() = playerService.getPlayers()

}
