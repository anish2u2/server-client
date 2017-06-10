package org.server.client.contract;

public interface Writer extends StreamInitializer, Concurrent {

	public void write(byte[] data);

	public void flush();

	public void flushAndClose();

}
