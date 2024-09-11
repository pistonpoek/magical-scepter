package net.pistonpoek.magicalscepter.mixin;

import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.scepter.ScepterHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class ScepterInfusion
        extends Entity implements Attackable {
    public ScepterInfusion(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract boolean isHolding(Predicate<ItemStack> predicate);

    @Shadow public abstract ItemStack getMainHandStack();

    @Shadow public abstract ItemStack getOffHandStack();

    @Shadow public abstract void setStackInHand(Hand hand, ItemStack stack);

    @Inject(at = @At("TAIL"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        // Try to infuse scepter if damage would return true, since entity would take damage then.
        if (callbackInfoReturnable.getReturnValue()) {
            tryInfuseScepter(source);
        }
    }

    /***
     * Try to infuse a scepter for the living entity.
     *
     * @param damageSource Damage source to potentially infuse the scepter with.
     * @return Truth assignment, if scepter is infused.
     */
    private boolean tryInfuseScepter(DamageSource damageSource) {
        // Check if the living entity is holding an infusable scepter
        if (!this.isHolding(ScepterHelper.IS_INFUSABLE_SCEPTER)) {
            return false;
        }

        // Get the item stack that is the infusable scepter.
        boolean foundInMainHand = true;
        ItemStack itemStack = this.getMainHandStack();
        if (!ScepterHelper.isInfusable(itemStack)) {
            foundInMainHand = false;
            itemStack = this.getOffHandStack();
        }

        // Get the infusion scepter for the damage source.
        Optional<RegistryEntry<Scepter>> scepter =
                ScepterHelper.getInfusion(ScepterHelper.getScepterRegistry(this.getWorld()),
                        damageSource, getLootContext(damageSource));

        // Check if there is an infusion scepter, if so infuse the held scepter.
        if (scepter.isPresent()) {
            ItemStack infusedScepter = ScepterHelper.setScepter(itemStack, scepter.get());

            if (foundInMainHand) {
                this.setStackInHand(Hand.MAIN_HAND, infusedScepter);
            } else {
                this.setStackInHand(Hand.OFF_HAND, infusedScepter);
            }
            return true;
        }

        return false;
    }

    private LootContext getLootContext(DamageSource damageSource) {
        LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld)this.getWorld())
                .add(LootContextParameters.THIS_ENTITY, this)
                .add(LootContextParameters.ORIGIN, this.getPos())
                .add(LootContextParameters.DAMAGE_SOURCE, damageSource)
                .addOptional(LootContextParameters.ATTACKING_ENTITY, damageSource.getAttacker())
                .addOptional(LootContextParameters.DIRECT_ATTACKING_ENTITY, damageSource.getSource());
        LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
        return new LootContext.Builder(lootContextParameterSet).build(Optional.empty());
    }

}
