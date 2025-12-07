package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.SpellCast;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import io.github.pistonpoek.magicalscepter.spell.target.AbsoluteTargetSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;

import java.util.Optional;

/**
 * TODO
 *
 * @param spellCast
 * @param caster
 * @param context
 */
public record ScheduledSpellCasting(SpellCast spellCast, AbsoluteTargetSource caster, SpellContextSource context) {
    public static final Codec<ScheduledSpellCasting> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    SpellCast.CODEC.fieldOf("spell_cast").forGetter(ScheduledSpellCasting::spellCast),
                    AbsoluteTargetSource.MAP_CODEC.fieldOf("caster").forGetter(ScheduledSpellCasting::caster),
                    SpellContextSource.CODEC.fieldOf("context").forGetter(ScheduledSpellCasting::context)
            ).apply(instance, ScheduledSpellCasting::new)
    );

    /**
     * TODO
     *
     * @param spellCasting
     */
    public ScheduledSpellCasting(SpellCasting spellCasting) {
        this(spellCasting.getSpellCast(),
                new AbsoluteTargetSource(spellCasting.getCaster().getUuid()),
                spellCasting.getContextSource());
    }

    /**
     * TODO
     *
     * @param server
     * @return
     */
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
}
