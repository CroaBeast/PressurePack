package me.croabeast.pressurepack.objects;

import me.croabeast.cupdater.*;
import me.croabeast.pressurepack.*;
import org.bukkit.configuration.file.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class SavedFile {

    private final Application main;
    private final Records records;
    private final String name;
    private final String location;
    private FileConfiguration file;
    private File rawYmlFile;

    public SavedFile(Application main, String name) {
        this.main = main;
        this.records = main.getRecords();
        this.name = name;
        this.location = name + ".yml";

        registerFile();
        Initializer init = main.getInitializer();
        init.FILES++;
        init.getFilesList().add(this);
    }

    private File catchFile() { return new File(main.getDataFolder(), location); }

    public FileConfiguration getFile() { return file; }

    public void reloadFile() {
        if (name.equals("config")) main.reloadConfig();
        file = YamlConfiguration.loadConfiguration(catchFile());
    }

    private void saveFile() {
        if (file == null || rawYmlFile == null) return;
        try {
            this.getFile().save(this.rawYmlFile);
        } catch (IOException e) {
            records.doRecord("&7The &e" + location + "&7 file&c couldn't be saved&7.");
            e.printStackTrace();
        }
        records.doRecord("&7The &e" + location + "&7 file has been&a saved&7.");
    }

    private void updatingFile() {
        try {
            ConfigUpdater.update(main, location, catchFile(), Collections.emptyList());
        } catch (IOException e) {
            records.doRecord("&7The &e" + location + "&7 file&c couldn't be updated&7.");
            e.printStackTrace();
        }
        records.doRecord("&7The &e" + location + "&7 file has been&a updated&7.");
    }

    private void saveDefaultFile() {
        if (name.equals("config")) {
            main.saveDefaultConfig();
            return;
        }
        if (rawYmlFile == null) rawYmlFile = catchFile();
        if (!rawYmlFile.exists())
            main.saveResource(location, false);
    }

    private void registerFile() {
        if (catchFile().exists()) return;
        records.doRecord("&cFile " + location + " missing... &7Generating!");
        saveDefaultFile();
    }

    public void updateInitFile() {
        if (main.getConfig().getBoolean("updater." + name)) updatingFile();
        reloadFile();
    }
}
