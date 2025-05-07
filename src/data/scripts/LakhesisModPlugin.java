//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.OfficerDataAPI;
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent;
import data.scripts.world.lakhesisgen;
import exerelin.campaign.SectorManager;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.characters.FullName.Gender;
import com.fs.starfarer.api.characters.ImportantPeopleAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import org.magiclib.campaign.MagicCaptainBuilder;
import org.magiclib.campaign.MagicFleetBuilder;
import org.magiclib.util.MagicCampaign;

import  java.util.*;

import java.util.Map;

public class LakhesisModPlugin extends BaseModPlugin {
    public static boolean isExerelin = false;
    private void addArbitersRetinue () {
        Map<String, Integer> skillLevels = new HashMap<>();
        skillLevels.put(Skills.COMBAT_ENDURANCE, 2);
        skillLevels.put(Skills.BALLISTIC_MASTERY, 2);
        skillLevels.put(Skills.POLARIZED_ARMOR, 2);
        skillLevels.put(Skills.GUNNERY_IMPLANTS, 2);
        skillLevels.put(Skills.ENERGY_WEAPON_MASTERY, 2);
        skillLevels.put(Skills.POINT_DEFENSE, 2);
        skillLevels.put(Skills.IMPACT_MITIGATION, 2);
        skillLevels.put(Skills.ORDNANCE_EXPERTISE, 2);
        skillLevels.put(Skills.FIELD_MODULATION, 2);
        skillLevels.put(Skills.HELMSMANSHIP, 2);
        skillLevels.put(Skills.TARGET_ANALYSIS, 2);
        skillLevels.put(Skills.MISSILE_SPECIALIZATION, 2);
        skillLevels.put(Skills.SYSTEMS_EXPERTISE, 2);
        skillLevels.put(Skills.CREW_TRAINING, 2);
        skillLevels.put(Skills.OFFICER_TRAINING, 2);
        skillLevels.put(Skills.CARRIER_GROUP, 2);
        skillLevels.put(Skills.TACTICAL_DRILLS, 2);
        skillLevels.put(Skills.FLUX_REGULATION, 2);
        skillLevels.put(Skills.OFFICER_MANAGEMENT, 2);
        skillLevels.put(Skills.BEST_OF_THE_BEST, 2);
        skillLevels.put(Skills.DAMAGE_CONTROL, 2);
        skillLevels.put(Skills.SENSORS, 2);
        skillLevels.put(Skills.NAVIGATION, 2);
        skillLevels.put(Skills.ELECTRONIC_WARFARE, 2);
        skillLevels.put(Skills.SUPPORT_DOCTRINE, 2);
        skillLevels.put(Skills.COORDINATED_MANEUVERS, 2);

        Map<String, Integer> retinue = new HashMap<>();
        retinue.put("lks_shalynn_Superiority", 2);
        retinue.put("lks_shalynn_Standard", 2);
        retinue.put("lks_phyran_Eigenweapon", 3);
        retinue.put("lks_varhla_Eigenweapon", 3);
        retinue.put("lks_phylas_Assault", 2);
        retinue.put("lks_phylas_Standard", 2);
        retinue.put("lks_valeryn_Standard", 2);
        retinue.put("lks_valeryn_Artillery", 2);
        retinue.put("lks_sharynn_Superiority", 2);
        retinue.put("lks_sharynn_Standard", 2);
        retinue.put("lks_valys_Standard", 4);

        SectorAPI sector = Global.getSector();
        StarSystemAPI system = sector.getStarSystem("Lakhesis");
        SectorEntityToken arynha = system.getEntityById("arynha");
        // The Arbiter's Retinue
       PersonAPI arbiter =  MagicCampaign.createCaptainBuilder(
               "lakhesismonarchy"
               ).setFirstName("The")
               .setLastName("Arbiter")
               .setGender(Gender.MALE)
               .setIsAI(true)
               .setAICoreType(Commodities.ALPHA_CORE)
               .setPersonality(Personalities.AGGRESSIVE)
               .setSkillPreference(OfficerManagerEvent.SkillPickPreference.YES_ENERGY_YES_BALLISTIC_YES_MISSILE_YES_DEFENSE)
               .setLevel(20)
               .setPortraitId("lksarbiter")
               .setPostId(Ranks.FACTION_LEADER)
               .setRankId(Ranks.FACTION_LEADER)
               .create();
       CampaignFleetAPI fleet =  MagicCampaign.createFleetBuilder()
               .setFleetName("The Arbiter's Retinue")
               .setFleetFaction("lakhesismonarchy")
               .setFleetType(FleetTypes.TASK_FORCE)
               .setAssignment(FleetAssignment.DEFEND_LOCATION)
               .setAssignmentTarget(arynha)
               .setCaptain(arbiter)
               .setFlagshipAutofit(false)
               .setFlagshipName("Arathan")
               .setFlagshipVariant("lks_arathan_Fleetbreaker")
               .setMinFP(1500)
               .setQualityOverride(10f)
               .setSupportFleet(retinue)
               .create();
    }

