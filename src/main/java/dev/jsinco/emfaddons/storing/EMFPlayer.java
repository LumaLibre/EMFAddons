package dev.jsinco.emfaddons.storing;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class EMFPlayer {
    private final static Gson gson = new Gson();

    private final String uuid;
    // The fish's name, the longest length of that fish the player has ever caught
    private Map<String, Float> allCaughtFish;
    private long lastUpdated = System.currentTimeMillis();


    public EMFPlayer(String uuid, Map<String, Float> allCaughtFish) {
        this.uuid = uuid;
        this.allCaughtFish = allCaughtFish;
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

    public void addCaughtFish(String fishName, float length) {
        allCaughtFish.put(fishName, length);
    }

    public float getLongestLength(String fishName) {
        if (!hasCaughtFish(fishName)) return 0;
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
