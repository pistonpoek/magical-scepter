package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SpellCastingManager extends PersistentState {
    private static final int MAX_CASTER_CASTINGS = Integer.MAX_VALUE;
    private static final String ID = ModIdentifier.key("spell_castings", "_");
    public static final Codec<SpellCastingManager> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.unboundedMap(Codec.STRING.xmap(UUID::fromString, UUID::toString),
                                            Codec.unboundedMap(Codec.STRING.xmap(Integer::parseInt, Object::toString), ScheduledSpellCasting.CODEC))
                                    .fieldOf("scheduled_castings").forGetter(spellCastingManager -> spellCastingManager.scheduledCastings),
                            Codec.INT.fieldOf("start_key").forGetter(spellCastingManager -> spellCastingManager.startKey)
                    )
                    .apply(instance, SpellCastingManager::new)
    );
    private final Map<UUID, Map<Integer, ScheduledSpellCasting>> scheduledCastings;
    private int startKey;

    private SpellCastingManager(Map<UUID, Map<Integer, ScheduledSpellCasting>> scheduledCastings, int startKey) {
        this.scheduledCastings = new HashMap<>(scheduledCastings);
        for (Map.Entry<UUID, Map<Integer, ScheduledSpellCasting>>
                scheduledCasting : scheduledCastings.entrySet()) {
            this.scheduledCastings.put(scheduledCasting.getKey(), new HashMap<>(scheduledCasting.getValue()));
        }
        this.startKey = startKey;
    }

    public SpellCastingManager() {
        this(new HashMap<>(), 0);
    }

    public static SpellCastingManager load(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager()
                .getOrCreate(SpellCastingManager.getPersistentStateType());
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

    public static PersistentStateType<SpellCastingManager> getPersistentStateType() {
        // TODO add data fix type to transition from previous version?
        return new PersistentStateType<>(ID,
                SpellCastingManager::new, CODEC, null);
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
