package data.scripts.conditions;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.Arrays;

public class LakhesisCondition extends BaseMarketConditionPlugin  {

	private static String [] Lakhesis = new String [] {
		"lakhesismonarchy"
	};
	public void apply(String id) {
		if (Arrays.asList(Lakhesis).contains(market.getFactionId())) {
			market.getStability().modifyFlat(id, 2, "Lakhesis social engineering");
			market.getAccessibilityMod().modifyPercent(id, -20f, "Lakhesis social engineering");
			Industry industry = market.getIndustry(Industries.POPULATION);
			if (industry!= null){
				industry.getDemand(Commodities.DRUGS).getQuantity().modifyMult(id + "_0", 0);
			}
			industry = market.getIndustry(Industries.MINING);
			if (industry != null){
				industry.getDemand(Commodities.DRUGS).getQuantity().modifyMult(id + "_0", 0);
			}
		}
	}

	public void unapply(String id) {
		market.getStability().unmodify(id);
		market.getAccessibilityMod().unmodify(id);
		market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodify(id);
	}
	@Override
	protected void createTooltipAfterDescription (TooltipMakerAPI tooltip, boolean expanded) {
		super.createTooltipAfterDescription(tooltip, expanded);
		tooltip.addPara("%s increase in stability under Lakhesis control", 10f, Misc.getHighlightColor(), "+" + 2);
		tooltip.addPara("%s accessibility", 10f,Misc.getHighlightColor(), "-" + 25 + "%");
		tooltip.addPara("Local drug demand %s.", 10f, Misc.getHighlightColor(), "nullified");
	}
}
