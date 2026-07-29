package com.nythral.sentinel.client.shield;

public record ShieldTint(
	float red,
	float green,
	float blue,
	float strength
) {

	public static final ShieldTint NONE =
		new ShieldTint(
			1.0F,
			1.0F,
			1.0F,
			0.0F
		);

	public boolean enabled() {
		return this.strength > 0.0F;
	}
}