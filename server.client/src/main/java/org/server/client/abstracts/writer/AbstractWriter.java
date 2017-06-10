package org.server.client.abstracts.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;

import org.server.client.contract.Writer;

public abstract class AbstractWriter implements Writer {

	private WeakReference<Socket> socket;
	protected WeakReference<OutputStream> stream;

	private boolean isWriteConnectionClosed;

	public void setSocket(Socket socket) {
		this.socket = new WeakReference<Socket>(socket);
	}

	protected Socket getSocket() {
		return socket.get();
	}

	public void flush() {
		try {
			socket.get().getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void flushAndClose() {
		flush();
		try {
			socket.get().getOutputStream().close();
			isWriteConnectionClosed = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isClosed() {
		return isWriteConnectionClosed;
	}

}