package io.github.pistonpoek.magicalscepter.scepter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Predicate;
import net.minecraft.core.Holder;

/**
 * Predicate to test for matching scepter.
 *
 * @param scepter Scepter registry entry to match.
 */
public record ScepterPredicate(Holder<Scepter> scepter) implements Predicate<Holder<Scepter>> {
    public static final Codec<ScepterPredicate> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Scepter.ENTRY_CODEC.fieldOf("scepter").forGetter(ScepterPredicate::scepter)
                    )
                    .apply(instance, ScepterPredicate::new)
    );

    /**
     * Create a scepter predicate with the specified scepter registry entry.
     *
     * @param scepter Scepter registry entry to use.
     * @return Scepter predicate with the specified scepter.
     */
    public static ScepterPredicate of(Holder<Scepter> scepter) {
        return new ScepterPredicate(scepter);
    }

    @Override
    public boolean test(Holder<Scepter> scepter) {
        return scepter.unwrap().equals(this.scepter.unwrap());
    }
}
