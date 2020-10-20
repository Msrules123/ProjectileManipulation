package me.msrules123.projectilemanipulation.projectile.impl;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a custom arrow projectile that tracks nearby entities, but will timeout if the arrow moves at too much of an angle
 */
public class ArrowTrackingProjectile extends AbstractRangedTrackingProjectile {

    private static final int TRACKING_RANGE = 5;

    private static final double TIMEOUT_ANGLE_IN_RADIANS = Math.toRadians(90);
    private static final int NUMBER_OF_ANGLES = 20;

    public ArrowTrackingProjectile() {
        super(TRACKING_RANGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doesLivingEntityMatch(LivingEntity livingEntity) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startProjectileTracking(Projectile projectile, LivingEntity target) {
        new ArrowTrackingRunnable(projectile, target).runTaskTimer(getPlugin(), 0L, 1L);
    }

    /**
     * {@inheritDoc}
     *
     * Throws {@link UnsupportedOperationException} as this class uses a non-default implementation of the tracking runnable
     */
    @Override
    protected boolean trackTick(Projectile projectile, LivingEntity target) {
        throw new UnsupportedOperationException("ArrowTrackingProjectile uses the non-default runnable and does not support this method");
    }

    /**
     * Handles the tracking and timeout of the given projectile every tick
     */
    private class ArrowTrackingRunnable extends BukkitRunnable {

        private final Projectile projectile;
        private final LivingEntity livingEntity;

        private final Queue<Double> angleQueue;
        private double totalAngles;

        public ArrowTrackingRunnable(Projectile projectile, LivingEntity livingEntity) {
            this.projectile = projectile;
            this.livingEntity = livingEntity;

            this.angleQueue = new LinkedList<>();
            this.totalAngles = 0;
        }

        @Override
        public void run() {
            if (!shouldDoTrackingTick(projectile, livingEntity)) {
                destroyProjectile(projectile);
                cancel();

                return;
            }

            // If the number of angles in our queue is full, remove it from the queue and subtract it from our total
            if (angleQueue.size() == NUMBER_OF_ANGLES) {
                double angle = angleQueue.poll();
                totalAngles -= angle;
            }

            Vector trackingVector = getTrackingVector(projectile, livingEntity);

            Vector normalizedTrackingVector = trackingVector.clone().normalize();
            Vector normalizedProjectileVelocity = projectile.getVelocity().clone().normalize();

            // Gets angle between the new tracking vector and the old tracking vector
            double angle = normalizedProjectileVelocity.angle(normalizedTrackingVector);
            totalAngles += angle;

            // If the total angles is greater than or equal to the timeout angle, destroy the projectile
            if (Double.compare(totalAngles, TIMEOUT_ANGLE_IN_RADIANS) >= 0) {
                destroyProjectile(projectile);
                cancel();

                return;
            }

            // Add the angle to our queue and continue tracking the target
            angleQueue.add(angle);
            projectile.setVelocity(trackingVector);
        }
    }

}
