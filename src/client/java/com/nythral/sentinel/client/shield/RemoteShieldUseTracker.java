package com.nythral.sentinel.client.shield;

import com.nythral.sentinel.client.config.SentinelConfigManager;
import com.nythral.sentinel.client.mixin.LivingEntityShieldAccessor;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class RemoteShieldUseTracker {

	private static final int MAX_USE_DURATION = 72000;

	private static final Map<UUID, Integer> REMAINING_USE_TICKS =
		new HashMap<>();

	private RemoteShieldUseTracker() {
	}

	public static void tick(Minecraft minecraft) {
		if (!SentinelConfigManager.get().enabled) {
			REMAINING_USE_TICKS.clear();
			return;
		}

		if (
			minecraft.level == null
				|| minecraft.player == null
		) {
			REMAINING_USE_TICKS.clear();
			return;
		}

		REMAINING_USE_TICKS.keySet().removeIf(
			uuid -> minecraft.level.getPlayerByUUID(uuid) == null
		);

		for (Player player : minecraft.level.players()) {
			if (player == minecraft.player) {
				continue;
			}

			updatePlayer(player);
		}
	}

	public static boolean isUsingShield(Player player) {
		if (!SentinelConfigManager.get().enabled) {
			return false;
		}

		if (
			player == null
				|| !player.isUsingItem()
		) {
			return false;
		}

		if (player.getUseItem().is(Items.SHIELD)) {
			return true;
		}

		return findUsedShield(player).is(Items.SHIELD);
	}

	private static void updatePlayer(Player player) {
		if (!player.isUsingItem()) {
			REMAINING_USE_TICKS.remove(player.getUUID());
			return;
		}

		ItemStack currentUseItem = player.getUseItem();

		if (
			!currentUseItem.isEmpty()
				&& !currentUseItem.is(Items.SHIELD)
		) {
			REMAINING_USE_TICKS.remove(player.getUUID());
			return;
		}

		ItemStack shield = findUsedShield(player);

		if (shield.isEmpty()) {
			REMAINING_USE_TICKS.remove(player.getUUID());
			return;
		}

		int remainingTicks = REMAINING_USE_TICKS.getOrDefault(
			player.getUUID(),
			MAX_USE_DURATION
		);

		((LivingEntityShieldAccessor) player)
			.sentinelShield$setUseItem(shield);

		((LivingEntityShieldAccessor) player)
			.sentinelShield$setUseItemRemaining(remainingTicks);

		REMAINING_USE_TICKS.put(
			player.getUUID(),
			Math.max(remainingTicks - 1, 1)
		);
	}

	private static ItemStack findUsedShield(Player player) {
		ItemStack currentUseItem = player.getUseItem();

		if (currentUseItem.is(Items.SHIELD)) {
			return currentUseItem;
		}

		ItemStack usedHandItem =
			player.getItemInHand(player.getUsedItemHand());

		if (usedHandItem.is(Items.SHIELD)) {
			return usedHandItem;
		}

		ItemStack mainHandItem =
			player.getMainHandItem();

		if (
			!mainHandItem.isEmpty()
				&& !mainHandItem.is(Items.SHIELD)
				&& mainHandItem.getUseDuration(player) > 0
		) {
			return ItemStack.EMPTY;
		}

		ItemStack offhandItem =
			player.getOffhandItem();

		if (offhandItem.is(Items.SHIELD)) {
			return offhandItem;
		}

		if (mainHandItem.is(Items.SHIELD)) {
			return mainHandItem;
		}

		return ItemStack.EMPTY;
	}
}