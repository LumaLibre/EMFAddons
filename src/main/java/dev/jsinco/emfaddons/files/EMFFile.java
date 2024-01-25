package dev.jsinco.emfaddons.files;

import dev.jsinco.emfaddons.Util;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;

public class EMFFile {

    private final String fileName;
    private final File file;
    private final YamlConfiguration yamlConfiguration;

    public EMFFile(String fileName) {
        this.fileName = fileName;
        this.file = new File(Util.getEMFDataFolder(), fileName);
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            yamlConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getFile() {
        return yamlConfiguration;
    }
}
