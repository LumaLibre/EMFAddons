package dev.jsinco.emfaddons.storing;

import com.google.gson.Gson;
import dev.jsinco.emfaddons.EMFAddons;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public abstract class Database {
    private final static Gson gson = new Gson();
    private final static List<EMFPlayer> cachedPlayers = new ArrayList<>();

    private int bukkitTaskId = -1;

    public abstract Connection getConnection();
    public abstract void closeConnection();

    protected void initDatabase() {
        try (PreparedStatement statement = getConnection().prepareStatement(
                "CREATE TABLE IF NOT EXISTS emf_players (uuid VARCHAR(36) PRIMARY KEY, all_caught_fish TEXT);")){

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Async Bukkit Database task
        bukkitTaskId =
        Bukkit.getScheduler().scheduleSyncRepeatingTask(EMFAddons.getInstance(), () -> {
            saveCachedPlayers();
            cachedPlayers.removeIf(emfPlayer
                    -> emfPlayer.getLastUpdated() + 600000 < System.currentTimeMillis());
        },0L, 12000L);
    }

    public EMFPlayer fetchPlayer(UUID uuid) {
        return fetchPlayer(uuid.toString());
    }

    public EMFPlayer fetchPlayer(String uuid) {
        for (EMFPlayer player : cachedPlayers) {
            if (player.getUuid().equals(uuid)) {
                player.updateLastUpdated();
                return player;
            }
        }
        try (PreparedStatement statement = getConnection().prepareStatement(
                "SELECT * FROM emf_players WHERE uuid=?;")) {

            statement.setString(1, uuid);
            String allCaughtFishJson = statement.executeQuery().getString("all_caught_fish");
            Map<String, Double> allCaughtFish = allCaughtFishJson != null ? gson.fromJson(allCaughtFishJson, Map.class) : new HashMap<>();
            EMFPlayer player = new EMFPlayer(uuid, allCaughtFish);
            cachedPlayers.add(player);
            return player;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updatePlayer(String uuid, Map<String, Float> allCaughtFish) {
        EMFPlayer emfPlayer = fetchPlayer(uuid);
        emfPlayer.setAllCaughtFish(allCaughtFish);
    }

    public void saveCachedPlayers() {
        for (EMFPlayer player : cachedPlayers) {
            try (PreparedStatement statement = getConnection().prepareStatement(
                    "INSERT OR REPLACE INTO emf_players (uuid, all_caught_fish) VALUES (?, ?)")) {

                statement.setString(1, player.getUuid());
                statement.setString(2, gson.toJson(player.getAllCaughtFish()));
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelBukkitTask() {
        Bukkit.getScheduler().cancelTask(bukkitTaskId);
    }
}
