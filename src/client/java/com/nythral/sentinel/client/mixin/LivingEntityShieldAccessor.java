package com.nythral.sentinel.client.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityShieldAccessor {

	@Accessor("useItem")
	void sentinelShield$setUseItem(ItemStack itemStack);

	@Accessor("useItemRemaining")
	void sentinelShield$setUseItemRemaining(int ticks);
}