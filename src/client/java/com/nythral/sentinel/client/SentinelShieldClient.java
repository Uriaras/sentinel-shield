package com.nythral.sentinel.client;

import com.nythral.lib.client.api.NythralModuleRegistry;
import com.nythral.sentinel.client.config.SentinelConfigManager;
import com.nythral.sentinel.client.shield.LocalShieldStateTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class SentinelShieldClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SentinelConfigManager.load();

		ClientTickEvents.END_CLIENT_TICK.register(
			LocalShieldStateTracker::tick
		);

		NythralModuleRegistry.register(
			new SentinelShieldModule()
		);
	}
}