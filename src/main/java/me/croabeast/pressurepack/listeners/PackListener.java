package me.croabeast.pressurepack.listeners;

import me.croabeast.pressurepack.*;
import me.croabeast.pressurepack.objects.*;
import me.croabeast.pressurepack.utilities.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.util.*;

public class PackListener implements Listener {

    private final Application main;
    private final Records records;
    private final TextUtils text;
    private final PermUtils perms;

    public PackListener(Application main) {
        this.main = main;
        this.records = main.getRecords();
        this.text = main.getTextUtils();
        this.perms = main.getPermUtils();
        main.registerListener(this);
        main.getInitializer().LISTENERS++;
    }

    @EventHandler
    private void packStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        if (perms.hasPerm(player, "pressure-pack.bypass")) return;

        UUID id = player.getUniqueId();

        switch (event.getStatus()) {
            case ACCEPTED:
                perms.getWaitList().remove(id);
                break;

            case DECLINED:
                perms.getWaitList().remove(id);
                records.doRecord("&7" + player.getName() + " declined the ResourcePack.");
                text.runCommands(player, "decline");
                text.sendOrKick(player, "declined", main.getConfig().getBoolean("execute.kick"));
                break;

            case FAILED_DOWNLOAD:
                perms.getWaitList().remove(id);
                records.doRecord("&7" + player.getName() + " couldn't download the ResourcePack.");
                text.sendOrKick(player, "failed", false);
                text.runCommands(player, "fail");
                break;

            case SUCCESSFULLY_LOADED:
                perms.getWaitList().remove(id);
                records.doRecord("&7" + player.getName() + " accepted download the ResourcePack.");
                text.sendOrKick(player, "accepted", false);
                text.runCommands(player, "accept");
                break;

            default: break;
        }
    }
}
