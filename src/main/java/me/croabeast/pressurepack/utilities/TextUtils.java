package me.croabeast.pressurepack.utilities;

import me.clip.placeholderapi.*;
import me.croabeast.iridiumapi.*;
import me.croabeast.pressurepack.*;
import org.apache.commons.lang.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TextUtils {

    private final Application main;

    private final PAPI papi;

    // Initializer for PAPI
    public interface PAPI {
        String parsePAPI(Player player, String message);
    }

    public TextUtils(Application main) {
        this.main = main;
        papi =  (p, line) -> !main.getInitializer().HAS_PAPI ? line :
                (p != null ? PlaceholderAPI.setPlaceholders(p, line) : line);
    }

    @NotNull
    public String parsePAPI(Player player, String message) {
        if (message == null) message = "";
        return IridiumAPI.process(papi.parsePAPI(player, message));
    }

    public void sendCentered(Player player, String message) {
        message = parsePAPI(player, message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') previousCode = true;
            else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                FontInfo dFI = FontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ?
                        dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = FontInfo.SPACE.getLength() + 1;
        int compensated = 0;

        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        player.sendMessage(sb + message);
    }

    public void sendMixed(Player player, String message) {
        if (!message.startsWith("[C]")) player.sendMessage(parsePAPI(player, message));
        else sendCentered(player, message.replace("[C]", ""));
    }

    public List<String> fileList(FileConfiguration file, String path) {
        return  !file.isList(path) ?
                Collections.singletonList(file.getString(path)) :
                file.getStringList(path);
    }
    
    private List<String> toList(String path) {
        return fileList(main.getConfig(), path);
    }
    
    public void send(CommandSender sender, String path, String key, String value) {
        for (String line : fileList(main.getLang(), path)) {
            if (line == null || line.equals("")) continue;
            if (key != null && value != null)
                line = line.replace("{" + key + "}", value);
            
            if (sender instanceof ConsoleCommandSender) {
                line = line.replace("[C]", "");
                main.getRecords().doRecord(line);
            } 
            else {
                Player player = (Player) sender; 
                line = line.replace("{PLAYER}", player.getName());
                sendMixed(player, line);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void sendOrKick(Player player, String key, boolean kickPlayer) {
        key = "pack-messages." + key;
        List<String> list = fileList(main.getLang(), key);
        
        if (kickPlayer) {
            String joinList = StringUtils.join(list, "\n");
            if (joinList.contains("[C]")) joinList = joinList.replace("[C]", "");
            joinList = joinList.replace("{PLAYER}", player.getName());

            player.kickPlayer(
                    list == null || list.size() == 0 ?
                            "&cPlease, accept the ResourcePack" :
                            parsePAPI(player, joinList)
            );
        }
        
        else send(player, key, null, null);
    }

    public void runCommands(Player player, String key) {
        for (String line : toList("execute." + key)) {
            if (line == null || line.equals("")) continue;
            line = line.replace("{PLAYER}", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line);
        }
    }
}
