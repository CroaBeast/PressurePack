package me.croabeast.pressurepack.services;

import me.croabeast.pressurepack.*;
import me.croabeast.pressurepack.utilities.*;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.serializer.legacy.*;
import org.bukkit.entity.*;

public class PaperPack extends ResourcePack {

    private final Application main;

    public PaperPack(Application main, String url, String hash) {
        super(url, hash);
        this.main = main;
    }

    @Override
    public void setResourcePack(Player player) {
        if (main.MC_VERSION > 16) {
            String rawText = main.getLang().getString("pack-messages.prompt-text");
            rawText = main.getTextUtils().parsePAPI(player, rawText);
            Component text = LegacyComponentSerializer.legacyAmpersand().deserialize(rawText);
            player.setResourcePack(url, hash, true, text);
        }
        else player.setResourcePack(url, PackUtils.parseHex(hash));
    }
}
