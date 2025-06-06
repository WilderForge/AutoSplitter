package com.wildermods.autosplitter.net;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketOpen;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.wildermods.autosplitter.AutosplitCommandSender;
import com.wildermods.autosplitter.Main;
import com.wildermods.autosplitter.livesplit.Command;
import com.wildermods.autosplitter.livesplit.Commands;
import com.wildermods.provider.util.logging.Logger;
import com.wildermods.wilderforge.launch.logging.Debug;

@WebSocket
public class SplitterWebSocket implements AutosplitCommandSender {

	private static final Logger LOGGER = new Logger(SplitterWebSocket.class);
	
	private final ScheduledExecutorService heartbeatScheduler = Executors.newScheduledThreadPool(1, runnable -> {
		Thread thread = new Thread(runnable, "Splitter Heartbeat");
		thread.setDaemon(true);
		return thread;
	});
	private Session session;
	private String sessionID;
	
	@OnWebSocketClose
	public void onWebSocketClose(int statusCode, String reason) {
		Debug.trace();
		sessionLog("Client Disconnected. Code: " + statusCode + " Reason: " + reason);
	}
	
    @OnWebSocketOpen
    public void onWebSocketConnect(Session session) {
    	Main.timer.getServer().clients.add(this);
        this.session = session;
        this.sessionID = session.getRemoteSocketAddress().toString();
        if(this.sessionID.startsWith("/") && this.sessionID.length() > 1) {
        	this.sessionID = this.sessionID.substring(1);
        }
        Thread.currentThread().setName("AutoSplitterConnection");
        sessionLog("Received connection from " + session.getRemoteSocketAddress());
        startHeartbeat();
    }

    private void startHeartbeat() {
        heartbeatScheduler.scheduleAtFixedRate(this::sendHeartbeat, 0, 1, TimeUnit.SECONDS); // Adjust the interval as needed
    }

    private void sendHeartbeat() {
        if (session != null && session.isOpen()) {
            send(Commands.ping);
        }
    }
	
	@OnWebSocketError
	public void onWebSocketError(Throwable cause) {
		LOGGER.catching(cause, sessionID);
	}
	
	@OnWebSocketMessage
	public void onWebSocketMessage(Session session, String text) {
		if(!text.equals("{\"success\":null}")) {
			sessionLog("RECEIVED: " + text);
		}
	}
	
	private void sessionLog(String log) {
		LOGGER.info(log, sessionID);
	}
	
	public void send(Command command) {
		if(!command.name.equals("ping")) {
			sessionLog("Sending " + command);
		}
		session.sendText(command.toString(), null);
	}
	
}
