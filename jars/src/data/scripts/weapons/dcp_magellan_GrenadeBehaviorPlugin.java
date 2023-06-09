package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;

public class dcp_magellan_GrenadeBehaviorPlugin implements OnFireEffectPlugin {
   public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
      float speedMult = 0.5F + 0.5F * (float)Math.random();
      projectile.getVelocity().scale(speedMult);
      float angVel = (float)((double)Math.signum((float)Math.random() - 0.5F) * (0.5D + Math.random()) * 720.0D);
      projectile.setAngularVelocity(angVel);
      if (projectile instanceof MissileAPI) {
         MissileAPI missile = (MissileAPI)projectile;
         float flightTimeMult = 0.5F + 0.5F * (float)Math.random();
         missile.setMaxFlightTime(missile.getMaxFlightTime() * flightTimeMult);
      }

      if (weapon != null) {
         float delay = 0.1F + 0.1F * (float)Math.random();
         weapon.setRefireDelay(delay);
      }

   }
}
