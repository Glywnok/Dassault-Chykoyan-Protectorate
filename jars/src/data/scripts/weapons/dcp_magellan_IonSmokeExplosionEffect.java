package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ProximityExplosionEffect;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_IonSmokeExplosionEffect implements ProximityExplosionEffect {
   private static final float FX_DURATION = 2.0F;
   private static final float NEBULA_RAMPUP = 0.1F;
   private static final Color BURST_COLOR = new Color(60, 90, 120, 75);
   private static final Color SMOKE_COLOR = new Color(120, 180, 210, 125);
   private static final float NEBULA_SIZE = 20.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 15.0F;
   private static final float SMOKE_RADIUS = 25.0F;
   private static final int SMOKE_COUNT = 3;
   private static final int FLARE_COUNT = 1;

   public void onExplosion(DamagingProjectileAPI explosion, DamagingProjectileAPI originalProjectile) {
      CombatEngineAPI engine = Global.getCombatEngine();
      Vector2f v_proj = new Vector2f(originalProjectile.getVelocity());
      Vector2f loc_boom = new Vector2f(explosion.getLocation());
      Vector2f v_boom = new Vector2f(explosion.getVelocity());
      Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_boom, new Vector2f()).scale(0.1F);
      engine.addNebulaSmokeParticle(loc_boom, v_comp, NEBULA_SIZE, 15.0F, 0.1F, 0.3F, 2.0F, SMOKE_COLOR);
      engine.addNebulaParticle(loc_boom, v_comp, NEBULA_SIZE, 15.0F, 0.1F, 0.6F, 2.0F, SMOKE_COLOR);

      int i;
      for(i = 0; i <= 2; ++i) {
         Vector2f random_point = new Vector2f(MathUtils.getRandomPointInCircle(loc_boom, 25.0F));
         engine.spawnExplosion(random_point, v_comp, BURST_COLOR, NEBULA_SIZE * 2.0F, 0.1F);
         engine.addNebulaSmokeParticle(random_point, v_comp, NEBULA_SIZE / 2.0F, 15.0F, 0.1F, 0.3F, 2.0F, SMOKE_COLOR);
      }

      for(i = 0; i <= 0; ++i) {
         float angle = (float)(Math.random() * 360.0D);
         engine.spawnProjectile(originalProjectile.getSource(), originalProjectile.getWeapon(), "dcp_magellan_microflare_brief", loc_boom, angle, v_comp);
      }

   }
}
