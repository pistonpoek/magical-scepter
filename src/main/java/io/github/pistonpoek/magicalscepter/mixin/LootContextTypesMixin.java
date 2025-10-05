package io.github.pistonpoek.magicalscepter.mixin;

import com.google.common.collect.BiMap;
import io.github.pistonpoek.magicalscepter.loot.context.ModLootContextTypes;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootContextTypes.class)
public class LootContextTypesMixin {
    @Shadow
    @Final
    private static BiMap<Identifier, ContextType> MAP;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void registerModLootContextTypes(CallbackInfo callbackInfo) {
        MAP.putAll(ModLootContextTypes.getLootContextMap());
    }
}
