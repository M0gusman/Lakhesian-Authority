package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

import java.util.HashMap;
import java.util.Map;

public class ArathanWeaponMod extends BaseHullMod {
	private static Map mag = new HashMap();
	static {
		mag.put(WeaponAPI.WeaponSize.SMALL, 3f);
		mag.put(WeaponAPI.WeaponSize.MEDIUM,6f);
		mag.put(WeaponAPI.WeaponSize.LARGE, 8f);
	}

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
	stats.getDynamic().getMod(Stats.SMALL_BALLISTIC_MOD).modifyFlat(id,-(float) mag.get(WeaponAPI.WeaponSize.SMALL));
	stats.getDynamic().getMod(Stats.SMALL_ENERGY_MOD).modifyFlat(id,-(float) mag.get(WeaponAPI.WeaponSize.SMALL));
	stats.getDynamic().getMod(Stats.SMALL_MISSILE_MOD).modifyFlat(id,-(float) mag.get(WeaponAPI.WeaponSize.SMALL));
	stats.getDynamic().getMod(Stats.MEDIUM_BALLISTIC_MOD).modifyFlat(id,-(float) mag.get(WeaponAPI.WeaponSize.MEDIUM));
	stats.getDynamic().getMod(Stats.MEDIUM_ENERGY_MOD).modifyFlat(id,-(float) mag.get(WeaponAPI.WeaponSize.MEDIUM));
	stats.getDynamic().getMod(Stats.MEDIUM_MISSILE_MOD).modifyFlat(id,-(float) mag.get(WeaponAPI.WeaponSize.MEDIUM));
	stats.getDynamic().getMod(Stats.LARGE_BALLISTIC_MOD).modifyFlat(id,-(float) mag.get(WeaponAPI.WeaponSize.LARGE));
	stats.getDynamic().getMod(Stats.LARGE_ENERGY_MOD).modifyFlat(id,-(float) mag.get(WeaponAPI.WeaponSize.LARGE));
	stats.getDynamic().getMod(Stats.LARGE_MISSILE_MOD).modifyFlat(id,-(float) mag.get(WeaponAPI.WeaponSize.LARGE));
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) mag.get(WeaponAPI.WeaponSize.SMALL)).intValue();
		if (index == 1) return "" + ((Float) mag.get(WeaponAPI.WeaponSize.MEDIUM)).intValue();
		if (index == 2) return "" + ((Float) mag.get(WeaponAPI.WeaponSize.LARGE)).intValue();
		return null;
	}

	@Override
	public boolean affectsOPCosts() {
		return true;
	}

}








