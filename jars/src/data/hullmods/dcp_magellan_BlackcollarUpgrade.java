package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class dcp_magellan_BlackcollarUpgrade extends BaseHullMod {
   private static final Set<String> BLOCKED_HULLMODS = new HashSet(4);
   public static final float HEALTH_BONUS = 100.0F;
   public static final float TURN_PENALTY = 10.0F;
   public static float DMOD_AVOID_CHANCE = 30.0F;
   public static float RECOIL_BONUS = 15.0F;
   private static final float PROFILE_DECREASE = 25.0F;
   private static final float MALFUNCTION_DECREASE = 50.0F;

   public int getDisplaySortOrder() {
      return 0;
   }

   public int getDisplayCategoryIndex() {
      return 0;
   }

   private String getString(String key) {
      return Global.getSettings().getString("HullMod", "dcp_magellan_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getWeaponHealthBonus().modifyPercent(id, 100.0F);
      stats.getEngineHealthBonus().modifyPercent(id, 50.0F);
      stats.getDynamic().getMod("dmod_acquire_prob_mod").modifyMult(id, 1.0F - DMOD_AVOID_CHANCE * 0.01F);
      stats.getSensorProfile().modifyMult(id, 0.75F);
      stats.getCriticalMalfunctionChance().modifyMult(id, 0.5F);
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float pad2S = 4.0F;
      float padS = 2.0F;
      Color h = Misc.getHighlightColor();
      Color bad = Misc.getNegativeHighlightColor();
      Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
      Color bcr = dcp_magellan_hullmodUtils.getBlackcollarHLColor();
      Color bcrbg = dcp_magellan_hullmodUtils.getBlackcollarBGColor();
      tooltip.addSectionHeading(this.getString("MagellanEngTitle"), bcr, bcrbg, Alignment.MID, pad);
      tooltip.addPara("- " + this.getString("MagellanEngDesc1"), pad, h, new String[]{"100%"});
      tooltip.addPara("- " + this.getString("MagellanEngDesc3"), padS, h, new String[]{"50%"});
      tooltip.addPara("- " + this.getString("MagellanEngDesc4"), padS, h, new String[]{"30%"});
      LabelAPI label = tooltip.addPara("——— " + this.getString("BlackcollarSubtitle") + " ———", bcr, pad2S);
      label.setAlignment(Alignment.MID);
      tooltip.addPara("- " + this.getString("BlackcollarModDesc5"), pad2S, h, new String[]{"15%"});
      tooltip.addPara("- " + this.getString("BlackcollarModDesc6"), padS, h, new String[]{"25%"});
      tooltip.addPara("- " + this.getString("BlackcollarModDesc7"), padS, h, new String[]{"50%"});
      tooltip.addSectionHeading(this.getString("MagellanIncompTitle"), bad, badbg, Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/DCP/icons/tooltip/dcp_hullmod_incompatible.png", 40.0F);
      text.addPara(this.getString("magellanAllIncomp"), padS);
      text.addPara("- Hardened Shields", bad, padS);
      text.addPara("- Armored Weapon Mounts", bad, 0.0F);
      text.addPara("- Converted Hangar", bad, 0.0F);
      if (Global.getSettings().getModManager().isModEnabled("roider")) {
         text.addPara("- Fighter Clamps", bad, 0.0F);
      }

      tooltip.addImageWithText(pad);
   }

   public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
      Iterator var3 = BLOCKED_HULLMODS.iterator();

      while(var3.hasNext()) {
         String tmp = (String)var3.next();
         if (ship.getVariant().getHullMods().contains(tmp)) {
            ship.getVariant().removeMod(tmp);
            DCPBlockedHullmodDisplayScript.showBlocked(ship);
         }
      }

   }

   static {
      BLOCKED_HULLMODS.add("hardenedshieldemitter");
      BLOCKED_HULLMODS.add("armoredweapons");
      BLOCKED_HULLMODS.add("converted_hangar");
      BLOCKED_HULLMODS.add("roider_fighterClamps");
   }
}
