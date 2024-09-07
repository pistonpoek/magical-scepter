//package net.pistonpoek.magical_scepter.datagen;
//
//import com.google.common.collect.Maps;
//import com.google.common.collect.Sets;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.mojang.serialization.JsonOps;
//import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
//import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
//import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
//import net.minecraft.data.DataProvider;
//import net.minecraft.data.DataWriter;
//import net.minecraft.data.server.loottable.BlockLootTableGenerator;
//import net.minecraft.loot.LootTable;
//import net.minecraft.loot.LootTables;
//import net.minecraft.registry.*;
//import net.minecraft.resource.featuretoggle.FeatureFlags;
//import net.minecraft.util.Identifier;
//import net.pistonpoek.magical_scepter.ModRegistryKeys;
//import net.pistonpoek.magical_scepter.item.scepter.Scepter;
//import net.pistonpoek.magical_scepter.item.scepter.Scepters;
//
//import java.nio.file.Path;
//import java.util.*;
//import java.util.concurrent.CompletableFuture;
//import java.util.function.BiConsumer;
//
//public abstract class ScepterProvider implements DataProvider {
//    private final FabricDataOutput output;
//    private final Set<Identifier> excludedFromStrictValidation = new HashSet<>();
//    protected final Map<RegistryKey<Scepter>, Scepter.Builder> scepters;
//    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;
//
//    protected ScepterProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
//        this.output = dataOutput;
//        this.registryLookup = registryLookup;
//        this.scepters =
//    }
//
//    /**
//     * Implement this method to add scepters.
//     */
//    public abstract void generate();
//
//    public void accept(BiConsumer<RegistryKey<Scepter>, Scepter.Builder> biConsumer) {
//        generate();
//
//        for (Map.Entry<RegistryKey<Scepter>, Scepter.Builder> entry : scepters.entrySet()) {
//            RegistryKey<Scepter> registryKey = entry.getKey();
//
////            if (registryKey == Scepters.DEFAULT_KEY) {
////                continue;
////            }
//
//            biConsumer.accept(registryKey, entry.getValue());
//        }
//    }
//
//    @Override
//    public CompletableFuture<?> run(DataWriter writer) {
//        HashMap<Identifier, LootTable> builders = Maps.newHashMap();
//        HashMap<Identifier, ResourceCondition[]> conditionMap = new HashMap<>();
//
//        return registryLookup.thenCompose(lookup -> {
//            accept((registryKey, builder) -> {
//                ResourceCondition[] conditions = FabricDataGenHelper.consumeConditions(builder);
//                conditionMap.put(registryKey.getValue(), conditions);
//
//                if (builders.put(registryKey.getValue(), builder.build()) != null) {
//                    throw new IllegalStateException("Duplicate loot table " + registryKey.getValue());
//                }
//            });
//
//            RegistryOps<JsonElement> ops = lookup.getOps(JsonOps.INSTANCE);
//            final List<CompletableFuture<?>> futures = new ArrayList<>();
//
//            for (Map.Entry<Identifier, LootTable> entry : builders.entrySet()) {
//                JsonObject tableJson = (JsonObject) LootTable.CODEC.encodeStart(ops, entry.getValue()).getOrThrow(IllegalStateException::new);
//                FabricDataGenHelper.addConditions(tableJson, conditionMap.remove(entry.getKey()));
//                futures.add(DataProvider.writeToPath(writer, tableJson, getOutputPath(output, entry.getKey())));
//            }
//
//            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
//        });
//    }
//
//    private static Path getOutputPath(FabricDataOutput dataOutput, Identifier id) {
//        return dataOutput.getResolver(ModRegistryKeys.SCEPTER).resolveJson(id);
//    }
//
//    @Override
//    public String getName() {
//        return "Scepters";
//    }
//}
