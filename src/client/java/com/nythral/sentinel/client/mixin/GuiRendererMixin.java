package com.nythral.sentinel.client.mixin;

import com.nythral.sentinel.client.config.SentinelConfigManager;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRenderer.class)
public abstract class GuiRendererMixin {

	@Shadow
	@Final
	private GuiRenderState renderState;

	@Inject(
		method = "prepareItemElements",
		at = @At("HEAD")
	)
	private void sentinelShield$markShieldsAnimated(
		CallbackInfo callbackInfo
	) {
		if (!SentinelConfigManager.get().enabled) {
			return;
		}

		this.renderState.forEachItem(
			guiItemRenderState -> {
				TrackingItemStackRenderState itemState =
					guiItemRenderState.itemStackRenderState();

				Object modelIdentity =
					itemState.getModelIdentity();

				if (
					modelIdentity != null
						&& modelIdentity
							.toString()
							.contains("shield")
				) {
					itemState.setAnimated();
				}
			}
		);
	}
}