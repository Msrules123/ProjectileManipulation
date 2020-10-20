package me.msrules123.projectilemanipulation.projectile.impl;

import me.msrules123.projectilemanipulation.projectile.RangedTrackingProjectile;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

import java.util.Comparator;
import java.util.Optional;

/**
 * An abstract class designed to get the closest nearby entity within the tracking range provided
 */
public abstract class AbstractRangedTrackingProjectile extends AbstractTrackingProjectile implements RangedTrackingProjectile {

    private final int trackingRange;

    protected AbstractRangedTrackingProjectile(int trackingRange) {
        this.trackingRange = trackingRange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTrackingRange() {
        return trackingRange;
    }

    /**
     * {@inheritDoc}
     *
     * Gets the closest nearby entity that matches {@link this#doesLivingEntityMatch(LivingEntity)}
     * and is within the provided {@link RangedTrackingProjectile#getTrackingRange() tracking range}
     */
    @Override
    public Optional<LivingEntity> getProjectileTarget(Player shooter, Projectile projectile) {
        Vector projectileLocation = projectile.getLocation().toVector().clone();
        int trackingRange = getTrackingRange();

        return projectile.getNearbyEntities(trackingRange, trackingRange, trackingRange)
                .stream()
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .filter(this::doesLivingEntityMatch)
                .filter(entity -> !entity.getUniqueId().equals(shooter.getUniqueId()))
                .min(getClosestEntityComparator(projectileLocation));
    }

    private Comparator<LivingEntity> getClosestEntityComparator(Vector projectileLocation) {
        return Comparator.comparingDouble(e -> getDistanceSquared(projectileLocation, e));
    }

    private double getDistanceSquared(Vector projectileLocation, LivingEntity livingEntity) {
        return livingEntity.getLocation().toVector().clone().distanceSquared(projectileLocation);
    }

    /**
     * Used as a {@link java.util.function.Predicate} to check for
     * matching targets in {@link this#getProjectileTarget(Player, Projectile)}
     * @param livingEntity the entity to check
     * @return true if the entity matches the filter, else false
     */
    protected abstract boolean doesLivingEntityMatch(LivingEntity livingEntity);

}
