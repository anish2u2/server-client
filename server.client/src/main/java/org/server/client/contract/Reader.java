package org.server.client.contract;

public interface Reader extends StreamInitializer, Concurrent {
	
	public String getRequestAddress(); 
	
	public enum RESPONSE_TYPE {
		FILE, STRING, BYTE, INPUT_STREAM
	};

	public Object read(RESPONSE_TYPE responseType);

	public void close();

}
