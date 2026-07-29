package com.nythral.sentinel.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nythral.sentinel.client.render.SentinelShieldRenderContext;
import com.nythral.sentinel.client.shield.ShieldTint;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShieldSpecialRenderer.class)
public abstract class ShieldSpecialRendererMixin {

	@Unique
	private static final ThreadLocal<ItemDisplayContext> SENTINEL_DISPLAY_CONTEXT =
		new ThreadLocal<>();

	@Inject(
		method = "submit",
		at = @At("HEAD")
	)
	private void sentinelShield$captureDisplayContext(
		DataComponentMap components,
		ItemDisplayContext displayContext,
		PoseStack poseStack,
		SubmitNodeCollector collector,
		int light,
		int overlay,
		boolean glint,
		int outlineColor,
		CallbackInfo callbackInfo
	) {
		SENTINEL_DISPLAY_CONTEXT.set(displayContext);
	}

	@Redirect(
		method = "submit",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ZZILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"
		)
	)
	private void sentinelShield$submitTintedShield(
		SubmitNodeCollector collector,
		ModelPart modelPart,
		PoseStack poseStack,
		RenderType renderType,
		int light,
		int overlay,
		TextureAtlasSprite sprite,
		boolean sheeted,
		boolean glint,
		int originalColor,
		ModelFeatureRenderer.CrumblingOverlay crumblingOverlay,
		int outlineColor
	) {
		ItemDisplayContext displayContext =
			SENTINEL_DISPLAY_CONTEXT.get();

		int color = originalColor;

		if (shouldTint(displayContext)) {
			ShieldTint tint =
				SentinelShieldRenderContext.resolveShieldTint(
					displayContext
				);

			if (tint.enabled()) {
				color = mixColor(
					originalColor,
					tint
				);
			}
		}

		collector.submitModelPart(
			modelPart,
			poseStack,
			renderType,
			light,
			overlay,
			sprite,
			sheeted,
			glint,
			color,
			crumblingOverlay,
			outlineColor
		);
	}

	@Inject(
		method = "submit",
		at = @At("RETURN")
	)
	private void sentinelShield$clearDisplayContext(
		DataComponentMap components,
		ItemDisplayContext displayContext,
		PoseStack poseStack,
		SubmitNodeCollector collector,
		int light,
		int overlay,
		boolean glint,
		int outlineColor,
		CallbackInfo callbackInfo
	) {
		SENTINEL_DISPLAY_CONTEXT.remove();
	}

	@Unique
	private static boolean shouldTint(
		ItemDisplayContext displayContext
	) {
		return displayContext
			== ItemDisplayContext.FIRST_PERSON_LEFT_HAND
			|| displayContext
			== ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
			|| displayContext
			== ItemDisplayContext.THIRD_PERSON_LEFT_HAND
			|| displayContext
			== ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
	}

	@Unique
	private static int mixColor(
		int originalColor,
		ShieldTint tint
	) {
		float strength = Math.clamp(
			tint.strength(),
			0.0F,
			1.0F
		);

		int alpha =
			originalColor >>> 24 & 0xFF;

		int originalRed =
			originalColor >>> 16 & 0xFF;

		int originalGreen =
			originalColor >>> 8 & 0xFF;

		int originalBlue =
			originalColor & 0xFF;

		if (alpha == 0) {
			alpha = 255;
		}

		int red = mixChannel(
			originalRed,
			toChannel(tint.red()),
			strength
		);

		int green = mixChannel(
			originalGreen,
			toChannel(tint.green()),
			strength
		);

		int blue = mixChannel(
			originalBlue,
			toChannel(tint.blue()),
			strength
		);

		return alpha << 24
			| red << 16
			| green << 8
			| blue;
	}

	@Unique
	private static int toChannel(float value) {
		return Math.clamp(
			Math.round(
				Math.clamp(
					value,
					0.0F,
					1.0F
				) * 255.0F
			),
			0,
			255
		);
	}

	@Unique
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