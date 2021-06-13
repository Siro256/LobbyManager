package dev.siro256.spigotpl.lobbymanager.command

import dev.siro256.spigotpl.lobbymanager.BungeeCordMessagingChannel
import dev.siro256.spigotpl.lobbymanager.Gate
import dev.siro256.spigotpl.lobbymanager.LobbyManager
import dev.siro256.spigotpl.lobbymanager.ServerGateManager
import dev.siro256.spigotpl.lobbymanager.util.CommandUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.StringBuilder

object GateCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        args.let {
            if (it.isEmpty()) {
                sender.sendMessage("${ChatColor.YELLOW}Missing argument. Please check ${ChatColor.BOLD}/${label} help.")
                return true
            }

            when (it[0]) {
                "help" -> {
                    showHelp(sender, label)
                    return true
                }
                "add" -> {
                    addGate(sender, label, args)
                    return true
                }
                "delete" -> {
                    deleteGate(sender, label, args)
                    return true
                }
                "list" -> {
                    listGates(sender)
                    return true
                }
                else -> {
                    sender.sendMessage("${ChatColor.YELLOW}Invalid argument(s). Please check ${ChatColor.BOLD}/${label} help.")
                    return true
                }
            }
        }
    }

    private fun showHelp(sender: CommandSender, label: String) {
        val commandMap = mapOf(
            "/$label help" to "Show this page.",
            "/$label add <name> <server>" to "Add gate to the area that selected by wand.",
            "/$label delete <name>" to "Delete gate.",
            "/$label list" to "List of gates."
        )

        commandMap.forEach { (key, _) ->
            LobbyManager.INSTANCE.logger.info(key.length.toString())
        }

        sender.sendMessage(CommandUtil.generateHelp(label, commandMap))
    }

    private fun addGate(sender: CommandSender, label: String, args: Array<out String>) {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}This command can't execute at console. Please execute in player.")
            return
        }

        when (args.size) {
            1 -> {
                sender.sendMessage("${ChatColor.YELLOW}Missing gate name and server name. Please check ${ChatColor.BOLD}/$label help.")
                return
            }
            2 -> {
                sender.sendMessage("${ChatColor.YELLOW}Missing server name. Please check ${ChatColor.BOLD}/$label help.")
                return
            }
            3 -> {
                //Do nothing
            }
            else -> {
                sender.sendMessage("${ChatColor.YELLOW}Too many arguments. Please check ${ChatColor.BOLD}/$label help.")
                return
            }
        }

        if (!LobbyManager.playerWandPosition.containsKey(sender.uniqueId)) {
            sender.sendMessage("${ChatColor.YELLOW}Please select two positions first.")
            return
        } else if (LobbyManager.playerWandPosition[sender.uniqueId]!!.first == null) {
            sender.sendMessage("${ChatColor.YELLOW}Please select the first position first.")
            return
        } else if (LobbyManager.playerWandPosition[sender.uniqueId]!!.second == null) {
            sender.sendMessage("${ChatColor.YELLOW}Please select the second position first.")
            return
        }

        if (LobbyManager.serverGates.containsKey(args[1])) {
            sender.sendMessage("${ChatColor.YELLOW}That named gate is already exists. Please use another name.")
            return
        }

        Bukkit.getScheduler().runTaskAsynchronously(LobbyManager.INSTANCE) {
            @Suppress("UNCHECKED_CAST")

            if (!BungeeCordMessagingChannel.getServers().contains(args[1])) {
                sender.sendMessage("${ChatColor.YELLOW}That server is not exist. Please check the server name.")
                return@runTaskAsynchronously
            }

            @Suppress("UNCHECKED_CAST")
            val position = LobbyManager.playerWandPosition[sender.uniqueId] as Pair<Location, Location>

            LobbyManager.serverGates[args[1]] = Gate(
                args[2],
                position.first.x,
                position.first.y,
                position.first.z,
                position.second.x,
                position.second.y,
                position.second.z
            )
            ServerGateManager.syncConfigToVariable()
            sender.sendMessage("${ChatColor.GREEN}Add gate \"${args[1]}\" to server ${args[2]}, location x: ${position.first.x} - ${position.second.x} y: ${position.first.y} - ${position.second.y} z: ${position.first.z} - ${position.second.z}")
        }
    }

    private fun deleteGate(sender: CommandSender, label: String, args: Array<out String>) {
        when (args.size) {
            1 -> {
                sender.sendMessage("${ChatColor.YELLOW}Missing gate name. Please check ${ChatColor.BOLD}/$label help.")
                return
            }
            2 -> {
                //Do nothing
            }
            else -> {
                sender.sendMessage("${ChatColor.YELLOW}Too many arguments. Please check ${ChatColor.BOLD}/$label help.")
                return
            }
        }

        if (!LobbyManager.serverGates.containsKey(args[1])) {
            sender.sendMessage("${ChatColor.YELLOW}That named gate isn't exist. Please check the name.")
            return
        }

        val location = LobbyManager.serverGates[args[1]]

        LobbyManager.serverGates.remove(args[1])
        ServerGateManager.syncConfigToVariable()
        sender.sendMessage("${ChatColor.GREEN}Removed gate \"${args[1]}\" to location $location")
    }

    private fun listGates(sender: CommandSender) {
        val stringBuilder = StringBuilder()

        stringBuilder.append("${ChatColor.GREEN}Server gates:")

        LobbyManager.serverGates.forEach { (name, gateData) ->
            stringBuilder.append("\n  ${ChatColor.YELLOW}${ChatColor.BOLD}$name${ChatColor.WHITE}:\n")
            stringBuilder.append("    ${ChatColor.YELLOW}${ChatColor.BOLD}Server: ${ChatColor.YELLOW}${gateData.server}\n")
            stringBuilder.append("    ${ChatColor.YELLOW}${ChatColor.BOLD}X: ${ChatColor.YELLOW}${gateData.firstX} - ${gateData.secondX}\n")
            stringBuilder.append("    ${ChatColor.YELLOW}${ChatColor.BOLD}Y: ${ChatColor.YELLOW}${gateData.firstY} - ${gateData.secondY}\n")
            stringBuilder.append("    ${ChatColor.YELLOW}${ChatColor.BOLD}Z: ${ChatColor.YELLOW}${gateData.firstZ} - ${gateData.secondZ}")
        }
        stringBuilder.append("\n")
        sender.sendMessage(stringBuilder.toString())
    }
}
