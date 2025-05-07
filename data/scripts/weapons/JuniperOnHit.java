package data.scripts.weapons;

import java.awt.Color;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lwjgl.util.vector.Vector2f;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;


public class JuniperOnHit implements OnHitEffectPlugin {
	@Override
	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {


			if (projectile.didDamage()) {
				DamagingProjectileAPI e = engine.spawnDamagingExplosion(createExplosionSpec(), projectile.getSource(), point);
				e.addDamagedAlready(target);

			}
		}
	}


	
	public DamagingExplosionSpec createExplosionSpec() {
		float damage = 50f;
		DamagingExplosionSpec spec = new DamagingExplosionSpec(
				0.1f, // duration
				35f, // radius
				20f, // coreRadius
				damage, // maxDamage
				damage / 2f, // minDamage
				CollisionClass.PROJECTILE_FF, // collisionClass
				CollisionClass.PROJECTILE_FIGHTER, // collisionClassByFighter
				3f, // particleSizeMin
				3f, // particleSizeRange
				0.5f, // particleDuration
				150, // particleCount
				new Color(255,255,255,255), // particleColor
				new Color(210,24,24,175)  // explosionColor
		);

		spec.setDamageType(DamageType.HIGH_EXPLOSIVE);
		spec.setUseDetailedExplosion(false);
		spec.setSoundSetId("explosion_flak");
		return spec;
	}
}




