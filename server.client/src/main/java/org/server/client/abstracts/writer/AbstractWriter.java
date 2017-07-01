package org.server.client.abstracts.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;

import org.server.client.contract.Writer;

public abstract class AbstractWriter implements Writer {

	private WeakReference<Socket> socket;
	protected WeakReference<OutputStream> stream;

	public void setSocket(Socket socket) {
		this.socket = new WeakReference<Socket>(socket);
	}

	protected Socket getSocket() {
		return socket.get();
	}

	public void flushAndClose() {
		flush();
		try {
			if (!socket.get().isClosed())
				socket.get().getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isClosed() {
		return getSocket().isClosed();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		socket = null;
		stream = null;
	}
}
