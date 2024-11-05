package com.wildermods.autosplitter.net;

import org.eclipse.jetty.ee10.websocket.server.JettyWebSocketServlet;
import org.eclipse.jetty.ee10.websocket.server.JettyWebSocketServletFactory;

public class SplitterSocketServlet extends JettyWebSocketServlet {

	@Override
	protected void configure(JettyWebSocketServletFactory factory) {
		factory.register(SplitterWebSocket.class);
	}

}
