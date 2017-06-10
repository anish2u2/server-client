package org.server.client.factory.contracts;

import org.server.client.contract.Reader;
import org.server.client.contract.Writer;

public interface StreamFactory {

	public Writer getWriteStream();

	public Reader getReader();

}
