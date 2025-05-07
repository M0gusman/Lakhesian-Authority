package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

import java.util.HashSet;
import java.util.Set;

public class ArathanRangeMod extends BaseHullMod {
	private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(4);


	static {
		//id of all hmods you want to block
		BLOCKED_HULLMODS.add("targetingunit");
		BLOCKED_HULLMODS.add("dedicated_targeting_core");
	}

	public static final float RECOIL_BONUS = 75f;
	public static final float PROJ_SPEED_BONUS = 50f;
	
	public static float RANGE_BONUS = 175f;
	public static float PD_MINUS = 125f;
	public static float VISION_BONUS = 1650f;
	public static float AUTOFIRE_AIM = 0.75f;
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int)Math.round(RANGE_BONUS) + "%";
		if (index == 1) return "" + (int)Math.round(RANGE_BONUS - PD_MINUS) + "%";
		//if (index == 0) return "" + (int)RANGE_THRESHOLD;
		//if (index == 1) return "" + (int)((RANGE_MULT - 1f) * 100f);
		//if (index == 1) return "" + new Float(VISION_BONUS).intValue();
		return null;
	}
	
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getBallisticWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
		stats.getEnergyWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
		//stats.getBeamWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);

		stats.getNonBeamPDWeaponRangeBonus().modifyPercent(id, -PD_MINUS);
		stats.getBeamPDWeaponRangeBonus().modifyPercent(id, -PD_MINUS);


		stats.getSightRadiusMod().modifyFlat(id, VISION_BONUS);
		stats.getAutofireAimAccuracy().modifyFlat(id, AUTOFIRE_AIM);


		stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		//stats.getProjectileSpeedMult().modifyPercent(id, PROJ_SPEED_BONUS);


	}
	@Override
       public void  applyEffectsAfterShipCreation(ShipAPI ship, String id){
		   for (String hullmod : BLOCKED_HULLMODS) {
			   if (ship.getVariant().hasHullMod(hullmod)) {
				   ship.getVariant().removeMod(hullmod);
			   }
		   }
	   }
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return !ship.getVariant().getHullMods().contains("dedicated_targeting_core") && !ship.getVariant().getHullMods().contains("advancedcore") && !ship.getVariant().getHullMods().contains("targetingunit");
	}


	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains("dedicated_targeting_core")) {
			return "Incompatible with Dedicated Targeting Core";
		}
		if (ship.getVariant().getHullMods().contains("advancedcore")) {
			return "Incompatible with Advanced Targeting Core";
		}
		if (ship.getVariant().getHullMods().contains("targetingunit")) {
			return "Incompatible with Integrated Targeting Unit";
		}
		return null;
	}


}
