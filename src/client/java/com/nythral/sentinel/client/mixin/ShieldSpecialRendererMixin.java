package com.nythral.sentinel.client.mixin;

import com.nythral.sentinel.client.render.SentinelShieldRenderContext;
import com.nythral.sentinel.client.shield.ShieldTint;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ShieldSpecialRenderer.class)
public abstract class ShieldSpecialRendererMixin {

	@ModifyArg(
		method = "submit",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ZZILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"
		),
		index = 10
	)
	private int sentinelShield$modifyShieldColor(
		int originalColor,
		ItemDisplayContext displayContext
	) {
		if (!isFirstPerson(displayContext)) {
			return originalColor;
		}

		ShieldTint tint =
			SentinelShieldRenderContext.resolveLocalShieldTint();

		if (!tint.enabled()) {
			return originalColor;
		}

		return mixColor(
			originalColor,
			tint
		);
	}

	private static boolean isFirstPerson(
		ItemDisplayContext displayContext
	) {
		return displayContext
			== ItemDisplayContext.FIRST_PERSON_LEFT_HAND
			|| displayContext
			== ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
	}

	private static int mixColor(
		int originalColor,
		ShieldTint tint
	) {
		float strength = Math.clamp(
			tint.strength(),
			0.0F,
			1.0F
		);

		int alpha = (originalColor >>> 24) & 0xFF;
		int originalRed = (originalColor >>> 16) & 0xFF;
		int originalGreen = (originalColor >>> 8) & 0xFF;
		int originalBlue = originalColor & 0xFF;

		if (alpha == 0) {
			alpha = 255;
		}

		int targetRed = Math.round(
			tint.red() * 255.0F
		);

		int targetGreen = Math.round(
			tint.green() * 255.0F
		);

		int targetBlue = Math.round(
			tint.blue() * 255.0F
		);

		int red = mixChannel(
			originalRed,
			targetRed,
			strength
		);

		int green = mixChannel(
			originalGreen,
			targetGreen,
			strength
		);

		int blue = mixChannel(
			originalBlue,
			targetBlue,
			strength
		);

		return alpha << 24
			| red << 16
			| green << 8
			| blue;
	}

	private static int mixChannel(
		int original,
		int target,
		float strength
	) {
		return Math.clamp(
			Math.round(
				original
					+ (target - original)
					* strength
			),
			0,
			255
		);
	}
}