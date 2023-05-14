package data.hullmods;

import java.util.HashSet;
import java.util.Iterator;
import com.fs.starfarer.api.ui.LabelAPI;
import java.awt.Color;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.Global;
import java.util.Set;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_BlackcollarUpgrade extends BaseHullMod
{
    private static final Set<String> BLOCKED_HULLMODS;
    public static final float HEALTH_BONUS = 100.0f;
    public static final float TURN_PENALTY = 10.0f;
    public static float DMOD_AVOID_CHANCE;
    public static float RECOIL_BONUS;
    private static final float PROFILE_DECREASE = 25.0f;
    private static final float MALFUNCTION_DECREASE = 50.0f;

    public int getDisplaySortOrder() {
        return 0;
    }

    public int getDisplayCategoryIndex() {
        return 0;
    }

    private String getString(final String key) {
        return Global.getSettings().getString("Hullmod", "magellan_" + key);
    }

    public void applyEffectsBeforeShipCreation(final ShipAPI.HullSize hullSize, final MutableShipStatsAPI stats, final String id) {
        stats.getWeaponHealthBonus().modifyPercent(id, 100.0f);
        stats.getEngineHealthBonus().modifyPercent(id, 50.0f);
        stats.getDynamic().getMod("dmod_acquire_prob_mod").modifyMult(id, 1.0f - dcp_magellan_BlackcollarUpgrade.DMOD_AVOID_CHANCE * 0.01f);
        stats.getSensorProfile().modifyMult(id, 0.75f);
        stats.getCriticalMalfunctionChance().modifyMult(id, 0.5f);
    }

    public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
        final float pad = 10.0f;
        final float pad2S = 4.0f;
        final float padS = 2.0f;
        final Color h = Misc.getHighlightColor();
        final Color bad = Misc.getNegativeHighlightColor();
        final Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
        final Color bcr = dcp_magellan_hullmodUtils.getBlackcollarHLColor();
        final Color bcrbg = dcp_magellan_hullmodUtils.getBlackcollarBGColor();
        tooltip.addSectionHeading(this.getString("MagellanEngTitle"), bcr, bcrbg, Alignment.MID, pad);
        tooltip.addPara("- " + this.getString("MagellanEngDesc1"), pad, h, new String[] { "100%" });
        tooltip.addPara("- " + this.getString("MagellanEngDesc3"), padS, h, new String[] { "50%" });
        tooltip.addPara("- " + this.getString("MagellanEngDesc4"), padS, h, new String[] { "30%" });
        final LabelAPI label = tooltip.addPara("\u2014\u2014\u2014 " + this.getString("BlackcollarSubtitle") + " \u2014\u2014\u2014", bcr, pad2S);
        label.setAlignment(Alignment.MID);
        tooltip.addPara("- " + this.getString("BlackcollarModDesc5"), pad2S, h, new String[] { "15%" });
        tooltip.addPara("- " + this.getString("BlackcollarModDesc6"), padS, h, new String[] { "25%" });
        tooltip.addPara("- " + this.getString("BlackcollarModDesc7"), padS, h, new String[] { "50%" });
        tooltip.addSectionHeading(this.getString("MagellanIncompTitle"), bad, badbg, Alignment.MID, pad);
        final TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/hullmod_incompatible.png", 40.0f);
        text.addPara(this.getString("MagellanAllIncomp"), padS);
        text.addPara("- Hardened Shields", bad, padS);
        text.addPara("- Armored Weapon Mounts", bad, 0.0f);
        text.addPara("- Converted Hangar", bad, 0.0f);
        if (Global.getSettings().getModManager().isModEnabled("roider")) {
            text.addPara("- Fighter Clamps", bad, 0.0f);
        }
        tooltip.addImageWithText(pad);
    }

    public void applyEffectsAfterShipCreation(final ShipAPI ship, final String id) {
        for (final String tmp : dcp_magellan_BlackcollarUpgrade.BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                ship.getVariant().removeMod(tmp);
                DCPBlockedHullmodDisplayScript.showBlocked(ship);
            }
        }
    }

    static {
        BLOCKED_HULLMODS = new HashSet<String>(4);
        dcp_magellan_BlackcollarUpgrade.DMOD_AVOID_CHANCE = 30.0f;
        dcp_magellan_BlackcollarUpgrade.RECOIL_BONUS = 15.0f;
        dcp_magellan_BlackcollarUpgrade.BLOCKED_HULLMODS.add("hardenedshieldemitter");
        dcp_magellan_BlackcollarUpgrade.BLOCKED_HULLMODS.add("armoredweapons");
        dcp_magellan_BlackcollarUpgrade.BLOCKED_HULLMODS.add("converted_hangar");
        dcp_magellan_BlackcollarUpgrade.BLOCKED_HULLMODS.add("roider_fighterClamps");
    }
}