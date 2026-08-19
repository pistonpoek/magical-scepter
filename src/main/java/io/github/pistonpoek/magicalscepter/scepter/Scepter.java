package io.github.pistonpoek.magicalscepter.scepter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.storage.loot.LootContext;

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
                      Optional<Holder<Spell>> attackSpell,
                      Optional<Holder<Spell>> protectSpell,
                      Optional<ContextAwarePredicate> infusion) {
    public static final Codec<Scepter> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(Scepter::color),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("experience_cost").forGetter(Scepter::experienceCost),
                    Codec.BOOL.optionalFieldOf("infusable", false).forGetter(Scepter::infusable),
                    Spell.ENTRY_CODEC.optionalFieldOf("spell_attack").forGetter(Scepter::attackSpell),
                    Spell.ENTRY_CODEC.optionalFieldOf("spell_protect").forGetter(Scepter::protectSpell),
                    ContextAwarePredicate.CODEC.optionalFieldOf("infusion").forGetter(Scepter::infusion)
            ).apply(instance, Scepter::new)
    );
    public static final Codec<Scepter> NETWORK_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(Scepter::color),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("experience_cost").forGetter(Scepter::experienceCost),
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
                                               Optional<Holder<Spell>> attackSpell,
                                               Optional<Holder<Spell>> protectSpell) {
        return new Scepter(color, experienceCost, infusable, attackSpell, protectSpell, Optional.empty());
    }

    public static final Codec<Holder<Scepter>> ENTRY_CODEC = RegistryFixedCodec.create(ModRegistryKeys.SCEPTER);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Scepter>> ENTRY_PACKET_CODEC =
            ByteBufCodecs.holderRegistry(ModRegistryKeys.SCEPTER);
    public static final StreamCodec<RegistryFriendlyByteBuf, Scepter> PACKET_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

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
    public Optional<Holder<Spell>> getAttackSpell() {
        return attackSpell;
    }

    /**
     * Get the protect spell of the scepter.
     *
     * @return Optional protect spell entry.
     */
    public Optional<Holder<Spell>> getProtectSpell() {
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

        ContextAwarePredicate lootContextPredicate = infusion.get();

        return lootContextPredicate.matches(lootContext);
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
        private Holder<Spell> attackSpell = null;
        private Holder<Spell> protectSpell = null;
        private ContextAwarePredicate infusion = null;

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
        public Scepter.Builder infusion(ContextAwarePredicate infusion) {
            this.infusion = infusion;
            return this;
        }

        /**
         * Add an attack spell to the scepter.
         *
         * @param attackSpell Optional spell entry to use for casting a spell on attack.
         * @return Scepter builder to continue with.
         */
        public Scepter.Builder attackSpell(Holder<Spell> attackSpell) {
            this.attackSpell = attackSpell;
            return this;
        }

        /**
         * Add a protect spell to the scepter.
         *
         * @param protectSpell Optional spell entry to use for casting a spell on protect.
         * @return Scepter builder to continue with.
         */
        public Scepter.Builder protectSpell(Holder<Spell> protectSpell) {
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
