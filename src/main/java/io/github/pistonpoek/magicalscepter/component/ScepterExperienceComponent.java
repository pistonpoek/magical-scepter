package io.github.pistonpoek.magicalscepter.component;

import com.mojang.serialization.Codec;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

/**
 * Component that stores data value by the arcane scepter item.
 *
 * @param experience Experience stored currently in the item.
 */
public record ScepterExperienceComponent(int experience) implements TooltipProvider {
    public static final ScepterExperienceComponent DEFAULT = new ScepterExperienceComponent(0);
    public static final Codec<ScepterExperienceComponent> CODEC = Codec.intRange(0, Integer.MAX_VALUE).xmap(
            ScepterExperienceComponent::new,
            ScepterExperienceComponent::experience
    );
    public static final StreamCodec<ByteBuf, ScepterExperienceComponent> PACKET_CODEC = ByteBufCodecs.VAR_INT.map(
            ScepterExperienceComponent::new,
            ScepterExperienceComponent::experience
    );

    public static final String EXPERIENCE_KEY = createTranslationKey("experience");
    /**
     * Get the scepter experience component for an item stack.
     *
     * @param stack Item stack to get scepter experience component for.
     * @return Scepter experience component from the item stack.
     */
    public static Optional<ScepterExperienceComponent> get(ItemStack stack) {
        return Optional.ofNullable(stack.get(ModDataComponentTypes.SCEPTER_EXPERIENCE));
    }

    /**
     * Create a scepter experience component with the specified experience.
     *
     * @param experience Integer experience value to create component with.
     * @return Scepter experience component with the specified experience value.
     */
    public static ScepterExperienceComponent of(int experience) {
        return new ScepterExperienceComponent(
                Math.max(0, experience)
        );
    }

    /**
     * Set the experience value of the specified item stack.
     *
     * @param itemStack Item stack to set experience value for.
     * @param experience Integer experience value to set.
     */
    public static void set(ItemStack itemStack, int experience) {
        itemStack.set(ModDataComponentTypes.SCEPTER_EXPERIENCE, of(experience));
    }

    /**
     * Add additional experience to the specified item stack.
     *
     * @param itemStack Item stack to add experience to.
     * @param experience Integer experience value to add.
     */
    public static void add(ItemStack itemStack, int experience) {
        set(itemStack, getExperience(itemStack) + experience);
    }

    /**
     * Get experience component value for an item stack.
     *
     * @param stack Item stack to get experience component value for.
     * @return Experience component value from the item stack.
     */
    public static int getExperience(ItemStack stack) {
        return get(stack).orElse(DEFAULT).experience();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable(EXPERIENCE_KEY, experience));
    }

    /**
     * Create a scepter experience component translation key for the specified path.
     *
     * @param path String path to create the translation key with.
     * @return String translation key based on the specified path for a scepter experience component.
     */
    public static String createTranslationKey(String path) {
        return ModIdentifier.translationKey("scepter." + path);
    }
}
