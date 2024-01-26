package dev.jsinco.emfaddons.storing;

import com.google.gson.Gson;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EMFPlayer {
    private final static Gson gson = new Gson();

    private final String uuid;
    // The fish's name, the longest length of that fish the player has ever caught
    private Map<String, Float> allCaughtFish;
    private long lastUpdated = System.currentTimeMillis();


    /*public EMFPlayer(String uuid, <String, Float> allCaughtFish) {
        this.uuid = uuid;
        this.allCaughtFish = allCaughtFish;
    }*/

    public EMFPlayer(String uuid, Map<String, Double> allCaughtFish) {
        this.uuid = uuid;
        this.allCaughtFish = new HashMap<>();
        for (Map.Entry<String, Double> entry : allCaughtFish.entrySet()) {
            this.allCaughtFish.put(entry.getKey(), entry.getValue().floatValue());
        }
    }

    public EMFPlayer(String uuid, String allCaughtFish) {
        this.uuid = uuid;
        this.allCaughtFish = gson.fromJson(allCaughtFish, Map.class);
    }

    public String getUuid() {
        return uuid;
    }

    public Map<String, Float> getAllCaughtFish() {
        return allCaughtFish;
    }

    public void setAllCaughtFish(Map<String, Float> allCaughtFish) {
        this.allCaughtFish = allCaughtFish;
    }

    public void addCaughtFish(String fishName, Float length) {
        allCaughtFish.put(fishName, length);
    }

    public Float getLongestLength(String fishName) {
        if (!hasCaughtFish(fishName)) return 0.0f;
        return allCaughtFish.get(fishName);
    }

    public boolean hasCaughtFish(String fishName) {
        return allCaughtFish.containsKey(fishName);
    }


    public void updateLastUpdated() {
        lastUpdated = System.currentTimeMillis();
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

}
