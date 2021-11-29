package me.croabeast.pressurepack.listeners;

import io.papermc.lib.*;
import me.croabeast.pressurepack.*;
import me.croabeast.pressurepack.services.*;
import me.croabeast.pressurepack.utilities.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final Application main;
    private final PermUtils perms;

    public PlayerListener(Application main) {
        this.main = main;
        this.perms = main.getPermUtils();
        main.registerListener(this);
        main.getInitializer().LISTENERS++;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (perms.hasPerm(player, "pressure-pack.bypass")) return;

        UUID id = player.getUniqueId();
        String url = main.getURL();
        String hash = main.getHash();

        final ResourcePack pack;
        pack = PaperLib.isPaper() ? new Paper(main, url, hash) : new Spigot(url, hash);
        perms.getWaitList().put(id, pack);

        Scheduler run = new Scheduler();
        run.setTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(
                main, () -> {
                    if (perms.getWaitList().containsKey(id)) pack.setResourcePack(player);
                    else run.cancel();
                    },
                0L, main.getConfig().getInt("execute.gui-speed", 20))
        );
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (perms.hasPerm(player, "pressure-pack.bypass")) return;
        perms.getWaitList().remove(player.getUniqueId());
    }

    public static class Scheduler {

        private int task;

        public void setTask(int task) { this.task = task; }

        public void cancel() { Bukkit.getScheduler().cancelTask(task); }
    }
}
