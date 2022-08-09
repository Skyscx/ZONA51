package ru.skyscx.minigames.arena

import org.bukkit.event.Event
import org.bukkit.event.HandlerList


class ArenaEnd(_arena: Arena) : Event() {
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList = handlerList

    val Arena : Arena = _arena
}