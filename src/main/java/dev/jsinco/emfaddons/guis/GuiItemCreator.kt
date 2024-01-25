package dev.jsinco.emfaddons.guis

import dev.jsinco.emfaddons.EMFAddons
import dev.jsinco.emfaddons.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

class GuiItemCreator (file: YamlConfiguration, path: String) {

    companion object {
        private val plugin = EMFAddons.getInstance()
    }

    val items: MutableMap<ItemStack, List<Int>> = mutableMapOf()
    private val configSec = file.getConfigurationSection(path)!!

    init {
        for (key in configSec.getKeys(false)) {
            val item = ItemStack(Material.valueOf(configSec.getString("$key.material") ?: "AIR"))
            val meta = item.itemMeta!!
            meta.setDisplayName(Util.colorcode(configSec.getString("$key.name") ?: ""))
            meta.lore = configSec.getStringList("$key.lore").map { Util.colorcode(it) }
            configSec.getString("$key.linked")?.let {
                meta.persistentDataContainer.set(NamespacedKey(plugin, "gui-item"), PersistentDataType.STRING,
                    it
                )
            }

            if (configSec.contains("$key.glow") && configSec.getBoolean("$key.glow")) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true)
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            }

            if (item.type == Material.PLAYER_HEAD && configSec.contains("$key.head-64")) {
                Util.base64Head(meta as SkullMeta, configSec.getString("$key.head-64"))
            }
            item.itemMeta = meta
            items[item] = configSec.getIntegerList("$key.slots")
        }
    }

}