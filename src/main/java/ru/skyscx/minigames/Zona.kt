package ru.skyscx.minigames

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import ru.skyscx.minigames.arena.Arena
import ru.skyscx.minigames.arena.System
import ru.skyscx.minigames.events.Events


class Zona : JavaPlugin() {
    val system = System()
    val lobby : Location = Location(Bukkit.getWorld("world"), -284.0, 122.0,278.0, 45f, 0f)
    override fun onEnable() {
        // Plugin startup logic
        instance = this
        plugin = this

        Bukkit.getServer().pluginManager.registerEvents(Events, this)

        for (arena in system.ArenaLIST){
            Bukkit.getServer().pluginManager.registerEvents(arena, this)
        }
        for (i in Bukkit.getOnlinePlayers()){
            i.teleport(lobby)
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    companion object {


        var instance: Zona? = null
            private set
        var plugin: Plugin? = null
            private set
    }
}