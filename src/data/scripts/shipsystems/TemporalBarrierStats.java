package data.scripts.shipsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import org.lwjgl.util.vector.Vector2f;


public class TemporalBarrierStats extends BaseShipSystemScript {

	public static final float DAMAGE_MULT = 99f;
	private static final float DURATION = 5f;
	private static final float DAMAGE_FOR_MAX_BONUS = 8000f;
	private static final float DAMAGE_BONUS_STEP = 400f;
	private static final float BONUS_MULT_STEP = 2.5f;
	private static final float DAMAGE_BONUS_EXTRA_MULT = 1.5f;
	private static final float HIT_BONUS_EXTRA_MULT = 0.7f;

	private Float originalMass = null;
	private static State previousState = State.IDLE;
	private static float lastActivationTime = 0f;

	private SystemDamageTakenBuffListener listener;

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
		} else {
			return;
		}


		if (!ship.hasListenerOfClass(SystemDamageTakenBuffListener.class)) {
			listener = new SystemDamageTakenBuffListener();
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

		stats.getShieldDamageTakenMult().modifyPercent(id, 1f - calculatedDamageMult);
		stats.getArmorDamageTakenMult().modifyPercent(id, 1f - calculatedDamageMult);
		stats.getHullDamageTakenMult().modifyPercent(id, 1f - calculatedDamageMult);
	}
	public static class SystemDamageTakenBuffListener implements DamageTakenModifier, AdvanceableListener {
		private float damageTaken = 0f;
		private int hitsTaken = 0;

		@Override
		public void advance(float amount) {
		}

		@Override
		public String modifyDamageTaken(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
			//add a new buff data instance to the list
			//multiplies by DPS duration so that beams don't do wacky numbers
			float dmg = (damage.getDamage()) * (param instanceof BeamAPI ? damage.getDpsDuration() : 1);

			damageTaken += dmg;
			hitsTaken++;

			return "";
		}

		public void resetValues() {
			damageTaken = 0f;
		}

		public float GetBonusMult() {
			float damageMult = Math.min((int) damageTaken / (int) DAMAGE_BONUS_STEP, (int) DAMAGE_FOR_MAX_BONUS / (int) DAMAGE_BONUS_STEP ) * DAMAGE_BONUS_EXTRA_MULT * BONUS_MULT_STEP;
			float hitsMult = hitsTaken * BONUS_MULT_STEP * HIT_BONUS_EXTRA_MULT;

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
		stats.getShieldArcBonus().unmodify(id);
		stats.getShieldDamageTakenMult().unmodify(id);
		stats.getShieldTurnRateMult().unmodify(id);
		stats.getShieldUnfoldRateMult().unmodify(id);
		stats.getShieldUpkeepMult().unmodify(id);
		stats.getHullDamageTakenMult().unmodify(id);
		stats.getArmorDamageTakenMult().unmodify(id);
		stats.getShieldUpkeepMult().unmodify(id);
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
			return new StatusData("current damage reduction: " + calculateDamageMult(), false);
		}

		if (index == 2) {
			return new StatusData("Current mass: " + originalMass * 10f, false);
		}

		return null;
	}

}