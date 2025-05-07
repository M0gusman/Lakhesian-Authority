package data.scripts.weapons;

import java.util.ArrayList;
import java.util.List;


import com.fs.starfarer.api.combat.*;
import org.lazywizard.lazylib.combat.CombatUtils;



public class ValarysWeaponScript implements EveryFrameWeaponEffectPlugin  {

    private List<DamagingProjectileAPI> alreadyRegisteredProjectiles = new ArrayList<>();



    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        ShipAPI source = weapon.getShip();
        ShipAPI target = null;
        if (source.getWeaponGroupFor(weapon) != null) {
            if (source.getWeaponGroupFor(weapon).isAutofiring() && source.getSelectedGroupAPI() != source.getWeaponGroupFor(weapon)) {
                target = source.getWeaponGroupFor(weapon).getAutofirePlugin(weapon).getTargetShip();
            } else {
                target = source.getShipTarget();
            }
        }
        for (DamagingProjectileAPI proj : CombatUtils.getProjectilesWithinRange(weapon.getLocation(), 200f)) {
            if (proj.getWeapon() == weapon && !alreadyRegisteredProjectiles.contains(proj) && engine.isEntityInPlay(proj) && !proj.didDamage()) {
                engine.addPlugin(new ValarysTracking(proj, target));
                alreadyRegisteredProjectiles.add(proj);
            }
        }
            List<DamagingProjectileAPI> cloneList = new ArrayList<>(alreadyRegisteredProjectiles);
            for(DamagingProjectileAPI proj : cloneList){
                if (engine.isEntityInPlay(proj) || proj.didDamage()) {
                    alreadyRegisteredProjectiles.remove(proj);
                }
            }
        }


    }




