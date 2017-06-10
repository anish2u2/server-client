package org.server.client.factory.imple;

import org.server.client.factory.abstracts.AbstractStreamFactory;
import org.server.client.factory.contracts.StreamFactory;

public class StreamFactoryHandler extends AbstractStreamFactory {

	private static StreamFactory streamFactory;

	private StreamFactoryHandler() {

	}

	public static StreamFactory getInstance() {
		if (streamFactory == null) {
			streamFactory = new StreamFactoryHandler();
		}

		return streamFactory;
	}

}
