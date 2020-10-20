package me.msrules123.projectilemanipulation.projectile.impl;

import me.msrules123.projectilemanipulation.ProjectileManipulation;
import me.msrules123.projectilemanipulation.projectile.TrackingProjectile;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * An abstract class designed to provide calculations for tracking vectors and
 * a default implementation for changing the velocity of the desired projectile
 */
public abstract class AbstractTrackingProjectile implements TrackingProjectile {

    /**
     * {@inheritDoc}
     */
    @Override
    public void startProjectileTracking(Projectile projectile, LivingEntity target) {
        new DefaultTrackingRunnable(projectile, target).runTaskTimer(getPlugin(), 0L, 1L);
    }

    /**
     * Gets the vector to send the projectile on to reach the target using {@link TrackingProjectile#getProjectileSpeed(Projectile)}
     * @param projectile the projectile
     * @param target the target
     * @return the vector that should be applied to this projectile to send to the target location
     */
    protected Vector getTrackingVector(Projectile projectile, LivingEntity target) {
        Vector targetLocationToVector = target.getEyeLocation().toVector().clone();
        Vector projectileLocationToVector = projectile.getLocation().toVector().clone();

        // Subtracting the vectors of each location will give a resultant vector where the projectile needs to fly
        Vector projectileToTargetVector = targetLocationToVector.subtract(projectileLocationToVector);

        double projectileSpeed = getProjectileSpeed(projectile);

        // Normalize the vector to a length of 1, then multiply by the projectile speed per tick
        return projectileToTargetVector.normalize().multiply(projectileSpeed);
    }

    /**
     * Checks if both the {@link Projectile} and {@link LivingEntity} are valid
     * @param projectile the projectile
     * @param livingEntity the living entity
     * @return true if both the projectile and living entity are valid, else false
     */
    protected boolean shouldDoTrackingTick(Projectile projectile, LivingEntity livingEntity) {
        return projectile.isValid() && livingEntity.isValid();
    }

    /**
     * Tracks the given {@link Projectile} towards the target {@link LivingEntity} in the {@link DefaultTrackingRunnable}
     * @param projectile the launched projectile
     * @param target the target entity
     * @return true if the tracking was successful, false if the tracking was aborted
     */
    protected abstract boolean trackTick(Projectile projectile, LivingEntity target);

    /**
     * Removes the projectile, if valid, and spawns a small particle effect
     * @param projectile the projectile to destroy
     */
    protected void destroyProjectile(Projectile projectile) {
        if (!projectile.isValid()) {
            return;
        }

        Location projectileLocation = projectile.getLocation();
        projectileLocation.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, projectileLocation,1);

        projectile.remove();
    }

    /**
     * Gets a reference to the plugin class, used for initializing each {@link BukkitRunnable} task
     * @return the reference to the main plugin class
     */
    protected JavaPlugin getPlugin() {
        return ProjectileManipulation.getPlugin(ProjectileManipulation.class);
    }

    /**
     * Contains an implementation for default velocity updates using the methods
     * {@link this#trackTick(Projectile, LivingEntity)}
     */
    protected class DefaultTrackingRunnable extends BukkitRunnable {

        private final Projectile projectile;
        private final LivingEntity target;

        protected DefaultTrackingRunnable(Projectile projectile, LivingEntity target) {
            this.projectile = projectile;
            this.target = target;
        }

        @Override
        public void run() {
            if (!shouldDoTrackingTick(projectile, target)) {
                destroyProjectile(projectile);
                cancel();

                return;
            }

            if (!trackTick(projectile, target)) {
                destroyProjectile(projectile);
                cancel();
            }
        }

    }

}
