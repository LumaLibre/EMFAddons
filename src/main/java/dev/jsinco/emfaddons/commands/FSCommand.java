package dev.jsinco.emfaddons.commands;

import com.oheers.fish.competition.Competition;
import com.oheers.fish.messages.ConfigMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FSCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        Competition active = Competition.getCurrentlyActive();
        if (active == null) {
            ConfigMessage.NO_COMPETITION_RUNNING.getMessage().send(player);
            return true;
        }
        active.sendLeaderboard(player);
        return true;
    }
}
