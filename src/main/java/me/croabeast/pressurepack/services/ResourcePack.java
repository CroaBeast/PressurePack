package me.croabeast.pressurepack.services;

import me.croabeast.pressurepack.utilities.*;
import org.bukkit.entity.*;

public abstract class ResourcePack {

    protected final String url;
    protected final String hash;

    public ResourcePack(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }

    public byte[] getHashHex() {
        return PackUtils.parseHex(hash);
    }

    public abstract void setResourcePack(Player player);
}
