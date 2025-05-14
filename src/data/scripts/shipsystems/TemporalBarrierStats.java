package data.scripts.shipsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import org.lwjgl.util.vector.Vector2f;
//Written by CombustibleLemon
public class TemporalBarrierStats extends BaseShipSystemScript {

	public static final float DAMAGE_MULT = 99f;
	private static final float DURATION = 5f;
	private static final float DAMAGE_FOR_MAX_BONUS = 8000f;
	private static final float DAMAGE_BONUS_STEP = 400f;
	private static final float DAMAGE_BONUS_EXTRA_MULT = 1f;
	private static final float HIT_BONUS_EXTRA_MULT = 0.4f;
	private static final float BONUS_DECAY_RATE = 0.5f;

	private Float originalMass = null;
	private static State previousState = State.IDLE;
	private static float lastActivationTime = 0f;

	private SystemDamageTakenBuffListener listener;
	private ShipAPI ship;

	private static float getBonusMultStep(ShipAPI ship) {
		if (ship == null) {
			return 0f;
		}

		return  switch (ship.getHullSize()) {
			case DEFAULT, FIGHTER -> 0.25f;
            case FRIGATE -> 0.5f;
			case DESTROYER -> 1f;
			case CRUISER -> 1.5f;
			case CAPITAL_SHIP -> 2f;
		};
	}

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
		} else {
			return;
		}


		if (!ship.hasListenerOfClass(SystemDamageTakenBuffListener.class)) {
			listener = new SystemDamageTakenBuffListener(ship);
			ship.addListener(listener);
		}

		if (previousState == State.IDLE && state == State.IN){
			lastActivationTime = Global.getCombatEngine().getTotalElapsedTime(false);
		}

		if(previousState != state ){
			previousState = state;
		}

		if(originalMass == null){
			originalMass = ship.getMass();
		}

		if(state == State.OUT){
			ship.setMass(originalMass);
		} else {
			if (ship.getMass() == originalMass){
				ship.setMass(originalMass * 10f);
			}
		}

		float calculatedDamageMult = calculateDamageMult();

		stats.getTimeMult().modifyPercent(id, calculatedDamageMult*3f);
		stats.getShieldDamageTakenMult().modifyPercent(id, 1f - calculatedDamageMult);
		stats.getArmorDamageTakenMult().modifyPercent(id, 1f - calculatedDamageMult);
		stats.getHullDamageTakenMult().modifyPercent(id, 1f - calculatedDamageMult);
		stats.getMaxSpeed().modifyPercent(id, -75f);
		stats.getAcceleration().modifyPercent(id,-75f);
		stats.getMaxTurnRate().modifyPercent(id, -75f);
		stats.getTurnAcceleration().modifyPercent(id, -75f);
	}
	public static class SystemDamageTakenBuffListener implements DamageTakenModifier, AdvanceableListener {
		private float damageTaken = 0f;
		private float hitsTaken = 0f;
		private ShipAPI ship;

		SystemDamageTakenBuffListener(ShipAPI ship) {
			this.ship = ship;
		}


		@Override
		public void advance(float amount) {
			damageTaken -= damageTaken * BONUS_DECAY_RATE * amount;
			hitsTaken -= hitsTaken * BONUS_DECAY_RATE * amount;
		}

		@Override
		public String modifyDamageTaken(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
			//add a new buff data instance to the list
			//multiplies by DPS duration so that beams don't do wacky numbers
			float dmg = (damage.getDamage()) * (param instanceof BeamAPI ? damage.getDpsDuration() : 1);

			damageTaken += dmg;
			hitsTaken += 1;

			return "";
		}

		public void resetValues() {
			damageTaken = 0f;
		}

		public float GetBonusMult() {
			float damageMult = Math.min((int) damageTaken / (int) DAMAGE_BONUS_STEP, (int) DAMAGE_FOR_MAX_BONUS / (int) DAMAGE_BONUS_STEP ) * DAMAGE_BONUS_EXTRA_MULT * getBonusMultStep(ship);
			float hitsMult = hitsTaken * getBonusMultStep(ship) * HIT_BONUS_EXTRA_MULT;

			return damageMult + hitsMult;
		}
	}

	public void unapply(MutableShipStatsAPI stats, String id) {
		ShipAPI ship = (ShipAPI)stats.getEntity();
		if(ship == null){
			return;
		}

		if(originalMass == null){
			originalMass = ship.getMass();
		}

		if(ship.getMass() != originalMass){
			ship.setMass(originalMass);
		}

		if(listener != null){
			listener.resetValues();
			lastActivationTime = 0f;
		}

		previousState = State.IDLE;
		//stats.getShieldAbsorptionMult().unmodify(id);
		stats.getTimeMult().unmodify(id);
		stats.getShieldArcBonus().unmodify(id);
		stats.getShieldDamageTakenMult().unmodify(id);
		stats.getShieldTurnRateMult().unmodify(id);
		stats.getShieldUnfoldRateMult().unmodify(id);
		stats.getShieldUpkeepMult().unmodify(id);
		stats.getHullDamageTakenMult().unmodify(id);
		stats.getArmorDamageTakenMult().unmodify(id);
		stats.getShieldUpkeepMult().unmodify(id);
		stats.getMaxSpeed().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
	}

	public float calculateDamageMult() {
		float bonusMult = 0f;
		if(listener != null){
			bonusMult = listener.GetBonusMult();
		}

		float timeElapsed = Global.getCombatEngine().getTotalElapsedTime(false) - lastActivationTime;

		float timeMult = Math.max((DURATION - timeElapsed) / DURATION, 0);

		return Math.min(DAMAGE_MULT * timeMult + bonusMult, 99f);
	}

	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("current damage reduction: " + calculateDamageMult()*3f, false);
		}

		if (index == 2) {
			return new StatusData("Current mass: " + originalMass * 10f, false);
		}
		if (index == 3) {
			return new StatusData("Current timeflow: " + calculateDamageMult(), false);
		}
		if(index == 4){
			return new StatusData("Mobility reduced by 75%", true);
		}

		/*if (listener != null) {
			if (index == 3) {
				return new StatusData("damage Taken: " + listener.damageTaken, false);
			}
			if (index == 4) {
				return new StatusData("hits Taken: " + listener.hitsTaken, false);
			}
		}*/

		return null;
	}

}
