package data.scripts.shipsystems;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class TemporalBarrierStats extends BaseShipSystemScript {

	public static float DAMAGE_MULT = 0.5f;
	//public static float DAMAGE_MULT = 0.8f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		//stats.getShieldTurnRateMult().modifyMult(id, 1f);
		//stats.getShieldUnfoldRateMult().modifyPercent(id, 2000);
		
		//stats.getShieldDamageTakenMult().modifyMult(id, 0.1f);
		stats.getShieldDamageTakenMult().modifyMult(id, 1f - DAMAGE_MULT * effectLevel);
		
		stats.getShieldUpkeepMult().modifyMult(id, 0.6f);

		stats.getArmorDamageTakenMult().modifyMult(id, 1f - DAMAGE_MULT * effectLevel);

		stats.getHullDamageTakenMult().modifyMult(id, 1f - DAMAGE_MULT * effectLevel);

		stats.getTimeMult().modifyFlat(id, 2f);
		//System.out.println("level: " + effectLevel);
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		//stats.getShieldAbsorptionMult().unmodify(id);
		stats.getShieldArcBonus().unmodify(id);
		stats.getShieldDamageTakenMult().unmodify(id);
		stats.getShieldTurnRateMult().unmodify(id);
		stats.getShieldUnfoldRateMult().unmodify(id);
		stats.getShieldUpkeepMult().unmodify(id);
		stats.getHullDamageTakenMult().unmodify(id);
		stats.getArmorDamageTakenMult().unmodify(id);
		stats.getTimeMult().unmodify(id);
		stats.getShieldUpkeepMult().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("shield damage taken reduced by 50%", false);
		}
		if (index == 1) {
			return new StatusData("shield upkeep reduced by 40%", false);
		}
		if (index == 2) {
			return new StatusData("armor and hull damage taken reduced by 50%", false);
		}
			if (index == 3) {
				return new StatusData("timeflow accelerated", false);
			}
			return null;
		}

	}