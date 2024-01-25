package dev.jsinco.emfaddons;

import dev.jsinco.emfaddons.commands.CommandManager;
import dev.jsinco.emfaddons.listeners.Events;
import dev.jsinco.emfaddons.storing.SQLite;
import org.bukkit.plugin.java.JavaPlugin;

public final class EMFAddons extends JavaPlugin {

    private static SQLite sqLite;
    private static EMFAddons instance;
    @Override
    public void onEnable() {
        instance = this;
        sqLite = new SQLite(this);
        getServer().getPluginManager().registerEvents(new Events(this, sqLite), this);
        getCommand("emfaddons").setExecutor(new CommandManager(this));
    }

    @Override
    public void onDisable() {
        sqLite.closeConnection();
    }

    public static SQLite getSQLite() {
        return sqLite;
    }

    public static EMFAddons getInstance() {
        return instance;
    }
}
