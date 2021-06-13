package dev.siro256.spigotpl.lobbymanager.listener

import dev.siro256.spigotpl.lobbymanager.LobbyManager
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object WandListener: Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.material != Material.WOOD_AXE) return

        event.isCancelled = true

        var wandPosition = if (LobbyManager.playerWandPosition.containsKey(event.player.uniqueId)) LobbyManager.playerWandPosition[event.player.uniqueId] else null to null

        if (event.action == Action.LEFT_CLICK_BLOCK) {
            wandPosition = wandPosition!!.copy(first = event.clickedBlock.location)
            event.player.sendMessage("${ChatColor.LIGHT_PURPLE}First position set to (${event.clickedBlock.location.blockX}, ${event.clickedBlock.location.blockY}, ${event.clickedBlock.location.blockZ}).")
        }
        else if (event.action == Action.RIGHT_CLICK_BLOCK) {
            wandPosition = wandPosition!!.copy(second = event.clickedBlock.location)
            event.player.sendMessage("${ChatColor.LIGHT_PURPLE}Second position set to (${event.clickedBlock.location.blockX}, ${event.clickedBlock.location.blockY}, ${event.clickedBlock.location.blockZ}).")
        }
        LobbyManager.playerWandPosition[event.player.uniqueId] = wandPosition!!
    }
}
