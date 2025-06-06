package com.wildermods.autosplitter.mixins;

import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.badlogic.gdx.files.FileHandle;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.wildermods.provider.util.logging.Logger;
import com.worldwalkergames.legacy.server.context.ServerDataContext;

@Debug(export = true)
@Mixin(ServerDataContext.class)
public class ServerDataContextMixin {

	private final @Unique Logger LOGGER = new Logger("TextureDubugger");
	
	@WrapMethod(method = "findInSpecificMod")
	public FileHandle wrapFindInSpecificMod(String assetPath, String modId, Operation<FileHandle> original) {
		LOGGER.info(assetPath, modId != null ? modId : "NO-MOD-PROVIDED");
		return original.call(assetPath, modId);
	}
	
}
