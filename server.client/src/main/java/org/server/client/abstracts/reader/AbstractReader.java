package org.server.client.abstracts.reader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.server.client.contract.Reader;

public abstract class AbstractReader implements Reader {

	private WeakReference<Socket> socket;
	private Lock lock;
	protected WeakReference<InputStream> stream;

	public AbstractReader() {
		lock = new ReentrantLock();
	}

	public void setSocket(Socket socket) {
		this.socket = new WeakReference<Socket>(socket);
	}

	protected Socket getSocket() {
		return socket.get();
	}

	public boolean isClosed() {
		return socket.get().isClosed();
	}

	public void lock() throws Exception {
		lock.lock();
	}

	public void unlock() throws Exception {
		lock.unlock();
	}

	public void close() {
		try {
			if (!socket.get().isClosed())
				socket.get().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		socket = null;
		lock = null;
		stream = null;
	}

}
