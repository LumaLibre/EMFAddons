package dev.jsinco.emfaddons.listeners

import dev.jsinco.emfaddons.guis.util.Gui
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class GuiListeners : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.inventory.holder is Gui) {
            val gui = event.inventory.holder as Gui
            gui.handleInvClick(event)
        }
    }
}