package dev.jsinco.emfaddons.guis.util

import dev.jsinco.emfaddons.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class PaginatedGui (
    val name: String,
    private val base: Inventory,
    items: List<ItemStack?>,
    startEndSlots: Pair<Int, Int>,
    ignoreSlots: List<Int>
) {
    companion object {
        private val filler = GuiUtil.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, "&0", listOf(), false)
    }

    val pages: MutableList<Inventory> = mutableListOf()
    val isEmpty = items.isEmpty()
    var size : Int = 0
        private set


    init {
        var currentPage = newPage()
        var currentItem = 0
        var currentSlot = startEndSlots.first
        while (currentItem < items.size) {
            if (ignoreSlots.contains(currentSlot)) {
                currentSlot++
                continue
            }

            if (currentSlot == startEndSlots.second) {
                currentPage = newPage()
                currentSlot = startEndSlots.first
            }

            if (currentPage.getItem(currentSlot) == null) {
                currentPage.setItem(currentSlot, items[currentItem])
                currentItem++
            }
            currentSlot++
        }
        for (i in startEndSlots.first until startEndSlots.second) {
            if (ignoreSlots.contains(i)) {
                continue
            } else if (currentPage.getItem(i) == null) {
                currentPage.setItem(i, filler)
            }
        }

        size = pages.size
    }

    private fun newPage(): Inventory {
        val inventory: Inventory = Bukkit.createInventory(base.holder, base.size, Util.colorcode(name))
        for (i in 0 until base.size) {
            inventory.setItem(i, base.getItem(i))
        }
        pages.add(inventory)
        return inventory
    }


    fun getPage(page: Int): Inventory {
        return pages[page]
    }

    fun indexOf(page: Inventory): Int {
        return pages.indexOf(page)
    }
}