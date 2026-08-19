package io.github.pistonpoek.magicalscepter.mixin.client.network;

import io.github.pistonpoek.magicalscepter.entity.spell.SpellGuardianBeamEntity;
import io.github.pistonpoek.magicalscepter.sound.SpellGuardianBeamSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPacketListener.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonPacketListenerImpl {

    protected ClientPlayNetworkHandlerMixin(Minecraft client, Connection connection, CommonListenerCookie connectionState) {
        super(client, connection, connectionState);
    }

    /**
     * Play spawn sounds for mod entities.
     *
     * @param entity       Entity to play spawn sound for.
     * @param callbackInfo Callback info of the method injection.
     */
    @Inject(method = "postAddEntitySoundInstance", at = @At("TAIL"))
    private void playSpawnSound(Entity entity, CallbackInfo callbackInfo) {
        if (entity instanceof SpellGuardianBeamEntity spellGuardianBeamEntity) {
            this.minecraft.getSoundManager().play(new SpellGuardianBeamSoundInstance(spellGuardianBeamEntity));
        }
    }
}
