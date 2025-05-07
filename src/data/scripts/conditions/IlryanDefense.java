package data.scripts.conditions;

import java.util.Arrays;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class IlryanDefense extends BaseMarketConditionPlugin {

	private static String [] Lakhesis = new String [] {
		"lakhesismonarchy"
	};
	public void apply(String id) {
		if (Arrays.asList(Lakhesis).contains(market.getFactionId())) {
			market.getStability().modifyFlat(id, 3, "Ilryan Defenses");
		} else {
			market.getStability().modifyFlat(id, -5, "Ilryan Defiance");
		}
		if (Arrays.asList(Lakhesis).contains((market.getFactionId()))) {
			market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).modifyMult(id, 1.5f, "Ilryan Defenses");
		}
	}

	public void unapply(String id) {
		market.getStability().unmodify(id);
		market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodify(id);
	}

	@Override
	protected void createTooltipAfterDescription (TooltipMakerAPI tooltip, boolean expanded){
		super.createTooltipAfterDescription(tooltip, expanded);
		tooltip.addPara("%s defense rating",10f, Misc.getHighlightColor(), "+" + 1.5 + "x");
		tooltip.addPara("%s increase in stability under Lakhesis control",10f, Misc.getHighlightColor(), "+" + 3);
		tooltip.addPara("%s decrease in stability otherwise",10f, Misc.getHighlightColor(), "-" + 5);
	}
}
