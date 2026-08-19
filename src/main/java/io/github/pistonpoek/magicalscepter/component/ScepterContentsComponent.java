package io.github.pistonpoek.magicalscepter.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

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
public record ScepterContentsComponent(Optional<Holder<Scepter>> scepter,
                                       Optional<Integer> customColor,
                                       Optional<Integer> customExperienceCost,
                                       Optional<Boolean> infusable,
                                       Optional<Holder<Spell>> customAttackSpell,
                                       Optional<Holder<Spell>> customProtectSpell) implements TooltipProvider {
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
    public static final StreamCodec<RegistryFriendlyByteBuf, ScepterContentsComponent> PACKET_CODEC = StreamCodec.composite(
            Scepter.ENTRY_PACKET_CODEC.apply(ByteBufCodecs::optional),
            ScepterContentsComponent::scepter,
            ByteBufCodecs.INT.apply(ByteBufCodecs::optional),
            ScepterContentsComponent::customColor,
            ByteBufCodecs.INT.apply(ByteBufCodecs::optional),
            ScepterContentsComponent::customExperienceCost,
            ByteBufCodecs.BOOL.apply(ByteBufCodecs::optional),
            ScepterContentsComponent::infusable,
            Spell.ENTRY_PACKET_CODEC.apply(ByteBufCodecs::optional),
            ScepterContentsComponent::customAttackSpell,
            Spell.ENTRY_PACKET_CODEC.apply(ByteBufCodecs::optional),
            ScepterContentsComponent::customProtectSpell,
            ScepterContentsComponent::new
    );

    public static final String MISSING_SPELL_KEY = createTranslationKey("missing_spell");
    public static final String NO_SPELLS_KEY = createTranslationKey("no_spells");
    public static final String ON_CAST_ATTACK_KEY = createTranslationKey("on_cast_attack");
    public static final String ON_CAST_PROTECT_KEY = createTranslationKey("on_cast_protect");

    public ScepterContentsComponent(Holder<Scepter> scepter) {
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
    public static Optional<Holder<Scepter>> getScepter(ItemStack stack) {
        return get(stack).flatMap(ScepterContentsComponent::scepter);
    }

    /**
     * Get scepter value for an item stack.
     *
     * @return Scepter value from the item stack.
     */
    private Optional<Scepter> getScepterValue() {
        return scepter.map(Holder::value);
    }

    /**
     * Set the scepter contents component of the item stack with the specified scepter.
     *
     * @param stack   Item stack to set the scepter contents component for.
     * @param scepter Scepter to update the scepter contents with.
     * @return Specified item stack updated with the scepter in the scepter contents component.
     */
    public static ItemStack setScepter(ItemStack stack, Holder<Scepter> scepter) {
        stack.update(SCEPTER_CONTENTS, DEFAULT, scepter, ScepterContentsComponent::with);
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
                .map(ARGB::opaque).orElse(BASE_COLOR);
    }

    /**
     * Get the experience cost of casting a spell with the scepter.
     *
     * @return Experience cost.
     */
    public int getExperienceCost() {
        return customExperienceCost
                .or(() -> scepter.map(Holder::value).map(Scepter::getExperienceCost))
                .orElse(BASE_EXPERIENCE_COST);
    }

    /**
     * Check if the player has enough experience for the casting cost.
     *
     * @param player Player to check experience for.
     * @return Truth assignment, if the player has enough experience.
     */
    public boolean hasEnoughExperience(Player player) {
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
        return Scepters.getTranslationKey(
                scepter.flatMap(Holder::unwrapKey).orElse(null));
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
        return get(stack).flatMap(ScepterContentsComponent::getAttackSpell).map(Holder::value);
    }

    /**
     * Get attack spell entry for scepter contents.
     *
     * @return Attack spell entry from scepter contents.
     */
    public Optional<Holder<Spell>> getAttackSpell() {
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
        return get(stack).flatMap(ScepterContentsComponent::getProtectSpell).map(Holder::value);
    }

    /**
     * Get protect spell entry for scepter contents.
     *
     * @return Protect spell entry from scepter contents.
     */
    public Optional<Holder<Spell>> getProtectSpell() {
        return customProtectSpell
                .or(() -> getScepterValue().flatMap(Scepter::getProtectSpell));
    }

    /**
     * Construct a scepter contents component using the specified scepter.
     *
     * @param scepter Scepter to construct component with.
     * @return Scepter contents component made with the scepter.
     */
    public ScepterContentsComponent with(Holder<Scepter> scepter) {
        return new ScepterContentsComponent(Optional.of(scepter), this.customColor, this.customExperienceCost,
                this.infusable, this.customAttackSpell, this.customProtectSpell);
    }

    private static final ChatFormatting ATTACK_SPELL_FORMATTING = ChatFormatting.DARK_GREEN;
    private static final ChatFormatting PROTECT_SPELL_FORMATTING = ChatFormatting.BLUE;
    private static final Component MISSING_SPELL_TEXT = ModIdentifier.translatable(MISSING_SPELL_KEY)
            .withStyle(ChatFormatting.DARK_GRAY);

    /**
     * Get the name of the attack spell.
     *
     * @return Text that represents the attack spell.
     */
    public Component getAttackSpellName() {
        Optional<Holder<Spell>> attackSpell = getAttackSpell();
        if (attackSpell.isEmpty()) {
            return MISSING_SPELL_TEXT;
        }
        MutableComponent mutableText = Spell.getName(attackSpell.get());
        return ComponentUtils.mergeStyles(mutableText, Style.EMPTY.withColor(ATTACK_SPELL_FORMATTING));
    }

    /**
     * Get the name of the protect spell.
     *
     * @return Text that represents the protect spell.
     */
    public Component getProtectSpellName() {
        Optional<Holder<Spell>> protectSpell = getProtectSpell();
        if (protectSpell.isEmpty()) {
            return MISSING_SPELL_TEXT;
        }
        MutableComponent mutableText = Spell.getName(protectSpell.get());
        return ComponentUtils.mergeStyles(mutableText, Style.EMPTY.withColor(PROTECT_SPELL_FORMATTING));
    }

    private static final ChatFormatting TITLE_FORMATTING = ChatFormatting.GRAY;
    private static final Component NO_SPELLS_TEXT = Component.translatable(NO_SPELLS_KEY)
            .withStyle(TITLE_FORMATTING);
    private static final Component CAST_ATTACK_TEXT = Component.translatable(ON_CAST_ATTACK_KEY)
            .withStyle(TITLE_FORMATTING);
    private static final Component CAST_PROTECT_TEXT = Component.translatable(ON_CAST_PROTECT_KEY)
            .withStyle(TITLE_FORMATTING);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip,
                              TooltipFlag type, DataComponentGetter components) {
        Optional<Holder<Spell>> attackSpell = getAttackSpell();
        Optional<Holder<Spell>> protectSpell = getProtectSpell();

        if (attackSpell.isEmpty() && protectSpell.isEmpty()) {
            tooltip.accept(NO_SPELLS_TEXT);
            return;
        }

        tooltip.accept(CommonComponents.EMPTY);

        if (attackSpell.isPresent()) {
            tooltip.accept(CAST_ATTACK_TEXT);
            tooltip.accept(CommonComponents.space().append(getAttackSpellName()));
        }

        if (protectSpell.isPresent()) {
            tooltip.accept(CAST_PROTECT_TEXT);
            tooltip.accept(CommonComponents.space().append(getProtectSpellName()));
        }
    }

    /**
     * Create a scepter contents component translation key for the specified path.
     *
     * @param path String path to create the translation key with.
     * @return String translation key based on the specified path for a scepter contents component.
     */
    public static String createTranslationKey(String path) {
        return ModIdentifier.translationKey("scepter." + path);
    }
}
