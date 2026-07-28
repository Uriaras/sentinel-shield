package com.nythral.sentinel.client.shield;

import com.nythral.sentinel.client.config.SentinelConfig;
import com.nythral.sentinel.client.config.SentinelConfigManager;

public final class ShieldTintResolver {

	private ShieldTintResolver() {
	}

	public static ShieldTint resolve(ShieldState state) {
		SentinelConfig config =
			SentinelConfigManager.get();

		if (!config.enabled) {
			return ShieldTint.NONE;
		}

		SentinelConfig.ShieldColorConfig colorConfig =
			switch (state) {
				case READY -> config.ready;
				case DELAY -> config.delay;
				case COOLDOWN -> config.cooldown;
			};

		if (colorConfig == null || colorConfig.strength <= 0.0F) {
			return ShieldTint.NONE;
		}

		return fromHex(
			colorConfig.color,
			colorConfig.strength
		);
	}

	private static ShieldTint fromHex(
		String hex,
		float strength
	) {
		if (!SentinelConfigManager.isValidHexColor(hex)) {
			return ShieldTint.NONE;
		}

		String normalized =
			SentinelConfigManager.normalizeHexColor(hex)
				.substring(1);

		int rgb;

		try {
			rgb = Integer.parseInt(
				normalized,
				16
			);
		} catch (NumberFormatException exception) {
			return ShieldTint.NONE;
		}

		float red =
			((rgb >> 16) & 0xFF)
				/ 255.0F;

		float green =
			((rgb >> 8) & 0xFF)
				/ 255.0F;

		float blue =
			(rgb & 0xFF)
				/ 255.0F;

		return new ShieldTint(
			red,
			green,
			blue,
			Math.clamp(
				strength,
				0.0F,
				1.0F
			)
		);
	}
}