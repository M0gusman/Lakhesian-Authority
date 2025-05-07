package data.scripts.shipsystems;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

public class TemporalDriveStats extends BaseShipSystemScript {

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		if (state == ShipSystemStatsScript.State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
		} else {
			stats.getMaxSpeed().modifyFlat(id, 200f * effectLevel);
			stats.getMaxTurnRate().modifyFlat(id, 500f * effectLevel);
			stats.getTurnAcceleration().modifyFlat(id, 1000f * effectLevel);
			stats.getAcceleration().modifyFlat(id, 1200f * effectLevel);
			stats.getTimeMult().modifyFlat(id, 3f);
		}
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getMaxSpeed().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getTimeMult().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("increased engine power", false);
		}
		if (index == 1) {
			return new StatusData("timeflow accelerated", false );
		}
		if (index == 2) {
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


