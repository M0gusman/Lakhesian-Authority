package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import java.awt.*;


    public class AnnihilaEveryFrameEffect implements EveryFrameWeaponEffectPlugin{
        float DAMAGE_MULTIPLIER = 1.2f;
        @Override
        public void advance (float amount, CombatEngineAPI engine, WeaponAPI weapon){
         if(engine.isPaused()) return;

            for(DamagingProjectileAPI p: engine.getProjectiles()){
                weapon.getSpec().setUnaffectedByProjectileSpeedBonuses(true);
                if(p.getWeapon() == weapon){
                    float damage = p.getProjectileSpec().getDamage().getBaseDamage();

                    if(p.getElapsed() < 0.4f)
                        p.setDamageAmount(damage+(damage*p.getElapsed()/0.8f));
                     else
                        p.setDamageAmount(damage*DAMAGE_MULTIPLIER);

                }
        }
    }
}
