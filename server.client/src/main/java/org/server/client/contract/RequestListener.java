package org.server.client.contract;

public interface RequestListener {

	public void processRequest(Reader reader, Writer writer);

}
