package com.wildermods.autosplitter.net;

import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;

import com.wildermods.autosplitter.AutosplitCommandSender;
import com.wildermods.autosplitter.Main;
import com.wildermods.autosplitter.livesplit.Command;
import com.wildermods.autosplitter.time.TimerSettings;
import com.wildermods.wilderforge.launch.logging.Logger;

public class SplitterServer extends Server implements AutosplitCommandSender {

	private static final Logger LOGGER = new Logger(SplitterServer.class);
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 55555;
    private static SplitterServer INSTANCE = null;
	
    final CopyOnWriteArrayList<SplitterWebSocket> clients = new CopyOnWriteArrayList<>();
    
    private Server server;
    
	public SplitterServer() throws Exception {
		this(Main.getDefaultConfig().deriveSettings());
	}

	public SplitterServer(TimerSettings settings) throws Exception {
		this(settings.host(), settings.port());
	}
	
	private SplitterServer(String host, int port) throws Exception {
		establishServer(host, port);
	}
	
	public void establishServer(String host, int port) {
		if(server != null) {
			try {
				server.stop();
			} catch (Exception e) {
				LOGGER.catching(e);
			}
		}
		try {
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
		catch(Exception e) {
			Main.LOGGER.catching(e);
		}
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
	
	public SplitterServer getInstance() {
		return INSTANCE;
	}
	
}
