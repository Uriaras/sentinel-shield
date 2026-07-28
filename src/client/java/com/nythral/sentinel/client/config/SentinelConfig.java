package com.nythral.sentinel.client.config;

public final class SentinelConfig {
	public boolean enabled = true;
	public boolean fixRemoteShieldAnimation = true;

	public ShieldColorConfig ready = new ShieldColorConfig(true, "#55FF55", 0.25F);
	public ShieldColorConfig delay = new ShieldColorConfig(true, "#FFFF55", 0.55F);
	public ShieldColorConfig cooldown = new ShieldColorConfig(true, "#FF5555", 0.75F);

	public static final class ShieldColorConfig {
		public boolean enabled;
		public String color;
		public float strength;

		public ShieldColorConfig() {
		}

		public ShieldColorConfig(boolean enabled, String color, float strength) {
			this.enabled = enabled;
			this.color = color;
			this.strength = strength;
		}
	}
}