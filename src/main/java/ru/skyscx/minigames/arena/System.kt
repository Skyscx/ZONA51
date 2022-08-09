package ru.skyscx.minigames.arena

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

class System {
    val ArenaLIST : ArrayList<Arena> = ArrayList()
    val turn : TURN = TURN()

    constructor(){
        generateArenas()
    }

    fun addPlayerInTurn(_player: Player, _count: Int = 2) {
        turn.addPlayerTurn(_player, _count)
        Bukkit.getConsoleSender().sendMessage("Player ${_player.name} add in turn")
        checkStartArena(_count)
    }
    fun checkStartArena(_count: Int) {
        if (turn.size(_count)!! >= _count) {
            val arena: Arena? = findArena()

            for (i in 0 until _count) {
                val player: Player? = turn.getPlayer(_count)
                arena?.AddPlayer(player)
                Bukkit.getConsoleSender().sendMessage("Player ${player?.name} add in arena: ${arena?.name}")
            }
        }
    }
    fun findArena() : Arena? {

        for (arena in ArenaLIST) {
            if (arena.GameStatus == Arena.ArenaStatus.WAIT) return arena
        }

        return null
    }
    fun generateArenas() {
        var arena = Arena("ARENA1", Location(Bukkit.getWorld("world"), -259.0, 107.0, 253.0), Location(Bukkit.getWorld("world"), -309.0, 107.0, 303.0))
        /*1 - CT*/arena.addSpawnLocCT(-304.0, 108.0, 302.0, -150f, 0f)
        /*2 - CT arena.addSpawnLocCT(-306.0, 108.0, 302.0, 30f, 0f)*/
        /*3 - CT arena.addSpawnLocCT(-308.0, 108.0, 302.0, -150f, 0f)*/
        /*4 - CT arena.addSpawnLocCT(-308.0, 108.0, 300.0, 30f, 0f)*/
        /*5 - CT arena.addSpawnLocCT(-308.0, 108.0, 298.0, -150f, 0f)*/
        /*ENDERMAN*/ arena.addSpawnLocEnder(-306.0, 108.0, 289.0, -150f, 0f)
        /*1 - HERO*/ arena.addSpawnLocHERO(-264.0, 108.0, 254.0, -150f, 0f)
        /*2 - HERO arena.addSpawnLocHERO(-262.0, 108.0, 254.0, 30f, 0f)*/
        /*3 - HERO arena.addSpawnLocHERO(-260.0, 108.0, 254.0, -150f, 0f)*/
        /*4 - HERO arena.addSpawnLocHERO(-260.0, 108.0, 256.0, 30f, 0f)*/
        /*5 - HERO arena.addSpawnLocHERO(-260.0, 108.0, 258.0, -150f, 0f)*/
        /*GAME LOAD LOBBY*/ arena.addSpawnLocLOAD(-284.5, 108.0, 301.0, -150f, 0f)
        /*GAME LOAD LOBBY*/ arena.addSpawnLocLOAD(-284.5, 108.0, 301.0, -150f, 0f)

        /*
        * /*GAME LOAD LOBBY*/ arena.addSpawnLocLOAD(-284.5, 108.0, 301.0, -150f, 0f)
        /*GAME LOAD LOBBY*/ arena.addSpawnLocLOAD(-284.5, 108.0, 301.0, -150f, 0f)
        /*GAME LOAD LOBBY*/ arena.addSpawnLocLOAD(-284.5, 108.0, 301.0, -150f, 0f)
        /*GAME LOAD LOBBY*/ arena.addSpawnLocLOAD(-284.5, 108.0, 301.0, -150f, 0f)
        /*GAME LOAD LOBBY*/ arena.addSpawnLocLOAD(-284.5, 108.0, 301.0, -150f, 0f)
        /*GAME LOAD LOBBY*/ arena.addSpawnLocLOAD(-284.5, 108.0, 301.0, -150f, 0f)
        /*GAME LOAD LOBBY*/ arena.addSpawnLocLOAD(-284.5, 108.0, 301.0, -150f, 0f)
        /*GAME LOAD LOBBY*/ arena.addSpawnLocLOAD(-284.5, 108.0, 301.0, -150f, 0f)
        * */

        ArenaLIST.add(arena)

        /*
        arena = Arena("ARENA2", Location(Bukkit.getWorld("world"), -259.0, 107.0, 253.0), Location(Bukkit.getWorld("world"), -309.0, 107.0, 303.0))
        arena.addSpawnLoc(-284.5, 108.0, 301.0, -150f, 0f)
        arena.addSpawnLoc(-284.5, 108.0, 255.0, 30f, 0f)
        ArenaLIST.add(arena)*/
    }
}