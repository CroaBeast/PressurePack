package me.croabeast.pressurepack.utilities;

import me.croabeast.pressurepack.*;
import me.croabeast.pressurepack.services.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class PermUtils {

    private final Application main;

    public PermUtils(Application main) {
        this.main = main;
    }

    protected Map<UUID, ResourcePack> waitList = new HashMap<>();
    public Map<UUID, ResourcePack> getWaitList() { return waitList; }

    public boolean hasPerm(CommandSender sender, String perm) {
        if (sender instanceof ConsoleCommandSender) return true;
        Player player = (Player) sender;
        return  main.getInitializer().HAS_VAULT ?
                Initializer.Perms.playerHas(null, player, perm) :
                player.hasPermission(perm);
    }
}
