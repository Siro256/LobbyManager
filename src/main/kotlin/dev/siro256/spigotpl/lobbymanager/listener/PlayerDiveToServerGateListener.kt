package dev.siro256.spigotpl.lobbymanager.listener

import dev.siro256.spigotpl.lobbymanager.BungeeCordMessagingChannel
import dev.siro256.spigotpl.lobbymanager.LobbyManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import kotlin.math.min
import kotlin.math.max

object PlayerDiveToServerGateListener: Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val location = event.player.location

        LobbyManager.serverGates.forEach { (_, gateData) ->
            if (location.x !in min(gateData.firstX, gateData.secondX)..max(gateData.firstX, gateData.secondX)) return
            if (location.y !in min(gateData.firstY, gateData.secondY)..max(gateData.firstY, gateData.secondY)) return
            if (location.z !in min(gateData.firstZ, gateData.secondZ)..max(gateData.firstZ, gateData.secondZ)) return
            BungeeCordMessagingChannel.sendPlayerToAnotherServer(event.player, gateData.server)
        }
    }
}
