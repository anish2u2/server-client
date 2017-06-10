package org.server.client.imple.writer;

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
			if (stream == null || stream.get() == null)
				stream = new WeakReference<OutputStream>(getSocket().getOutputStream());
			stream.get().write(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void lock() throws Exception {
		this.lock.lock();
	}

	public void unlock() throws Exception {
		this.lock.unlock();
	}

}
