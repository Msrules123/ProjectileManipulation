package me.msrules123.projectilemanipulation.projectile;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface TrackingProjectile {

    /**
     * Gets the optional {@link LivingEntity} target that the projectile should follow
     * @param shooter the {@link Player} that shot the {@link Projectile}
     * @param projectile the projectile that was fired
     * @return the optional target entity that the projectile should follow
     */
    Optional<LivingEntity> getProjectileTarget(Player shooter, Projectile projectile);

    /**
     * Gets the projectile speed for the given {@link Projectile}
     * @param projectile the projectile to get the speed for
     * @return the speed for the given projectile
     */
    default double getProjectileSpeed(Projectile projectile) {
        return projectile.getVelocity().length();
    }

    /**
     * Starts the tracking of the given {@link Projectile} towards the target {@link LivingEntity}
     * @param projectile the launched projectile
     * @param target the target to hit with the projectile
     */
    void startProjectileTracking(Projectile projectile, LivingEntity target);

}
