package com.wildermods.autosplitter.net;

import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;

import com.wildermods.autosplitter.AutosplitCommandSender;
import com.wildermods.autosplitter.livesplit.Command;

public class SplitterServer extends Server implements AutosplitCommandSender {

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 16834;
	
    final CopyOnWriteArrayList<SplitterWebSocket> clients = new CopyOnWriteArrayList<>();
    
    private final Server server;
    
	public SplitterServer() throws Exception {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}

	public SplitterServer(String host, int port) throws Exception {
		Server server = new Server(new InetSocketAddress(host, port));
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		
		JettyWebSocketServletContainerInitializer.configure(context, null);
		ServletHolder holder = new ServletHolder("splitter", new SplitterSocketServlet());
		context.addServlet(holder, "/");
		
		this.server = server;
		this.server.start();
	}
	
	public Server getImpl() {
		return server;
	}

	@Override
	public void send(Command command) {
		for(SplitterWebSocket client : clients) {
			client.send(command);
		}
	}
	
}
