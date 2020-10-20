package me.msrules123.projectilemanipulation.projectile;

import me.msrules123.projectilemanipulation.projectile.impl.ArrowTrackingProjectile;
import me.msrules123.projectilemanipulation.projectile.impl.EggTrackingProjectile;
import me.msrules123.projectilemanipulation.projectile.impl.EnderpearlTrackingProjectile;
import org.bukkit.entity.EntityType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages the associations between {@link EntityType EntityTypes} and {@link TrackingProjectile TrackingProjectiles}
 */
public class TrackingProjectileManager {

    /**
     * Quick cache of entity type to tracking projectile, backed by {@link EnumMap} in the constructor
     */
    private final Map<EntityType, TrackingProjectile> entityTypeToProjectile;

    public TrackingProjectileManager() {
        this.entityTypeToProjectile = new EnumMap<>(EntityType.class);

        entityTypeToProjectile.put(EntityType.ARROW, new ArrowTrackingProjectile());
        entityTypeToProjectile.put(EntityType.EGG, new EggTrackingProjectile());
        entityTypeToProjectile.put(EntityType.ENDER_PEARL, new EnderpearlTrackingProjectile());
    }

    /**
     * Gets the {@link Optional} {@link TrackingProjectile} associated with the {@link EntityType}
     * @param entityType the entity type
     * @return an optional containing the tracking projectile associated with this entity type, if it exists
     */
    public Optional<TrackingProjectile> getTrackingProjectile(EntityType entityType) {
        return Optional.ofNullable(entityTypeToProjectile.get(entityType));
    }

}
