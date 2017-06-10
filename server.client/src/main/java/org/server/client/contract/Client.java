package org.server.client.contract;

public interface Client extends InitConnection {

	public Writer getWriter();

	public Reader getReader();

}
