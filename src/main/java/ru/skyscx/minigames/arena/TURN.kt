package ru.skyscx.minigames.arena

import org.bukkit.entity.Player

class TURN {
    private val TURN: HashMap<Int, ArrayList<Player>> = HashMap()

    fun size(count: Int) : Int? {
        return TURN.get(count)?.size
    }

    fun addPlayerTurn(player: Player, count: Int) : Boolean {
        for (p in TURN.values) {
            if (p.contains(player)) return false
        }

        if (!TURN.containsKey(count)) TURN.put(count, ArrayList())

        val array = TURN.get(count)
        array?.add(player)
        array?.let { TURN.put(count, it) }

        return false
    }
    fun removePlayer(player : Player) : Boolean {
        for (i in TURN.values) {
            if (i.contains(player)) {
                i.remove(player)
                return true
            }
        }

        return false
    }

    fun getPlayer(count: Int) : Player? {
        val player: Player? = TURN.get(count)?.get(0)
        TURN.get(count)?.remove(player)
        return player
    }

    fun hasPlayerInTurn(player: Player) : Boolean {
        for (i in TURN.values) {
            if (i.contains(player)) {
                return true
            }
        }

        return false
    }
}