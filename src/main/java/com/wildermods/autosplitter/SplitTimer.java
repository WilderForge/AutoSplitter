package com.wildermods.autosplitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.ArrayList;

import com.wildermods.autosplitter.mixins.WaitingForGameDialogAccessor;
import com.wildermods.wilderforge.api.mixins.v1.Cast;
import com.wildermods.wilderforge.launch.WilderForge;
import com.wildermods.wilderforge.launch.logging.Logger;

import com.worldwalkergames.legacy.context.LegacyViewDependencies;
import com.worldwalkergames.legacy.ui.menu.WaitingForGameDialog;
import com.worldwalkergames.ui.popup.IPopUp;
import com.worldwalkergames.ui.popup.PopUpManager;

public class SplitTimer extends PausingTimer implements Timed {

	private static final Logger LOGGER = new Logger(SplitTimer.class);
	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 16834;
	
	private final LegacyViewDependencies dependencies = WilderForge.getViewDependencies();
	
	private Socket socket;
	private PrintWriter writer;
	private String host;
	private int port;
	private boolean wasLoading = false;
	private ArrayList<Pause> sentPauses = new ArrayList<Pause>();
	
	public SplitTimer() {
		this(DEFAULT_HOST, 16834);
	}
	
	public SplitTimer(String host, int port) {
		super(new RealTimer());
		setHost(host);
		setPort(port);
	}
	
	public void connect() throws IOException {
		if(socket != null) {
			throw new IOException(new IllegalStateException("Socket is still open!"));
		}
		socket = new Socket(host, port);
		writer = new PrintWriter(socket.getOutputStream());
	}
	
	public void disconnect() throws IOException {
		if(socket == null) {
			throw new IOException(new IllegalStateException("Socket already closed!"));
		}
		writer.flush();
		socket.close();
		writer = null;
		socket = null;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public void start() {
		super.start();
		try {
			send("startorsplit");
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	public void split() {
		try {
			send("split");
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	@Override
	public void stop() {
		super.stop();
	}
	
	@Override
	public void pause() {
		super.pause();
		try {
			send("pausegametime");
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	@Override
	public void unpause() {
		super.unpause();
		try {
			send("unpausegametime");
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private void send(String data) throws IOException {
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		writer.write(data + "\r\n");
		writer.flush();
	}
	
	public boolean isLoading() {
		if(dependencies != null) {
			PopUpManager popups = dependencies.popUpManager;
			if(popups != null) {
				for(IPopUp popup : popups.getPopups()) {
					if(popup instanceof WaitingForGameDialog) {
						WaitingForGameDialogAccessor dialogAccessor = Cast.from(popup);
						switch(dialogAccessor.getContext()) {
							case campaignMission:
							case loadCampaign:
							case loadScenario:
							case newCampaign:
							case scenario:
							case legacyBrowser: //If in the future there are categories that span multiple campaigns, opening the legacy would be a legal maneuver, even though it would slow you down.
								return true;
								
							case changedMods: //shouln't be changing mods in the middle of a run...
								return false;
								
							default:
								return true;
						
						}
					}
				}
			}
		}
		return false;
	}
	
}
