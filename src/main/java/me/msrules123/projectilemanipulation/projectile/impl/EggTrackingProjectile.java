package me.msrules123.projectilemanipulation.projectile.impl;

import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Represents an egg that tracks {@link Monster Monsters} within the range
 */
public class EggTrackingProjectile extends AbstractRangedTrackingProjectile {

    private static final int EGG_TRACKING_RANGE = 20;

    private static final double FAR_AWAY_SPEED = 1.0;

    private static final double TRACKING_CLOSE_RANGE_SQUARED = 25;
    private static final double TRACKING_CLOSE_SPEED_MULTIPLIER = 2.0;

    public EggTrackingProjectile() {
        super(EGG_TRACKING_RANGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doesLivingEntityMatch(LivingEntity livingEntity) {
        return livingEntity instanceof Monster;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getProjectileSpeed(Projectile projectile) {
        return FAR_AWAY_SPEED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean trackTick(Projectile projectile, LivingEntity target) {
        Vector trackingVector = getTrackingVector(projectile, target);

        Vector projectileLocation = projectile.getLocation().toVector().clone();
        Vector targetLocation = target.getLocation().toVector().clone();

        double distanceBetweenProjectileAndTarget = projectileLocation.distanceSquared(targetLocation);

        // If within the tracking range, give speed boost to the provided arrow
        if (Double.compare(distanceBetweenProjectileAndTarget, TRACKING_CLOSE_RANGE_SQUARED) >= 0) {
            trackingVector.multiply(TRACKING_CLOSE_SPEED_MULTIPLIER);
        }

        projectile.setVelocity(trackingVector);

        return true;
    }

}
