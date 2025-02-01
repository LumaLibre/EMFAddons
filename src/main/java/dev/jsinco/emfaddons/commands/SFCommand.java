package dev.jsinco.emfaddons.commands;

import com.oheers.fish.api.economy.Economy;
import com.oheers.fish.config.messages.ConfigMessage;
import com.oheers.fish.gui.guis.SellGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SFCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        new SellGUI(player, SellGUI.SellState.NORMAL, null).open();
        return true;
    }

    private boolean checkEconomy(@NotNull CommandSender sender) {
        if (!Economy.getInstance().isEnabled()) {
            ConfigMessage.ECONOMY_DISABLED.getMessage().send(sender);
            return false;
        }
        return true;
    }
}
