package data.scripts.hullmods;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import org.jetbrains.annotations.NotNull;

public class ArathanArmorMod extends BaseHullMod {
	private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(4);


	static {
		//id of all hmods you want to block
		BLOCKED_HULLMODS.add("heavyarmor");
	}
	public final float ARMOR_BONUS = 300f;
	public final float HIGH_EXPLOSIVE_REDUCTION = 40f;
	public final float EMP_REDUCTION = 50f;
	public final float MAX_ARMOR_DMG_REDUCTION = 100f;

    @Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getArmorBonus().modifyFlat(id, 300);
		stats.getHighExplosiveDamageTakenMult().modifyMult(id, 0.6f);
		stats.getEmpDamageTakenMult().modifyMult(id, 0.5f);
		stats.getMaxArmorDamageReduction().modifyPercent(id, 100f);

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
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) ARMOR_BONUS;
		if (index == 1) return "" + (int) HIGH_EXPLOSIVE_REDUCTION + "%";
		if (index == 2) return "" + (int) EMP_REDUCTION + "%";
		if (index == 3) return "" + (int) MAX_ARMOR_DMG_REDUCTION + "%";
		return null;
	}

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return !ship.getVariant().getHullMods().contains("heavyarmor");



	}

     @Override
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains("heavyarmor")) {
			return "Incompatible with Heavy Armor";
		}
	return null;
	}
}