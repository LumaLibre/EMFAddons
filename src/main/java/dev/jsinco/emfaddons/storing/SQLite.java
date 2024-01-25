package dev.jsinco.emfaddons.storing;

import dev.jsinco.emfaddons.EMFAddons;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLite extends Database {

    private final EMFAddons plugin;
    private Connection connection;
    private final File sqlFile;


    public SQLite(final EMFAddons plugin) {
        this.plugin = plugin;
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        sqlFile = new File(plugin.getDataFolder(), "data.db");
        if (!sqlFile.exists()) {
            try {
                sqlFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        initDatabase();
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + sqlFile);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        }
        return null;
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                saveCachedPlayers();
                cancelBukkitTask();
                connection.close();
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "SQLite exception on close", ex);
        }
    }
}
