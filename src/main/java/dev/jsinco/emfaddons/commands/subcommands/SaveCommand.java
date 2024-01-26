package dev.jsinco.emfaddons.commands.subcommands;

import dev.jsinco.emfaddons.EMFAddons;
import dev.jsinco.emfaddons.Util;
import dev.jsinco.emfaddons.commands.SubCommand;
import dev.jsinco.emfaddons.files.EMFFile;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SaveCommand implements SubCommand {
    @Override
    public void execute(EMFAddons plugin, CommandSender sender, String[] args) {
        EMFAddons.getSQLite().saveCachedPlayers();
        sender.sendMessage(Util.colorcode(new EMFFile("messages.yml").getFile().getString("prefix") + "Saved all players to database."));
    }

    @Override
    public List<String> tabComplete(EMFAddons plugin, CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermission() {
        return "emfaddons.save";
    }
}
