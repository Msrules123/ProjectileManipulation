# ProjectileManipulation

Overwrites default egg, arrow, and enderpearl projectiles when players launch them.

Custom projectiles are handled through the TrackingProjectile and RangedTrackingProjectile interfaces, and their respective abstract classes.

AbstractTrackingProjectile class holds protected methods for vector tracking from a source projectile to a target entity and a default runnable for basic velocity tracking.

 
