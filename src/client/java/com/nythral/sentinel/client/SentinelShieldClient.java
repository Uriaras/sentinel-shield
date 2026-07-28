package com.nythral.sentinel.client;

import com.nythral.sentinel.client.config.SentinelConfigManager;
import net.fabricmc.api.ClientModInitializer;

public final class SentinelShieldClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SentinelConfigManager.load();
	}
}