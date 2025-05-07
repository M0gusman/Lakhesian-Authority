package data.scripts.conditions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.campaign.Faction;

import java.util.Arrays;
import java.util.Locale;


public class LakhesisGrowthCondition extends BaseMarketConditionPlugin implements  MarketImmigrationModifier  {

public final float GROWTH_BONUS = 25f;


	private static String [] Lakhesis = new String [] {
			"lakhesismonarchy"
	};
	public void apply(String id) {
		 if (Arrays.asList(Lakhesis).contains(market.getFactionId())) {
         market.addImmigrationModifier(this);
		 }
	}
    public void unapply (String id){
       market.removeImmigrationModifier(this);
	}

	public void modifyIncoming (MarketAPI market, PopulationComposition incoming){
		incoming.add("lakhesismonarchy", 30f);
		incoming.getWeight().modifyFlat(getModId(), getThisImmigrationBonus() , Misc.ucFirst(condition.getName().toLowerCase()));
	}
    protected float getThisImmigrationBonus(){return 5* market.getSize();}
	@Override
	protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
		super.createTooltipAfterDescription(tooltip, expanded);
		tooltip.addPara(
				"%s growth boost (based on market size).",
				10f,
				Misc.getHighlightColor(),
				"+" + getThisImmigrationBonus());
	}
	}






