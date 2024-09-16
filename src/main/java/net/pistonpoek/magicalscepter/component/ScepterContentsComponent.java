package net.pistonpoek.magicalscepter.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.ColorHelper;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.pistonpoek.magicalscepter.spell.Spell;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static net.pistonpoek.magicalscepter.component.ModDataComponentTypes.SCEPTER_CONTENTS;

public record ScepterContentsComponent(Optional<RegistryEntry<Scepter>> scepter,
                                       Optional<Boolean> infusable,
                                       Optional<Integer> customColor,
                                       Optional<String> customName,
                                       Optional<RegistryEntry<Spell>> customAttackSpell,
                                       Optional<RegistryEntry<Spell>> customProtectSpell) {
    public static final Codec<ScepterContentsComponent> BASE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Scepter.ENTRY_CODEC.optionalFieldOf("scepter").forGetter(ScepterContentsComponent::scepter),
                            Codec.BOOL.optionalFieldOf("infusable").forGetter(ScepterContentsComponent::infusable),
                            Codec.INT.optionalFieldOf("custom_color").forGetter(ScepterContentsComponent::customColor),
                            Codec.STRING.optionalFieldOf("custom_name").forGetter(ScepterContentsComponent::customName),
                            Spell.ENTRY_CODEC.optionalFieldOf("custom_attack_spell").forGetter(ScepterContentsComponent::customAttackSpell),
                            Spell.ENTRY_CODEC.optionalFieldOf("custom_protect_spell").forGetter(ScepterContentsComponent::customProtectSpell)
                    )
                    .apply(instance, ScepterContentsComponent::new)
    );

    public static final Codec<ScepterContentsComponent> CODEC = Codec.withAlternative(BASE_CODEC,
            Scepter.ENTRY_CODEC, ScepterContentsComponent::with);
    public static final PacketCodec<RegistryByteBuf, ScepterContentsComponent> PACKET_CODEC = PacketCodec.tuple(
            Scepter.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional),
            ScepterContentsComponent::scepter,
            PacketCodecs.BOOL.collect(PacketCodecs::optional),
            ScepterContentsComponent::infusable,
            PacketCodecs.INTEGER.collect(PacketCodecs::optional),
            ScepterContentsComponent::customColor,
            PacketCodecs.STRING.collect(PacketCodecs::optional),
            ScepterContentsComponent::customName,
            Spell.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional),
            ScepterContentsComponent::customAttackSpell,
            Spell.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional),
            ScepterContentsComponent::customProtectSpell,
            ScepterContentsComponent::new
    );

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
     * @param stack Item stack to get scepter value for.
     * @return Scepter value from the item stack.
     */
    private static Optional<Scepter> getScepterValue(ItemStack stack) {
        return getScepter(stack).map(RegistryEntry::value);
    }

    /**
     * Set the scepter contents component of the item stack with the specified scepter.
     *
     * @param stack Item stack to set the scepter contents component for.
     * @param scepter Scepter to update the scepter contents with.
     * @return Specified item stack updated with the scepter in the scepter contents component.
     */
    public static ItemStack setScepter(ItemStack stack, RegistryEntry<Scepter> scepter) {
        stack.set(SCEPTER_CONTENTS, ScepterContentsComponent.with(scepter));
        return stack;
    }

    /**
     * Checks if a scepter item stack is infusable.
     *
     * @param stack Item stack to check infusable value for.
     * @return Truth assignment, if stack is infusable.
     */
    public static boolean isInfusable(ItemStack stack) {
        return get(stack).flatMap(ScepterContentsComponent::infusable)
                .or(() -> getScepterValue(stack).map(Scepter::isInfusable))
                .orElse(false);
    }

    /**
     * Get color value for an item stack.
     *
     * @param stack Item stack to get color value for.
     * @return Color value from the item stack.
     */
    public static int getColor(ItemStack stack) {
        return get(stack).flatMap(ScepterContentsComponent::customColor)
                .or(() -> getScepterValue(stack).map(Scepter::getColor))
                    .map(ColorHelper.Argb::fullAlpha)
                .orElse(ColorHelper.Argb.getArgb(0, 0, 0, 0));
    }

    /**
     * Get name value for an item stack.
     *
     * @param stack Item stack to get name value for.
     * @return Name value from the item stack.
     */
    public static String getTranslationKey(ItemStack stack) {
        return get(stack).flatMap(ScepterContentsComponent::customName)
                .or(() -> getScepter(stack).flatMap(RegistryEntry::getKey)
                        .map(key -> key.getValue().getPath().replace("/", ".")))
                .orElse("");
    }

    /**
     * Get attack spell value for an item stack.
     *
     * @param stack Item stack to get attack spell value for.
     * @return Attack spell value from the item stack.
     */
    public static Optional<RegistryEntry<Spell>> getAttackSpell(ItemStack stack) {
        return get(stack).flatMap(ScepterContentsComponent::customAttackSpell)
                .or(() -> getScepterValue(stack).map(Scepter::getAttackSpell));
    }

    /**
     * Get protect spell value for an item stack.
     *
     * @param stack Item stack to get protect spell value for.
     * @return Protect spell value from the item stack.
     */
    public static Optional<RegistryEntry<Spell>> getProtectSpell(ItemStack stack) {
        return get(stack).flatMap(ScepterContentsComponent::customProtectSpell)
                .or(() -> getScepterValue(stack).map(Scepter::getProtectSpell));
    }

    /**
     * Create a scepter contents component with the specified scepter.
     *
     * @param scepter Scepter to create the contents component with.
     * @return Scepter contents component made with the scepter.
     */
    public static ScepterContentsComponent with(RegistryEntry<Scepter> scepter) {
        return new ScepterContentsComponent(Optional.of(scepter), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
    }
}
