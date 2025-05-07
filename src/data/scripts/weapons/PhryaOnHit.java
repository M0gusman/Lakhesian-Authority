package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import java.awt.Color;

//taken from Juliet Delta Tango 1-5's script on Discord, also with Ruddygreat's help
public class PhryaOnHit implements OnHitEffectPlugin{

    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if(projectile.didDamage()){
            engine.spawnExplosion(point,point, new Color(100,80,6,255), 100f, 0.4f);
            if(!shieldHit && target instanceof  ShipAPI){
                engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, 50f, projectile.getDamageAmount(), 50f, null, 10f,new Color(100,80,6,255), new Color(255,255,255,255) );
            }
            if (target instanceof ShipAPI){
                if (((ShipAPI) target).hasListenerOfClass(DamageTakenMultiplier.class)){
                    DamageTakenMultiplier listener = ((ShipAPI) target).getListeners(DamageTakenMultiplier.class).get(0);
                      listener.Stacks++;
                }else{
                    DamageTakenMultiplier newlistener = new DamageTakenMultiplier((ShipAPI) target);
                    newlistener.Stacks++;
                    ((ShipAPI) target).addListener(newlistener);
                }
            }
        }
    }


    private class DamageTakenMultiplier implements AdvanceableListener{
        public int Stacks = 0;
        public float stackremovetime = 10f;
        public float currTime = 0f;
        String id = "lks_phrya_debuff";

        private ShipAPI target;

        public float DamageDebuff = 1f;
        public float FluxDebuff = 0.5f;

        public DamageTakenMultiplier(ShipAPI target){
            this.target = target;
        }

        @Override
        public void advance(float amount) {
            currTime += amount;

            if(currTime >= stackremovetime){
                Stacks--;
                currTime=0;
            }

            if(Stacks == 0) {
                target.getMutableStats().getArmorDamageTakenMult().unmodifyPercent("lks_phrya_debuff");
                target.getMutableStats().getShieldDamageTakenMult().unmodifyPercent("lks_phrya_debuff");
                target.getMutableStats().getHullDamageTakenMult().unmodifyPercent("lks_phrya_debuff");

                target.getMutableStats().getEnergyWeaponFluxCostMod().unmodifyPercent("lks_phrya_debuff");
                target.getMutableStats().getBallisticWeaponFluxCostMod().unmodifyPercent("lks_phrya_debuff");
                target.getMutableStats().getMissileWeaponFluxCostMod().unmodifyPercent("lks_phrya_debuff");

                target.removeListener(this);
            }else{
                target.getMutableStats().getArmorDamageTakenMult().modifyPercent("lks_phrya_debuff", DamageDebuff*Stacks);
                target.getMutableStats().getShieldDamageTakenMult().modifyPercent("lks_phrya_debuff", DamageDebuff*Stacks);
                target.getMutableStats().getHullDamageTakenMult().modifyPercent("lks_phrya_debuff", DamageDebuff*Stacks);

                target.getMutableStats().getEnergyWeaponFluxCostMod().modifyPercent("lks_phrya_debuff", FluxDebuff*Stacks);
                target.getMutableStats().getBallisticWeaponFluxCostMod().modifyPercent("lks_phrya_debuff", FluxDebuff*Stacks);
                target.getMutableStats().getMissileWeaponFluxCostMod().modifyPercent("lks_phrya_debuff", FluxDebuff*Stacks);
            }
            Stacks = MathUtils.clamp(Stacks, 0, 100);
        }

    }
}
