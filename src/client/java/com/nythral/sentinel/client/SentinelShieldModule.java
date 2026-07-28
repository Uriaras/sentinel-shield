package com.nythral.sentinel.client;

import com.nythral.lib.client.api.NythralModule;
import com.nythral.sentinel.client.screen.SentinelSettingsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SentinelShieldModule implements NythralModule {

	@Override
	public String id() {
		return "sentinel-shield";
	}

	@Override
	public Component name() {
		return Component.literal("Sentinel Shield");
	}

	@Override
	public Component description() {
		return Component.literal("Shield state colors");
	}

	@Override
	public Screen createSettingsScreen(Screen parent) {
		return new SentinelSettingsScreen(parent);
	}
}