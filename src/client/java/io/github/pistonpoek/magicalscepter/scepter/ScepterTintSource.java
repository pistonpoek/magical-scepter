package io.github.pistonpoek.magicalscepter.scepter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record ScepterTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<ScepterTintSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(ScepterTintSource::defaultColor)
            ).apply(instance, ScepterTintSource::new)
    );

    /**
     * Constructs a scepter tint source.
     */
    public ScepterTintSource() {
        this(ScepterContentsComponent.BASE_COLOR);
    }

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity user) {
        ScepterContentsComponent scepterContentsComponent = stack.get(ModDataComponentTypes.SCEPTER_CONTENTS);
        return scepterContentsComponent != null
                ? ARGB.opaque(scepterContentsComponent.getColor(this.defaultColor))
                : ARGB.opaque(this.defaultColor);
    }

    @Override
    public MapCodec<ScepterTintSource> type() {
        return CODEC;
    }
}
