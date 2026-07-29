package com.nythral.sentinel.client.shield;

public record PlayerShieldState(
	ShieldState state,
	float cooldownProgress
) {

	public static final PlayerShieldState READY =
		new PlayerShieldState(
			ShieldState.READY,
			0.0F
		);
}