     @Override
    public void onNewGameAfterEconomyLoad(){
        ImportantPeopleAPI ip = Global.getSector().getImportantPeople();
        MarketAPI arynha = Global.getSector().getEconomy().getMarket("arynha");
        if (arynha != null) {
            addArbitersRetinue();

            PersonAPI nurturer = Global.getFactory().createPerson();
            nurturer.setId("nurturer");
            nurturer.setFaction("lakhesismonarchy");
            nurturer.setGender(Gender.FEMALE);
            nurturer.setImportance(PersonImportance.VERY_HIGH);
            nurturer.setRankId(Ranks.FACTION_LEADER);
            nurturer.setPostId(Ranks.FACTION_LEADER);
            nurturer.setVoice(Voices.OFFICIAL);
            nurturer.getName().setFirst("The");
            nurturer.getName().setLast("Nurturer");
            nurturer.setPortraitSprite("graphics/portraits/characters/lks_nurturer.png");
            arynha.setAdmin(nurturer);
            nurturer.getStats().setSkillLevel(Skills.HYPERCOGNITION, 4);
            nurturer.getStats().setSkillLevel("supremecognition", 4);
            nurturer.addTag(Tags.CONTACT_TRADE);
            nurturer.getStats().setSkillLevel(Skills.BALLISTIC_MASTERY, 2);
            nurturer.getStats().setSkillLevel(Skills.COMBAT_ENDURANCE, 2);
            nurturer.getStats().setSkillLevel(Skills.GUNNERY_IMPLANTS, 2);
            nurturer.getStats().setSkillLevel(Skills.ENERGY_WEAPON_MASTERY, 2);
            nurturer.getStats().setSkillLevel(Skills.POLARIZED_ARMOR, 2);
            nurturer.getStats().setSkillLevel(Skills.ORDNANCE_EXPERTISE, 2);
            nurturer.getStats().setSkillLevel(Skills.POINT_DEFENSE, 2);
            nurturer.getStats().setSkillLevel(Skills.MISSILE_SPECIALIZATION, 2);
            nurturer.getStats().setSkillLevel(Skills.SYSTEMS_EXPERTISE, 2);
            nurturer.getStats().setSkillLevel(Skills.HELMSMANSHIP, 2);
            nurturer.getStats().setSkillLevel(Skills.CREW_TRAINING, 2);
            nurturer.getStats().setSkillLevel(Skills.OFFICER_TRAINING, 2);
            nurturer.getStats().setSkillLevel(Skills.CARRIER_GROUP, 2);
            nurturer.getStats().setLevel(13);
            nurturer.setPersonality(Personalities.STEADY);
            arynha.getCommDirectory().addPerson(nurturer);
            arynha.addPerson(nurturer);
            ip.addPerson(nurturer);
        }
        MarketAPI ilryan = Global.getSector().getEconomy().getMarket("ilryan");
        if (ilryan != null) {
            PersonAPI arbiter = Global.getFactory().createPerson();
            arbiter.setId("arbiter");
            arbiter.setFaction("lakhesismonarchy");
            arbiter.setGender(Gender.MALE);
            arbiter.setImportance(PersonImportance.VERY_HIGH);
            arbiter.setRankId(Ranks.FACTION_LEADER);
            arbiter.setPostId(Ranks.FACTION_LEADER);
            arbiter.setVoice(Voices.OFFICIAL);
            arbiter.getName().setFirst("The");
            arbiter.getName().setLast("Arbiter");
            arbiter.setPortraitSprite("graphics/portraits/characters/lks_arbiter.png");
            ilryan.setAdmin(arbiter);
            arbiter.getStats().setSkillLevel(Skills.HYPERCOGNITION, 4);
            arbiter.getStats().setSkillLevel("supremecognition", 4);
            arbiter.addTag(Tags.CONTACT_TRADE);
            arbiter.addTag(Tags.CONTACT_MILITARY);
            arbiter.getStats().setSkillLevel(Skills.BALLISTIC_MASTERY, 2);
            arbiter.getStats().setSkillLevel(Skills.COMBAT_ENDURANCE, 2);
            arbiter.getStats().setSkillLevel(Skills.GUNNERY_IMPLANTS, 2);
            arbiter.getStats().setSkillLevel(Skills.ENERGY_WEAPON_MASTERY, 2);
            arbiter.getStats().setSkillLevel(Skills.POLARIZED_ARMOR, 2);
            arbiter.getStats().setSkillLevel(Skills.ORDNANCE_EXPERTISE, 2);
            arbiter.getStats().setSkillLevel(Skills.POINT_DEFENSE, 2);
            arbiter.getStats().setSkillLevel(Skills.MISSILE_SPECIALIZATION, 2);
            arbiter.getStats().setSkillLevel(Skills.SYSTEMS_EXPERTISE, 2);
            arbiter.getStats().setSkillLevel(Skills.HELMSMANSHIP, 2);
            arbiter.getStats().setSkillLevel(Skills.CREW_TRAINING, 2);
            arbiter.getStats().setSkillLevel(Skills.OFFICER_TRAINING, 2);
            arbiter.getStats().setSkillLevel(Skills.CARRIER_GROUP, 2);
            arbiter.getStats().setSkillLevel(Skills.FIELD_MODULATION, 2);
            arbiter.getStats().setSkillLevel(Skills.IMPACT_MITIGATION, 2);
            arbiter.getStats().setSkillLevel(Skills.DAMAGE_CONTROL, 2);
            arbiter.getStats().setSkillLevel(Skills.ELECTRONIC_WARFARE, 2);
            arbiter.getStats().setSkillLevel(Skills.OFFICER_TRAINING, 2);
            arbiter.getStats().setSkillLevel(Skills.FLUX_REGULATION, 2);
            arbiter.getStats().setSkillLevel(Skills.TACTICAL_DRILLS, 2);
            arbiter.getStats().setLevel(20);
            arbiter.setPersonality(Personalities.AGGRESSIVE);
            ilryan.getCommDirectory().addPerson(arbiter);
            ilryan.addPerson(arbiter);
            ip.addPerson(arbiter);

        }

    }

    @Override
    public void onNewGame() {
        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
        if (!haveNexerelin || SectorManager.getCorvusMode()){
            new lakhesisgen().generate(Global.getSector());
        }
    }

}
