package dev.jsinco.emfaddons;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.UUID;

public class Util {
    private final static EMFAddons plugin = EMFAddons.getInstance();
    private static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";


    public static File getEMFDataFolder() {
        return new File(plugin.getDataFolder().getParent() + File.separator + "EvenMoreFish");
    }

    public static String colorcode(String text) {
        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                //get the next string
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }
        return finalText.toString();
    }

    public static void base64Head(SkullMeta meta, String base64) {
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.getProperties().add(new ProfileProperty("textures", base64));
        meta.setPlayerProfile(profile);
    }

    public static String uppercaseFirst(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
