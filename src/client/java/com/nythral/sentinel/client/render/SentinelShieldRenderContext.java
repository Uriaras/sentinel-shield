package com.nythral.sentinel.client.render;

import com.nythral.sentinel.client.shield.LocalShieldStateTracker;
import com.nythral.sentinel.client.shield.ShieldState;
import com.nythral.sentinel.client.shield.ShieldTint;
import com.nythral.sentinel.client.shield.ShieldTintResolver;
import net.minecraft.client.Minecraft;

public final class SentinelShieldRenderContext {

	private SentinelShieldRenderContext() {
	}

	public static ShieldTint resolveLocalShieldTint() {
		Minecraft minecraft = Minecraft.getInstance();

		if (minecraft.player == null) {
			return ShieldTint.NONE;
		}

		ShieldState state =
			LocalShieldStateTracker.getCurrentState();

		return ShieldTintResolver.resolve(state);
	}
}