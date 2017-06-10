package org.server.client.contract;

public interface Connection {

	public void connect(String address, int port, int numberOfCOnnections) throws Exception;

	public void disconnect() throws Exception;

}
