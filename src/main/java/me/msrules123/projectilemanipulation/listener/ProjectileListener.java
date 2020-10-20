package me.msrules123.projectilemanipulation.listener;

import me.msrules123.projectilemanipulation.projectile.TrackingProjectile;
import me.msrules123.projectilemanipulation.projectile.TrackingProjectileManager;
import me.msrules123.projectilemanipulation.projectile.impl.ArrowTrackingProjectile;
import me.msrules123.projectilemanipulation.projectile.impl.EggTrackingProjectile;
import me.msrules123.projectilemanipulation.projectile.impl.EnderpearlTrackingProjectile;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Optional;

public class ProjectileListener implements Listener {

    private final TrackingProjectileManager trackingProjectileManager;

    public ProjectileListener(TrackingProjectileManager trackingProjectileManager) {
        this.trackingProjectileManager = trackingProjectileManager;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        ProjectileSource projectileSource = projectile.getShooter();

        if (!(projectileSource instanceof Player)) {
            return;
        }

        Player shooter = (Player) projectileSource;

        EntityType entityType = projectile.getType();
        Optional<TrackingProjectile> optionalTrackingProjectile = trackingProjectileManager.getTrackingProjectile(entityType);

        if (!optionalTrackingProjectile.isPresent()) {
            return;
        }

        TrackingProjectile trackingProjectile = optionalTrackingProjectile.get();
        Optional<LivingEntity> optionalTargetEntity = trackingProjectile.getProjectileTarget(shooter, projectile);

        if (!optionalTargetEntity.isPresent()) {
            return;
        }

        LivingEntity targetEntity = optionalTargetEntity.get();
        trackingProjectile.startProjectileTracking(projectile, targetEntity);
    }

}
