package data.scripts.shipsystems;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

import java.util.HashMap;

public class TemporalDriveStats extends BaseShipSystemScript {
	private static HashMap<HullSize, Float> hullsize = new HashMap();
	static {
		hullsize.put(HullSize.DEFAULT, 0.25f);
		hullsize.put(HullSize.FIGHTER, 0.25f);
		hullsize.put(ShipAPI.HullSize.FRIGATE, 0.25f);
		hullsize.put(ShipAPI.HullSize.DESTROYER, 0.5f);
		hullsize.put(ShipAPI.HullSize.CRUISER, 0.75f);
		hullsize.put(ShipAPI.HullSize.CAPITAL_SHIP, 1f);
	}
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		if (state == ShipSystemStatsScript.State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
		} else {
			stats.getMaxSpeed().modifyFlat(id, 200f * effectLevel);
			stats.getAcceleration().modifyFlat(id, 50f);
			stats.getMaxTurnRate().modifyFlat(id, 50f * effectLevel);
			stats.getTurnAcceleration().modifyFlat(id, 50f * effectLevel);
		}
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getMaxSpeed().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("increased engine power", false);
		}
		if (index == 1) {
			return new StatusData("increased maneuverablility", false);
		}
		return null;
	}
	
	
//	public float getActiveOverride(ShipAPI ship) {
//		if (ship.getHullSize() == HullSize.FRIGATE) {
//			return 1f;
//		}
//		if (ship.getHullSize() == HullSize.DESTROYER) {
//			return 0.75f;
//		}
//		if (ship.getHullSize() == HullSize.CRUISER) {
//			return 0.5f;
//		}
//		if (ship.getHullSize() == HullSize.CAPITAL_SHIP) {
//			return 0.5f;
//		}
//		return -1;
//	}
	public float getInOverride(ShipAPI ship) {
		return -1;
	}
	public float getOutOverride(ShipAPI ship) {
		return -1;
	}
	
	public float getRegenOverride(ShipAPI ship) {
		return -1;
	}

	public int getUsesOverride(ShipAPI ship) {
		if (ship.getHullSize() == HullSize.FRIGATE) {
			return 2;
		}
		if (ship.getHullSize() == HullSize.DESTROYER) {
			return 3;
		}
		if (ship.getHullSize() == HullSize.CRUISER) {
			return 4;
		}
		if (ship.getHullSize() == HullSize.CAPITAL_SHIP) {
			return 4;
		}
		return -1;
	}
}


