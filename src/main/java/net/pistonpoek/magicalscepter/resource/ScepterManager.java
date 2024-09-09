//package net.pistonpoek.magicalscepter.resource;
//
//import com.google.common.collect.ImmutableMap;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import com.mojang.serialization.JsonOps;
//import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
//import net.minecraft.registry.*;
//import net.minecraft.registry.entry.RegistryEntry;
//import net.minecraft.resource.Resource;
//import net.minecraft.resource.ResourceFinder;
//import net.minecraft.resource.ResourceManager;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.JsonHelper;
//import net.pistonpoek.magicalscepter.MagicalScepter;
//import net.pistonpoek.magicalscepter.registry.ModIdentifier;
//import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
//import net.pistonpoek.magicalscepter.scepter.Scepter;
//import org.slf4j.Logger;
//
//import java.io.IOException;
//import java.io.Reader;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil.GSON;
//
//public record ScepterManager(
//        RegistryWrapper.WrapperLookup registryLookup) implements SimpleSynchronousResourceReloadListener {
//    public static final Identifier ID = ModIdentifier.of("scepter_reload_listener");
//    private static final Logger LOGGER = MagicalScepter.LOGGER;
//    private static Map<Identifier, RegistryEntry<Scepter>> scepters = ImmutableMap.of();
//    private static final String dataType = RegistryKeys.getPath(ModRegistryKeys.SCEPTER);
//
//    @Override
//    public Identifier getFabricId() {
//        return ID;
//    }
//
//    @Override
//    public void reload(ResourceManager manager) {
//        Map<Identifier, JsonElement> dataObjects = getDataObjects(manager);
//
//        com.google.common.collect.ImmutableMap.Builder<Identifier, RegistryEntry<Scepter>> builder = ImmutableMap.builder();
//        RegistryOps<JsonElement> registryOps = this.registryLookup.getOps(JsonOps.INSTANCE);
//
//        for (Map.Entry<Identifier, JsonElement> entry : dataObjects.entrySet()) {
//            Identifier identifier = (Identifier)entry.getKey();
//
//            try {
//                Scepter scepter = Scepter.CODEC.parse(registryOps, (JsonElement)entry.getValue()).getOrThrow(JsonParseException::new);
//                builder.put(identifier, new RegistryEntry<Scepter>(identifier, scepter));
//
//            } catch (IllegalArgumentException | JsonParseException exception) {
//                LOGGER.error("Parsing error loading scepter {}", identifier, exception);
//            }
//        }
//
//        scepters = builder.build();
//        LOGGER.info("Loaded {} scepters", scepters.size());
//    }
//
//    public static RegistryEntry<Scepter> getScepter(Identifier identifier) {
//        return scepters.get(identifier);
//    }
//
//    public static Collection<RegistryEntry<Scepter>> getScepters() {
//        return List.copyOf(scepters.values());
//    }
//
//    private Map<Identifier, JsonElement> getDataObjects(ResourceManager manager) {
//        ResourceFinder resourceFinder = ResourceFinder.json(dataType);
//        Map<Identifier, JsonElement> results = new HashMap<>();
//
//        for (Map.Entry<Identifier, Resource> entry : resourceFinder.findResources(manager).entrySet()) {
//            Identifier identifier = (Identifier) entry.getKey();
//            Identifier identifier2 = resourceFinder.toResourceId(identifier);
//
//            try {
//                Reader reader = ((Resource) entry.getValue()).getReader();
//
//                try {
//                    JsonElement jsonElement = JsonHelper.deserialize(GSON, reader, JsonElement.class);
//                    JsonElement jsonElement2 = (JsonElement) results.put(identifier2, jsonElement);
//                    if (jsonElement2 != null) {
//                        throw new IllegalStateException("Duplicate data file ignored with ID " + identifier2);
//                    }
//                } catch (Throwable var13) {
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (Throwable var12) {
//                            var13.addSuppressed(var12);
//                        }
//                    }
//
//                    throw var13;
//                }
//
//                if (reader != null) {
//                    reader.close();
//                }
//            } catch (IllegalArgumentException | IOException | JsonParseException var14) {
//                LOGGER.error("Couldn't parse data file {} from {}", identifier2, identifier, var14);
//            }
//        }
//        return results;
//    }
//
//    @Override
//    public Collection<Identifier> getFabricDependencies() {
//        return SimpleSynchronousResourceReloadListener.super.getFabricDependencies();
//    }
//
//}
