package com.nythral.sentinel.client.shield;

import com.nythral.sentinel.client.config.SentinelConfig;
import com.nythral.sentinel.client.config.SentinelConfigManager;

public final class ShieldTintResolver {

	private ShieldTintResolver() {
	}

	public static ShieldTint resolve(
		PlayerShieldState playerState
	) {
		SentinelConfig config =
			SentinelConfigManager.get();

		if (
			!config.enabled
				|| playerState == null
				|| playerState.state() == null
		) {
			return ShieldTint.NONE;
		}

		if (
			playerState.state() == ShieldState.COOLDOWN
				&& config.smoothCooldownColor
		) {
			return interpolateCooldown(
				config,
				playerState.cooldownProgress()
			);
		}

		SentinelConfig.ShieldColorConfig colorConfig =
			switch (playerState.state()) {
				case READY, ACTIVE -> config.ready;
				case DELAY -> config.delay;
				case COOLDOWN -> config.cooldown;
			};

		return resolveColor(colorConfig);
	}

	private static ShieldTint interpolateCooldown(
		SentinelConfig config,
		float cooldownProgress
	) {
		ShieldTint readyTint =
			resolveColor(config.ready);

		ShieldTint cooldownTint =
			resolveColor(config.cooldown);

		float progress = Math.clamp(
			cooldownProgress,
			0.0F,
			1.0F
		);

		return new ShieldTint(
			lerp(
				readyTint.red(),
				cooldownTint.red(),
				progress
			),
			lerp(
				readyTint.green(),
				cooldownTint.green(),
				progress
			),
			lerp(
				readyTint.blue(),
				cooldownTint.blue(),
				progress
			),
			lerp(
				readyTint.strength(),
				cooldownTint.strength(),
				progress
			)
		);
	}

	private static ShieldTint resolveColor(
		SentinelConfig.ShieldColorConfig colorConfig
	) {
		if (
			colorConfig == null
				|| !SentinelConfigManager.isValidHexColor(
					colorConfig.color
				)
		) {
			return ShieldTint.NONE;
		}

		String hex = SentinelConfigManager
			.normalizeHexColor(colorConfig.color)
			.substring(1);

		try {
			int rgb = Integer.parseInt(hex, 16);

			return new ShieldTint(
				((rgb >> 16) & 0xFF) / 255.0F,
				((rgb >> 8) & 0xFF) / 255.0F,
				(rgb & 0xFF) / 255.0F,
				Math.clamp(
					colorConfig.strength,
					0.0F,
					1.0F
				)
			);
		} catch (NumberFormatException exception) {
			return ShieldTint.NONE;
		}
	}

	private static float lerp(
		float start,
		float end,
		float progress
	) {
		return start + (end - start) * progress;
	}
}