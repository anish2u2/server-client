package org.server.client.imple.writer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.server.client.abstracts.writer.AbstractWriter;

public class WriterImple extends AbstractWriter {
	private final Lock lock;

	public WriterImple() {
		lock = new ReentrantLock();
	}

	public void write(byte[] data) {
		try {
			getSocket().getOutputStream().write(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void flush() {
		try {
			if (!isClosed())
				getSocket().getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void lock() throws Exception {
		this.lock.lock();
	}

	public void unlock() throws Exception {
		this.lock.unlock();
	}

	@Override
	public void writeLineFeed() {

		try {
			if (stream == null || stream.get() == null) {
				stream = new WeakReference<OutputStream>(getSocket().getOutputStream());
			}
			stream.get().write(13);
			stream.get().write(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeFile(String fileName) {
		try {
			DataOutputStream stream = new DataOutputStream(getSocket().getOutputStream());
			File file = new File(fileName);
			stream.writeUTF(file.getName());
			stream.writeLong(file.length());
			FileInputStream inputStream = new FileInputStream(file);
			byte[] buffer = new byte[4096];
			while (inputStream.read(buffer) != -1) {
				stream.write(buffer);
			}
			stream.write(buffer);
			stream.flush();
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
