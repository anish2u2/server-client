package org.server.client.abstracts.client;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

import org.server.client.abstracts.common.AbstractInitConnection;
import org.server.client.contract.Client;

public abstract class AbstractClient extends AbstractInitConnection implements Client {

	private WeakReference<Socket> clientSocket;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	protected void initialize(String address, int port, int numberOfConnections) {
		Socket client = null;
		try {
			client = new Socket(address, port);
		} catch (Exception ex) {
			try {
				client = new Socket(InetAddress.getByName(address), port);
			} catch (IOException e) {
				try {
					client = new Socket(InetAddress.getByAddress(address.getBytes()), port);
				} catch (IOException e1) {
					logger.warning(e1.getMessage());
				}
				logger.warning(e.getMessage());
			}
			logger.warning(ex.getMessage());
		}
		clientSocket = new WeakReference<Socket>(client);

	}

	protected Socket getSocket() {
		return clientSocket.get();
	}

	protected void shutDownClient() {
		try {
			this.clientSocket.get().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
