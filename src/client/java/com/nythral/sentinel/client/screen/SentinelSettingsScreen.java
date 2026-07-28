package com.nythral.sentinel.client.screen;

import com.nythral.lib.client.screen.NythralStyledScreen;
import com.nythral.sentinel.client.config.SentinelConfig;
import com.nythral.sentinel.client.config.SentinelConfigManager;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SentinelSettingsScreen extends NythralStyledScreen {

	private final Screen parent;

	private Button enabledButton;
	private Button shieldBreakFlashButton;

	private EditBox readyColorField;
	private EditBox delayColorField;
	private EditBox cooldownColorField;

	public SentinelSettingsScreen(Screen parent) {
		super(
			Component.literal(
				"Sentinel Shield"
			)
		);

		this.parent = parent;
	}

	@Override
	protected void init() {
		SentinelConfig config =
			SentinelConfigManager.get();

		this.enabledButton = addLeftButton(
			0,
			enabledText(),
			button -> {
				config.enabled =
					!config.enabled;

				refreshLabels();
				SentinelConfigManager.save();
			}
		);

		this.shieldBreakFlashButton = addRightButton(
			0,
			shieldBreakFlashText(),
			button -> {
				config.shieldBreakFlash =
					!config.shieldBreakFlash;

				refreshLabels();
				SentinelConfigManager.save();
			}
		);

		addRenderableWidget(
			new StrengthSlider(
				leftColumnX(),
				rowY(1),
				CONTROL_WIDTH,
				CONTROL_HEIGHT,
				"Ready Strength",
				config.ready.strength,
				value -> {
					config.ready.strength =
						(float) value;

					SentinelConfigManager.save();
				}
			)
		);

		this.readyColorField = createColorField(
			rightColumnX(),
			rowY(1),
			"Ready Color",
			config.ready.color,
			value -> config.ready.color = value
		);

		addRenderableWidget(
			new StrengthSlider(
				leftColumnX(),
				rowY(2),
				CONTROL_WIDTH,
				CONTROL_HEIGHT,
				"Delay Strength",
				config.delay.strength,
				value -> {
					config.delay.strength =
						(float) value;

					SentinelConfigManager.save();
				}
			)
		);

		this.delayColorField = createColorField(
			rightColumnX(),
			rowY(2),
			"Delay Color",
			config.delay.color,
			value -> config.delay.color = value
		);

		addRenderableWidget(
			new StrengthSlider(
				leftColumnX(),
				rowY(3),
				CONTROL_WIDTH,
				CONTROL_HEIGHT,
				"Cooldown Strength",
				config.cooldown.strength,
				value -> {
					config.cooldown.strength =
						(float) value;

					SentinelConfigManager.save();
				}
			)
		);

		this.cooldownColorField = createColorField(
			rightColumnX(),
			rowY(3),
			"Cooldown Color",
			config.cooldown.color,
			value -> config.cooldown.color = value
		);

		addLeftFooterButton(
			Component.literal(
				"Reset Settings"
			),
			button -> {
				SentinelConfigManager.reset();
				rebuildWidgets();
			}
		);

		addRightFooterButton(
			Component.literal(
				"Done"
			),
			button -> onClose()
		);
	}

	private EditBox createColorField(
		int x,
		int y,
		String hint,
		String initialValue,
		Consumer<String> setter
	) {
		EditBox field = new EditBox(
			this.font,
			x,
			y,
			CONTROL_WIDTH,
			CONTROL_HEIGHT,
			Component.literal(
				hint
			)
		);

		field.setMaxLength(
			7
		);

		field.setValue(
			initialValue
		);

		field.setHint(
			Component.literal(
				"#RRGGBB"
			)
		);

		field.setResponder(
			value -> {
				if (!SentinelConfigManager.isValidHexColor(value)) {
					return;
				}

				setter.accept(
					SentinelConfigManager.normalizeHexColor(
						value
					)
				);

				SentinelConfigManager.save();
			}
		);

		return addRenderableWidget(
			field
		);
	}

	private Component enabledText() {
		return Component.literal(
			"Enabled: "
				+ onOff(
					SentinelConfigManager
						.get()
						.enabled
				)
		);
	}

	private Component shieldBreakFlashText() {
		return Component.literal(
			"Shield Break Flash: "
				+ onOff(
					SentinelConfigManager
						.get()
						.shieldBreakFlash
				)
		);
	}

	private static String onOff(boolean value) {
		return value
			? "On"
			: "Off";
	}

	private void refreshLabels() {
		this.enabledButton.setMessage(
			enabledText()
		);

		this.shieldBreakFlashButton.setMessage(
			shieldBreakFlashText()
		);
	}

	@Override
	public void onClose() {
		saveColorFields();

		if (this.minecraft != null) {
			this.minecraft.setScreen(
				this.parent
			);
		}
	}

	private void saveColorFields() {
		saveColorField(
			this.readyColorField,
			value -> SentinelConfigManager
				.get()
				.ready
				.color = value
		);

		saveColorField(
			this.delayColorField,
			value -> SentinelConfigManager
				.get()
				.delay
				.color = value
		);

		saveColorField(
			this.cooldownColorField,
			value -> SentinelConfigManager
				.get()
				.cooldown
				.color = value
		);

		SentinelConfigManager.save();
	}

	private static void saveColorField(
		EditBox field,
		Consumer<String> setter
	) {
		String value =
			field.getValue();

		if (!SentinelConfigManager.isValidHexColor(value)) {
			return;
		}

		setter.accept(
			SentinelConfigManager.normalizeHexColor(
				value
			)
		);
	}

	private static final class StrengthSlider
		extends AbstractSliderButton {

		private static final int LEVELS = 5;

		private final String label;
		private final DoubleConsumer setter;

		private StrengthSlider(
			int x,
			int y,
			int width,
			int height,
			String label,
			float initialValue,
			DoubleConsumer setter
		) {
			super(
				x,
				y,
				width,
				height,
				Component.empty(),
				snap(
					initialValue
				)
			);

			this.label = label;
			this.setter = setter;

			updateMessage();
		}

		@Override
		protected void updateMessage() {
			int percent =
				(int) Math.round(
					snap(
						this.value
					) * 100.0
				);

			setMessage(
				Component.literal(
					this.label
						+ ": "
						+ percent
						+ "%"
				)
			);
		}

		@Override
		protected void applyValue() {
			this.value = snap(
				this.value
			);

			this.setter.accept(
				this.value
			);

			updateMessage();
		}

		private static double snap(double value) {
			double clamped = Math.clamp(
				value,
				0.0,
				1.0
			);

			int level =
				(int) Math.round(
					clamped * LEVELS
				);

			level = Math.clamp(
				level,
				0,
				LEVELS
			);

			return level
				/ (double) LEVELS;
		}
	}
}