package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ArathanFluxMod extends BaseHullMod {
	public final float NONMISSILE_FLUX_COST_REDUCTION = 35f;
	public final float FLUX_DISSIPATION_INCREASE = 20f;
	public final float FLUX_CAPACITY_INCREASE = 30f;
	public final float VENT_SPEED_INCREASE = 33f;
	public final float BEAM_DAMAGE_TAKEN = 35f;
	public final float TIME_MULT = 1.25f;
	public final float OVERLOAD_TIME = 50f;

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getBallisticWeaponFluxCostMod().modifyMult(id, 0.65f);
		stats.getEnergyWeaponFluxCostMod().modifyMult(id, 0.65f);
		stats.getMissileWeaponFluxCostMod().modifyMult(id, 0f);
		stats.getFluxDissipation().modifyPercent(id, 20f);
		stats.getFluxCapacity().modifyPercent(id, 30f);
		stats.getVentRateMult().modifyMult(id, 0.67f);
		stats.getBeamDamageTakenMult().modifyMult(id,0.65f);
		stats.getTimeMult().modifyMult(id, 1.25f);
		stats.getOverloadTimeMod().modifyMult(id, 1.5f);

	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) NONMISSILE_FLUX_COST_REDUCTION + "%";
		if (index == 1) return "" + (int) FLUX_DISSIPATION_INCREASE + "%";
		if (index == 2) return "" + (int) FLUX_CAPACITY_INCREASE + "%";
		if (index == 3) return "" + (int) VENT_SPEED_INCREASE + "%";
		if (index == 4) return "" + (int) BEAM_DAMAGE_TAKEN + "%";
		if (index == 5) return "" + (int) TIME_MULT + "x";
		if (index == 6) return "" + (int) OVERLOAD_TIME + "%";
		return null;
	}


}
