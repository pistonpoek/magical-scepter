package net.pistonpoek.magical_scepter.item.scepter;

import net.minecraft.entity.EntityType;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.pistonpoek.magical_scepter.ModRegistryKeys;
import net.pistonpoek.magical_scepter.item.scepter.infusion.ScepterInfusions;
import net.pistonpoek.magical_scepter.item.scepter.spell.ScepterSpells;
import net.pistonpoek.magical_scepter.util.ModIdentifier;
import net.pistonpoek.magical_scepter.item.scepter.Scepter.Definition;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

//import static net.pistonpoek.magical_scepter.ModRegistries.SCEPTER;

public class Scepters {
    public static final RegistryKey<Scepter> MAGICAL_KEY = of("magical");
    public static final RegistryKey<Scepter> BLAZE_KEY = of("blaze");
    public static final RegistryKey<Scepter> BREEZE_KEY = of("breeze");
    public static final RegistryKey<Scepter> DRAGON_KEY = of("dragon");
    public static final RegistryKey<Scepter> EVOKER_KEY = of("evoker");
    public static final RegistryKey<Scepter> GHAST_KEY = of("ghast");
    public static final RegistryKey<Scepter> GUARDIAN_KEY = of("guardian");
    public static final RegistryKey<Scepter> SHULKER_KEY = of("shulker");
    public static final RegistryKey<Scepter> WARDEN_KEY = of("warden");
    public static final RegistryKey<Scepter> WITHER_KEY = of("wither");
    public static final RegistryKey<Scepter> DEFAULT_KEY = MAGICAL_KEY;

//    public static final RegistryEntry<Scepter> MAGICAL = Scepters.register(MAGICAL_KEY, Scepter.builder(
//                    new Definition(0xBC7C5C, true, Optional.empty(),
//                            Optional.of(Registries.SOUND_EVENT.getEntry(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL)))));
//
//    protected static LootCondition.@NotNull Builder damagedByBlaze() {
//        return DamageSourcePropertiesLootCondition.builder(
//                DamageSourcePredicate.Builder.create()
//                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.BLAZE))
//        );
//    }
//    public static final RegistryEntry<Scepter> BLAZE = Scepters.register(BLAZE_KEY, Scepter.builder(
//            new Definition(0xFF9900, false, Optional.of(LootContextPredicate.create(damagedByBlaze().build())),
//                    Optional.of(Registries.SOUND_EVENT.getEntry(SoundEvents.ENTITY_BLAZE_SHOOT)))));
//
//    protected static LootCondition.@NotNull Builder damagedByBreeze() {
//        return DamageSourcePropertiesLootCondition.builder(
//                DamageSourcePredicate.Builder.create()
//                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.BREEZE))
//        );
//    }
//    public static final RegistryEntry<Scepter> BREEZE = Scepters.register(BREEZE_KEY, Scepter.builder(
//            new Definition(0xBDC9FF, false, Optional.of(LootContextPredicate.create(damagedByBreeze().build())),
//                    Optional.of(Registries.SOUND_EVENT.getEntry(SoundEvents.ENTITY_BREEZE_SHOOT)))));
//
//    protected static LootCondition.@NotNull Builder damagedByDragon() {
//        return DamageSourcePropertiesLootCondition.builder(
//                DamageSourcePredicate.Builder.create()
//                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.ENDER_DRAGON))
//        );
//    }
//    public static final RegistryEntry<Scepter> DRAGON = Scepters.register(DRAGON_KEY, Scepter.builder(
//            new Definition(0xB823F5, false, Optional.of(LootContextPredicate.create(damagedByDragon().build())),
//                    Optional.of(Registries.SOUND_EVENT.getEntry(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT)))));
//
//    protected static LootCondition.@NotNull Builder damagedByEvokerFangs() {
//        return DamageSourcePropertiesLootCondition.builder(
//                DamageSourcePredicate.Builder.create()
//                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.EVOKER))
//                        .directEntity(EntityPredicate.Builder.create().type(EntityType.EVOKER_FANGS))
//        );
//    }
//    public static final RegistryEntry<Scepter> EVOKER = Scepters.register(EVOKER_KEY, Scepter.builder(
//            new Definition(0x959B9B, false, Optional.of(LootContextPredicate.create(damagedByEvokerFangs().build())),
//                    Optional.of(Registries.SOUND_EVENT.getEntry(SoundEvents.ENTITY_EVOKER_CAST_SPELL)))));
//
//    protected static LootCondition.@NotNull Builder damagedByGhast() {
//        return DamageSourcePropertiesLootCondition.builder(
//                DamageSourcePredicate.Builder.create()
//                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.GHAST))
//        );
//    }
//    public static final RegistryEntry<Scepter> GHAST = Scepters.register(GHAST_KEY, Scepter.builder(
//            new Definition(0xCD5CAB, false, Optional.of(LootContextPredicate.create(damagedByGhast().build())),
//                    Optional.of(Registries.SOUND_EVENT.getEntry(SoundEvents.ENTITY_GHAST_SHOOT)))));

//    public static final RegistryEntry<Scepter> GUARDIAN = Scepters.register("guardian", new Scepter(0x4f7d8c, ScepterSpells.GUARDIAN_BEAM, ScepterSpells.GUARDIAN_BEAM, ScepterInfusions.INFUSABLE)); // NIGHT_VISION spell? 698C82 is cyan color from regular guardian, C2FF66 is night vision color, 96C674 is in between.
//    public static final RegistryEntry<Scepter> SHULKER = Scepters.register("shulker", new Scepter(0xCEFFFF, ScepterSpells.SHULKER_BULLET, ScepterSpells.SLOW_FALLING, ScepterInfusions.INFUSABLE)); // Levitation effect color is 0xCEFFFF
//    public static final RegistryEntry<Scepter> WARDEN = Scepters.register("warden", new Scepter(0x2ce3eb, ScepterSpells.WARDEN, ScepterSpells.KNOCKBACK_RESISTANCE, ScepterInfusions.INFUSABLE));
//    public static final RegistryEntry<Scepter> WITHER = Scepters.register("wither", new Scepter(0x736156, ScepterSpells.WITHER_SKULL, ScepterSpells.WITHER_SKULL, ScepterInfusions.INFUSABLE)); // Wither effect color is 0x736156

    private static RegistryEntry<Scepter> register(RegistryKey<Scepter> key, Scepter.Builder builder) {
        //return Registry.registerReference(SCEPTER, key, builder.build(key.getValue()));
        return null;
    }

    public static Identifier registerAndGetDefault() {
        return DEFAULT_KEY.getValue();
    }

    private static RegistryKey<Scepter> of(String id) {
        return RegistryKey.of(ModRegistryKeys.SCEPTER, ModIdentifier.of(id));
    }
}
