package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_FlashfireOnHit implements OnHitEffectPlugin {
   private static final float SPARK_CHANCE = 0.6F;
   private static final float FLUXRAISE_MULT = 1.0F;
   private static final Color EXPLOSION_COLOR = new Color(175, 175, 225, 200);
   private static final float NEBULA_SIZE = 12.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 16.0F;
   private static final float NEBULA_DUR = 1.8F;
   private static final float NEBULA_RAMPUP = 0.1F;
   private static final Color PARTICLE_COLOR = new Color(155, 225, 255, 255);
   private static final Color GLOW_COLOR = new Color(85, 85, 100, 25);
   private static final float PARTICLE_SIZE = 5.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 1.8F;
   private static final int PARTICLE_COUNT = 2;
   private static final String SFX = "dcp_magellan_kineticspall_crit";
   private static final float CONE_ANGLE = 45.0F;
   private static final float VEL_MIN = 0.2F;
   private static final float VEL_MAX = 0.4F;
   private static final float A_2 = 22.5F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f loc_target = new Vector2f(target.getLocation());
      Vector2f v_target = new Vector2f(target.getVelocity());
      Vector2f v_proj = new Vector2f(projectile.getVelocity());
      Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.1F);
      if (!shieldHit && !projectile.isFading() && target instanceof ShipAPI) {
         float fluxmult = projectile.getDamageAmount() * 1.0F;
         engine.addSwirlyNebulaParticle(point, v_comp, NEBULA_SIZE, 16.0F, 0.1F, 0.3F, 1.8F, EXPLOSION_COLOR, true);
         if (Math.random() <= 0.6000000238418579D) {
            float speed = projectile.getVelocity().length();
            float facing = projectile.getFacing();

            for(int i = 1; i <= 2; ++i) {
               float angle = MathUtils.getRandomNumberInRange(facing - 22.5F, facing + 22.5F);
               float vel = MathUtils.getRandomNumberInRange(speed * -0.2F, speed * -0.4F);
               Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
               engine.addHitParticle(point, vector, 5.0F, 255.0F, 1.8F, PARTICLE_COLOR);
               engine.addHitParticle(point, vector, 20.0F, 255.0F, 1.3499999F, GLOW_COLOR);
            }
         }

         ShipAPI targetship = (ShipAPI)target;
         targetship.getFluxTracker().increaseFlux(fluxmult, false);
      }

      Global.getSoundPlayer().playSound("dcp_magellan_kineticspall_crit", 1.0F, 1.0F, loc_target, v_comp);
   }
}
