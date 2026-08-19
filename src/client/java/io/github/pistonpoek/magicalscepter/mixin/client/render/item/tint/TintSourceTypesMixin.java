package io.github.pistonpoek.magicalscepter.mixin.client.render.item.tint;

import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.scepter.ScepterTintSource;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ItemTintSources.class)
public class TintSourceTypesMixin {
    @Shadow
    @Final
    public static ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends ItemTintSource>> ID_MAPPER;

    /**
     * Add mod tint sources to the {@link #ID_MAPPER} field during bootstrap.
     *
     * @param callbackInfo Callback info of the method injection.
     */
    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void bootstrap(CallbackInfo callbackInfo) {
        ID_MAPPER.put(ModIdentifier.of("scepter"), ScepterTintSource.CODEC);
    }
}
