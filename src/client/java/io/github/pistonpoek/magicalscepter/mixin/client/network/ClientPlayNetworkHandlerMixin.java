package io.github.pistonpoek.magicalscepter.mixin.client.network;

import io.github.pistonpoek.magicalscepter.entity.projectile.GuardianBoltEntity;
import io.github.pistonpoek.magicalscepter.sound.GuardianBoltSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler {

    protected ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    /**
     * Play spawn sounds for mod entities.
     *
     * @param entity Entity to play spawn sound for.
     * @param callbackInfo Callback info of the method injection.
     */
    @Inject(method = "playSpawnSound", at = @At("TAIL"))
    private void playSpawnSound(Entity entity, CallbackInfo callbackInfo) {
        if (entity instanceof GuardianBoltEntity guardianBoltEntity) {
            this.client.getSoundManager().play(new GuardianBoltSoundInstance(guardianBoltEntity));
        }
    }
}
