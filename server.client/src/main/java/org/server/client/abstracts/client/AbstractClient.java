package org.server.client.abstracts.client;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import org.server.client.abstracts.common.AbstractInitConnection;
import org.server.client.contract.Client;
import org.server.client.contract.IpAddressDetail;
import org.server.client.logger.LoggerAPI;

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

	public List<IpAddressDetail> getActiveAddress() {
		try {
			String siteLocalAddress = null;
			for (@SuppressWarnings("rawtypes")
			Enumeration networkInterface = NetworkInterface.getNetworkInterfaces(); networkInterface
					.hasMoreElements();) {
				NetworkInterface network = (NetworkInterface) networkInterface.nextElement();
				for (@SuppressWarnings("rawtypes")
				Enumeration inetAddress = network.getInetAddresses(); inetAddress.hasMoreElements();) {
					InetAddress address = (InetAddress) inetAddress.nextElement();
					LoggerAPI.logInfo("---------------------------------");
					LoggerAPI.logInfo("Host address:" + address.getHostAddress());
					LoggerAPI.logInfo("Is loop back address:" + address.isLoopbackAddress());
					LoggerAPI.logInfo("Is Site Local back address:" + address.isSiteLocalAddress());
					LoggerAPI.logInfo("Is Link Local back address:" + address.isLinkLocalAddress());
					LoggerAPI.logInfo("Is Any Local back address:" + address.isAnyLocalAddress());
					LoggerAPI.logInfo("---------------------------------");
					if (address.isSiteLocalAddress()) {
						siteLocalAddress = address.getHostAddress();
						break;
					}
				}
			}
			return findIpOnThisNetwork(siteLocalAddress);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		clientSocket = null;
	}
}
