package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import org.slf4j.Logger;

import java.util.Optional;

public record RunFunctionSpellEffect(Identifier function) implements SpellEffect {
    public static final MapCodec<RunFunctionSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(Identifier.CODEC.fieldOf("function").forGetter(RunFunctionSpellEffect::function))
                    .apply(instance, RunFunctionSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        MinecraftServer minecraftServer = context.getWorld().getServer();
        CommandFunctionManager commandFunctionManager = minecraftServer.getCommandFunctionManager();
        Optional<CommandFunction<ServerCommandSource>> optional = commandFunctionManager.getFunction(this.function);

        if (optional.isEmpty()) {
            MagicalScepter.LOGGER.error("Spell run_function effect failed for non-existent function {}", this.function);
            return;
        }

        ServerCommandSource serverCommandSource = minecraftServer.getCommandSource()
                .withLevel(2)
                .withSilent()
                .withEntity(context.caster())
                .withWorld(context.getWorld())
                .withPosition(context.position())
                .withRotation(new Vec2f(context.pitch(), context.yaw()));
        commandFunctionManager.execute(optional.get(), serverCommandSource);
    }

    @Override
    public MapCodec<RunFunctionSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}
