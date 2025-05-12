package dev.jsinco.emfaddons.commands;

import com.oheers.fish.api.economy.Economy;
import com.oheers.fish.gui.guis.SellGui;
import com.oheers.fish.messages.ConfigMessage;
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

        if (!checkEconomy(sender)) {
            return true;
        }

        new SellGui(player, SellGui.SellState.NORMAL, null).open();
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
