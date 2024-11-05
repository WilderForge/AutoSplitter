package com.wildermods.autosplitter.time;

import java.util.ArrayList;

import com.wildermods.autosplitter.AutosplitCommandSender;
import com.wildermods.autosplitter.Main;
import com.wildermods.autosplitter.livesplit.Command;
import com.wildermods.autosplitter.livesplit.Commands;
import com.wildermods.autosplitter.net.SplitterServer;
import com.wildermods.wilderforge.launch.WilderForge;
import com.wildermods.wilderforge.launch.logging.Logger;

import com.worldwalkergames.legacy.context.LegacyViewDependencies;

public class SplitTimer extends PausingTimer implements Timed, AutosplitCommandSender {

    private static final Logger LOGGER = new Logger(SplitTimer.class);

    private final LegacyViewDependencies dependencies = WilderForge.getViewDependencies();
    private SplitterServer server;
    private ArrayList<Pause> sentPauses = new ArrayList<>();
    private TimerSettings settings;

    public SplitTimer() throws Exception {
    	this(TimerSettings.defaultSettings());
    }
    
    public SplitTimer(TimerSettings settings) throws Exception {
        this(new SplitterServer(settings.host(), settings.port()));
    }

    public SplitTimer(String host, int port) throws Exception {
        this(new SplitterServer(host, port));
    }
    
    private SplitTimer(SplitterServer server) {
    	super(new RealTimeSegment());
    	this.server = server;
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
			Main.timer = new SplitTimer(settings);
		} catch (Exception e) {
			LOGGER.catching(e);
			Main.timer = null;
		}
    }

	@Override
	public void send(Command command) {
		server.send(command);
	}
	
	public void enqueuePause(Pause pause) {
		
	}
    
}