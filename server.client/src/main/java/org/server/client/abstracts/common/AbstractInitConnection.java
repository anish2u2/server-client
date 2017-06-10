package org.server.client.abstracts.common;

import org.server.client.contract.InitConnection;

public abstract class AbstractInitConnection implements InitConnection {

	public void _init(String address, int port, int numberOfConnections) {
		initialize(address, port, numberOfConnections);
	}

	protected abstract void initialize(String address, int port, int numberOfConnections);
}
