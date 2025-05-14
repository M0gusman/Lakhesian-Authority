package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.FastTrig;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.util.MagicRender;
import org.lazywizard.lazylib.MathUtils;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;

public class LakhesisStrikeTimeflow extends BaseHullMod {

    public static  Object STATUS_KEY1 = new Object();
    public static void battlespace(
            @NotNull  com.fs.starfarer.api.graphics.SpriteAPI sprite,
            org.lwjgl.util.vector.Vector2f loc,
            org.lwjgl.util.vector.Vector2f vel,
            @NotNull  org.lwjgl.util.vector.Vector2f size,
            org.lwjgl.util.vector.Vector2f growth,
            float angle,
            float spin,
            java.awt.Color color,
            boolean additive,
            float jitterRange,
            float jitterTilt,
            float flickerRange,
            float flickerMedian,
            float maxDelay,
            float fadein,
            float full,
            float fadeout,
            com.fs.starfarer.api.combat.CombatEngineLayers layer
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
    private static HashMap<ShipAPI.HullSize, Float> hullsize = new HashMap();
    static {
        hullsize.put(HullSize.DEFAULT, 0.25f);
        hullsize.put(HullSize.FIGHTER, 0.25f);
        hullsize.put(ShipAPI.HullSize.FRIGATE, 0.5f);
        hullsize.put(ShipAPI.HullSize.DESTROYER, 0.75f);
        hullsize.put(ShipAPI.HullSize.CRUISER, 1.25f);
        hullsize.put(ShipAPI.HullSize.CAPITAL_SHIP, 1.5f);
    }
   public float TimeMultLimit = 300f;
   IntervalUtil afterimgint = new IntervalUtil(0.5f, 1f);

   @Override
   public void advanceInCombat(ShipAPI ship, float amount) {
       if(ship.isAlive()) {
           float velocityBonus = ship.getVelocity().length();
           float Limit = TimeMultLimit;

           afterimgint.advance(amount);
           if (afterimgint.intervalElapsed()) {
               renderCustomAfterimage(ship, new Color(255, 255, 255, 40), 1f);
           }
           if (ship.getSystem().isActive()) {
               Limit = TimeMultLimit * 2.5f;
               velocityBonus = velocityBonus * 2.5f;
           }
           ship.getMutableStats().getTimeMult().modifyPercent(spec.getId(), Math.round(Math.min(Limit, velocityBonus * hullsize.get(ship.getHullSize()))));

           Global.getCombatEngine().maintainStatusForPlayerShip(STATUS_KEY1, "graphics/icons/hullsys/temporal_shell.png", "Chronodrive", "Current Timeflow Increase: " + Math.round(Math.min(Limit, velocityBonus * hullsize.get(ship.getHullSize()))) + "%", false);
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
                        + "Cavalry series of ships, projecting a temporal acceleration "
                        + "field that increases in intensity as speed builds up. "
                        + "Larger ships are capable of fitting larger chronodrives, "
                        + "increasing the intensity of the temporal field.",
                opad, h, "temporal acceleration field", "speed", "Larger ships", "intensity");

        //tooltip.addPara("The maximum range is capped, based on the largest slot.", opad);

        tooltip.addSectionHeading("Temporal Field Projection - Cavalry", Alignment.MID, opad);


        tooltip.addPara("Accelerates the ship's timeflow, based on current speed", opad);

        float col1W = 120;
        float colW = (int) ((width - col1W - 4f) / 1f);
        float lastW = colW;

        tooltip.beginTable(Misc.getBasePlayerColor(), Misc.getDarkPlayerColor(), Misc.getBrightPlayerColor(),
                20f, true, true,
                new Object [] {"Ship size", col1W, "Acceleration effect", lastW});

        Color reallyG = g;
        if (Global.CODEX_TOOLTIP_MODE) {
            g = h;
        }

        Color c = null;
        if (hullSize == HullSize.FRIGATE) c = h;
        else c = g;
        tooltip.addRow(Alignment.MID, c, "Frigate",
                Alignment.MID, c, "+" + (float) hullsize.get(HullSize.FRIGATE) + "% every 1 su of speed ");
        if (hullSize == HullSize.DESTROYER) c = h;
        else c = g;
        tooltip.addRow(Alignment.MID, c, "Destroyer",
                Alignment.MID, c, "+" + (float) hullsize.get(HullSize.DESTROYER) + "% every 1 su of speed ");
        if (hullSize == HullSize.CRUISER) c = h;
        else c = g;
        tooltip.addRow(Alignment.MID, c, "Cruiser",
                Alignment.MID, c, "+" + (float) hullsize.get(HullSize.CRUISER) + "% every 1 su of speed ");
        if (hullSize == HullSize.CAPITAL_SHIP) c = h;
        else c = g;
        tooltip.addRow(Alignment.MID, c, "Capital",
                Alignment.MID, c, "+" + (float) hullsize.get(HullSize.CAPITAL_SHIP) + "% every 1 su of speed ");

        tooltip.addTable("", 0, opad);
        tooltip.addPara("The temporal acceleration bonus is capped at 3x for all hull sizes.", opad, h, "3x");

        tooltip.addSectionHeading("Chronodrive Overcharge", Alignment.MID, opad + 7f);

        tooltip.addPara("Increases timeflow bonus limit and gain by 2.5x on system activation.", opad, h, "2.5x");

    }

    public float getTooltipWidth() {
        return 412f;
    }


}
