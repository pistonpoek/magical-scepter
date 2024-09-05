package net.pistonpoek.magical_scepter.mixin;

import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.pistonpoek.magical_scepter.item.scepter.Scepter;
import net.pistonpoek.magical_scepter.item.scepter.ScepterUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class ScepterSpellInfusion
        extends Entity implements Attackable {
    public ScepterSpellInfusion(EntityType<?> type, World world) {
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
     * @param source Damage source to potentially infuse the scepter with.
     * @return Truth assignment, if scepter is infused.
     */
    private boolean tryInfuseScepter(DamageSource source) {
        // Check if the living entity is holding an infusable scepter
        if (!this.isHolding(ScepterUtil.IS_INFUSABLE_SCEPTER)) {
            return false;
        }

        // Get the item stack that is the infusable scepter.
        boolean foundInMainHand = true;
        ItemStack itemStack = this.getMainHandStack();
        if (!ScepterUtil.isInfusable(itemStack)) {
            foundInMainHand = false;
            itemStack = this.getOffHandStack();
        }

        // Get the infusion scepter for the damage source.
        Optional<RegistryEntry<Scepter>> scepter = ScepterUtil.getInfusion(source);

        // Check if there is an infusion scepter, if so infuse the held scepter.
        if (scepter.isPresent()) {
            ItemStack infusedScepter = ScepterUtil.setScepter(itemStack, scepter.get());

            if (foundInMainHand) {
                this.setStackInHand(Hand.MAIN_HAND, infusedScepter);
            } else {
                this.setStackInHand(Hand.OFF_HAND, infusedScepter);
            }
            return true;
        }

        return false;
    }

}
