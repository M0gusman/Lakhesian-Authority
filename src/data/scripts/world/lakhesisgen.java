package data.scripts.world;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import data.scripts.world.systems.Lakhesis;


public class lakhesisgen implements SectorGeneratorPlugin {
    public lakhesisgen() {
    }

    public static void initFactionRelationships(SectorAPI sector) {
        FactionAPI hegemony = sector.getFaction("hegemony");
        FactionAPI tritachyon = sector.getFaction("tritachyon");
        FactionAPI pirates = sector.getFaction("pirates");
        FactionAPI church = sector.getFaction("luddic_church");
        FactionAPI path = sector.getFaction("luddic_path");
        FactionAPI indep = sector.getFaction("independent");
        FactionAPI diktat = sector.getFaction("sindrian_diktat");
        FactionAPI persean = sector.getFaction("persean");
        FactionAPI lakhesis = sector.getFaction("lakhesismonarchy");
        FactionAPI remnant = sector.getFaction("remnant");
        
        lakhesis.setRelationship(path.getId(), -1f);
        lakhesis.setRelationship(hegemony.getId(), -0.3f);
        lakhesis.setRelationship(church.getId(), -0.3f);
        lakhesis.setRelationship(pirates.getId(), -1f);
        lakhesis.setRelationship(tritachyon.getId(), -0.2f);
        lakhesis.setRelationship(indep.getId(), 0f);
        lakhesis.setRelationship(persean.getId(), -0.3f);
        lakhesis.setRelationship(diktat.getId(), -0.3f);
        lakhesis.setRelationship(remnant.getId(), 0);


        lakhesis.setRelationship("keruvim", -0.2f);
        lakhesis.setRelationship("mayasura", -0.15f);
        lakhesis.setRelationship("draco", RepLevel.HOSTILE);
        lakhesis.setRelationship("fang", RepLevel.HOSTILE);
        lakhesis.setRelationship("metelson", RepLevel.FAVORABLE);
        lakhesis.setRelationship("new_galactic_order", RepLevel.HOSTILE);
        lakhesis.setRelationship("junk_pirates", RepLevel.HOSTILE);
        lakhesis.setRelationship("junk_pirates_hounds", RepLevel.HOSTILE);
        lakhesis.setRelationship("junk_pirates_junkboys", RepLevel.HOSTILE);
        lakhesis.setRelationship("junk_pirates_technicians", RepLevel.HOSTILE);
        lakhesis.setRelationship("blade_breakers", RepLevel.VENGEFUL);
        lakhesis.setRelationship("cabal", RepLevel.HOSTILE);
        lakhesis.setRelationship("mess", RepLevel.VENGEFUL);
        lakhesis.setRelationship("interstellarimperium",RepLevel.FAVORABLE);
        lakhesis.setRelationship("vic", RepLevel.SUSPICIOUS);
        lakhesis.setRelationship("kadur_remnant", -0.3f);
        lakhesis.setRelationship("ironshell", -0.3f);
        lakhesis.setRelationship("exalted", RepLevel.SUSPICIOUS);
    }

    public void generate(SectorAPI sector) {
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("lakhesismonarchy");
        initFactionRelationships(sector);
        (new Lakhesis()).generate(sector);
    }



}
