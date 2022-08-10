package ru.skyscx.minigames.events

import com.google.common.eventbus.Subscribe
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import ru.skyscx.minigames.Zona
import ru.skyscx.minigames.arena.ArenaEnd
import ru.skyscx.minigames.arena.PlayerAddInTurn
import ru.skyscx.minigames.other.messages

object Events : Listener {
    @EventHandler
    fun playerJoinTurn(event: PlayerInteractEvent){
        if (event.item?.type != Material.SNOW_BALL) return

        if (Zona.instance?.system?.turn?.hasPlayerInTurn(event.player) == false){
            event.player.sendMessage(messages.Turn2)
            Zona.instance?.system?.addPlayerInTurn(event.player, 2)
        }else{
            event.player.sendMessage(messages.Turn1)
        }
    }
    /*
    * @EventHandler
    fun playerDamage(event: EntityDamageEvent){
        if (event.entity.type != EntityType.PLAYER) return
        event.isCancelled = true
    }*/
    @EventHandler
    fun playerJoinServer(event: PlayerJoinEvent){
        val player: Player = event.player
        player.inventory.clear()

        player.inventory.addItem(ItemStack(Material.SNOW_BALL))
        event.player.teleport(Zona.instance?.lobby!!)
    }
    @EventHandler
    fun playerLeaveServer(event: PlayerQuitEvent){
        Zona.instance?.system?.turn?.removePlayer(event.player)
    }
    @EventHandler
    fun playerFood(event: FoodLevelChangeEvent){
        event.foodLevel = 20
    }
    @EventHandler
    fun playerAddInTurn(event: PlayerAddInTurn){
        Zona.instance?.system?.addPlayerInTurn(event.player, event.size)
    }
    @EventHandler
    fun ArenaEnd(event: ArenaEnd){
        Zona.instance?.system?.checkStartArena(event.Arena.sizeAll)
    }

    @EventHandler
    fun EnderGod(event: EntityDamageByEntityEvent) {
        if (event.entity.type != EntityType.ENDERMAN) return
        event.isCancelled = true
    }



}