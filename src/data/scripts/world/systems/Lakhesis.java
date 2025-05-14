package data.scripts.world.systems;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin.AsteroidFieldParams;


public class Lakhesis {

	public void generate(SectorAPI sector) {
		
		StarSystemAPI system = sector.createStarSystem("Lakhesis");
		LocationAPI hyper = Global.getSector().getHyperspace();
		system.getLocation().set(32500, -1200);
		system.setBackgroundTextureFilename("graphics/backgrounds/background4.jpg");
		
		// create the star and generate the hyperspace anchor for this system
		// Canaan, the promised land.
		PlanetAPI lakhesis_star = system.initStar("lakhesis", // unique id for this star
				                              "star_blue_giant",  // id in planets.json
										    900f, 		  // radius (in pixels at default zoom)
										    500f); // corona radius, from star edge
		
		system.setLightColor(new Color(225, 245, 255)); // light color in entire system, affects all entities
		
		PlanetAPI khyr = system.addPlanet("khyr", lakhesis_star, "Khyr", "barren_venuslike", 0, 105, 2500, 120);
		khyr.getSpec().setPitch(100f);
		khyr.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "volturn"));
		khyr.getSpec().setGlowColor(new Color(250,225,195,255));
		khyr.applySpecChanges();
		khyr.setCustomDescriptionId("planet_khyr");
		MarketAPI khyrMarketPlace = addMarketplace.addMarketplace(
				"lakhesianauth",
				khyr,
				null,
				"Khyr",
				5,
				new ArrayList<>(Arrays.asList(
						Conditions.HOT,
						Conditions.ORE_ABUNDANT,
						Conditions.POPULATION_5,
						Conditions.RARE_ORE_MODERATE,
						"lakhesiscondition",
						"lakhesisgrowth"



				)),
				new ArrayList<>(Arrays.asList(
						Industries.POPULATION,
						Industries.BATTLESTATION_HIGH,
						Industries.REFINING,
						Industries.MINING,
						Industries.SPACEPORT,
						Industries.HEAVYBATTERIES,
						Industries.MILITARYBASE
		)),
				new ArrayList<>(Arrays.asList(
						Submarkets.SUBMARKET_OPEN,
						Submarkets.SUBMARKET_STORAGE,
						Submarkets.SUBMARKET_BLACK,
						Submarkets.GENERIC_MILITARY
				)),
				0.3f
		);
		khyrMarketPlace.getIndustry("battlestation_high").setAICoreId(Commodities.ALPHA_CORE);


		// Add a gate.
		PlanetAPI arynha = system.addPlanet("arynha", lakhesis_star, "Arynha", "terran", 60, 190, 6500, 250);
		arynha.getSpec().setPitch( 190.0f);
		arynha.getSpec().setPlanetColor(new Color(255,245,225,255));
		arynha.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "volturn"));
		arynha.getSpec().setGlowColor(new Color(250,225,195,255));
		arynha.getSpec().setUseReverseLightForGlow(true);
		arynha.applySpecChanges();
		arynha.setCustomDescriptionId("planet_arynha");
		MarketAPI arynhaMarketPlace = addMarketplace.addMarketplace(
				"lakhesianauth",
				arynha,
				null,
				"Arynha",
				7,

				new ArrayList<>(Arrays.asList(
				Conditions.POPULATION_7,
				Conditions.HABITABLE,
				Conditions.FARMLAND_RICH,
				Conditions.ORE_ABUNDANT,
				Conditions.ORGANICS_COMMON,
				Conditions.REGIONAL_CAPITAL,
						"lakhesiscondition",
						Conditions.HOT
				)),

				new ArrayList<>(Arrays.asList(
						Industries.STARFORTRESS_HIGH,
						Industries.FARMING,
						Industries.MINING,
						Industries.HIGHCOMMAND,
						Industries.MEGAPORT,
						Industries.POPULATION,
						Industries.LIGHTINDUSTRY,
						Industries.WAYSTATION


				)),

				new ArrayList<>(Arrays.asList(
						Submarkets.SUBMARKET_OPEN,
						Submarkets.SUBMARKET_STORAGE,
						Submarkets.GENERIC_MILITARY,
						Submarkets.SUBMARKET_BLACK
				)),


						0.3f

						);
		arynhaMarketPlace.addIndustry(Industries.HEAVYBATTERIES);
		arynhaMarketPlace.getIndustry("highcommand").setAICoreId(Commodities.ALPHA_CORE);
		arynhaMarketPlace.getIndustry("heavybatteries").setAICoreId(Commodities.ALPHA_CORE);
		arynhaMarketPlace.getIndustry("starfortress_high").setAICoreId(Commodities.ALPHA_CORE);
		
			PlanetAPI turin = system.addPlanet("turin", arynha, "Turin", "barren", 0, 50, 1500, 30);
		turin.getSpec().setTexture(Global.getSettings().getSpriteName("planets", "barren03"));
		turin.getSpec().setPlanetColor(new Color(235,255,245,255));
		turin.getSpec().setPitch( 140.0f);
		turin.applySpecChanges();
		turin.setCustomDescriptionId("turin_planet");

		MarketAPI turinMarketPlace = addMarketplace.addMarketplace(
				"lakhesianauth",
				turin,
				null,
				"Turin",
				6,

				new ArrayList<>(Arrays.asList(
						Conditions.POPULATION_6,
						Conditions.ORE_SPARSE,
						Conditions.RARE_ORE_SPARSE,
						Conditions.NO_ATMOSPHERE,
						"lakhesiscondition"
				)),

				new ArrayList<>(Arrays.asList(
						Industries.STARFORTRESS_HIGH,
						Industries.MINING,
						Industries.MEGAPORT,
						Industries.POPULATION,
						Industries.WAYSTATION,
						Industries.MILITARYBASE,
						Industries.REFINING,
						Industries.HEAVYBATTERIES


				)),

				new ArrayList<>(Arrays.asList(
						Submarkets.SUBMARKET_OPEN,
						Submarkets.SUBMARKET_STORAGE,
						Submarkets.GENERIC_MILITARY,
						Submarkets.SUBMARKET_BLACK
				)),


				0.3f

		);
		turinMarketPlace.addIndustry(Industries.FUELPROD, new ArrayList<>(Arrays.asList(Items.SYNCHROTRON)));
		turinMarketPlace.getIndustry("starfortress_high").setAICoreId(Commodities.ALPHA_CORE);
			// Langrangrians for Gilead: relay + jump
			// L4
			SectorEntityToken relay = system.addCustomEntity("lakhesis_relay", // unique id
					 "Lakhesis Relay", // name - if null, defaultName from custom_entities.json will be used
					 "comm_relay", // type of object, defined in custom_entities.json
					 "lakhesianauth"); // faction
			relay.setCircularOrbitPointingDown(lakhesis_star, 60-60, 5000, 250);
			
			// L5
			JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("arynha_jump", "Arynha Jump-point");
			jumpPoint1.setCircularOrbit( arynha, 60+60, 2500, 250);
			jumpPoint1.setRelatedPlanet(arynha);
			system.addEntity(jumpPoint1);
			
		PlanetAPI girash = system.addPlanet("girash", lakhesis_star, "Girash", "gas_giant", 180, 220, 8000, 350);
		girash.getSpec().setPitch( 100.0f);
		girash.getSpec().setPlanetColor(new Color(245,255,195,255));
		girash.applySpecChanges();
		girash.setCustomDescriptionId("girash_planet");

		MarketAPI girashMarketPlace = addMarketplace.addMarketplace(
				"lakhesianauth",
				girash,
				null,
				"Girash",
				6,

				new ArrayList<>(Arrays.asList(
						Conditions.POPULATION_6,
					    Conditions.VOLATILES_ABUNDANT,
						Conditions.EXTREME_WEATHER,
						Conditions.HIGH_GRAVITY,
						"lakhesiscondition"
				)),

				new ArrayList<>(Arrays.asList(
						Industries.STARFORTRESS_HIGH,
						Industries.MINING,
						Industries.MEGAPORT,
						Industries.POPULATION,
						Industries.WAYSTATION,
						Industries.HIGHCOMMAND,
						Industries.LIGHTINDUSTRY,
						Industries.REFINING



				)),

				new ArrayList<>(Arrays.asList(
						Submarkets.SUBMARKET_OPEN,
						Submarkets.SUBMARKET_STORAGE,
						Submarkets.GENERIC_MILITARY,
						Submarkets.SUBMARKET_BLACK
				)),


				0.3f

		);
		girashMarketPlace.addIndustry(Industries.HEAVYBATTERIES,new ArrayList<>(Arrays.asList(Items.DRONE_REPLICATOR)));
		girashMarketPlace.getIndustry("heavybatteries").setAICoreId(Commodities.ALPHA_CORE);
		girashMarketPlace.getIndustry("starfortress_high").setAICoreId(Commodities.ALPHA_CORE);
		girashMarketPlace.getIndustry("highcommand").setAICoreId(Commodities.ALPHA_CORE);

		system.addRingBand(girash, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 400, 30, Terrain.RING, null);
		
			PlanetAPI ilryan = system.addPlanet("ilryan", lakhesis_star, "Ilryan", "barren", 0, 100, 3000, 40);
			ilryan.getSpec().setTilt( 190f );
		    ilryan.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "volturn"));
		    ilryan.getSpec().setGlowColor(new Color(250,225,195,255));
		    ilryan.applySpecChanges();
		    ilryan.setCustomDescriptionId("planet_ilryan");
		MarketAPI ilryanMarketPlace = addMarketplace.addMarketplace(
				"lakhesianauth",
				ilryan,
				null,
				"Ilryan",
				6,

				new ArrayList<>(Arrays.asList(
						Conditions.POPULATION_6,
						Conditions.ORE_RICH,
						Conditions.VERY_HOT,
						Conditions.RARE_ORE_RICH,
						Conditions.ORGANICS_COMMON,
						Conditions.NO_ATMOSPHERE,
						"lakhesiscondition",
						"ilryandefense",
						"milsociety"
				)),

				new ArrayList<>(Arrays.asList(
						Industries.STARFORTRESS_HIGH,
						Industries.MINING,
						Industries.MEGAPORT,
						Industries.POPULATION,
						Industries.WAYSTATION


				)),

				new ArrayList<>(Arrays.asList(
						Submarkets.SUBMARKET_OPEN,
						Submarkets.SUBMARKET_STORAGE,
						Submarkets.GENERIC_MILITARY,
						Submarkets.SUBMARKET_BLACK
				)),


				0.3f

		);
		ilryanMarketPlace.addIndustry(Industries.HEAVYBATTERIES);
		ilryanMarketPlace.addIndustry(Industries.ORBITALWORKS, new ArrayList<>(Arrays.asList(Items.PRISTINE_NANOFORGE)));
		ilryanMarketPlace.addIndustry(Industries.HIGHCOMMAND, new ArrayList<>(Arrays.asList(Items.CRYOARITHMETIC_ENGINE)));
		ilryanMarketPlace.addIndustry(Industries.REFINING, new ArrayList<>(Arrays.asList(Items.CATALYTIC_CORE)));
		ilryanMarketPlace.getIndustry("heavybatteries").setAICoreId(Commodities.ALPHA_CORE);
		ilryanMarketPlace.getIndustry("starfortress_high").setAICoreId(Commodities.ALPHA_CORE);
		ilryanMarketPlace.getIndustry("highcommand").setAICoreId(Commodities.ALPHA_CORE);

			SectorEntityToken girash_loc = system.addCustomEntity(null, null, "sensor_array", "lakhesianauth");
			girash_loc.setCircularOrbitPointingDown(girash, 0 + 180, 1000, 40);
			
		// and have asteroids on the other side, too. - L5 is behind
		SectorEntityToken girashL5 = system.addTerrain(Terrain.ASTEROID_FIELD,
				new AsteroidFieldParams(
					450f, // min radius
					600f, // max radius
					25, // min asteroid count
					45, // max asteroid count
					4f, // min asteroid radius 
					12f, // max asteroid radius
					"Girash L5 Asteroids")); // null for default name
		
		girashL5.setCircularOrbit(lakhesis_star, 180 - 60, 6500, 350);
		
		// Lagrangrian gate - embedd in some lovely asteroids? - L4 is ahead
		SectorEntityToken gate1 = system.addCustomEntity("lakhesis_gate", // unique id
				 "Gate of Lakhesis", // name - if null, defaultName from custom_entities.json will be used
				 "inactive_gate", // type of object, defined in custom_entities.json
				 null); // faction
		gate1.setCircularOrbit(system.getEntityById("lakhesis"), 180 + 60, 6500, 350);
		
		SectorEntityToken girashL4 = system.addTerrain(Terrain.ASTEROID_FIELD,
				new AsteroidFieldParams(
					450f, // min radius
					600f, // max radius
					25, // min asteroid count
					45, // max asteroid count
					4f, // min asteroid radius 
					12f, // max asteroid radius
					"Girash L4 Asteroids")); // null for default name
		
		girashL4.setCircularOrbit(lakhesis_star, 180+60, 6500, 350);
			
		float radiusAfter = StarSystemGenerator.addOrbitingEntities(system, lakhesis_star, StarAge.OLD,
				1, 3, // min/max entities to add
				10000, // radius to start adding at 
				4, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
				true, // whether to use custom or system-name based names
				true); // whether to allow habitable worlds

		
		system.autogenerateHyperspaceJumpPoints(true, true);
	}
}
