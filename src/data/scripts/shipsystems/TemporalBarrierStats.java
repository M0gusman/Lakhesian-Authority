package data.scripts.shipsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import data.scripts.hullmods.LakhesisGuardTimeflow;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.Map;

public class TemporalBarrierStats extends BaseShipSystemScript {

	public static float DAMAGE_MULT = 99f;
	//public static float DAMAGE_MULT = 0.8f;
	private Float mass = null;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		float Mult = DAMAGE_MULT;
		Map<String, Object> customCombatData = Global.getCombatEngine().getCustomData();

		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
		} else {
			return;
		}
		if(mass == null){
			mass = ship.getMass();
		}
		if(state == state.OUT){
			ship.setMass(mass);
		}
		else{
			if (ship.getMass() == mass){
				ship.setMass(mass * 10f);
			}
			SystemDamageTakenBuffListener listener;
			if (!ship.hasListenerOfClass(SystemDamageTakenBuffListener.class)) {
				listener = new SystemDamageTakenBuffListener();
				ship.addListener(listener);
			} else {
				listener = ship.getListeners(SystemDamageTakenBuffListener.class).get(0);
			}
			DAMAGE_MULT *= listener.GetBonusScale();
		}

		//stats.getShieldTurnRateMult().modifyMult(id, 1f);
		//stats.getShieldUnfoldRateMult().modifyPercent(id, 2000);
		
		//stats.getShieldDamageTakenMult().modifyMult(id, 0.1f);
		stats.getShieldDamageTakenMult().modifyPercent(id,  - DAMAGE_MULT * effectLevel);

		stats.getArmorDamageTakenMult().modifyPercent(id,  - DAMAGE_MULT * effectLevel);

		stats.getHullDamageTakenMult().modifyPercent(id,  - DAMAGE_MULT * effectLevel);

		//System.out.println("level: " + effectLevel);

	}
	public class SystemDamageTakenBuffListener implements DamageTakenModifier, AdvanceableListener {

		//data class to hold some info
		public static class BuffData {
			float damage;
			float lifetime;

			public BuffData(float damage, float lifetime) {
				this.damage = damage;
				this.lifetime = lifetime;
			}
		}

		public ArrayList<BuffData> buffData = new ArrayList<>();

		//bonus scale is 0 below this value
		public static final float DamageForMinBonus = 200f;
		//bonus scales up to 1 at this value
		public static final float DamageforMaxBonus = 8000f;

		@Override
		public void advance(float amount) {

			//go over every buff and decrement their remaining lifetime
			for (BuffData buff : new ArrayList<>(buffData)) {
				buff.lifetime -= amount;
			}

			//fancy removal code
			buffData.removeIf(buffData -> buffData.lifetime < 0);
		}

		@Override
		public String modifyDamageTaken(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
			//add a new buff data instance to the list
			//multiplies by DPS duration so that beams don't do wacky numbers
			buffData.add(new BuffData((damage.getDamage() + 200f) * (param instanceof BeamAPI ? damage.getDpsDuration():1), 5));
			return "";
		}

		public float GetBonusScale() {

			float damTaken = DamageforMaxBonus;
			for (BuffData buff : buffData) {
				damTaken += buff.damage;
			}

			//if less than min damage, return 0
			if (damTaken < DamageForMinBonus) {
				return  0;
			}

			//if greater than max, return 1
			if (damTaken > DamageforMaxBonus) {
				return 1;
			}

			return (damTaken - DamageForMinBonus) / (DamageforMaxBonus - DamageForMinBonus);
		}
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		ShipAPI ship = (ShipAPI)stats.getEntity();
		if(ship == null){
			return;
		}
		if(mass == null){
			mass = ship.getMass();
		}
		if(ship.getMass() != mass){
			ship.setMass(mass);
		}
		//stats.getShieldAbsorptionMult().unmodify(id);
		stats.getShieldArcBonus().unmodify(id);
		stats.getShieldDamageTakenMult().unmodify(id);
		stats.getShieldTurnRateMult().unmodify(id);
		stats.getShieldUnfoldRateMult().unmodify(id);
		stats.getShieldUpkeepMult().unmodify(id);
		stats.getHullDamageTakenMult().unmodify(id);
		stats.getArmorDamageTakenMult().unmodify(id);
		stats.getShieldUpkeepMult().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("current damage reduction: " + DAMAGE_MULT, false);
		}
		if (index == 2) {
			return new StatusData("Current mass: " + mass, false);
		}
			return null;
		}

	}