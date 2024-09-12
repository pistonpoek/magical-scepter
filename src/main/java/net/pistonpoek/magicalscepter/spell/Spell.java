package net.pistonpoek.magicalscepter.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;

import java.util.List;
import java.util.Optional;

public record Spell(int experienceCost, int cooldown,
                    List<SpellEffect> spells) {
    public static final Codec<Spell> BASE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("experience_cost").forGetter(Spell::experienceCost),
                            Codec.INT.fieldOf("cooldown").forGetter(Spell::cooldown),
                            SpellEffect.CODEC.listOf().fieldOf("spells").forGetter(Spell::spells)
                    )
                    .apply(instance, Spell::new)
    );
    public static final Codec<RegistryEntry<Spell>> ENTRY_CODEC = RegistryFixedCodec.of(ModRegistryKeys.SPELL);
    public static final Codec<Spell> CODEC = Codec.withAlternative(BASE_CODEC, ENTRY_CODEC, RegistryEntry::value);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Spell>> ENTRY_PACKET_CODEC =
            PacketCodecs.registryEntry(ModRegistryKeys.SPELL);
    public static final PacketCodec<RegistryByteBuf, Spell> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public boolean isInstant() {
        return true;
    }

    public int getCastDuration() {
        return 0;
    }

    public void castSpell(LivingEntity caster, ItemStack itemStack, EquipmentSlot slot) {
        MagicalScepter.LOGGER.info("Casting spell");
        ServerWorld serverWorld = (ServerWorld)caster.getWorld();
        for (SpellEffect spell : spells) {
            spell.apply(serverWorld, caster, caster.getPos());
        }
    }

    private static LootContext createEnchantedEntityLootContext(ServerWorld world, Entity entity, Vec3d pos) {
        LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder(world)
                .add(LootContextParameters.THIS_ENTITY, entity)
                .add(LootContextParameters.ENCHANTMENT_LEVEL, 1)
                .add(LootContextParameters.ORIGIN, pos)
                .build(LootContextTypes.ENCHANTED_ENTITY);
        return new LootContext.Builder(lootContextParameterSet).build(Optional.empty());
    }

    @Environment(EnvType.CLIENT)
    public void displaySpell(World world, LivingEntity caster, int remainingCastTicks) {
//        MagicalScepter.LOGGER.info("Displaying spell" + (sound.map(soundEventRegistryEntry -> " with sound " + soundEventRegistryEntry.value().getId().toShortTranslationKey()).orElse("")));
//        Random random = world.random;
//        sound.ifPresent(sound -> caster.playSound(sound.value(), 3.0F,
//                (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F)
//        );
    }

    public void updateSpell(LivingEntity caster, int remainingCastTicks) {

    }

    public void endSpell(LivingEntity caster, int remainingCastTicks) {
        MagicalScepter.LOGGER.info("End spell");
    }
}
