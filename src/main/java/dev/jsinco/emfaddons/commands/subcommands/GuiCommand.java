package dev.jsinco.emfaddons.commands.subcommands;

import dev.jsinco.emfaddons.EMFAddons;
import dev.jsinco.emfaddons.commands.SubCommand;
import dev.jsinco.emfaddons.guis.CollectedFishGui;
import dev.jsinco.emfaddons.guis.SelectorGui;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GuiCommand implements SubCommand {
    @Override
    public void execute(EMFAddons plugin, CommandSender sender, String[] args) {
        if (args.length < 4) {
            if (sender instanceof Player player) {
                player.openInventory(SelectorGui.getInv());
                return;
            }
            sender.sendMessage("Usage: /emfaddons gui <playerToView> <player> <rarity>");
            return;
        }

        Player playerToView = Bukkit.getPlayerExact(args[1]);
        Player player = Bukkit.getPlayerExact(args[2]);
        String rarity = args[3];

        CollectedFishGui gui = new CollectedFishGui(rarity, playerToView.getUniqueId());
        gui.openInventory(player);
    }

    @Override
    public List<String> tabComplete(EMFAddons plugin, CommandSender sender, String[] args) {
        switch (args.length) {
            case 2 -> {
                    return List.of("<playerToView>");
                }
            case 3 -> {
                return List.of("<player>");
            }
            case 4 -> {
                return List.of("<rarity>");
            }
        }
        return null;
    }

    @Override
    public String getPermission() {
        return "emfaddons.fishdiary.gui";
    }
}
