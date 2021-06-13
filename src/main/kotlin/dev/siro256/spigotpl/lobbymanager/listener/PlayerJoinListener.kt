package dev.siro256.spigotpl.lobbymanager.listener

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinListener: Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        player.gameMode = GameMode.ADVENTURE
        player.teleport(Location(Bukkit.getWorld("world"), 0.5, 100.5, 0.5, -90F, 0F))
        player.inventory.clear()
    }
}
