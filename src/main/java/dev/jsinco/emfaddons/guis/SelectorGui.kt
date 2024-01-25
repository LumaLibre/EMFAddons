package dev.jsinco.emfaddons.guis

import dev.jsinco.emfaddons.EMFAddons
import dev.jsinco.emfaddons.files.FileManager
import dev.jsinco.emfaddons.guis.util.Gui
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.persistence.PersistentDataType

// Static class because everyone can use the same selector gui. No need to personalize this!
object SelectorGui : Gui {
    private val plugin: EMFAddons = EMFAddons.getInstance()
    private val file = FileManager("aquadiary.yml").generateYamlFile()

    @JvmStatic
    val inv: Inventory = Bukkit.createInventory(this, file.getInt("selector-gui.size"), file.getString("selector-gui.title") ?: "Selector")

    init {
        val items = GuiItemCreator(file, "selector-gui.items").items
        for (item in items) {
            for (slot in item.value) {
                inv.setItem(slot, item.key)
            }
        }
    }

    override fun handleInvClick(event: InventoryClickEvent) {
        event.isCancelled = true
        val item = event.currentItem ?: return
        val guiItemType = item.itemMeta.persistentDataContainer.get(NamespacedKey(plugin, "gui-item"), PersistentDataType.STRING) ?: return

        val collectedFishGui = CollectedFishGui(guiItemType, event.whoClicked.uniqueId)
        collectedFishGui.openInventory(event.whoClicked as Player)
    }

    override fun getInventory(): Inventory {
        return inv
    }
}