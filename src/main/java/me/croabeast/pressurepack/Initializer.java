package me.croabeast.pressurepack;

import me.croabeast.pressurepack.listeners.*;
import me.croabeast.pressurepack.objects.*;
import net.milkbowl.vault.permission.*;
import org.bukkit.plugin.*;

import java.util.*;

public class Initializer {

    private final Application main;
    private final Records records;

    public static Permission Perms;

    public int LISTENERS = 0;
    public int FILES = 0;

    public boolean HAS_PAPI;
    public boolean HAS_VAULT;

    public SavedFile config;
    public SavedFile lang;

    public Initializer(Application main) {
        this.main = main;
        this.records = main.getRecords();

        HAS_PAPI = isPlugin("PlaceholderAPI");
        HAS_VAULT = isPlugin("Vault");
    }

    private boolean isPlugin(String name) {
        return main.getPlugin(name) != null;
    }

    protected Set<SavedFile> filesList = new HashSet<>();
    public Set<SavedFile> getFilesList() { return filesList; }

    public void registerConfigFile() {
        records.doRecord("&bLoading plugin's files...");

        config = new SavedFile(main, "config");
        lang = new SavedFile(main, "lang");

        filesList.forEach(SavedFile::updateInitFile);
        records.doRecord("&7Loaded &e" + FILES + "&7 file in the plugin's folder.");
    }

    public void registerHooks() {
        records.doRecord("", "&bChecking all plugin hooks...");
        showPluginInfo("PlaceholderAPI");

        if (!HAS_VAULT)
            records.doRecord("&7Vault&c isn't installed&7, using default system.");
        else {
            ServicesManager servMngr = main.getServer().getServicesManager();
            RegisteredServiceProvider<Permission> rsp = servMngr.getRegistration(Permission.class);
            if (rsp != null) {
                Perms = rsp.getProvider();
                records.doRecord("&7Vault&a installed&7, hooking in a perm plugin...");
            }
            else records.doRecord("&7Unknown perm provider&7, using default system.");
        }
    }

    public void registerListeners() {
        records.doRecord("", "&bLoading all the listeners...");
        new PlayerListener(main);
        new PackListener(main);
        records.doRecord("&7Registered &e" + LISTENERS + "&7 plugin's listeners.");
    }

    public void reloadFiles() {
        filesList.forEach(SavedFile::reloadFile);
    }

    public void showPluginInfo(String name) {
        String pluginVersion;
        String isHooked;

        if (isPlugin(name)) {
            pluginVersion = main.getPlugin(name).getDescription().getVersion();
            isHooked = " &aenabled&7. Hooking...";
        } else {
            pluginVersion = "";
            isHooked = "&cnot found&7. Unhooking...";
        }

        records.doRecord("&7" + name + " " + pluginVersion + isHooked);
    }
}
