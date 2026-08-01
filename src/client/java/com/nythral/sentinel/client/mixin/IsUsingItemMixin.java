package com.nythral.sentinel.client.mixin;

import com.nythral.sentinel.client.config.SentinelConfigManager;
import com.nythral.sentinel.client.shield.RemoteShieldUseTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.IsUsingItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IsUsingItem.class)
public abstract class IsUsingItemMixin {

	@Inject(
		method = "get",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void sentinelShield$fixRemoteShieldAnimation(
		ItemStack itemStack,
		ClientLevel level,
		LivingEntity owner,
		int seed,
		ItemDisplayContext displayContext,
		CallbackInfoReturnable<Boolean> callbackInfo
	) {
		if (!SentinelConfigManager.get().enabled) {
			return;
		}

		if (!itemStack.is(Items.SHIELD)) {
			return;
		}

		if (!(owner instanceof Player player)) {
			return;
		}

		if (player == Minecraft.getInstance().player) {
			return;
		}

		callbackInfo.setReturnValue(
			RemoteShieldUseTracker.isUsingShield(player)
		);
	}
}