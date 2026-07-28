package com.nythral.sentinel.client.shield;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ShieldStateResolver {
	private static final int SHIELD_DELAY_TICKS = 5;

	private ShieldStateResolver() {
	}

	public static ShieldState resolve(LivingEntity entity, ItemStack shieldStack) {
		if (entity instanceof Player player && player.getCooldowns().isOnCooldown(shieldStack)) {
			return ShieldState.COOLDOWN;
		}

		if (isUsingShield(entity, shieldStack) && entity.getTicksUsingItem() < SHIELD_DELAY_TICKS) {
			return ShieldState.DELAY;
		}

		return ShieldState.READY;
	}

	private static boolean isUsingShield(LivingEntity entity, ItemStack shieldStack) {
		if (!entity.isUsingItem()) {
			return false;
		}

		ItemStack usedStack = entity.getUseItem();

		return usedStack == shieldStack || usedStack.is(Items.SHIELD);
	}
}