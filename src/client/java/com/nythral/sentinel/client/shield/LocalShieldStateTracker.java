package com.nythral.sentinel.client.shield;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class LocalShieldStateTracker {

	private static final int SHIELD_DELAY_TICKS = 5;

	private static ShieldState currentState = ShieldState.READY;
	private static int usingTicks = 0;
	private static float cooldownProgress = 0.0F;

	private LocalShieldStateTracker() {
	}

	public static void tick(Minecraft minecraft) {
		LocalPlayer player = minecraft.player;

		if (player == null) {
			reset();
			return;
		}

		ItemStack shieldStack = findShieldStack(player);

		if (shieldStack.isEmpty()) {
			reset();
			return;
		}

		if (player.getCooldowns().isOnCooldown(shieldStack)) {
			currentState = ShieldState.COOLDOWN;
			usingTicks = 0;

			cooldownProgress = Math.clamp(
				player.getCooldowns().getCooldownPercent(
					shieldStack,
					0.0F
				),
				0.0F,
				1.0F
			);

			System.out.println(
				"Sentinel Shield cooldown progress: "
					+ cooldownProgress
			);

			return;
		}

		cooldownProgress = 0.0F;

		if (isUsingShield(player)) {
			usingTicks++;

			if (usingTicks <= SHIELD_DELAY_TICKS) {
				currentState = ShieldState.DELAY;
			} else {
				currentState = ShieldState.READY;
			}

			return;
		}

		usingTicks = 0;
		currentState = ShieldState.READY;
	}

	public static ShieldState getCurrentState() {
		return currentState;
	}

	public static float getCooldownProgress() {
		return cooldownProgress;
	}

	private static boolean isUsingShield(LocalPlayer player) {
		if (!player.isUsingItem()) {
			return false;
		}

		return player.getUseItem().is(Items.SHIELD);
	}

	private static ItemStack findShieldStack(LocalPlayer player) {
		ItemStack mainHand = player.getMainHandItem();

		if (mainHand.is(Items.SHIELD)) {
			return mainHand;
		}

		ItemStack offHand = player.getOffhandItem();

		if (offHand.is(Items.SHIELD)) {
			return offHand;
		}

		return ItemStack.EMPTY;
	}

	private static void reset() {
		currentState = ShieldState.READY;
		usingTicks = 0;
		cooldownProgress = 0.0F;
	}
}