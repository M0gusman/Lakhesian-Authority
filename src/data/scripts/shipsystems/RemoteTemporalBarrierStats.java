package data.scripts.shipsystems;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;


public class RemoteTemporalBarrierStats extends BaseShipSystemScript {
	public static final Object KEY_JITTER = new Object();

	public static final float DAMAGE_TAKEN_PERCENT = 50;

	public static final Color JITTER_UNDER_COLOR = new Color(250, 0, 0,155);
	public static final Color JITTER_COLOR = new Color(250, 0, 0, 155);
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
		} else {
			return;
		}
		if (effectLevel > 0) {
			float jitterLevel = effectLevel;
			float maxRangeBonus = 5f;
			float jitterRangeBonus = jitterLevel * maxRangeBonus;
			for (ShipAPI fighter : getFighters(ship)) {
				if (fighter.isHulk()) continue;
				MutableShipStatsAPI fStats = fighter.getMutableStats();
//				fStats.getBallisticWeaponDamageMult().modifyPercent(id, DAMAGE_INCREASE_PERCENT * effectLevel);
//				fStats.getEnergyWeaponDamageMult().modifyPercent(id, DAMAGE_INCREASE_PERCENT * effectLevel);
//				fStats.getMissileWeaponDamageMult().modifyPercent(id, DAMAGE_INCREASE_PERCENT * effectLevel);

				fStats.getTimeMult().modifyFlat(id, 2f);
				fStats.getHullDamageTakenMult().modifyMult(id, 1f - 0.01f * DAMAGE_TAKEN_PERCENT * effectLevel);
				fStats.getArmorDamageTakenMult().modifyMult(id, 1f - 0.01f * DAMAGE_TAKEN_PERCENT * effectLevel);
				fStats.getShieldDamageTakenMult().modifyMult(id, 1f - 0.01f * DAMAGE_TAKEN_PERCENT * effectLevel);

				if (jitterLevel > 0) {
					//fighter.setWeaponGlow(effectLevel, new Color(255,50,0,125), EnumSet.allOf(WeaponType.class));
					fighter.setWeaponGlow(effectLevel, Misc.setAlpha(JITTER_UNDER_COLOR, 255), EnumSet.allOf(WeaponAPI.WeaponType.class));

					fighter.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 5, 0f, jitterRangeBonus);
					fighter.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 2, 0f, 0 + jitterRangeBonus * 1f);
					Global.getSoundPlayer().playLoop("system_targeting_feed_loop", ship, 1f, 1f, fighter.getLocation(), fighter.getVelocity());
				}
			}
		}
	}


	private List<ShipAPI> getFighters(ShipAPI carrier) {
		List<ShipAPI> result = new ArrayList<ShipAPI>();

//		this didn't catch fighters returning for refit
//		for (FighterLaunchBayAPI bay : carrier.getLaunchBaysCopy()) {
//			if (bay.getWing() == null) continue;
//			result.addAll(bay.getWing().getWingMembers());
//		}

		for (ShipAPI ship : Global.getCombatEngine().getShips()) {
			if (!ship.isFighter()) continue;
			if (ship.getWing() == null) continue;
			if (ship.getWing().getSourceShip() == carrier) {
				result.add(ship);
			}
		}

		return result;
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
		} else {
			return;
		}
		for (ShipAPI fighter : getFighters(ship)) {
			if (fighter.isHulk()) continue;
			MutableShipStatsAPI fstats = fighter.getMutableStats();
			fstats.getTimeMult().unmodify(id);
			fstats.getHullDamageTakenMult().unmodify(id);
			fstats.getArmorDamageTakenMult().unmodify(id);
		}
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {

		if (index == 0) {
			return new StatusData("fighter timeflow accelerated", false);
		}
		if (index == 1) {
			return new StatusData("fighter damage taken reduced", false);
		}
		return null;
	}
}
