package org.server.client.contract;

import java.io.InputStream;

public interface Reader extends StreamInitializer, Concurrent {

	public String getRequestAddress();

	public enum RESPONSE_TYPE {
		 STRING, BYTE, INPUT_STREAM
	};
	
	public void readFile(String dirPath);
	
	public InputStream getInputStream();

	public Object read(RESPONSE_TYPE responseType);

	public void close();

}
