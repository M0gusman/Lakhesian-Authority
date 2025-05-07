package data.scripts.weapons;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lwjgl.util.vector.Vector2f;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;


public class JuniperLargeOnHit implements OnHitEffectPlugin {
private static final float BONUS_DAMAGE = 100f;
	@Override
	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

		if(!(target instanceof ShipAPI))
			return;

		if (projectile.didDamage()) {
			Global.getCombatEngine().applyDamage(target, point, BONUS_DAMAGE, DamageType.HIGH_EXPLOSIVE, 0, false, false, null, false);


		}



		float damage = 100f;
		DamagingExplosionSpec spec = new DamagingExplosionSpec(
				0.3f, // duration
				50f, // radius
				35f, // coreRadius
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
		spec.setDamageType(DamageType.HIGH_EXPLOSIVE);
		spec.setUseDetailedExplosion(false);
		spec.setSoundSetId("explosion_flak");



	}
}





