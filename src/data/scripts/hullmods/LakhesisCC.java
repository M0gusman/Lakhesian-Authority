package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;


public class LakhesisCC extends BaseHullMod {

	public static final float RECOIL_BONUS = 20f;
	public static float AUTOFIRE_AIM = 0.2f;
	public static final float FIGHTER_MANEUVERABILITY = 25f;

	
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getAutofireAimAccuracy().modifyFlat(id, AUTOFIRE_AIM);
		stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getCombatWeaponRepairTimeMult().modifyPercent(id, -10f);
		stats.getFighterRefitTimeMult().modifyPercent(id, -10f);
		stats.getCombatEngineRepairTimeMult().modifyPercent(id, -10f);
		//stats.getProjectileSpeedMult().modifyPercent(id, PROJ_SPEED_BONUS);


	}

	public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
			MutableShipStatsAPI stats = fighter.getMutableStats();
			stats.getMaxTurnRate().modifyPercent(id, FIGHTER_MANEUVERABILITY);
			stats.getTurnAcceleration().modifyPercent(id, FIGHTER_MANEUVERABILITY);
			stats.getAutofireAimAccuracy().modifyFlat(id, AUTOFIRE_AIM);
			stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
			stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
			super.applyEffectsToFighterSpawnedByShip(fighter, ship, id);

	}
	public String getDescriptionParam (int index, HullSize hullSize){
		if (index == 0) return  "" + (int) RECOIL_BONUS + "%";
		if (index == 1) return  "" + (int) AUTOFIRE_AIM + "%";
		if (index == 2) return  "" + (int) FIGHTER_MANEUVERABILITY + "%";
		if (index == 3) return  "" + 10 + "%";
		if (index == 4) return  "" + 10 + "%";
		return null;
	}
}



