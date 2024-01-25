package dev.jsinco.emfaddons.guis.util

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.InventoryHolder

interface Gui : InventoryHolder {

    fun handleInvClick(event: InventoryClickEvent)
}