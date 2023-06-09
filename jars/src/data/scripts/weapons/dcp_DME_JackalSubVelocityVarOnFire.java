package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.MathUtils;

public class dcp_DME_JackalSubVelocityVarOnFire implements OnFireEffectPlugin {
   public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
      projectile.getVelocity().scale(MathUtils.getRandomNumberInRange(0.6F, 1.0F));
   }
}
