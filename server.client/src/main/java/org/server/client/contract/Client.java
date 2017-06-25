package org.server.client.contract;

import java.util.List;

public interface Client extends InitConnection {

	public Writer getWriter();

	public Reader getReader();

	public List<IpAddressDetail> getActiveAddress();

}
