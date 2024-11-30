package io.github.pistonpoek.magicalscepter.mixin.client.render.item.tint;

import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import io.github.pistonpoek.magicalscepter.scepter.ScepterTintSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(TintSourceTypes.class)
public class TintSourceTypesMixin {
    @Shadow
    @Final
    public static Codecs.IdMapper<Identifier, MapCodec<? extends TintSource>> ID_MAPPER;

    @Inject(method="bootstrap", at=@At("TAIL"))
    private static void bootstrap(CallbackInfo callbackInfo) {
        ID_MAPPER.put(ModIdentifier.of("scepter"), ScepterTintSource.CODEC);
    }
}
