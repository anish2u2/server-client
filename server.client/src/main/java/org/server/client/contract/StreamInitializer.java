package org.server.client.contract;

import java.net.Socket;

public interface StreamInitializer {

	public void setSocket(Socket socket);
	
	public boolean isClosed();
}
