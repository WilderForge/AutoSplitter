package com.wildermods.autosplitter.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.wildermods.wilderforge.api.mixins.v1.Descriptor;
import com.worldwalkergames.legacy.game.mission.MissionParticipantSystem;
import com.worldwalkergames.legacy.game.mission.model.Participant;

@Mixin(MissionParticipantSystem.class)
public class MissionParticipantSystemMixin {

		@Inject(
			method = "saveGame("
						+ "Lcom/worldwalkergames/legacy/game/mission/model/Participant;"
						+ Descriptor.STRING
						+ Descriptor.INT
						+ Descriptor.STRING
					+ ")" + Descriptor.VOID,
			at = @At("HEAD"),
			require = 1
		)
		protected void onSaveGame(Participant from, String overwrite, int autosaveSlot, String note, CallbackInfo c) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
}
