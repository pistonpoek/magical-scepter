package net.pistonpoek.magical_scepter.item.scepter;

import com.mojang.serialization.Codec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.pistonpoek.magical_scepter.ModRegistries;
import net.pistonpoek.magical_scepter.ModRegistryKeys;
import net.pistonpoek.magical_scepter.item.scepter.infusions.ScepterInfusion;
import net.pistonpoek.magical_scepter.item.scepter.spell.ScepterSpell;
import org.jetbrains.annotations.Nullable;

public class Scepter {

    public static final Codec<RegistryEntry<Scepter>> CODEC = ModRegistries.SCEPTER.getEntryCodec();
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Scepter>> PACKET_CODEC = PacketCodecs.registryEntry(ModRegistryKeys.SCEPTER);

    @Nullable
    private final String baseName;

    public static Scepter byId(String id) {
        return ModRegistries.SCEPTER.get(Identifier.tryParse(id));
    }

    private final int color;
    private final ScepterSpell primarySpell;
    private final ScepterSpell secondarySpell;
    private final ScepterInfusion scepterInfusion;

    private boolean castingPrimarySpell = true;

    public Scepter(int color, ScepterSpell primarySpell, ScepterSpell secondarySpell, ScepterInfusion scepterInfusion) {
        this(null, color, primarySpell, secondarySpell, scepterInfusion);
    }

    public Scepter(@Nullable String baseName, int color, ScepterSpell primarySpell, ScepterSpell secondarySpell, ScepterInfusion scepterInfusion) {
        this.baseName = baseName;
        this.color = color;
        this.primarySpell = primarySpell;
        this.secondarySpell = secondarySpell;
        this.scepterInfusion = scepterInfusion;
    }

    public void castSpell(LivingEntity caster) {
        getCastingSpell().castSpell(caster);
    }

    public void updateSpell(LivingEntity caster, int scepterTick) {
        ScepterSpell castingSpell = getCastingSpell();
        if (castingSpell.isInstant()) {
            caster.stopUsingItem();
        } else {
            castingSpell.updateSpell(caster, castingSpell.getCastDuration() - scepterTick);
        }
    }

    public void displaySpell(LivingEntity caster, int scepterTick) {
        ScepterSpell castingSpell = getCastingSpell();
        if (castingSpell.isInstant()) {
            castingSpell.displaySpell(caster, castingSpell.getCastDuration());
        } else {
            castingSpell.displaySpell(caster, castingSpell.getCastDuration() - scepterTick);
        }
    }

    public void endSpell(LivingEntity caster, int scepterTick) {
        ScepterSpell castingSpell = getCastingSpell();
        castingSpell.endSpell(caster, castingSpell.getCastDuration() - scepterTick);
    }

    public int getColor() {
        return this.color;
    }

    public boolean isInfusable() {
        return scepterInfusion.isInfusable();
    }

    public ScepterInfusion getInfusionPredicate() {
        return scepterInfusion;
    }

    public void setCastingSpell(boolean castingPrimarySpell) {
        this.castingPrimarySpell = castingPrimarySpell;
    }

    public ScepterSpell getCastingSpell() {
        if (castingPrimarySpell) {
            return primarySpell;
        } else {
            return secondarySpell;
        }
    }

    public boolean isInstantSpell() {
        return getCastingSpell().isInstant();
    }

    public int getExperienceCost() {
        return getCastingSpell().getExperienceCost();
    }

    public int getSpellCooldown() {
        return getCastingSpell().getSpellCooldown();
    }

    public String finishTranslationKey(String prefix) {
        return prefix + (this.baseName == null ? ModRegistries.SCEPTER.getId(this).getPath() : this.baseName);
    }
}
