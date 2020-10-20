package me.msrules123.projectilemanipulation.projectile.impl;

import me.msrules123.projectilemanipulation.util.HashableWorldlessLocation;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents the slow-moving, custom enderpearl tracking projectile that tracks the entity on a player's crosshair
 */
public class EnderpearlTrackingProjectile extends AbstractTrackingProjectile {

    private static final double PROJECTILE_SPEED = 0.5;
    private static final int CURSOR_MAX_DISTANCE = 25;

    /**
     * {@inheritDoc}
     *
     * Gets the closest LivingEntity in the player's crosshair, if one exists
     */
    @Override
    public Optional<LivingEntity> getProjectileTarget(Player shooter, Projectile projectile) {
        int distance = CURSOR_MAX_DISTANCE;

        // Collect the living entities within the max distance, excluding the shooter, and map them to their location
        Map<HashableWorldlessLocation, LivingEntity> livingEntities = projectile
                .getNearbyEntities(distance, distance, distance)
                .stream()
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .filter(entity -> !entity.getUniqueId().equals(shooter.getUniqueId()))
                .collect(
                        Collectors.toMap(
                                entity -> HashableWorldlessLocation.fromBukkitLocation(entity.getEyeLocation()),
                                Function.identity()
                        )
                );

        BlockIterator blockIterator = new BlockIterator(shooter, CURSOR_MAX_DISTANCE);
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            HashableWorldlessLocation hashableLocation = HashableWorldlessLocation.fromBukkitLocation(block.getLocation());

            LivingEntity livingEntity = livingEntities.get(hashableLocation);

            if (livingEntity != null) {
                return Optional.of(livingEntity);
            }
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getProjectileSpeed(Projectile projectile) {
        return PROJECTILE_SPEED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean trackTick(Projectile projectile, LivingEntity target) {
        Vector trackingVector = getTrackingVector(projectile, target);
        projectile.setVelocity(trackingVector);

        return true;
    }

}
