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
	private Button smoothCooldownButton;

	private EditBox readyColorField;
	private EditBox delayColorField;
	private EditBox cooldownColorField;

	public SentinelSettingsScreen(Screen parent) {
		super(Component.literal("Sentinel Shield"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		SentinelConfig config = SentinelConfigManager.get();

		this.enabledButton = addLeftButton(
			0,
			enabledText(),
			button -> {
				config.enabled = !config.enabled;
				refreshLabels();
				SentinelConfigManager.save();
			}
		);

		this.smoothCooldownButton = addRightButton(
			0,
			smoothCooldownText(),
			button -> {
				config.smoothCooldownColor =
					!config.smoothCooldownColor;

				refreshLabels();
				SentinelConfigManager.save();
			}
		);

		addColorRow(
			1,
			"Ready",
			config.ready,
			field -> this.readyColorField = field
		);

		addColorRow(
			2,
			"Delay",
			config.delay,
			field -> this.delayColorField = field
		);

		addColorRow(
			3,
			"Cooldown",
			config.cooldown,
			field -> this.cooldownColorField = field
		);

		addLeftFooterButton(
			Component.literal("Reset"),
			button -> {
				SentinelConfigManager.reset();
				rebuildWidgets();
			}
		);

		addRightFooterButton(
			Component.literal("Done"),
			button -> onClose()
		);
	}

	private void addColorRow(
		int row,
		String label,
		SentinelConfig.ShieldColorConfig colorConfig,
		Consumer<EditBox> fieldSetter
	) {
		addRenderableWidget(
			new StrengthSlider(
				leftColumnX(),
				rowY(row),
				CONTROL_WIDTH,
				CONTROL_HEIGHT,
				label + " Strength",
				colorConfig.strength,
				value -> colorConfig.strength = (float) value
			)
		);

		EditBox field = createColorField(
			rightColumnX(),
			rowY(row),
			label + " Color",
			colorConfig.color,
			value -> colorConfig.color = value
		);

		fieldSetter.accept(field);
	}

	private EditBox createColorField(
		int x,
		int y,
		String label,
		String initialValue,
		Consumer<String> setter
	) {
		EditBox field = new EditBox(
			this.font,
			x,
			y,
			CONTROL_WIDTH,
			CONTROL_HEIGHT,
			Component.literal(label)
		);

		field.setMaxLength(7);
		field.setValue(initialValue);
		field.setHint(Component.literal("#RRGGBB"));

		field.setResponder(
			value -> {
				if (
					SentinelConfigManager.isValidHexColor(value)
				) {
					setter.accept(
						SentinelConfigManager
							.normalizeHexColor(value)
					);
				}
			}
		);

		return addRenderableWidget(field);
	}

	private Component enabledText() {
		return Component.literal(
			"Enabled: "
				+ onOff(
					SentinelConfigManager.get().enabled
				)
		);
	}

	private Component smoothCooldownText() {
		return Component.literal(
			"Smooth Cooldown: "
				+ onOff(
					SentinelConfigManager
						.get()
						.smoothCooldownColor
				)
		);
	}

	private static String onOff(boolean value) {
		return value ? "On" : "Off";
	}

	private void refreshLabels() {
		this.enabledButton.setMessage(enabledText());

		this.smoothCooldownButton.setMessage(
			smoothCooldownText()
		);
	}

	@Override
	public void onClose() {
		saveColorFields();
		SentinelConfigManager.save();

		if (this.minecraft != null) {
			this.minecraft.setScreen(this.parent);
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
	}

	private static void saveColorField(
		EditBox field,
		Consumer<String> setter
	) {
		if (
			field == null
				|| !SentinelConfigManager.isValidHexColor(
					field.getValue()
				)
		) {
			return;
		}

		setter.accept(
			SentinelConfigManager.normalizeHexColor(
				field.getValue()
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
				snap(initialValue)
			);

			this.label = label;
			this.setter = setter;

			updateMessage();
		}

		@Override
		protected void updateMessage() {
			int percent = (int) Math.round(
				snap(this.value) * 100.0
			);

			setMessage(
				Component.literal(
					this.label + ": " + percent + "%"
				)
			);
		}

		@Override
		protected void applyValue() {
			this.value = snap(this.value);
			this.setter.accept(this.value);
			updateMessage();
		}

		private static double snap(double value) {
			double clamped = Math.clamp(
				value,
				0.0,
				1.0
			);

			int level = Math.clamp(
				(int) Math.round(
					clamped * LEVELS
				),
				0,
				LEVELS
			);

			return level / (double) LEVELS;
		}
	}
}