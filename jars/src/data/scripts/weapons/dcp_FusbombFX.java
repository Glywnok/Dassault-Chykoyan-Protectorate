package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DMEUtils;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author HarmfulMechanic
 * Based on scripts by Trylobot, Uomoz and Cycerin
 */
public class dcp_FusbombFX implements EveryFrameWeaponEffectPlugin {
    
    private static final float FIRE_DURATION = 0.3f; // Firing cycle time
    private static final Color FLASH_COLOR = new Color(240,30,90,255);
    private static final float OFFSET = 2f;
    private static final Color PARTICLE_COLOR = new Color(240,30,90,155); // Particle color

    private float elapsed = 0f;

    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused()) {
            return;
        }

        if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            // explosion (frame 0 only)
            if (elapsed <= 0f) {
                Vector2f explosion_offset = DMEUtils.translate_polar(weapon_location, OFFSET +
                                                                          ((0.05f * 100f) - 2f), weapon.getCurrAngle());
                engine.spawnExplosion(explosion_offset, ship.getVelocity(), FLASH_COLOR, 50f, 0.25f);
            }

            elapsed += amount;

            // particles
            Vector2f particle_offset = DMEUtils.translate_polar(weapon_location, OFFSET, weapon.getCurrAngle());
            float size, speed, angle;
            Vector2f velocity;
            // more particles to start with, fewer later on
            int particle_count_this_frame = (int) (20f * (FIRE_DURATION - elapsed));
            for (int x = 0; x < particle_count_this_frame; x++) {
                size = DMEUtils.get_random(3f, 6f);
                speed = DMEUtils.get_random(50f, 150f);
                angle = weapon.getCurrAngle() + DMEUtils.get_random(-5f, 5f);
                velocity = DMEUtils.translate_polar(ship.getVelocity(), speed, angle);
                engine.addHitParticle(particle_offset, velocity, size, 1.5f, 0.6f, PARTICLE_COLOR);
            }
        } else {
            elapsed = 0f;
        }
    }
}
