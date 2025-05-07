package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class LakhesisGeneralMod extends BaseHullMod {
	public final float NONMISSILE_FLUX_COST_REDUCTION = 12.5f;
	public final float FLUX_DISSIPATION_INCREASE = 15f;
	public final float FLUX_CAPACITY_INCREASE = 12.5f;
	public final float VENT_SPEED_INCREASE = 15f;
	public final float PEAK_CR_DURATION = 60f;
	public final float CR_LOSS_RATE = 35f;

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getBallisticWeaponFluxCostMod().modifyMult(id, 0.875f);
		stats.getEnergyWeaponFluxCostMod().modifyMult(id, 0.875f);
		stats.getMissileWeaponFluxCostMod().modifyMult(id, 0f);
		stats.getFluxDissipation().modifyPercent(id, 15f);
		stats.getFluxCapacity().modifyPercent(id, 12.5f);
		stats.getVentRateMult().modifyMult(id, 0.85f);
		stats.getPeakCRDuration().modifyFlat(id, 60f);
		stats.getCRLossPerSecondPercent().modifyMult(id, 0.65f);

	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) NONMISSILE_FLUX_COST_REDUCTION + "%";
		if (index == 1) return "" + (int) FLUX_DISSIPATION_INCREASE + "%";
		if (index == 2) return "" + (int) FLUX_CAPACITY_INCREASE + "%";
		if (index == 3) return "" + (int) VENT_SPEED_INCREASE + "%";
		if (index == 4) return "" + (int) PEAK_CR_DURATION;
		if (index == 5) return "" + (int) CR_LOSS_RATE + "%";
		return null;
	}


}
