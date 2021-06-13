package dev.siro256.spigotpl.lobbymanager.command

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object WandCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true

        sender.inventory.addItem(ItemStack(Material.WOOD_AXE))
        sender.sendMessage("${ChatColor.LIGHT_PURPLE}Left click: select pos #1; Right click: select pos #2")
        return true
    }
}
