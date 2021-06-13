package dev.siro256.spigotpl.lobbymanager

import dev.siro256.spigotpl.lobbymanager.command.GateCommand
import dev.siro256.spigotpl.lobbymanager.command.WandCommand
import dev.siro256.spigotpl.lobbymanager.listener.PlayerDiveToServerGateListener
import dev.siro256.spigotpl.lobbymanager.listener.PlayerJoinListener
import dev.siro256.spigotpl.lobbymanager.listener.WandListener
import org.bukkit.ChatColor
import org.bukkit.Difficulty
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.HashMap

@Suppress("unused")
class LobbyManager: JavaPlugin() {

    override fun onEnable() {
        logger.info("${ChatColor.GREEN}Starting LobbyManager...")

        INSTANCE = this

        ServerGateManager.initializeConfiguration()

        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")
        server.messenger.registerIncomingPluginChannel(this, "BungeeCord", BungeeCordMessagingChannel)

        server.worlds.forEach {
            it.difficulty = Difficulty.PEACEFUL
        }

        //Register events
        listOf(
            PlayerJoinListener,
            WandListener,
            PlayerDiveToServerGateListener
        ).forEach { server.pluginManager.registerEvents(it, this) }

        //Register commands
        mapOf(
            "/wand" to WandCommand,
            "gate" to GateCommand
        ).forEach { (command, executor) ->
            getCommand(command).executor = executor
        }

        logger.info("${ChatColor.GREEN}LobbyManager finished starting!")
    }

    override fun onDisable() {
        server.messenger.unregisterOutgoingPluginChannel(this)
        server.messenger.unregisterIncomingPluginChannel(this)
    }

    companion object {
        lateinit var INSTANCE: LobbyManager
        val playerWandPosition = HashMap<UUID, Pair<Location?, Location?>>()
        var serverGates = hashMapOf<String, Gate>()
    }
}
