package dev.siro256.spigotpl.lobbymanager

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.nio.file.Paths

object ServerGateManager {
    fun initializeConfiguration() {
        val configFile = File("${Paths.get("").toAbsolutePath()}/plugins/LobbyManager/", "gates.json")

        if (!configFile.exists()) {
            configFile.apply {
                parentFile.mkdirs()
                createNewFile()
                writeText("{}")
            }
            return
        }
        val configText = configFile.readText()

        LobbyManager.serverGates = Gson().fromJson<HashMap<String, Gate>>(configText)
    }

    fun syncConfigToVariable() {
        val configFile = File(Paths.get("").toAbsolutePath().toString(), "/plugins/LobbyManager/gates.json")

        configFile.writeText(Gson().toJson(LobbyManager.serverGates))
    }
}

private inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

data class Gate(val server: String, val firstX: Double, val firstY: Double, val firstZ: Double, val secondX: Double, val secondY: Double, val secondZ: Double)
