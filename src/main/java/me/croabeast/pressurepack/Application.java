package me.croabeast.pressurepack;

import me.croabeast.pressurepack.command.*;
import me.croabeast.pressurepack.objects.*;
import me.croabeast.pressurepack.utilities.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;

import java.util.*;

public final class Application extends JavaPlugin {

    private Application main;
    private Records records;
    private Initializer init;
    private TextUtils text;
    private PermUtils perms;

    public String PLUGIN_VERSION;
    public String MC_FORK;
    public int MC_VERSION;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        main = this;

        String version = Bukkit.getBukkitVersion().split("-")[0];
        MC_VERSION = Integer.parseInt(version.split("\\.")[1]);
        MC_FORK = Bukkit.getVersion().split("-")[1] + " " + version;

        PLUGIN_VERSION = getDescription().getVersion();

        records = new Records(main);
        init = new Initializer(main);
        text = new TextUtils(main);
        perms = new PermUtils(main);

        records.rawRecord(
                "&0* *&e___&0 * * * * &e___",
                "&0* &e|___)&0 * * * &e|___)",
                "&0* &e|&0 * &eRESSURE | &0* &eACK &fv" + PLUGIN_VERSION,
                "&0* &7Developer: " + getDescription().getAuthors().get(0),
                "&0* &7Software: " + MC_FORK,
                "&0* &7Java Version: " + System.getProperty("java.version"),
                ""
        );

        if (MC_VERSION < 9) {
            records.doRecord(
                    "&cThis plugin doesn't work in 1.8 or older.",
                    "&cUpdate your server version, then install the plugin."
            );
            Bukkit.getPluginManager().disablePlugin(main);
            return;
        }

        init.registerConfigFile();
        init.registerHooks();
        init.registerListeners();

        new Executor(main);

        try {
            PackUtils.perform(getURL(), getHash(), (url, config, match) -> {
                if (match) return;
                records.doRecord("",
                        "&cThe hash doesn't match the URL provider.",
                        "&7Original URL bytes: &b" + Arrays.toString(url),
                        "&7Your config.yml bytes: &b" + Arrays.toString(config),
                        "&eCorrect that error as soon as possible."
                );
            });
        } catch (Exception e) {
            records.doRecord("",
                    "&cYour SHA-1 hash/url is invalid.",
                    "&cPlease change it to a valid one.",
                    "&7Localized error: &e" + e.getLocalizedMessage()
            );
        }

        records.doRecord("",
                "&7PressurePack " + PLUGIN_VERSION + " was&a loaded&7 in " +
                        (System.currentTimeMillis() - start) + " ms."
        );
        records.rawRecord("");
    }

    @Override
    public void onDisable() {
        records.rawRecord(
                "&0* *&e___&0 * * * * &e___",
                "&0* &e|___)&0 * * * &e|___)",
                "&0* &e|&0 * &eRESSURE | &0* &eACK &fv" + PLUGIN_VERSION,
                "&0* &7Developer: " + getDescription().getAuthors().get(0), ""
        );
        records.doRecord(
                "&7PressurePack &c" + PLUGIN_VERSION + "&7 was totally disabled.",
                "&7Hope we can see you again&c nwn"
        );
        main = null;
    }

    public Initializer getInitializer() { return init; }
    public Records getRecords() { return records; }
    public TextUtils getTextUtils() { return text; }
    public PermUtils getPermUtils() { return perms; }

    public FileConfiguration getLang() { return init.lang.getFile(); }

    public Plugin getPlugin(String name) {
        return Bukkit.getPluginManager().getPlugin(name);
    }

    public void registerListener(Listener listener) {
        main.getServer().getPluginManager().registerEvents(listener, main);
    }

    public String getURL() {
        return main.getConfig().getString("resource-pack.url", "");
    }

    public String getHash() {
        return main.getConfig().getString("resource-pack.hash", "");
    }
}
