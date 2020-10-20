package me.msrules123.projectilemanipulation;

import me.msrules123.projectilemanipulation.listener.ProjectileListener;
import me.msrules123.projectilemanipulation.projectile.TrackingProjectileManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ProjectileManipulation extends JavaPlugin {

    @Override
    public void onEnable() {
        TrackingProjectileManager trackingProjectileManager = new TrackingProjectileManager();
        ProjectileListener projectileListener = new ProjectileListener(trackingProjectileManager);

        getServer().getPluginManager().registerEvents(projectileListener, this);
    }

}
