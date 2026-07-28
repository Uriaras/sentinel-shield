package com.nythral.sentinel.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SentinelConfigManager {
	private static final Gson GSON = new GsonBuilder()
		.setPrettyPrinting()
		.create();

	private static final Path CONFIG_PATH = FabricLoader.getInstance()
		.getConfigDir()
		.resolve("sentinel-shield.json");

	private static SentinelConfig config = new SentinelConfig();

	private SentinelConfigManager() {
	}

	public static SentinelConfig get() {
		return config;
	}

	public static void load() {
		if (!Files.exists(CONFIG_PATH)) {
			config = new SentinelConfig();
			save();
			return;
		}

		try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
			SentinelConfig loaded = GSON.fromJson(reader, SentinelConfig.class);
			config = loaded != null ? loaded : new SentinelConfig();
			validate();
		} catch (IOException | RuntimeException exception) {
			config = new SentinelConfig();
			save();
		}
	}

	public static void save() {
		validate();

		try {
			Files.createDirectories(CONFIG_PATH.getParent());

			try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
				GSON.toJson(config, writer);
			}
		} catch (IOException exception) {
			throw new IllegalStateException("Could not save Sentinel Shield configuration.", exception);
		}
	}

	public static void reset() {
		config = new SentinelConfig();
		save();
	}

	private static void validate() {
		if (config.ready == null) {
			config.ready = new SentinelConfig.ShieldColorConfig(true, "#55FF55", 0.25F);
		}

		if (config.delay == null) {
			config.delay = new SentinelConfig.ShieldColorConfig(true, "#FFFF55", 0.55F);
		}

		if (config.cooldown == null) {
			config.cooldown = new SentinelConfig.ShieldColorConfig(true, "#FF5555", 0.75F);
		}

		validateColor(config.ready, "#55FF55");
		validateColor(config.delay, "#FFFF55");
		validateColor(config.cooldown, "#FF5555");
	}

	private static void validateColor(SentinelConfig.ShieldColorConfig colorConfig, String fallbackColor) {
		if (!isValidHexColor(colorConfig.color)) {
			colorConfig.color = fallbackColor;
		}

		colorConfig.color = normalizeHexColor(colorConfig.color);
		colorConfig.strength = Math.clamp(colorConfig.strength, 0.0F, 1.0F);
	}

	public static boolean isValidHexColor(String value) {
		if (value == null) {
			return false;
		}

		String normalized = value.startsWith("#") ? value.substring(1) : value;
		return normalized.matches("[0-9a-fA-F]{6}");
	}

	public static String normalizeHexColor(String value) {
		String normalized = value.startsWith("#") ? value.substring(1) : value;
		return "#" + normalized.toUpperCase();
	}
}