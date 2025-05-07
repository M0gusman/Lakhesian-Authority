package data.scripts.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import org.lazywizard.lazylib.CollisionUtils;
import org.lwjgl.util.vector.Vector2f;
import java.awt.*;
import java.util.List;

public class AnnihilaProjectilePierce extends BaseEveryFrameCombatPlugin {

    private final float DamageMult = 1f;
    private final float BaseSpeed = 6500f;
    private float counter = 0f;

    @Override
    public void advance(float amount, List events) {
        counter += amount;
        CombatEngineAPI engine = Global.getCombatEngine();
        if (engine.isPaused()) return;

        if (counter > 0.01f) {
            counter = 0f;
            for (DamagingProjectileAPI p : engine.getProjectiles()) {
                if (p != null) {
                    if (p.getCollisionClass() == CollisionClass.NONE && p.getWeapon() != null && p.getWeapon().getSpec() != null && !p.isFading()) {

                        Vector2f point = p.getLocation();
                        float damage = p.getDamageAmount();
                        float speed = p.getMoveSpeed();

                        if (p.getWeapon().getSpec().hasTag("lks_supercapspinal")) {
                            for (ShipAPI target : Global.getCombatEngine().getShips()) {
                                if (target.getCollisionClass()==CollisionClass.NONE) continue;
                                if (target==p.getSource()) continue;
                                //Collision detection, by KissaMies on Discord
                                if (CollisionUtils.isPointWithinBounds(p.getLocation(), target)){
                                    //we are hitting something, dmg code here
                                    DamagingExplosionSpec pierce1 = CalculateDamage(damage, speed, 50f);
                                    DamagingExplosionSpec pierce2 = Explosion(damage, speed, 500f);

                                    pierce1.setDamageType(DamageType.ENERGY);
                                    pierce2.setDamageType(DamageType.HIGH_EXPLOSIVE);

                                    engine.spawnDamagingExplosion(pierce1, p.getSource(), point, false);
                                    engine.spawnDamagingExplosion(pierce2,p.getSource(),point,false);

                                    target.getFluxTracker().increaseFlux(damage*0.5f, true);

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public DamagingExplosionSpec CalculateDamage(float Damage, float ProjSpeed, float AoE){
        float impact = Damage * DamageMult * (ProjSpeed/BaseSpeed);
        DamagingExplosionSpec damspec = new DamagingExplosionSpec(
                0.05f,
                AoE*1.5f,
                AoE*0.5f,
                impact,
                impact,
                CollisionClass.HITS_SHIPS_AND_ASTEROIDS,
                CollisionClass.PROJECTILE_FF,
                0f,
                0f,
                0f,
                0,
                new Color(255, 255, 255, 0),
                new Color(255, 255, 255, 0)
        );
        damspec.setShowGraphic(false);
        return damspec;
    }

    public DamagingExplosionSpec Explosion(float Damage, float ProjSpeed, float AoE){
        float impact = Damage * DamageMult * (ProjSpeed/BaseSpeed);
        DamagingExplosionSpec damspec = new DamagingExplosionSpec(
                0.1f,
                AoE*1f,
                AoE*0.6f,
                impact*0.6f,
                impact*0.6f,
                CollisionClass.HITS_SHIPS_AND_ASTEROIDS,
                CollisionClass.PROJECTILE_FF,
                10f,
                10f,
                0.4f,
                75,
                new Color(255, 255, 255, 255),
                new Color(100,80,6,255)
        );
        damspec.setUseDetailedExplosion(true);
        return damspec;
    }
}

