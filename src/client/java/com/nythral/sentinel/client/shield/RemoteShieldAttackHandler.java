package com.nythral.sentinel.client.shield;

import com.nythral.sentinel.client.config.SentinelConfigManager;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class RemoteShieldAttackHandler {

	private static final int SHIELD_DELAY_TICKS = 5;
	private static final float MINIMUM_ATTACK_STRENGTH = 0.9F;

	private RemoteShieldAttackHandler() {
	}

	public static void register() {
		AttackEntityCallback.EVENT.register(
			(attacker, level, hand, entity, hitResult) -> {
				if (!SentinelConfigManager.get().enabled) {
					return InteractionResult.PASS;
				}

				if (!level.isClientSide()) {
					return InteractionResult.PASS;
				}

				if (
					attacker
						!= Minecraft.getInstance().player
						|| attacker.isSpectator()
				) {
					return InteractionResult.PASS;
				}

				if (!(entity instanceof Player target)) {
					return InteractionResult.PASS;
				}

				if (target == attacker) {
					return InteractionResult.PASS;
				}

				ItemStack weapon =
					attacker.getItemInHand(hand);

				if (!weapon.is(ItemTags.AXES)) {
					return InteractionResult.PASS;
				}

				if (
					attacker.getAttackStrengthScale(0.0F)
						< MINIMUM_ATTACK_STRENGTH
				) {
					return InteractionResult.PASS;
				}

				if (!isActivelyBlocking(target)) {
					return InteractionResult.PASS;
				}

				RemoteShieldCooldownTracker.start(
					target,
					level.getGameTime()
				);

				return InteractionResult.PASS;
			}
		);
	}

	private static boolean isActivelyBlocking(
		Player player
	) {
		return player.isUsingItem()
			&& player.getUseItem().is(Items.SHIELD)
			&& player.getTicksUsingItem()
				>= SHIELD_DELAY_TICKS;
	}
}