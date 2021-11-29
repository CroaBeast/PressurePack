package me.croabeast.pressurepack.services;

import org.bukkit.entity.Player;

public class Spigot extends ResourcePack {

    public Spigot(String url, String hash) {
        super(url, hash);
    }

    @Override
    public void setResourcePack(Player player) {
        player.setResourcePack(url, getHashHex());
    }
}
