package org.server.client.imple.client;

import org.server.client.abstracts.client.AbstractClient;
import org.server.client.contract.Reader;
import org.server.client.contract.Writer;
import org.server.client.factory.imple.StreamFactoryHandler;

public class ClientImpl extends AbstractClient {

	@Override
	public Writer getWriter() {
		Writer writer = StreamFactoryHandler.getInstance().getWriteStream();
		writer.setSocket(getSocket());
		return writer;
	}

	@Override
	public Reader getReader() {
		Reader reader = StreamFactoryHandler.getInstance().getReader();
		reader.setSocket(getSocket());
		return reader;
	}

	@Override
	public void shutDown() {
		shutDownClient();
	}

}
