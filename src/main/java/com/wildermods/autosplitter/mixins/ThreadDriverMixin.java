package com.wildermods.autosplitter.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.worldwalkergames.logging.ALogger;
import com.worldwalkergames.threading.MultiThreadDriver;

import static com.wildermods.wilderforge.api.mixins.v1.Descriptor.*;

@Mixin(targets = "com.worldwalkergames.threading.MultiThreadDriver$ThreadDriver")
public class ThreadDriverMixin extends Thread {

	private @Unique @Final ALogger LOGGER = new ALogger(MultiThreadDriver.class.getSimpleName());
	
	@Redirect(
		method = "run",
		at = @At(
			value = "INVOKE",
			target = "Lcom/worldwalkergames/logging/ALogger;"
				+ "log1("
					+ STRING
					+ ARRAY_OF + OBJECT
				+ ")" + VOID
		)
	)
	private void redirectTaskTookTooLongMessage(ALogger logger, String message, Object... parameters) {
		logger.log3(message, parameters);
	}
	
	@ModifyConstant(
		method = "run",
		constant = @Constant(longValue = 200L),
		require = 1
	)
	private long redirectTaskTookTooLongMessage(long original) {
		return 50l;
	}
	
}
