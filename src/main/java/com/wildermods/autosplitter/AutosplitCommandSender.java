package com.wildermods.autosplitter;

import com.wildermods.autosplitter.livesplit.Command;
import com.wildermods.autosplitter.livesplit.Commands;

public interface AutosplitCommandSender {

	public default void send(Commands command) {
		send(command.get());
	}
	
	public void send(Command command);
	
}
