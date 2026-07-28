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
		DataComponentMap dataComponentMap,
		ItemDisplayContext itemDisplayContext,
		PoseStack poseStack,
		SubmitNodeCollector submitNodeCollector,
		int light,
		int overlay,
		boolean glint,
		int outlineColor,
		CallbackInfo callbackInfo
	) {
		SENTINEL_DISPLAY_CONTEXT.set(itemDisplayContext);
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
				SentinelShieldRenderContext.resolveLocalShieldTint();

			if (tint.enabled()) {
				color = createTextureTint(
					tint,
					displayContext
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
		DataComponentMap dataComponentMap,
		ItemDisplayContext itemDisplayContext,
		PoseStack poseStack,
		SubmitNodeCollector submitNodeCollector,
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
			== ItemDisplayContext.GUI;
	}

	@Unique
	private static int createTextureTint(
		ShieldTint tint,
		ItemDisplayContext displayContext
	) {
		float strength = Math.clamp(
			tint.strength(),
			0.0F,
			1.0F
		);

		int red = mixWithWhite(
			tint.red(),
			strength
		);

		int green = mixWithWhite(
			tint.green(),
			strength
		);

		int blue = mixWithWhite(
			tint.blue(),
			strength
		);

		int rgb =
			red << 16
				| green << 8
				| blue;

		if (displayContext == ItemDisplayContext.GUI) {
			return 0xFF000000 | rgb;
		}

		return rgb;
	}

	@Unique
	private static int mixWithWhite(
		float targetChannel,
		float strength
	) {
		float target = Math.clamp(
			targetChannel,
			0.0F,
			1.0F
		);

		float mixed =
			1.0F + (target - 1.0F) * strength;

		return Math.clamp(
			Math.round(mixed * 255.0F),
			0,
			255
		);
	}
}