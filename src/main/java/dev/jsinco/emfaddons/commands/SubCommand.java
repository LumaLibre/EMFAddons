package dev.jsinco.emfaddons.commands;

import dev.jsinco.emfaddons.EMFAddons;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    void execute(EMFAddons plugin, CommandSender sender, String[] args);

    List<String> tabComplete(EMFAddons plugin, CommandSender sender, String[] args);

    String getPermission();
}
