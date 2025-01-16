package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import com.mojang.serialization.DataResult;
import io.github.pistonpoek.magicalscepter.spell.cast.SpellCast;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import io.github.pistonpoek.magicalscepter.spell.target.AbsoluteTargetSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;

import java.util.Optional;

public record ScheduledSpellCasting(SpellCast spellCast, AbsoluteTargetSource caster, SpellContextSource context) {

    public ScheduledSpellCasting(SpellCasting spellCasting) {
        this(spellCasting.getSpellCast(),
                new AbsoluteTargetSource(spellCasting.getCaster().getUuid()),
                spellCasting.getContextSource());
    }

    public Optional<SpellCasting> load(MinecraftServer server) {
        Optional<Entity> loadedEntity = caster().load(server);
        if (loadedEntity.isEmpty()) {
            return Optional.empty();
        }
        if (loadedEntity.get() instanceof LivingEntity loadedCaster) {
            return Optional.of(new SpellCasting(spellCast(), loadedCaster, context()));
        }
        return Optional.empty();
    }

    public static ScheduledSpellCasting fromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup registries) {
        SpellCast spellCast = SpellCast.CODEC
                .decode(RegistryOps.of(NbtOps.INSTANCE, registries), nbtCompound.get("SpellCast")).getOrThrow().getFirst();
        AbsoluteTargetSource targetSource = AbsoluteTargetSource.MAP_CODEC.codec()
                .decode(RegistryOps.of(NbtOps.INSTANCE, registries), nbtCompound.get("Caster")).getOrThrow().getFirst();
        SpellContextSource contextSource = SpellContextSource.CODEC
                .decode(RegistryOps.of(NbtOps.INSTANCE, registries), nbtCompound.get("Context")).getOrThrow().getFirst();
        return new ScheduledSpellCasting(spellCast, targetSource, contextSource);
    }

    public NbtCompound writeNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup registries) {
        final DataResult<NbtElement> encodedSpellCast = SpellCast.CODEC
                .encodeStart(RegistryOps.of(NbtOps.INSTANCE, registries), spellCast());
        nbtCompound.put("SpellCast", encodedSpellCast.getOrThrow());
        final DataResult<NbtElement> encodedCaster = AbsoluteTargetSource.MAP_CODEC.codec()
                .encodeStart(RegistryOps.of(NbtOps.INSTANCE, registries), caster());
        nbtCompound.put("Caster", encodedCaster.getOrThrow());
        final DataResult<NbtElement> encodedContextSource = SpellContextSource.CODEC
                .encodeStart(RegistryOps.of(NbtOps.INSTANCE, registries), context());
        nbtCompound.put("Context", encodedContextSource.getOrThrow());
        return nbtCompound;
    }
}
