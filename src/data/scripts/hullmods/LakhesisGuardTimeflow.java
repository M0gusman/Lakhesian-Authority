package data.scripts.hullmods;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.campaign.ListenerManager;
import data.scripts.shipsystems.TemporalBarrierStats;
import org.lazywizard.lazylib.FastTrig;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.util.MagicRender;
import org.lazywizard.lazylib.MathUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;

public class LakhesisGuardTimeflow  extends BaseHullMod {

    public static  Object STATUS_KEY1 = new Object();
    public static  Object STATUS_KEY2 = new Object();
    public static void battlespace(
            @NotNull  SpriteAPI sprite,
            Vector2f loc,
            Vector2f vel,
            @NotNull  Vector2f size,
            Vector2f growth,
            float angle,
            float spin,
            Color color,
            boolean additive,
            float jitterRange,
            float jitterTilt,
            float flickerRange,
            float flickerMedian,
            float maxDelay,
            float fadein,
            float full,
            float fadeout,
            CombatEngineLayers layer
    ){}
    public void renderCustomAfterimage(ShipAPI ship, Color color, float duration) { // idk why i put this here
        SpriteAPI sprite = ship.getSpriteAPI();
        float offsetX = sprite.getWidth() / 2f - sprite.getCenterX();
        float offsetY = sprite.getHeight() / 2f - sprite.getCenterY();
        float trueOffsetX = (float) (FastTrig.cos(Math.toRadians((ship.getFacing() - 90f))) * offsetX - FastTrig.sin(Math.toRadians((ship.getFacing() - 90f))) * offsetY);
        float trueOffsetY = (float) (FastTrig.sin(Math.toRadians((ship.getFacing() - 90f))) * offsetX + FastTrig.cos(Math.toRadians((ship.getFacing() - 90f))) * offsetY);
        MagicRender.battlespace(
                Global.getSettings().getSprite(ship.getHullSpec().getSpriteName()),
                new Vector2f(ship.getLocation().getX() + trueOffsetX, ship.getLocation().getY() + trueOffsetY),
                new Vector2f(0f, 0f),
                new Vector2f(ship.getSpriteAPI().getWidth(), ship.getSpriteAPI().getHeight()),
                new Vector2f(0f, 0f),
                ship.getFacing() - 90f,
                0f,
                color,
                true,
                0f,
                0f,
                0f,
                0f,
                0f,
                0.01f,
                0.1f,
                duration,
                CombatEngineLayers.BELOW_SHIPS_LAYER
        );
    }
    private static HashMap<HullSize, Float> hullsize = new HashMap();
    static {
        hullsize.put(HullSize.DEFAULT, 0.25f);
        hullsize.put(HullSize.FIGHTER, 0.25f);
        hullsize.put(HullSize.FRIGATE, 2.5f);
        hullsize.put(HullSize.DESTROYER, 3f);
        hullsize.put(HullSize.CRUISER, 3.5f);
        hullsize.put(HullSize.CAPITAL_SHIP, 4f);
    }
    public float TimeMultLimit = 300f;
    float DmgredBonus =  10f;
    public float DmgRedLimit = 20f;
    public float TimeMultLowerLimit = 100f;
    IntervalUtil afterimgint = new IntervalUtil(0.5f, 1f);

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if(ship.isAlive()) {
            float Limit = TimeMultLimit;
            float Hullscaling = hullsize.get(ship.getHullSize());
            float RedBonus = DmgredBonus;
            float TimeBonus = RedBonus;

            afterimgint.advance(amount);
            if (afterimgint.intervalElapsed()) {
                renderCustomAfterimage(ship, new Color(255, 255, 255, 40), 1f);
            }
            DamageTakenBuffListener listener;
            if (!ship.hasListenerOfClass(DamageTakenBuffListener.class)) {
                listener = new DamageTakenBuffListener();
                ship.addListener(listener);
            } else {
                listener = ship.getListeners(DamageTakenBuffListener.class).get(0);
            }
            float bonus = 10f * listener.GetBonusScale();
            float FinalBonus = RedBonus + (bonus * Hullscaling);
            TimeBonus = FinalBonus;
            if (ship.getSystem().isActive()) {
                TimeBonus = 0f;
                FinalBonus = 0f;
            }


            ship.getMutableStats().getArmorDamageTakenMult().modifyPercent(spec.getId(), -Math.min(DmgRedLimit, FinalBonus));
            ship.getMutableStats().getShieldDamageTakenMult().modifyPercent(spec.getId(), -Math.min(DmgRedLimit, FinalBonus));
            ship.getMutableStats().getHullDamageTakenMult().modifyPercent(spec.getId(), -Math.min(DmgRedLimit, FinalBonus));
            ship.getMutableStats().getTimeMult().modifyPercent(spec.getId(), Math.max(TimeMultLowerLimit, Math.min(Limit, TimeBonus * 2f)));

            Global.getCombatEngine().maintainStatusForPlayerShip(STATUS_KEY1, "graphics/icons/hullsys/temporal_shell.png", "Chronodrive", "Current Timeflow Increase: " + Math.round(Math.max(0f, Math.min(Limit, TimeBonus * 3f))) + "%", false);
            Global.getCombatEngine().maintainStatusForPlayerShip(STATUS_KEY2, "graphics/icons/hullsys/damper_field.png", "Chronodrive - Defense Field", "Current Damage Reduction: " + Math.round(Math.min(DmgRedLimit, FinalBonus)) + "%", false);
        }
    }

    //listener, by Ruddygreat
    public class DamageTakenBuffListener implements DamageTakenModifier, AdvanceableListener {

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
        public static final float DamageForMinBonus = 0;
        //bonus scales up to 1 at this value
        public static final float DamageforMaxBonus = 4000;

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

            float damTaken = 0;
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
    public String getDescriptionParam(int index, HullSize hullSize) {
        //if (index == 0) return "" + (int)RANGE_PENALTY_PERCENT + "%";
        return null;
    }

    @Override
    public boolean shouldAddDescriptionToTooltip(HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
        return false;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color h = Misc.getHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();
        Color t = Misc.getTextColor();
        Color g = Misc.getGrayColor();

        tooltip.addPara("A configuration of Lakhesian Chronodrive technology geared for the "
                        + "Legionary series of ships, projecting a temporal acceleration "
                        + "field and a damage-dampening field both increasing in intensity "
                        + "as the ship receives hits. Larger ships are capable of  "
                        + "installing larger chronodrives, increasing the intensity of both fields.",
                opad, h, "temporal acceleration field", "damage-dampening field", "speed", "Larger ships", "intensity");

        //tooltip.addPara("The maximum range is capped, based on the largest slot.", opad);

        tooltip.addSectionHeading("Temporal Field Projection - Legionary", Alignment.MID, opad);


        tooltip.addPara("Accelerates the ship's timeflow and damage reduction, based on hits and damage taken", opad);

        float col1W = 120;
        float colW = (int) ((width - col1W - 4f) / 1f);
        float lastW = colW;

        tooltip.beginTable(Misc.getBasePlayerColor(), Misc.getDarkPlayerColor(), Misc.getBrightPlayerColor(),
                20f, true, true,
                new Object [] {"Ship size", col1W, "Acceleration and dmg. red. effect", lastW});

        Color reallyG = g;
        if (Global.CODEX_TOOLTIP_MODE) {
            g = h;
        }

        Color c = null;
        if (hullSize == HullSize.FRIGATE) c = h;
        else c = g;
        tooltip.addRow(Alignment.MID, c, "Frigate",
                Alignment.MID, c, "+" + (float) hullsize.get(HullSize.FRIGATE) + "% every 400 points of damage taken");
        if (hullSize == HullSize.DESTROYER) c = h;
        else c = g;
        tooltip.addRow(Alignment.MID, c, "Destroyer",
                Alignment.MID, c, "+" + (float) hullsize.get(HullSize.DESTROYER) + "% every 400 points of damage taken");
        if (hullSize == HullSize.CRUISER) c = h;
        else c = g;
        tooltip.addRow(Alignment.MID, c, "Cruiser",
                Alignment.MID, c, "+" + (float) hullsize.get(HullSize.CRUISER) + "% every 400 points of damage taken");
        if (hullSize == HullSize.CAPITAL_SHIP) c = h;
        else c = g;
        tooltip.addRow(Alignment.MID, c, "Capital",
                Alignment.MID, c, "+" + (float) hullsize.get(HullSize.CAPITAL_SHIP) + "% every 400 points of damage taken");

        tooltip.addTable("", 0, opad);
        tooltip.addPara("Hits also increase the effects by half the listed effects for all hull sizes.", opad, h, "half");
        tooltip.addPara("The temporal acceleration bonus is capped at 3x for all hull sizes.", opad, h, "3x");
        tooltip.addPara("The damage reduction bonus is capped at 40%% for all hull sizes.", opad, h, "40%");

        tooltip.addSectionHeading("Chronodrive Overcharge", Alignment.MID, opad + 7f);

        tooltip.addPara("Increases timeflow bonus limit and gain by 2.5x on system activation.", opad, h, "2.5x");

    }

    public float getTooltipWidth() {
        return 412f;
    }


}