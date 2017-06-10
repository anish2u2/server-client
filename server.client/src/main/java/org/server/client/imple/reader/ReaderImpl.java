package org.server.client.imple.reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

import org.server.client.abstracts.reader.AbstractReader;

public class ReaderImpl extends AbstractReader {

	public Object read(RESPONSE_TYPE responseType) {
		Object response = null;

		try {
			if (stream == null || stream.get() == null)
				stream = new WeakReference<InputStream>(getSocket().getInputStream());
			if (RESPONSE_TYPE.INPUT_STREAM == responseType) {
				response = stream.get();
			} else if (RESPONSE_TYPE.STRING == responseType) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream.get()));
				StringBuffer buffer = new StringBuffer();
				String line = reader.readLine();
				while (line != null && !line.equalsIgnoreCase("")) {
					buffer.append(line);
					line = reader.readLine();
				}
				response = buffer.toString();
			} else {
				ByteManipulator byteManipulator = new ByteManipulator();
				byte[] buffer = new byte[4096];
				while ((stream.get().read(buffer)) != -1) {
					byteManipulator.readBytes(buffer);
				}
				response = byteManipulator.getByteData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public final class ByteManipulator {
		private StringBuffer buffer;

		public ByteManipulator() {
			buffer = new StringBuffer();
		}

		public void readBytes(byte[] data) {
			buffer.append(data);
		}

		public byte[] getByteData() {
			return buffer.toString().getBytes();
		}

		public String getStringValue() {
			return buffer.toString();
		}
	}

	@Override
	public String getRequestAddress() {
		String address = getSocket().getLocalAddress().getCanonicalHostName();
		return address;
	}
}
