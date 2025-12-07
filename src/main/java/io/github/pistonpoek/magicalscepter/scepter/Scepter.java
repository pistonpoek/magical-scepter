package io.github.pistonpoek.magicalscepter.scepter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import net.minecraft.loot.context.LootContext;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

/**
 * Scepter type that provides properties for the magical scepter item behavior.
 *
 * @param color Integer color for the scepter to be filled.
 * @param experienceCost Integer cost of experience to cast a spell.
 * @param infusable Truth assignment, if the scepter type is able to change by infusion.
 * @param attackSpell Optional spell entry to use for casting a spell on attack.
 * @param protectSpell Optional spell entry to use for casting a spell on protect.
 * @param infusion Optional loot context predicate to determine when to infuse a scepter with this type.
 */
public record Scepter(int color, int experienceCost, boolean infusable,
                      Optional<RegistryEntry<Spell>> attackSpell,
                      Optional<RegistryEntry<Spell>> protectSpell,
                      Optional<LootContextPredicate> infusion) {
    public static final Codec<Scepter> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codecs.RGB.fieldOf("color").forGetter(Scepter::color),
                    Codecs.NON_NEGATIVE_INT.fieldOf("experience_cost").forGetter(Scepter::experienceCost),
                    Codec.BOOL.optionalFieldOf("infusable", false).forGetter(Scepter::infusable),
                    Spell.ENTRY_CODEC.optionalFieldOf("spell_attack").forGetter(Scepter::attackSpell),
                    Spell.ENTRY_CODEC.optionalFieldOf("spell_protect").forGetter(Scepter::protectSpell),
                    LootContextPredicate.CODEC.optionalFieldOf("infusion").forGetter(Scepter::infusion)
            ).apply(instance, Scepter::new)
    );
    public static final Codec<Scepter> NETWORK_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codecs.RGB.fieldOf("color").forGetter(Scepter::color),
                    Codecs.NON_NEGATIVE_INT.fieldOf("experience_cost").forGetter(Scepter::experienceCost),
                    Codec.BOOL.optionalFieldOf("infusable", false).forGetter(Scepter::infusable),
                    Spell.ENTRY_CODEC.optionalFieldOf("spell_attack").forGetter(Scepter::attackSpell),
                    Spell.ENTRY_CODEC.optionalFieldOf("spell_protect").forGetter(Scepter::protectSpell)
            ).apply(instance, Scepter::createClientScepter)
    );

    /**
     * Create a client scepter that does not utilize the infusion property.
     *
     * @param color Integer color for the scepter to be filled.
     * @param experienceCost Integer cost of experience to cast a spell.
     * @param infusable Truth assignment, if the scepter type is able to change by infusion.
     * @param attackSpell Optional spell entry to use for casting a spell on attack.
     * @param protectSpell Optional spell entry to use for casting a spell on protect.
     * @return Scepter created without infusion for the client to use.
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Scepter createClientScepter(int color, int experienceCost, boolean infusable,
                                               Optional<RegistryEntry<Spell>> attackSpell,
                                               Optional<RegistryEntry<Spell>> protectSpell) {
        return new Scepter(color, experienceCost, infusable, attackSpell, protectSpell, Optional.empty());
    }

    public static final Codec<RegistryEntry<Scepter>> ENTRY_CODEC = RegistryFixedCodec.of(ModRegistryKeys.SCEPTER);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Scepter>> ENTRY_PACKET_CODEC =
            PacketCodecs.registryEntry(ModRegistryKeys.SCEPTER);
    public static final PacketCodec<RegistryByteBuf, Scepter> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    /**
     * Get the color of the scepter.
     *
     * @return Integer color value of the scepter.
     */
    public int getColor() {
        return color;
    }

    /**
     * Get whether the scepter is infusable.
     *
     * @return Truth assignment, if the scepter is infusable.
     */
    public boolean isInfusable() {
        return infusable;
    }

    /**
     * Get the experience spell casting cost used by the scepter.
     *
     * @return Integer experience cost.
     */
    public int getExperienceCost() {
        return experienceCost;
    }

    /**
     * Get the attack spell of the scepter.
     *
     * @return Optional attack spell entry.
     */
    public Optional<RegistryEntry<Spell>> getAttackSpell() {
        return attackSpell;
    }

    /**
     * Get the protect spell of the scepter.
     *
     * @return Optional protect spell entry.
     */
    public Optional<RegistryEntry<Spell>> getProtectSpell() {
        return protectSpell;
    }

    /**
     * Check if the scepter infused in the specified loot context.
     *
     * @param lootContext Loot context to check in.
     * @return Truth assignment, if the scepter infuses in the specified loot context.
     */
    public boolean infuses(LootContext lootContext) {
        if (infusion.isEmpty()) return false;

        LootContextPredicate lootContextPredicate = infusion.get();

        return lootContextPredicate.test(lootContext);
    }

    /**
     * Create a builder for the scepter.
     *
     * @param color Integer color for the scepter to be filled.
     * @param experienceCost Integer cost of experience to cast a spell.
     * @param infusable Truth assignment, if the scepter type is able to change by infusion.
     * @return Scepter builder to add optional properties to.
     */
    public static Scepter.Builder builder(int color, int experienceCost, boolean infusable) {
        return new Scepter.Builder(color, experienceCost, infusable);
    }

    /**
     * Scepter builder to be used to construct a scepter.
     */
    public static class Builder {
        private final int color;
        private final boolean infusable;
        private final int experienceCost;
        private RegistryEntry<Spell> attackSpell = null;
        private RegistryEntry<Spell> protectSpell = null;
        private LootContextPredicate infusion = null;

        /**
         * Construct a scepter builder using the required scepter properties.
         *
         * @param color Integer color for the scepter to be filled.
         * @param experienceCost Integer cost of experience to cast a spell.
         * @param infusable Truth assignment, if the scepter type is able to change by infusion.
         */
        private Builder(int color, int experienceCost, boolean infusable) {
            this.color = color;
            this.experienceCost = experienceCost;
            this.infusable = infusable;
        }

        /**
         * Add a loot context predicate as scepter infusion.
         *
         * @param infusion Optional loot context predicate to determine when to infuse a scepter with this type.
         * @return Scepter builder to continue with.
         */
        public Scepter.Builder infusion(LootContextPredicate infusion) {
            this.infusion = infusion;
            return this;
        }

        /**
         * Add an attack spell to the scepter.
         *
         * @param attackSpell Optional spell entry to use for casting a spell on attack.
         * @return Scepter builder to continue with.
         */
        public Scepter.Builder attackSpell(RegistryEntry<Spell> attackSpell) {
            this.attackSpell = attackSpell;
            return this;
        }

        /**
         * Add an protect spell to the scepter.
         *
         * @param protectSpell Optional spell entry to use for casting a spell on protect.
         * @return Scepter builder to continue with.
         */
        public Scepter.Builder protectSpell(RegistryEntry<Spell> protectSpell) {
            this.protectSpell = protectSpell;
            return this;
        }

        /**
         * Build a scepter from the properties in the builder.
         *
         * @return Scepter made with properties in the builder.
         */
        public Scepter build() {
            return new Scepter(color, experienceCost, infusable,
                    Optional.ofNullable(attackSpell),
                    Optional.ofNullable(protectSpell),
                    Optional.ofNullable(infusion));
        }
    }

}
