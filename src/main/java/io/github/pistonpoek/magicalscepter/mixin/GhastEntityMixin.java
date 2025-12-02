package io.github.pistonpoek.magicalscepter.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.pistonpoek.magicalscepter.entity.projectile.SpellFireballEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.GhastEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GhastEntity.class)
public class GhastEntityMixin {
    @ModifyReturnValue(method = "isFireballFromPlayer", at = {@At("RETURN")})
    private static boolean isFireballFromPlayer(boolean original, DamageSource damageSource) {
        return original || damageSource.getSource() instanceof SpellFireballEntity;
    }
}
