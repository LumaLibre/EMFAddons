package dev.jsinco.emfaddons.commands;

import dev.jsinco.emfaddons.EMFAddons;
import dev.jsinco.emfaddons.Util;
import dev.jsinco.emfaddons.files.EMFFile;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.node.Node;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NotifyNewRecordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        LuckPerms luckPerms = EMFAddons.getLuckPermsAPI();
        if (luckPerms == null) return true;
        Node node = Node.builder("emfaddons.notifyrecord").build();
        String prefix = new EMFFile("messages.yml").getFile().getString("prefix");
        if (player.hasPermission("emfaddons.notifyrecord")) {
            luckPerms.getUserManager().modifyUser(player.getUniqueId(), user -> user.data().remove(node));
            player.sendMessage(Util.colorcode(prefix + "You will no longer be notified of aquatic journal records"));
        } else {
            luckPerms.getUserManager().modifyUser(player.getUniqueId(), user -> user.data().add(node));
            player.sendMessage(Util.colorcode(prefix + "You will now be notified of aquatic journal records"));
        }


        return true;
    }
}
