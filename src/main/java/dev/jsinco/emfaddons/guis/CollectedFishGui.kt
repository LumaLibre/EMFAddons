package dev.jsinco.emfaddons.guis

import dev.jsinco.emfaddons.EMFAddons
import dev.jsinco.emfaddons.Util
import dev.jsinco.emfaddons.files.EMFFile
import dev.jsinco.emfaddons.files.FileManager
import dev.jsinco.emfaddons.guis.util.Gui
import dev.jsinco.emfaddons.guis.util.GuiUtil
import dev.jsinco.emfaddons.guis.util.PaginatedGui
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*


class CollectedFishGui (
    val rarity: String,
    val uuid: String
) : Gui {
    override fun getInventory(): Inventory { return base }

    constructor(rarity: String, uuid: UUID) : this(rarity, uuid.toString())

    private val base = Bukkit.createInventory(this, 54, "$rarity Fish")
    private val raritiesFile = EMFFile("rarities.yml").file

    private val rarityColor: String = raritiesFile.getString("rarities.$rarity.colour") ?: ""

    private val collectedFish: Map<String, Float> = EMFAddons.getSQLite().fetchPlayer(uuid).allCaughtFish
    private val fishFile = EMFFile("fish.yml").file
    private val collectedFishOfRarity: MutableMap<String, Float> = mutableMapOf()
    private val fishItems: MutableList<ItemStack> = mutableListOf()


    val paginatedGui: PaginatedGui

    init {
        val borderItems = GuiItemCreator(FileManager("aquadiary.yml").generateYamlFile(), "collected-fish-gui.border-items").items
        for (item in borderItems) {
            for (slot in item.value) {
                base.setItem(slot, item.key)
            }
        }
    }


    init {
        val configSec = fishFile.getConfigurationSection("fish.$rarity")!!

        for (fishKey in configSec.getKeys(false)) {
            if (collectedFish.contains(fishKey)) {
                collectedFishOfRarity[fishKey] = collectedFish[fishKey]!!
            }
        }


        for (fishKey in collectedFishOfRarity) {
            val keyString = fishKey.key
            val item = ItemStack(if (configSec.contains("$keyString.item.material")) {
                Material.valueOf(configSec.getString("$keyString.item.material") ?: "COD")
            } else if (configSec.contains("$keyString.item.head-64") || configSec.contains("$fishKey.item.head-uuid")) {
                Material.PLAYER_HEAD
            } else {
                Material.COD
            })

            val meta = item.itemMeta!!
            if (item.type == Material.PLAYER_HEAD) {
                meta as SkullMeta
                if (configSec.contains("$keyString.item.head-64")) {
                    Util.base64Head(meta, configSec.getString("$keyString.item.head-64")!!)
                    //val profile: PlayerProfile = Bukkit.createProfile(UUID.randomUUID())
                    //profile.properties.add(ProfileProperty("textures", configSec.getString("$keyString.item.head-64")!!))
                    //meta.playerProfile = profile
                } else if (configSec.contains("$keyString.item.head-uuid")) {
                    meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(configSec.getString("$keyString.item.head-uuid")!!)))
                }
            }

            if (configSec.getBoolean("$fishKey.glowing")) {
                meta.addEnchant(Enchantment.LUCK, 1, true)
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            meta.setDisplayName(Util.colorcode("$rarityColor${fishKey.key}"))
            val loreList: MutableList<String> = mutableListOf()
            loreList.add(Util.colorcode("&fLargest Catch&7: &e${fishKey.value}"))
            if (configSec.getStringList("$fishKey.item.lore").isNotEmpty()) loreList.add("")
            loreList.addAll(configSec.getStringList("$fishKey.item.lore").map { Util.colorcode(it) })
            loreList.addAll(listOf("", Util.colorcode("$rarityColor&l${rarity.uppercase()}")))

            meta.lore = loreList
            item.itemMeta = meta
            fishItems.add(item)
        }

        paginatedGui = PaginatedGui("$rarity Fish", base, fishItems, Pair(19, 34), listOf(26, 27))
    }

    init {
        val guiArrows = GuiUtil.getGuiArrows()
        for (page in paginatedGui.pages) {
            page.setItem(49, GuiUtil.getBackButton())
            if (paginatedGui.indexOf(page) != 0) {
                page.setItem(48, guiArrows.first)
            }
            if (paginatedGui.indexOf(page) != paginatedGui.pages.size - 1) {
                page.setItem(50, guiArrows.second)
            }
        }
    }

    fun openInventory(player: Player) {
        player.openInventory(paginatedGui.getPage(0))
    }

    override fun handleInvClick(event: InventoryClickEvent) {
        event.isCancelled = true
    }
}