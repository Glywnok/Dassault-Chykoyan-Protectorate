package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.GenericPluginManagerAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.CampaignPlugin.PickPriority;
import com.fs.starfarer.api.campaign.listeners.ListenerManagerAPI;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import data.scripts.world.ISTLGen;
import data.scripts.world.MagellanGen;
import exerelin.campaign.SectorManager;
import java.io.IOException;
import org.apache.log4j.Level;
import org.dark.shaders.light.LightData;
import org.dark.shaders.util.ShaderLib;
import org.dark.shaders.util.TextureData;
import org.json.JSONException;
import org.json.JSONObject;
import com.fs.starfarer.combat.entities.ship.O0OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO;

/*
package data;

import com.fs.starfarer.api.BaseModPlugin;

public class CrashModPlugin extends BaseModPlugin {
   public void onApplicationLoad() {
      throw new RuntimeException("What the fuck did you expect?");
   }
}

 */

public class DCPModPlugin extends BaseModPlugin {
   private static final String DME_SETTINGS = "DCPsettings.ini";
   public static boolean RANDOMBREAKERS;
   public static final String ITANO_MISSILE_ID = "dcp_DME_AMM";
   public static final String GRAD_MISSILE_ID = "dcp_DME_TBM";
   public static final boolean isExerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");

   private static void initDME() {
      (new ISTLGen()).generate(Global.getSector());
   }
   private static void initMagellan(){(new MagellanGen()).generate(Global.getSector());}

   private static void initBreakers() {
      (new ISTLGen()).randombreakers(Global.getSector());
   }

   public void onNewGame() {
      if (!isExerelin || SectorManager.getCorvusMode()) {
         initDME();
         initMagellan();
      }

      if (!RANDOMBREAKERS) {
         initBreakers();
      }
   }

   public void onGameLoad(boolean newGame) {
      Global.getSector().addTransientScript(new data.hullmods.DCPBlockedHullmodDisplayScript());
      this.addBreakerBeaconListener();
   }

   private static void loadDMESettings() throws IOException, JSONException {
      JSONObject setting = Global.getSettings().loadJSON("DCPsettings.ini");
      RANDOMBREAKERS = setting.getBoolean("noRandomBreakers");
   }

   protected void addBreakerBeaconListener() {
      SectorAPI sector = Global.getSector();
      GenericPluginManagerAPI plugins = sector.getGenericPlugins();
      ListenerManagerAPI listeners = Global.getSector().getListenerManager();
      if (!listeners.hasListenerOfClass(data.scripts.campaign.intel.dcp_DME_DiscoverEntityListener.class)) {
         listeners.addListener(new data.scripts.campaign.intel.dcp_DME_DiscoverEntityListener());
      }

   }

   public void onApplicationLoad() {

      boolean hasGraphicsLib = Global.getSettings().getModManager().isModEnabled("shaderLib");
      if (hasGraphicsLib) {
         ShaderLib.init();
         LightData.readLightDataCSV("data/lights/dcp_light_data.csv");
         TextureData.readTextureDataCSV("data/lights/dcp_texture_data.csv");
      }

      boolean hasshit = Global.getSettings().getModManager().isModEnabled("lyr_ehm");
      boolean DME = Global.getSettings().getModManager().isModEnabled("istl_dassaultmikoyan");
      boolean NGO = Global.getSettings().getModManager().isModEnabled("TheDragn_tying_nooses");
      boolean MAG = Global.getSettings().getModManager().isModEnabled("mag_protect");
      //boolean BB = Global.getSettings().getModManager().isModEnabled("");
      boolean DF = Global.getSettings().getModManager().isModEnabled("hm_flagpack");

      if (DME) {
         throw new RuntimeException("DCP works as DME replacement, please disable original DME, or the bootleg of it.");
      }

      if (MAG) {
         throw new RuntimeException("DCP works as Magellan Protectorate replacement, please disable original MP, or the bootleg of it.");
      }
      /*
      if (DF) {
         throw new RuntimeException("DCP works as Deluxe Player Flags replacement, please disable original DPF, or the bootleg of it.");
      }

      if (NGO) {
         throw new RuntimeException("No original NGO bitch. Disable it.");
      }

      if (BB) {
         throw new RuntimeException("keepin it here");
      }


      if (hasshit) {
         throw new RuntimeException("Fuck you");
      }
       */
      try {
         loadDMESettings();
      } catch (JSONException | IOException var3) {
         Global.getLogger(DCPModPlugin.class).log(Level.ERROR, "DCPsettings.ini loading failed!" + var3.getMessage());
      }

   }

   public PluginPick<MissileAIPlugin> pickMissileAI(MissileAPI missile, ShipAPI launchingShip) {
      String var3 = missile.getProjectileSpecId();
      byte var4 = -1;
      switch(var3.hashCode()) {
      case 718816516:
         if (var3.equals("dcp_DME_AMM")) {
            var4 = 0;
         }
         break;
      case 718834434:
         if (var3.equals("dcp_DME_TBM")) {
            var4 = 1;
         }
      }

      switch(var4) {
      case 0:
         return new PluginPick(new data.scripts.weapons.ai.dcp_DME_Itano_AMM_AI(missile, launchingShip), PickPriority.MOD_SET);
      case 1:
         return new PluginPick(new data.scripts.weapons.ai.dcp_DME_GradMissileAI(missile, launchingShip), PickPriority.MOD_SET);
      default:
         return null;
      }
   }
}
