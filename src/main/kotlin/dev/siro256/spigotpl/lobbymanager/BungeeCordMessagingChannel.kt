package dev.siro256.spigotpl.lobbymanager

import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

@Suppress("UnstableApiUsage")
object BungeeCordMessagingChannel: PluginMessageListener {
    private var servers: List<String>? = null

    fun getServers(): List<String> {
        val out = ByteStreams.newDataOutput()
        out.writeUTF("GetServers")

        Bukkit.getOnlinePlayers().first().sendPluginMessage(LobbyManager.INSTANCE, "BungeeCord", out.toByteArray())

        while (servers == null) Thread.sleep(5)

        val serverList = servers
        servers = null
        return serverList!!
    }

    fun sendPlayerToAnotherServer(player: Player, server: String) {
        val out = ByteStreams.newDataOutput()
        out.writeUTF("Connect")
        out.writeUTF(server)

        player.sendPluginMessage(LobbyManager.INSTANCE, "BungeeCord", out.toByteArray())
    }

    override fun onPluginMessageReceived(channel: String?, player: Player?, message: ByteArray) {
        if (!channel.equals("BungeeCord")) return
        val input = ByteStreams.newDataInput(message)

        when (input.readUTF()) {
            "GetServers" -> {
                servers = input.readUTF().split(", ")
            }
        }
    }
}
