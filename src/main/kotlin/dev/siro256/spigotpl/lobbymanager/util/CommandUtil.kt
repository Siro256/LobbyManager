package dev.siro256.spigotpl.lobbymanager.util

import org.bukkit.ChatColor
import java.lang.StringBuilder

object CommandUtil {
    //(Command to Description)
    fun generateHelp(commandName: String, commandMap: Map<String, String>): String {
        if (commandMap.keys.isNullOrEmpty() || commandMap.values.isNullOrEmpty()) return "\n==== Command help ($commandName) ====\n\n${"=".repeat(25 + commandName.length)}"

        val tempArray = ArrayList<Pair<String?, String>>()
        val helpMessage = StringBuilder()
        var commandLengthMax = 0
        var descriptionLengthMax = 0

        commandMap.forEach { (cmdName, description) ->
            val tempDescription = description.split("\n")

            if (cmdName.length > commandLengthMax) commandLengthMax = cmdName.length
            if (tempDescription.maxOrNull()!!.length > descriptionLengthMax) descriptionLengthMax = tempDescription.maxOrNull()!!.length

            tempArray.add(cmdName to tempDescription[0])
            tempDescription.drop(1).forEach {
                tempArray.add(null to it)
            }
        }

        var headerEqualLength = commandLengthMax + descriptionLengthMax - commandName.length - 10
        if (headerEqualLength < 0) headerEqualLength = 8

        helpMessage.append(
            if (headerEqualLength % 2 == 0)
                "\n${ChatColor.GRAY}${"=".repeat(headerEqualLength / 2)} Command help ($commandName) ${"=".repeat(headerEqualLength / 2)}\n"
            else "\n${ChatColor.GRAY}${"=".repeat(headerEqualLength / 2 + 1)} Command help ($commandName) ${"=".repeat(headerEqualLength / 2)}\n"
        )

        tempArray.forEach {
            helpMessage.append("${if (it.first != null) "\n" else ""}${ChatColor.GOLD}${ChatColor.BOLD}${it.first ?: ""}${" ".repeat(commandLengthMax - (it.first?.length ?: 0))}  ${ChatColor.WHITE}${if (it.first != null) "---" else "   "}  ${ChatColor.UNDERLINE}${it.second}\n")
        }
        helpMessage.append("\n${ChatColor.GRAY}${"=".repeat(headerEqualLength + commandName.length + 17)}\n")

        return helpMessage.toString()
    }
}
