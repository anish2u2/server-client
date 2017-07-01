package org.server.client.contract;

public interface Writer extends StreamInitializer, Concurrent {

	public void write(byte[] data);

	public void writeLineFeed();

	public void flush();

	public void writeUTF(String message);

	public void writeFile(String fileName);

	public void flushAndClose();

}
