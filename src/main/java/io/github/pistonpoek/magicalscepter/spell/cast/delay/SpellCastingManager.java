package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SpellCastingManager extends PersistentState {
    private static final int MAX_CASTER_CASTINGS = Integer.MAX_VALUE;
    private static final int VERSION = 0;
    private final Map<UUID, Map<Integer, ScheduledSpellCasting>> scheduledCastings = new HashMap<>();
    private int startKey = 0;

    public static SpellCastingManager load(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager()
                .getOrCreate(SpellCastingManager.getPersistentStateType(),
                        ModIdentifier.key("scheduled_castings", "_"));
    }

    public void schedule(ServerWorld world, SpellCasting spellCasting, int delay) {
        ScheduledSpellCasting scheduledSpellCasting = new ScheduledSpellCasting(spellCasting.clone());
        UUID casterUuid = spellCasting.getCaster().getUuid();
        int key = generateKey(casterUuid);

        if (key == -1) {
            MagicalScepter.LOGGER.info("Max casting amount reached for caster {}", casterUuid);
            return;
        }

        store(scheduledSpellCasting, casterUuid, key);
        SpellCastingScheduler.schedule(world, new SpellCastingTimerCallback(casterUuid, key), delay);
    }

    private void store(ScheduledSpellCasting scheduledSpellCasting, UUID casterUuid, int key) {
        if (!scheduledCastings.containsKey(casterUuid)) {
            scheduledCastings.put(casterUuid, new HashMap<>());
        }
        scheduledCastings.get(casterUuid).put(key, scheduledSpellCasting);
        this.markDirty();
    }

    Optional<SpellCasting> retrieve(MinecraftServer server, UUID casterUuid, int key) {
        if (!scheduledCastings.containsKey(casterUuid)) {
            return Optional.empty();
        }
        if (!scheduledCastings.get(casterUuid).containsKey(key)) {
            return Optional.empty();
        }
        this.markDirty();
        if (scheduledCastings.get(casterUuid).size() == 1) {
            return scheduledCastings.remove(casterUuid).remove(key).load(server);
        }
        return scheduledCastings.get(casterUuid).remove(key).load(server);
    }

    public boolean clear(MinecraftServer server, UUID casterUuid) {
        if (!scheduledCastings.containsKey(casterUuid)) {
            return false;
        }
        if (scheduledCastings.get(casterUuid).isEmpty()) {
            return false;
        }
        for (int key : scheduledCastings.remove(casterUuid).keySet()) {
            SpellCastingScheduler.clear(server, new SpellCastingTimerCallback(casterUuid, key));
        }
        this.markDirty();
        return true;
    }

    private int generateKey(UUID casterUuid) {
        int nextKey = startKey;
        if (!scheduledCastings.containsKey(casterUuid)) {
            startKey = nextKey + 1 % MAX_CASTER_CASTINGS;
            return nextKey;
        }

        do {
            if (!scheduledCastings.get(casterUuid).containsKey(nextKey)) {
                startKey = nextKey + 1 % MAX_CASTER_CASTINGS;
                return nextKey;
            }
            nextKey = nextKey + 1 % MAX_CASTER_CASTINGS;
        } while (nextKey != startKey);

        MagicalScepter.LOGGER.warn("Caster reached scheduled casting limit of {}", MAX_CASTER_CASTINGS);
        return -1;
    }

    public static PersistentState.Type<SpellCastingManager> getPersistentStateType() {
        return new PersistentState.Type<>(SpellCastingManager::new, SpellCastingManager::fromNbt, null);
    }

    public static SpellCastingManager fromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup registries) {
        SpellCastingManager spellCastingManager = new SpellCastingManager();
        int version = nbtCompound.getInt("Version");
        if (version != VERSION) {
            return spellCastingManager;
        }

        spellCastingManager.startKey = nbtCompound.getInt("StartKey");
        NbtCompound spellCastings = nbtCompound.getCompound("ScheduledCastings");

        for (String uuidKey : spellCastings.getKeys()) {

            NbtCompound casterCastings = spellCastings.getCompound(uuidKey);
            Map<Integer, ScheduledSpellCasting> castingMap = new HashMap<>();

            for (String key : casterCastings.getKeys()) {
                castingMap.put(Integer.parseInt(key),
                        ScheduledSpellCasting.fromNbt(casterCastings.getCompound(key), registries));
            }
            spellCastingManager.scheduledCastings.put(UUID.fromString(uuidKey), castingMap);
        }

        return spellCastingManager;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup registries) {
        nbtCompound.putInt("Version", VERSION);
        nbtCompound.putInt("StartKey", startKey);

        NbtCompound encodedScheduledCastings = new NbtCompound();
        for (UUID uuid : scheduledCastings.keySet()) {

            NbtCompound encodedCasterCastings = new NbtCompound();
            for (int key : scheduledCastings.get(uuid).keySet()) {

                encodedCasterCastings.put(String.valueOf(key),
                        scheduledCastings.get(uuid).get(key).writeNbt(new NbtCompound(), registries));
            }
            encodedScheduledCastings.put(uuid.toString(), encodedCasterCastings);
        }
        nbtCompound.put("ScheduledCastings", encodedScheduledCastings);

        return nbtCompound;
    }

    public static boolean clear(@NotNull LivingEntity entity) {
        MinecraftServer server = entity.getServer();
        if (server == null) {
            return false;
        }
        return SpellCastingManager.load(server).clear(server, entity.getUuid());
    }

    /**
     * Clear the scheduled spell castings after death.
     *
     * @see net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDeath
     */
    public static void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if (entity == null) {
            return;
        }
        clear(entity);
    }

}
