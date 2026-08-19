package io.github.pistonpoek.magicalscepter.mixin;

import com.google.common.collect.BiMap;
import io.github.pistonpoek.magicalscepter.loot.context.ModLootContextTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootContextParamSets.class)
public class LootContextTypesMixin {
    @Shadow
    @Final
    private static BiMap<Identifier, ContextKeySet> REGISTRY;

    /**
     * Register mod loot context types during initialization.
     *
     * @param callbackInfo Callback info to return values to initialization.
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void registerModLootContextTypes(CallbackInfo callbackInfo) {
        REGISTRY.putAll(ModLootContextTypes.getLootContextMap());
    }
}
