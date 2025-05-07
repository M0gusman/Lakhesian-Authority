package data.scripts.weapons;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lwjgl.util.vector.Vector2f;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import com.fs.starfarer.api.combat.ShipAPI;


public class HylaOnHit implements OnHitEffectPlugin {

private static final float BONUS_DAMAGE = 50f;
private static final float FLUXRAISE_MULT = 1.2f;

	@Override
	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

		if (!(target instanceof ShipAPI))
			return;

		if (projectile.didDamage()) {
			Global.getCombatEngine().applyDamage(target, point, BONUS_DAMAGE, DamageType.ENERGY, 0, false, false, null, false);
			ShipAPI targetship = (ShipAPI) target;

			if (!shieldHit && target instanceof ShipAPI) {
				// calculate a number to raise target flux by
				float fluxmult = projectile.getDamageAmount() * FLUXRAISE_MULT;
				// get target max flux level
				float maxflux = targetship.getMaxFlux();
				//Check that the target can handle the flux; if so, raise target ship flux on hull hit
				if (maxflux > (fluxmult * 1.5)) {
					targetship.getFluxTracker().increaseFlux(fluxmult, true);
				}

			}
		}



		float damage = 50f;
		DamagingExplosionSpec spec = new DamagingExplosionSpec(
				0.3f, // duration
				35f, // radius
				20f, // coreRadius
				damage, // maxDamage
				damage, // minDamage
				CollisionClass.PROJECTILE_FF, // collisionClass
				CollisionClass.PROJECTILE_FIGHTER, // collisionClassByFighter
				3f, // particleSizeMin
				3f, // particleSizeRange
				0.5f, // particleDuration
				150, // particleCount
				new Color(255, 255, 255, 255), // particleColor
				new Color(210, 24, 24, 175)  // explosionColor
		);
        engine.spawnDamagingExplosion(spec, projectile.getSource(), point, false);
		spec.setDamageType(DamageType.ENERGY);
		spec.setUseDetailedExplosion(false);
		spec.setSoundSetId("explosion_guardian");



	}
}





