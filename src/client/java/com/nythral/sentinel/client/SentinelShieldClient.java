package com.nythral.sentinel.client;

import com.nythral.lib.client.api.NythralModuleRegistry;
import com.nythral.sentinel.client.config.SentinelConfigManager;
import net.fabricmc.api.ClientModInitializer;

public final class SentinelShieldClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		System.out.println("[Sentinel Shield] Client initialization started");

		SentinelConfigManager.load();

		NythralModuleRegistry.register(
			new SentinelShieldModule()
		);

		System.out.println("[Sentinel Shield] Module registered in Nythral Lib");
	}
}