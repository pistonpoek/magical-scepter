package io.github.pistonpoek.magicalscepter.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;

import java.util.Optional;
import java.util.function.Consumer;

import static io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes.SCEPTER_CONTENTS;

/**
 * Component that stores data values used as contents for a magical scepter item.
 *
 * @param scepter              Scepter registry entry that is the default data and can be overruled by the other data values.
 * @param customColor          Color that should be used for visual representation of the scepter.
 * @param customExperienceCost Experience cost that should be used when casting a spell.
 * @param infusable            Truth assignment that determines if the scepter is allowed to be infused.
 * @param customAttackSpell    Attack spell of the scepter that is cast on hit.
 * @param customProtectSpell   Protect spell of the scepter that is cast on use.
 */
public record ScepterContentsComponent(Optional<RegistryEntry<Scepter>> scepter,
                                       Optional<Integer> customColor,
                                       Optional<Integer> customExperienceCost,
                                       Optional<Boolean> infusable,
                                       Optional<RegistryEntry<Spell>> customAttackSpell,
                                       Optional<RegistryEntry<Spell>> customProtectSpell) implements TooltipAppender {
    public static final ScepterContentsComponent DEFAULT =
            new ScepterContentsComponent(Optional.empty(), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.empty());
    public static final int BASE_COLOR = -4424612;
    public static final int BASE_EXPERIENCE_COST = 0;
    public static final Codec<ScepterContentsComponent> BASE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Scepter.ENTRY_CODEC.optionalFieldOf("scepter").forGetter(ScepterContentsComponent::scepter),
                            Codec.INT.optionalFieldOf("custom_color").forGetter(ScepterContentsComponent::customColor),
                            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("custom_experience_cost").forGetter(ScepterContentsComponent::customExperienceCost),
                            Codec.BOOL.optionalFieldOf("infusable").forGetter(ScepterContentsComponent::infusable),
                            Spell.ENTRY_CODEC.optionalFieldOf("custom_attack_spell").forGetter(ScepterContentsComponent::customAttackSpell),
                            Spell.ENTRY_CODEC.optionalFieldOf("custom_protect_spell").forGetter(ScepterContentsComponent::customProtectSpell)
                    )
                    .apply(instance, ScepterContentsComponent::new)
    );

    public static final Codec<ScepterContentsComponent> CODEC = Codec.withAlternative(BASE_CODEC,
            Scepter.ENTRY_CODEC, ScepterContentsComponent::new);
    public static final PacketCodec<RegistryByteBuf, ScepterContentsComponent> PACKET_CODEC = PacketCodec.tuple(
            Scepter.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional),
            ScepterContentsComponent::scepter,
            PacketCodecs.INTEGER.collect(PacketCodecs::optional),
            ScepterContentsComponent::customColor,
            PacketCodecs.INTEGER.collect(PacketCodecs::optional),
            ScepterContentsComponent::customExperienceCost,
            PacketCodecs.BOOLEAN.collect(PacketCodecs::optional),
            ScepterContentsComponent::infusable,
            Spell.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional),
            ScepterContentsComponent::customAttackSpell,
            Spell.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional),
            ScepterContentsComponent::customProtectSpell,
            ScepterContentsComponent::new
    );

    public ScepterContentsComponent(RegistryEntry<Scepter> scepter) {
        this(Optional.of(scepter), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * Get the scepter contents component for an item stack.
     *
     * @param stack Item stack to get scepter contents component for.
     * @return Scepter contents component from the item stack.
     */
    public static Optional<ScepterContentsComponent> get(ItemStack stack) {
        return Optional.ofNullable(stack.get(ModDataComponentTypes.SCEPTER_CONTENTS));
    }

    /**
     * Get scepter component value for an item stack.
     *
     * @param stack Item stack to get scepter component value for.
     * @return Scepter component value from the item stack.
     */
    public static Optional<RegistryEntry<Scepter>> getScepter(ItemStack stack) {
        return get(stack).flatMap(ScepterContentsComponent::scepter);
    }

    /**
     * Get scepter value for an item stack.
     *
     * @return Scepter value from the item stack.
     */
    private Optional<Scepter> getScepterValue() {
        return scepter.map(RegistryEntry::value);
    }

    /**
     * Set the scepter contents component of the item stack with the specified scepter.
     *
     * @param stack   Item stack to set the scepter contents component for.
     * @param scepter Scepter to update the scepter contents with.
     * @return Specified item stack updated with the scepter in the scepter contents component.
     */
    public static ItemStack setScepter(ItemStack stack, RegistryEntry<Scepter> scepter) {
        stack.apply(SCEPTER_CONTENTS, DEFAULT, scepter, ScepterContentsComponent::with);
        return stack;
    }

    /**
     * Get the scepter color.
     *
     * @param defaultColor Color value to use when no color is present.
     * @return Color value.
     */
    public int getColor(int defaultColor) {
        return this.customColor.orElseGet(() -> getScepterValue().map(Scepter::getColor).orElse(defaultColor));
    }

    /**
     * Get color value for scepter contents.
     *
     * @return Color value from the scepter contents.
     */
    public int getColor() {
        return this.customColor
                .or(() -> getScepterValue().map(Scepter::getColor))
                .map(ColorHelper::fullAlpha).orElse(BASE_COLOR);
    }

    /**
     * Get the experience cost of casting a spell with the scepter.
     *
     * @return Experience cost.
     */
    public int getExperienceCost() {
        return customExperienceCost
                .or(() -> scepter.map(RegistryEntry::value).map(Scepter::getExperienceCost))
                .orElse(BASE_EXPERIENCE_COST);
    }

    /**
     * Check if the player has enough experience for the casting cost.
     *
     * @param player Player to check experience for.
     * @return Truth assignment, if the player has enough experience.
     */
    public boolean hasEnoughExperience(PlayerEntity player) {
        return PlayerExperience.getTotalExperience(player) >= getExperienceCost();
    }

    /**
     * Checks if a scepter item stack is infusable.
     *
     * @param stack Item stack to check infusable value for.
     * @return Truth assignment, if stack is infusable.
     */
    public static boolean isInfusable(ItemStack stack) {
        return get(stack).flatMap(ScepterContentsComponent::isInfusable)
                .orElse(true);
    }

    /**
     * Checks if scepter contents is infusable.
     *
     * @return Truth assignment, if scepter contents is infusable.
     */
    public Optional<Boolean> isInfusable() {
        return infusable
                .or(() -> getScepterValue().map(Scepter::isInfusable));
    }

    /**
     * Get the scepter contents key to use for translations.
     *
     * @return String key that may differentiate scepter content components.
     */
    public String getTranslationKey() {
        return scepter.flatMap(RegistryEntry::getKey)
                .map(key -> key.getValue().getPath().replace("/", "."))
                .orElse("empty");
    }

    /**
     * Check if the specified item stack has a spell to cast.
     *
     * @param stack Item stack to check components for.
     * @return Truth assignment, if stack has a spell.
     */
    public static boolean hasSpell(ItemStack stack) {
        return get(stack).map(ScepterContentsComponent::hasSpell).orElse(false);
    }

    /**
     * Check if there is a spell present that could be cast.
     *
     * @return Truth assignment, if spell is present.
     */
    public boolean hasSpell() {
        return customAttackSpell.isPresent() || customProtectSpell.isPresent() ||
                getScepterValue().map(Scepter::getAttackSpell).isPresent() ||
                getScepterValue().map(Scepter::getProtectSpell).isPresent();
    }

    /**
     * Get attack spell value for an item stack.
     *
     * @param stack Item stack to get attack spell value for.
     * @return Attack spell value from the item stack.
     */
    public static Optional<Spell> getAttackSpell(ItemStack stack) {
        return get(stack).flatMap(ScepterContentsComponent::getAttackSpell).map(RegistryEntry::value);
    }

    /**
     * Get attack spell entry for scepter contents.
     *
     * @return Attack spell entry from scepter contents.
     */
    public Optional<RegistryEntry<Spell>> getAttackSpell() {
        return customAttackSpell
                .or(() -> getScepterValue().flatMap(Scepter::getAttackSpell));
    }

    /**
     * Get protect spell value for an item stack.
     *
     * @param stack Item stack to get protect spell value for.
     * @return Protect spell value from the item stack.
     */
    public static Optional<Spell> getProtectSpell(ItemStack stack) {
        return get(stack).flatMap(ScepterContentsComponent::getProtectSpell).map(RegistryEntry::value);
    }

    /**
     * Get protect spell entry for scepter contents.
     *
     * @return Protect spell entry from scepter contents.
     */
    public Optional<RegistryEntry<Spell>> getProtectSpell() {
        return customProtectSpell
                .or(() -> getScepterValue().flatMap(Scepter::getProtectSpell));
    }

    /**
     * Construct a scepter contents component using the specified scepter.
     *
     * @param scepter Scepter to construct component with.
     * @return Scepter contents component made with the scepter.
     */
    public ScepterContentsComponent with(RegistryEntry<Scepter> scepter) {
        return new ScepterContentsComponent(Optional.of(scepter), this.customColor, this.customExperienceCost,
                this.infusable, this.customAttackSpell, this.customProtectSpell);
    }

    private static final Formatting ATTACK_SPELL_FORMATTING = Formatting.DARK_GREEN;
    private static final Formatting PROTECT_SPELL_FORMATTING = Formatting.BLUE;
    private static final Text MISSING_SPELL_TEXT = ModIdentifier.translatable("scepter.missing_spell")
            .formatted(Formatting.DARK_GRAY);

    /**
     * Get the name of the attack spell.
     *
     * @return Text that represents the attack spell.
     */
    public Text getAttackSpellName() {
        Optional<RegistryEntry<Spell>> attackSpell = getAttackSpell();
        if (attackSpell.isEmpty()) {
            return MISSING_SPELL_TEXT;
        }
        MutableText mutableText = Spell.getName(attackSpell.get());
        return Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(ATTACK_SPELL_FORMATTING));
    }

    /**
     * Get the name of the protect spell.
     *
     * @return Text that represents the protect spell.
     */
    public Text getProtectSpellName() {
        Optional<RegistryEntry<Spell>> protectSpell = getProtectSpell();
        if (protectSpell.isEmpty()) {
            return MISSING_SPELL_TEXT;
        }
        MutableText mutableText = Spell.getName(protectSpell.get());
        return Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(PROTECT_SPELL_FORMATTING));
    }

    private static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    private static final Text NO_SPELLS_TEXT = ModIdentifier.translatable("scepter.no_spells")
            .formatted(TITLE_FORMATTING);
    private static final Text CAST_ATTACK_TEXT = ModIdentifier.translatable("scepter.on_cast_attack")
            .formatted(TITLE_FORMATTING);
    private static final Text CAST_PROTECT_TEXT = ModIdentifier.translatable("scepter.on_cast_protect")
            .formatted(TITLE_FORMATTING);

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip,
                              TooltipType type, ComponentsAccess components) {
        Optional<RegistryEntry<Spell>> attackSpell = getAttackSpell();
        Optional<RegistryEntry<Spell>> protectSpell = getProtectSpell();

        if (attackSpell.isEmpty() && protectSpell.isEmpty()) {
            tooltip.accept(NO_SPELLS_TEXT);
            return;
        }

        tooltip.accept(ScreenTexts.EMPTY);

        if (attackSpell.isPresent()) {
            tooltip.accept(CAST_ATTACK_TEXT);
            tooltip.accept(ScreenTexts.space().append(getAttackSpellName()));
        }

        if (protectSpell.isPresent()) {
            tooltip.accept(CAST_PROTECT_TEXT);
            tooltip.accept(ScreenTexts.space().append(getProtectSpellName()));
        }
    }
}
