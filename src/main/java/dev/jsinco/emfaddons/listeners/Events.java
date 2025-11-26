package dev.jsinco.emfaddons.listeners;

import com.oheers.fish.api.events.EMFFishCaughtEvent;
import com.oheers.fish.api.fishing.items.IFish;
import dev.jsinco.emfaddons.EMFAddons;
import dev.jsinco.emfaddons.Util;
import dev.jsinco.emfaddons.files.EMFFile;
import dev.jsinco.emfaddons.guis.SelectorGui;
import dev.jsinco.emfaddons.guis.util.Gui;
import dev.jsinco.emfaddons.storing.EMFPlayer;
import dev.jsinco.emfaddons.storing.SQLite;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

public class Events implements Listener {

    private final EMFAddons plugin;
    private final SQLite sqLite;

    public Events(EMFAddons plugin, SQLite sqLite) {
        this.plugin = plugin;
        this.sqLite = sqLite;
    }

    @EventHandler
    public void onEMFFish(EMFFishCaughtEvent event) {
        IFish fish = event.getFish();
        EMFPlayer emfPlayer = sqLite.fetchPlayer(event.getPlayer().getUniqueId().toString());


        if (!emfPlayer.hasCaughtFish(fish.getName()) || emfPlayer.getLongestLength(fish.getName()) < fish.getLength()) {
            emfPlayer.addCaughtFish(fish.getName(), fish.getLength());
            if (!event.getPlayer().hasPermission("emfaddons.notifyrecord")) return;
            event.getPlayer().sendMessage(Util.colorcode(
                    new EMFFile("messages.yml").getFile().getString("prefix") +
                            "You caught a new record for a &#f498f6" + fish.getName() + "&#E2E2E2!"
            ));
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof Gui) {
            ((Gui) event.getInventory().getHolder()).handleInvClick(event);
        }
    }

    @EventHandler
    public void onPlayerClicksFishJournal(PlayerInteractEvent event) {
        if (event.getItem() == null || !event.getAction().isRightClick() || !event.getItem().hasItemMeta()) return;
        if (!event.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "fish-diary"), PersistentDataType.BOOLEAN)) return;
        event.getPlayer().openInventory(SelectorGui.getInv());
        event.setCancelled(true);
    }
}
