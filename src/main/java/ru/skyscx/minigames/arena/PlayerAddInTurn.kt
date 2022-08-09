package ru.skyscx.minigames.arena

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerAddInTurn(_player: Player, _size: Int = 2) : Event() {
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList = handlerList

    val player: Player = _player
    val size: Int = _size

}