package io.github.pistonpoek.magicalscepter.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.cast.SpellCast;
import io.github.pistonpoek.magicalscepter.world.event.ModGameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;

public record Spell(List<SpellCast> casts, int cooldown, Component description) {
    public static final Codec<Spell> BASE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            SpellCast.CODEC.listOf().fieldOf("casts").forGetter(Spell::casts),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("cooldown").forGetter(Spell::cooldown),
                            ComponentSerialization.CODEC.fieldOf("description").forGetter(Spell::description)
                    )
                    .apply(instance, Spell::new)
    );
    public static final Codec<Spell> NETWORK_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("cooldown").forGetter(Spell::cooldown),
                    ComponentSerialization.CODEC.fieldOf("description").forGetter(Spell::description)
            ).apply(instance, Spell::createClientSpell)
    );

    private static Spell createClientSpell(int cooldown, Component description) {
        return new Spell(List.of(), cooldown, description);
    }

    public static final Codec<Holder<Spell>> ENTRY_CODEC = RegistryFixedCodec.create(ModRegistryKeys.SPELL);
    public static final Codec<Spell> CODEC = Codec.withAlternative(BASE_CODEC, ENTRY_CODEC, Holder::value);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Spell>> ENTRY_PACKET_CODEC =
            ByteBufCodecs.holderRegistry(ModRegistryKeys.SPELL);
    public static final StreamCodec<RegistryFriendlyByteBuf, Spell> PACKET_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    /**
     * Cast this spell for a specific living entity.
     *
     * @param caster Living entity to cast the spell for.
     */
    public void castSpell(@NotNull LivingEntity caster) {
        if (caster.level().isClientSide()) {
            return;
        }

        for (SpellCast cast : casts) {
            cast.invoke(caster);
        }

        caster.gameEvent(ModGameEvent.SPELL_CAST);
    }

    public int getCooldown() {
        return cooldown;
    }

    public String toString() {
        return "Spell " + this.description.getString();
    }

    public static MutableComponent getName(Holder<Spell> spell) {
        return spell.value().description.copy();
    }

    public static Spell.Builder builder(int cooldown, Component description) {
        return new Spell.Builder(cooldown, description);
    }

    public static class Builder {
        private final int cooldown;
        private final Component description;
        private final List<SpellCast> casts = new ArrayList<>();

        public Builder(int cooldown, Component description) {
            this.cooldown = cooldown;
            this.description = description;
        }

        public Spell.Builder addCast(SpellCast.Builder cast) {
            casts.add(cast.build());
            return this;
        }

        public Spell build() {
            return new Spell(casts, cooldown, description);
        }
    }
}
