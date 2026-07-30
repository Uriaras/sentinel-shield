package com.nythral.sentinel.client.compat;

import com.nythral.sentinel.client.screen.SentinelSettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public final class SentinelModMenuIntegration
	implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return SentinelSettingsScreen::new;
	}
}