package me.msrules123.projectilemanipulation.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Objects;

public class HashableWorldlessLocation {

    private final int x;
    private final int y;
    private final int z;

    public HashableWorldlessLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public static HashableWorldlessLocation fromBukkitLocation(Location location) {
        Block block = location.getBlock();
        return new HashableWorldlessLocation(block.getX(), block.getY(), block.getZ());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof HashableWorldlessLocation)) {
            return false;
        }

        HashableWorldlessLocation otherLocation = (HashableWorldlessLocation) other;
        return this.x == otherLocation.x && this.y == otherLocation.y && this.z == otherLocation.z;
    }

}
