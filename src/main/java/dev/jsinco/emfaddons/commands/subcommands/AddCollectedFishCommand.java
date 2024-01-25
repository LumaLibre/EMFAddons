package dev.jsinco.emfaddons.commands.subcommands;

import dev.jsinco.emfaddons.EMFAddons;
import dev.jsinco.emfaddons.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AddCollectedFishCommand implements SubCommand {
    @Override
    public void execute(EMFAddons plugin, CommandSender sender, String[] args) {

    }

    @Override
    public List<String> tabComplete(EMFAddons plugin, CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
