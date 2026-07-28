package com.nythral.sentinel.client.render;

import com.nythral.sentinel.client.shield.ShieldState;
import com.nythral.sentinel.client.shield.ShieldStateResolver;
import com.nythral.sentinel.client.shield.ShieldTint;
import com.nythral.sentinel.client.shield.ShieldTintResolver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class SentinelShieldRenderContext {

	private SentinelShieldRenderContext() {
	}

	public static ShieldTint resolveLocalShieldTint() {
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;

		if (player == null) {
			return ShieldTint.NONE;
		}

		ItemStack shieldStack = findShieldStack(player);

		if (shieldStack.isEmpty()) {
			return ShieldTint.NONE;
		}

		ShieldState state = ShieldStateResolver.resolve(
			player,
			shieldStack
		);

		return ShieldTintResolver.resolve(state);
	}

	private static ItemStack findShieldStack(LocalPlayer player) {
		ItemStack useItem = player.getUseItem();

		if (useItem.is(Items.SHIELD)) {
			return useItem;
		}

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
}