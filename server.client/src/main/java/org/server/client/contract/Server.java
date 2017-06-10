package org.server.client.contract;

public interface Server extends InitConnection {

	public Writer getWriter() throws Exception;

	public Reader getReader() throws Exception;
	
}
