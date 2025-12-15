package io.github.pistonpoek.magicalscepter.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.pistonpoek.magicalscepter.entity.spell.SpellFireballEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.GhastEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GhastEntity.class)
public class GhastEntityMixin {
    /**
     * Extend the is fireball from player method with fireball spell projectile.
     *
     * @param original Truth assignment, if originally the damage source is a fireball from a player.
     * @param damageSource Damage source to check with.
     * @return Truth assignment, if the damage source can be considered a fireball from a player.
     */
    @ModifyReturnValue(method = "isFireballFromPlayer", at = {@At("RETURN")})
    private static boolean isFireballFromPlayer(boolean original, DamageSource damageSource) {
        return original || damageSource.getSource() instanceof SpellFireballEntity;
    }
}
