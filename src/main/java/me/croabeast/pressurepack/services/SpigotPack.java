package me.croabeast.pressurepack.services;

import org.bukkit.entity.Player;

public class SpigotPack extends ResourcePack {

    public SpigotPack(String url, String hash) {
        super(url, hash);
    }

    @Override
    public void setResourcePack(Player player) {
        player.setResourcePack(url, getHashHex());
    }
}
