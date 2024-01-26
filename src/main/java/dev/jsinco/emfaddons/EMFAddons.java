package dev.jsinco.emfaddons;

import dev.jsinco.emfaddons.commands.CommandManager;
import dev.jsinco.emfaddons.commands.NotifyNewRecordCommand;
import dev.jsinco.emfaddons.guis.SelectorGui;
import dev.jsinco.emfaddons.guis.util.Gui;
import dev.jsinco.emfaddons.listeners.Events;
import dev.jsinco.emfaddons.storing.SQLite;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class EMFAddons extends JavaPlugin {

    private static SQLite sqLite;
    private static EMFAddons instance;
    private static boolean useLuckPermsAPI;
    @Override
    public void onEnable() {
        instance = this;
        sqLite = new SQLite(this);
        getServer().getPluginManager().registerEvents(new Events(this, sqLite), this);
        getCommand("emfaddons").setExecutor(new CommandManager(this));
        getCommand("notifynewfishrecord").setExecutor(new NotifyNewRecordCommand());
        useLuckPermsAPI = getServer().getPluginManager().getPlugin("LuckPerms") != null;
    }

    @Override
    public void onDisable() {
        sqLite.closeConnection();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof Gui) {
                player.closeInventory();
            }
        }
    }

    public static SQLite getSQLite() {
        return sqLite;
    }

    public static EMFAddons getInstance() {
        return instance;
    }

    public static LuckPerms getLuckPermsAPI() {
        if (!useLuckPermsAPI) return null;
        return Bukkit.getServicesManager().getRegistration(LuckPerms.class).getProvider();
    }

    public static boolean isUseLuckPermsAPI() {
        return useLuckPermsAPI;
    }
}
