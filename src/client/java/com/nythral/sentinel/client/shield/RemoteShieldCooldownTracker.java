package com.nythral.sentinel.client.shield;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public final class RemoteShieldCooldownTracker {

	private static final int COOLDOWN_TICKS = 100;

	private static final Map<UUID, Cooldown> COOLDOWNS =
		new HashMap<>();

	private RemoteShieldCooldownTracker() {
	}

	public static void start(
		Player player,
		long currentTick
	) {
		if (player == null) {
			return;
		}

		COOLDOWNS.put(
			player.getUUID(),
			new Cooldown(
				currentTick,
				currentTick + COOLDOWN_TICKS
			)
		);
	}

	public static boolean isCoolingDown(Player player) {
		return getCooldown(player) != null;
	}

	public static float getProgress(Player player) {
		Cooldown cooldown = getCooldown(player);

		if (cooldown == null) {
			return 0.0F;
		}

		long currentTick =
			player.level().getGameTime();

		long remaining =
			cooldown.endTick() - currentTick;

		return Math.clamp(
			remaining / (float) COOLDOWN_TICKS,
			0.0F,
			1.0F
		);
	}

	public static void tick(Minecraft minecraft) {
		if (minecraft.level == null) {
			COOLDOWNS.clear();
			return;
		}

		long currentTick =
			minecraft.level.getGameTime();

		COOLDOWNS.entrySet().removeIf(
			entry -> currentTick >= entry.getValue().endTick()
		);
	}

	private static Cooldown getCooldown(Player player) {
		if (player == null) {
			return null;
		}

		Cooldown cooldown =
			COOLDOWNS.get(player.getUUID());

		if (cooldown == null) {
			return null;
		}

		long currentTick =
			player.level().getGameTime();

		if (currentTick >= cooldown.endTick()) {
			COOLDOWNS.remove(player.getUUID());
			return null;
		}

		return cooldown;
	}

	private record Cooldown(
		long startTick,
		long endTick
	) {
	}
}