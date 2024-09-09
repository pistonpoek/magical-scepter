package net.pistonpoek.magicalscepter.resource;

import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.pistonpoek.magicalscepter.scepter.Scepter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SimpleScepterReloadListener implements SimpleResourceReloadListener<Scepter> {
    @Override
    public CompletableFuture load(ResourceManager manager, Profiler profiler, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<Void> apply(Scepter scepter, ResourceManager manager, Profiler profiler, Executor executor) {
        return null;
    }

    @Override
    public Identifier getFabricId() {
        return null;
    }
}
