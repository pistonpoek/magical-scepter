package io.github.pistonpoek.magicalscepter.scepter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;

public record ScepterTintSource(int defaultColor) implements TintSource {
    public static final MapCodec<ScepterTintSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codecs.RGB.fieldOf("default").forGetter(ScepterTintSource::defaultColor)
            ).apply(instance, ScepterTintSource::new)
    );

    public ScepterTintSource() {
        this(ScepterContentsComponent.BASE_COLOR);
    }

    @Override
    public int getTint(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user) {
        ScepterContentsComponent scepterContentsComponent = stack.get(ModDataComponentTypes.SCEPTER_CONTENTS);
        return scepterContentsComponent != null
                ? ColorHelper.fullAlpha(scepterContentsComponent.getColor(this.defaultColor))
                : ColorHelper.fullAlpha(this.defaultColor);
    }

    @Override
    public MapCodec<ScepterTintSource> getCodec() {
        return CODEC;
    }
}
