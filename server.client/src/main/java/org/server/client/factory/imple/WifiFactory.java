package org.server.client.factory.imple;

import org.server.client.contract.Client;
import org.server.client.contract.Server;
import org.server.client.contract.Wifi;
import org.server.client.factory.abstracts.AbstractWifi;
import org.server.client.imple.client.ClientImpl;
import org.server.client.imple.server.ServerImpl;

public class WifiFactory extends AbstractWifi {

	private static Wifi wifi;

	private WifiFactory() {

	}

	public static Wifi getInstance() {
		if (wifi == null) {
			wifi = new WifiFactory();
		}
		return wifi;
	}

	@Override
	public Server getServer() {
		if (getServerRef() == null) {
			setServerRef(new ServerImpl());
		}
		return getServerRef().get();
	}

	@Override
	public Client getClient() {
		if (getClientRef() == null)
			setClientRef(new ClientImpl());
		return getClientRef().get();
	}

}
