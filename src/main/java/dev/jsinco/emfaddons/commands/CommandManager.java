package dev.jsinco.emfaddons.commands;

import dev.jsinco.emfaddons.EMFAddons;
import dev.jsinco.emfaddons.commands.subcommands.GuiCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager implements TabExecutor {

    private final static Map<String, SubCommand> subCommands = new HashMap<>(Map.of(
            "gui", new GuiCommand()
    ));
    private final EMFAddons plugin;

    public CommandManager(EMFAddons plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage("Subcommand not found");
            return true;
        }
        String subCommandName = strings[0];

        if (!subCommands.containsKey(subCommandName)) {
            commandSender.sendMessage("Subcommand not found");
            return true;
        }

        SubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand.getPermission() != null && !commandSender.hasPermission(subCommand.getPermission())) {
            commandSender.sendMessage("You do not have permission to use this command");
            return true;
        }

        subCommand.execute(plugin, commandSender, strings);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return List.copyOf(subCommands.keySet());
        }

        if (subCommands.containsKey(strings[0])) {
            return subCommands.get(strings[0]).tabComplete(plugin, commandSender, strings);
        }
        return null;
    }
}
