package com.nythral.sentinel.client.shield;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class PlayerShieldStateResolver {

	private static final int SHIELD_DELAY_TICKS = 5;

	private PlayerShieldStateResolver() {
	}

	public static PlayerShieldState resolve(Player player) {
		if (player == null) {
			return PlayerShieldState.READY;
		}

		ItemStack shield = findShield(player);

		if (shield.isEmpty()) {
			return PlayerShieldState.READY;
		}

		if (isLocalPlayer(player)) {
			return resolveLocalPlayer(
				player,
				shield
			);
		}

		if (
			RemoteShieldCooldownTracker
				.isCoolingDown(player)
		) {
			return new PlayerShieldState(
				ShieldState.COOLDOWN,
				RemoteShieldCooldownTracker
					.getProgress(player)
			);
		}

		return resolveUsageState(player);
	}

	private static PlayerShieldState resolveLocalPlayer(
		Player player,
		ItemStack shield
	) {
		if (player.getCooldowns().isOnCooldown(shield)) {
			float progress = Math.clamp(
				player.getCooldowns()
					.getCooldownPercent(
						shield,
						0.0F
					),
				0.0F,
				1.0F
			);

			return new PlayerShieldState(
				ShieldState.COOLDOWN,
				progress
			);
		}

		return resolveUsageState(player);
	}

	private static PlayerShieldState resolveUsageState(
		Player player
	) {
		if (!isUsingShield(player)) {
			return PlayerShieldState.READY;
		}

		ShieldState state =
			player.getTicksUsingItem()
				< SHIELD_DELAY_TICKS
				? ShieldState.DELAY
				: ShieldState.ACTIVE;

		return new PlayerShieldState(
			state,
			0.0F
		);
	}

	private static boolean isLocalPlayer(Player player) {
		return player
			== Minecraft.getInstance().player;
	}

	private static boolean isUsingShield(Player player) {
		return player.isUsingItem()
			&& player.getUseItem().is(Items.SHIELD);
	}

	private static ItemStack findShield(Player player) {
		ItemStack usedItem = player.getUseItem();

		if (usedItem.is(Items.SHIELD)) {
			return usedItem;
		}

		ItemStack mainHand =
			player.getMainHandItem();

		if (mainHand.is(Items.SHIELD)) {
			return mainHand;
		}

		ItemStack offhand =
			player.getOffhandItem();

		if (offhand.is(Items.SHIELD)) {
			return offhand;
		}

		return ItemStack.EMPTY;
	}
}