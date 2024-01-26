package dev.jsinco.emfaddons.listeners;

import com.oheers.fish.api.EMFCompetitionEndEvent;
import com.oheers.fish.api.EMFCompetitionStartEvent;
import com.oheers.fish.api.EMFFishEvent;
import com.oheers.fish.fishing.items.Fish;
import dev.jsinco.emfaddons.EMFAddons;
import dev.jsinco.emfaddons.Util;
import dev.jsinco.emfaddons.files.EMFFile;
import dev.jsinco.emfaddons.guis.SelectorGui;
import dev.jsinco.emfaddons.guis.util.Gui;
import dev.jsinco.emfaddons.storing.EMFPlayer;
import dev.jsinco.emfaddons.storing.SQLite;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Calendar;
import java.util.Optional;

public class Events implements Listener {

    private final EMFAddons plugin;
    private final SQLite sqLite;

    public Events(EMFAddons plugin, SQLite sqLite) {
        this.plugin = plugin;
        this.sqLite = sqLite;
    }

    @EventHandler
    public void onEMFFish(EMFFishEvent event) {
        Fish fish = event.getFish();
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
        if (event.getItem() == null || !event.getAction().isRightClick()) return;
        if (!event.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "fish-diary"), PersistentDataType.BOOLEAN)) return;
        event.getPlayer().openInventory(SelectorGui.getInv());
        event.setCancelled(true);
    }


    @EventHandler
    public void onEMFStartCompetitionStart(EMFCompetitionStartEvent event) {
        if (!EMFAddons.isUseLuckPermsAPI()) {
            plugin.getLogger().warning("LuckPerms not found, cannot remove weekend permission");
            return;
        }
        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            LuckPerms luckPerms = EMFAddons.getLuckPermsAPI();
            Node node = Node.builder("emfaddons.weekend").build();
            Optional<Group> group = luckPerms.getGroupManager().loadGroup("default").join();
            if (group.isEmpty()) {
                plugin.getLogger().warning("Could not find default group");
                return;
            }
            if (!group.get().data().toCollection().contains(node)) {
                group.get().data().add(node);
                luckPerms.getGroupManager().saveGroup(group.get());
                plugin.getLogger().info("Weekend detected, granting weekend permission to all players");
            }
        }
    }

    @EventHandler
    public void onEMFEndCompetition(EMFCompetitionEndEvent event) {
        if (!EMFAddons.isUseLuckPermsAPI()) {
            plugin.getLogger().warning("LuckPerms not found, cannot remove weekend permission");
            return;
        }
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            LuckPerms luckPerms = EMFAddons.getLuckPermsAPI();

            Node node = Node.builder("emfaddons.weekend").build();
            Optional<Group> group = luckPerms.getGroupManager().loadGroup("default").join();
            if (group.isEmpty()) {
                plugin.getLogger().warning("Could not find default group");
                return;
            }
            if (group.get().data().toCollection().contains(node)) {
                group.get().data().remove(node);
                luckPerms.getGroupManager().saveGroup(group.get());
                plugin.getLogger().info("Weekend over, removing weekend permission from all players");
            }
        }
    }
}
