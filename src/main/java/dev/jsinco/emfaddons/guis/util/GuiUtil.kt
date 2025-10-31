package dev.jsinco.emfaddons.guis.util

import dev.jsinco.emfaddons.EMFAddons
import dev.jsinco.emfaddons.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object GuiUtil {
    private val plugin: EMFAddons = EMFAddons.getInstance()

    fun getGuiArrows(): Pair<ItemStack, ItemStack> {
        val pair: Pair<ItemStack, ItemStack> = Pair(ItemStack(Material.ARROW), ItemStack(Material.ARROW))

        var meta = pair.first.itemMeta!!
        meta.setDisplayName(Util.colorcode("&#f498f6Previous Page"))
        meta.persistentDataContainer.set(NamespacedKey(plugin, "collectedfish-gui-item"), PersistentDataType.STRING, "previous")
        pair.first.itemMeta = meta
        meta = pair.second.itemMeta!!
        meta.setDisplayName(Util.colorcode("&#f498f6Next Page"))
        meta.persistentDataContainer.set(NamespacedKey(plugin, "collectedfish-gui-item"), PersistentDataType.STRING, "next")
        pair.second.itemMeta = meta
        return pair
    }

    fun getBackButton(): ItemStack {
        val item = ItemStack(Material.BARRIER)
        val meta = item.itemMeta!!
        meta.setDisplayName(Util.colorcode("&cBack"))
        meta.persistentDataContainer.set(NamespacedKey(plugin, "collectedfish-gui-item"), PersistentDataType.STRING, "back")
        item.itemMeta = meta
        return item
    }

    fun createGuiItem(material: Material, name: String, lore: List<String>, glow: Boolean): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta!!
        meta.setDisplayName(Util.colorcode(name))
        meta.lore = lore.map { Util.colorcode(it) }
        if (glow) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        item.itemMeta = meta
        return item
    }
}