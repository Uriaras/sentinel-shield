package com.nythral.sentinel.client.config;

public final class SentinelConfig {
	public boolean enabled = true;
	public boolean shieldBreakFlash = false;

	public ShieldColorConfig ready = new ShieldColorConfig(
		"#55FF55",
		1.0F
	);

	public ShieldColorConfig delay = new ShieldColorConfig(
		"#FFFF55",
		1.0F
	);

	public ShieldColorConfig cooldown = new ShieldColorConfig(
		"#FF5555",
		1.0F
	);

	public static final class ShieldColorConfig {
		public String color;
		public float strength;

		public ShieldColorConfig() {
		}

		public ShieldColorConfig(
			String color,
			float strength
		) {
			this.color = color;
			this.strength = strength;
		}
	}
}