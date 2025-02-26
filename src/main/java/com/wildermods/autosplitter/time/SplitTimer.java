package com.wildermods.autosplitter.time;

import java.util.ArrayList;

import com.wildermods.autosplitter.AutosplitCommandSender;
import com.wildermods.autosplitter.Main;
import com.wildermods.autosplitter.config.AutosplitterConfiguration;
import com.wildermods.autosplitter.livesplit.Command;
import com.wildermods.autosplitter.livesplit.Commands;
import com.wildermods.autosplitter.net.SplitterServer;
import com.wildermods.wilderforge.api.modLoadingV1.config.ConfigSavedEvent;
import com.wildermods.wilderforge.launch.WilderForge;
import com.wildermods.wilderforge.launch.logging.Logger;

import com.worldwalkergames.legacy.context.LegacyViewDependencies;

import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SplitTimer extends PausingTimer implements Timed, AutosplitCommandSender {

    private static final Logger LOGGER = new Logger(SplitTimer.class);

    private final LegacyViewDependencies dependencies = WilderForge.getViewDependencies();
    private SplitterServer server;
    private ArrayList<Pause> sentPauses = new ArrayList<>();
    private TimerSettings settings;

    public SplitTimer() throws Exception {
    	this(Main.getDefaultConfig().deriveSettings());
    }
    
    public SplitTimer(TimerSettings settings) throws Exception {
    	super(new RealTimeSegment());
        this.settings = settings;
        this.server = new SplitterServer(settings);
    }
    
    private SplitTimer(SplitTimer parent) {
    	super(new RealTimeSegment());
    	this.settings = parent.settings;
    	this.server = parent.server;
    }

    public SplitterServer getServer() {
    	return server;
    }

    @Override
    public void start() {
    	start(false);
    }
    
    public void start(boolean startPaused) {
    	LOGGER.fatal("Timer starting");
        super.start();
        send(Commands.start);
        send(Commands.initializeGameTime);
        if(startPaused) {
        	send(Commands.pauseGameTime);
        	send(Commands.setGameTime.get().withArg("time", "0.0"));
            Pause pause = new Pause(this.getStart());
            this.pauses.add(pause);
        }
    }

    public void split() {
    	LOGGER.fatal("Timer splitting");
        send(Commands.split);
    }

    @Override
    public void pause() {
    	LOGGER.fatal("Timer pausing");
    	try {
    		super.pause();
    	}
    	catch(IllegalStateException e) {
    		start(true);
    		return;
    	}
        send(Commands.pauseGameTime);
    }

    @Override
    public void unpause() {
    	LOGGER.fatal("Timer unpausing");
        super.unpause();
        send(Commands.resumeGameTime);
    }
    
    public void reset() {
    	LOGGER.fatal("Timer resetting");
    	send(Commands.reset);
    	try {
			Main.timer = new SplitTimer(this);
		} catch (Exception e) {
			LOGGER.catching(e);
			Main.timer = null;
		}
    }

	@Override
	public void send(Command command) {
		server.send(command);
	}
	
	@SubscribeEvent
	public void onConfigSave(ConfigSavedEvent e) {
		System.gc();
		if(e.getCoremod().modId.equals(Main.MOD_ID))
		settings = ((AutosplitterConfiguration)e.getConfiguration()).deriveSettings();
	}
	
	public void enqueuePause(Pause pause) {
		
	}
    
}