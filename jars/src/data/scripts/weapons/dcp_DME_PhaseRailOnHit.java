package data.scripts.weapons;

import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_PhaseRailOnHit implements OnHitEffectPlugin {
   public static float DAMAGE = 400.0F;
   public static float DAMAGE_MAXRADIUS = 180.0F;
   public static float DAMAGE_MINRADIUS = 90.0F;
   private static final float FLUXLOWER_MULT = 0.2F;
   private static final Color BOOM_COLOR = new Color(75, 125, 200, 215);
   private static final Color NEBULA_COLOR = new Color(125, 175, 235, 255);
   private static final float NEBULA_SIZE = 18.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 30.0F;
   private static final float NEBULA_DUR = 1.6F;
   private static final float NEBULA_RAMPUP = 0.2F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      new Vector2f(target.getVelocity());
      if (target instanceof ShipAPI) {
         engine.spawnDamagingExplosion(this.createExplosionSpec(), projectile.getSource(), point);
         engine.addNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 30.0F, 0.2F, 0.3F, 1.6F, BOOM_COLOR, true);
      }

      engine.addSwirlyNebulaParticle(point, target.getVelocity(), NEBULA_SIZE * 0.75F, 22.5F, 0.1F, 0.2F, 1.2F, NEBULA_COLOR, true);
      if (!shieldHit && !projectile.isFading() && target instanceof ShipAPI) {
         ShipAPI targetship = (ShipAPI)target;
         float fluxmult = projectile.getDamageAmount() * 0.2F;
         targetship.getFluxTracker().decreaseFlux(fluxmult);
      }

   }

   public DamagingExplosionSpec createExplosionSpec() {
      DamagingExplosionSpec spec = new DamagingExplosionSpec(0.3F, DAMAGE_MAXRADIUS, DAMAGE_MINRADIUS, DAMAGE, DAMAGE / 4.0F, CollisionClass.PROJECTILE_FF, CollisionClass.PROJECTILE_FIGHTER, 7.0F, 3.0F, 1.0F, 60, NEBULA_COLOR, BOOM_COLOR);
      spec.setDamageType(DamageType.FRAGMENTATION);
      spec.setUseDetailedExplosion(false);
      spec.setSoundSetId("istl_shockarty_crit");
      return spec;
   }
}