package me.msrules123.projectilemanipulation.projectile;

/**
 * Represents a {@link TrackingProjectile} that has a range to detect entities within
 */
public interface RangedTrackingProjectile extends TrackingProjectile {

    /**
     * Gets the range to detect entities within
     * @return the range to detect entities within
     */
    int getTrackingRange();

}
