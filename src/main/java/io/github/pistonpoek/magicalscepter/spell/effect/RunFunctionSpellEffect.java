package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.phys.Vec2;
import java.util.Optional;

public record RunFunctionSpellEffect(Identifier function) implements SpellEffect {
    public static final MapCodec<RunFunctionSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(Identifier.CODEC.fieldOf("function").forGetter(RunFunctionSpellEffect::function))
                    .apply(instance, RunFunctionSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        MinecraftServer minecraftServer = context.getWorld().getServer();
        ServerFunctionManager commandFunctionManager = minecraftServer.getFunctions();
        Optional<CommandFunction<CommandSourceStack>> optional = commandFunctionManager.get(this.function);

        if (optional.isEmpty()) {
            MagicalScepter.LOGGER.error("Spell function effect failed for non-existent function {}", this.function);
            return;
        }

        CommandSourceStack serverCommandSource = minecraftServer.createCommandSourceStack()
                .withPermission(LevelBasedPermissionSet.GAMEMASTER)
                .withSuppressedOutput()
                .withEntity(context.target())
                .withLevel(context.getWorld())
                .withPosition(context.position())
                .withRotation(new Vec2(context.pitch(), context.yaw()));
        commandFunctionManager.execute(optional.get(), serverCommandSource);
    }

    @Override
    public MapCodec<RunFunctionSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}
