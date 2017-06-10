package org.server.client.contract;

public interface Concurrent {

	public void lock() throws Exception;

	public void unlock() throws Exception;
}
