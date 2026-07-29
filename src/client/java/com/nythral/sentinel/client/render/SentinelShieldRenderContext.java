package com.nythral.sentinel.client.render;

import com.nythral.sentinel.client.shield.PlayerShieldState;
import com.nythral.sentinel.client.shield.PlayerShieldStateResolver;
import com.nythral.sentinel.client.shield.ShieldTint;
import com.nythral.sentinel.client.shield.ShieldTintResolver;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;

public final class SentinelShieldRenderContext {

	private static Player focusedPlayer;

	private SentinelShieldRenderContext() {
	}

	public static void setFocusedPlayer(Player player) {
		focusedPlayer = player;
	}

	public static ShieldTint resolveShieldTint(
		ItemDisplayContext displayContext
	) {
		Player player = resolvePlayer(displayContext);

		if (player == null) {
			return ShieldTint.NONE;
		}

		PlayerShieldState state =
			PlayerShieldStateResolver.resolve(player);

		return ShieldTintResolver.resolve(state);
	}

	private static Player resolvePlayer(
		ItemDisplayContext displayContext
	) {
		if (
			displayContext
				== ItemDisplayContext.FIRST_PERSON_LEFT_HAND
				|| displayContext
				== ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
		) {
			return Minecraft.getInstance().player;
		}

		return focusedPlayer;
	}
}