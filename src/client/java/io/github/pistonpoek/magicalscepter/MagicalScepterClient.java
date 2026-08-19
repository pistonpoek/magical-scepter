package io.github.pistonpoek.magicalscepter;

import io.github.pistonpoek.magicalscepter.attack.ItemAttackCallback;
import io.github.pistonpoek.magicalscepter.entity.ModEntityTypes;
import io.github.pistonpoek.magicalscepter.network.ClientPlayPackets;
import io.github.pistonpoek.magicalscepter.render.entity.SorcererEntityRenderer;
import io.github.pistonpoek.magicalscepter.render.entity.SpellDragonFireballRenderer;
import io.github.pistonpoek.magicalscepter.render.entity.SpellGuardianBeamRenderer;
import io.github.pistonpoek.magicalscepter.render.entity.SpellWitherSkullEntityRenderer;
import io.github.pistonpoek.magicalscepter.render.entity.model.ModEntityModelLayers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

@Environment(EnvType.CLIENT)
public class MagicalScepterClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayPackets.init();

        EntityRenderers.register(ModEntityTypes.SORCERER, SorcererEntityRenderer::new);
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SORCERER,
                IllagerModel::createBodyLayer);

        EntityRenderers.register(ModEntityTypes.SPELL_GUARDIAN_BEAM, SpellGuardianBeamRenderer::new);
        EntityRenderers.register(ModEntityTypes.SPELL_DRAGON_FIREBALL, SpellDragonFireballRenderer::new);
        EntityRenderers.register(ModEntityTypes.SPELL_FIRE_CHARGE, context -> new ThrownItemRenderer<>(context, 0.75F, true));
        EntityRenderers.register(ModEntityTypes.SPELL_FIREBALL, context -> new ThrownItemRenderer<>(context, 3.0F, true));
        EntityRenderers.register(ModEntityTypes.SPELL_WITHER_SKULL, SpellWitherSkullEntityRenderer::new);
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.WITHER_SKULL,
                SpellWitherSkullEntityRenderer::getTexturedModelData);

        ClientPreAttackCallback.EVENT.register(new ItemAttackCallback());
    }
}