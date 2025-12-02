package io.github.pistonpoek.magicalscepter;

import io.github.pistonpoek.magicalscepter.attack.ItemAttackCallback;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.network.ClientPlayPackets;
import io.github.pistonpoek.magicalscepter.render.entity.GuardianBoltEntityRenderer;
import io.github.pistonpoek.magicalscepter.render.entity.SorcererEntityRenderer;
import io.github.pistonpoek.magicalscepter.render.entity.SpellWitherSkullEntityRenderer;
import io.github.pistonpoek.magicalscepter.render.entity.model.GuardianBoltEntityModel;
import io.github.pistonpoek.magicalscepter.render.entity.model.ModEntityModelLayers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;

@Environment(EnvType.CLIENT)
public class MagicalScepterClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayPackets.init();

        EntityRendererRegistry.register(ModEntityType.SORCERER, SorcererEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SORCERER,
                IllagerEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntityType.GUARDIAN_BOLT, GuardianBoltEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.GUARDIAN_BOLT,
                GuardianBoltEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntityType.SPELL_FIRE_CHARGE, context -> new FlyingItemEntityRenderer<>(context, 0.75F, true));
        EntityRendererRegistry.register(ModEntityType.SPELL_FIREBALL, context -> new FlyingItemEntityRenderer<>(context, 3.0F, true));
        EntityRendererRegistry.register(ModEntityType.SPELL_WITHER_SKULL, SpellWitherSkullEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.WITHER_SKULL,
                SpellWitherSkullEntityRenderer::getTexturedModelData);

        ClientPreAttackCallback.EVENT.register(new ItemAttackCallback());
    }
}