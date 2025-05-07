package data.scripts.conditions;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.econ.*;


public class IlryanFleets extends BaseMarketConditionPlugin {

	public void apply(String id) {
		market.getStability().modifyFlat(id, 2, "Military Stabilization");
		market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyPercent(id, 35f, "Military Buildup");
		market.getStats().getDynamic().getMod(Stats.OFFICER_PROB_MOD).modifyPercent(id, 50f);
		market.getStats().getDynamic().getMod(Stats.OFFICER_MAX_LEVEL_MOD).modifyPercent(id, 50f );


//		market.getStats().getDynamic().getStat(Stats.OFFICER_NUM_MULT).modifyFlat(id, ConditionData.HEADQUARTERS_OFFICER_NUM_MULT_BONUS);
//		market.getStats().getDynamic().getStat(Stats.OFFICER_LEVEL_MULT).modifyFlat(id, ConditionData.HEADQUARTERS_OFFICER_LEVEL_MULT_BONUS);
	}

	public void unapply(String id) {
		market.getStability().unmodify(id);
		market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodify(id);
		market.getStats().getDynamic().getMod(Stats.OFFICER_PROB_MOD).unmodify(id);
		market.getStats().getDynamic().getMod(Stats.OFFICER_MAX_LEVEL_MOD).unmodify(id);
//		market.getStats().getDynamic().getStat(Stats.OFFICER_NUM_MULT).unmodify(id);
//		market.getStats().getDynamic().getStat(Stats.OFFICER_LEVEL_MULT).unmodify(id);
	}

}
