package org.server.client.contract;

import java.net.Socket;

public interface StreamInitializer {

	public String END_CONNECTION = "END_CONNECTION";

	public void setSocket(Socket socket);

	public boolean isClosed();
}
