package org.server.client.abstracts.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.PriorityQueue;

import org.server.client.abstracts.common.AbstractInitConnection;
import org.server.client.contract.Server;
import org.server.client.contract.Work;
import org.server.client.thread.ThreadUtilityFactory;
import org.server.client.thread.WorkerThread;

public abstract class AbstractServer extends AbstractInitConnection implements Server {

	private ServerSocket server;

	@Override
	protected void initialize(String address, int port, int numberOfConnections) {
		try {
			if (address != null && port > 0 && numberOfConnections > 0)
				server = new ServerSocket(port, numberOfConnections, InetAddress.getByName(address));
			else if (port > 0 && numberOfConnections > 0) {
				server = new ServerSocket(port, numberOfConnections);
			} else {
				server = new ServerSocket(port);
			}
			System.out.println(server.getInetAddress().toString());
			InetAddress[] addresses = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
			for (InetAddress inet : addresses) {
				System.out.println("name of server:" + inet.getHostName());
				System.out.println("Server Ip:" + inet.getHostAddress());
			}
			final ServerSocket serverSocket = server;
			// final Map<Object, Object> threadLocalMap =
			// ThreadUtilityFactory.getInstance().getMap();
			final Server currentServer = this;
			serveRequest();
			System.out.println("Now trying to start server.....");
			WorkerThread.getWorker().startWorking(new Work() {
				public void doWork() {
					try {
						System.out.println("Starting work to handle server request..");
						while (true) {
							System.out.println("Starting Server.............");
							Socket socket = serverSocket.accept();
							System.out.println("Server accepted request..");
							addRequest(socket);
							// prioritySocketQueue.add(socket);
							// threadLocalMap.put("requestSockets",
							// prioritySocketQueue);
							System.out.println("Synchronizing on the server object..");
							synchronized (currentServer) {
								System.out.println("Notiying all objects..");
								currentServer.notifyAll();
							}

							Thread.sleep(600);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			System.out.println("server executed..");
		} catch (

		Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	protected Socket getSocket() {
		return (Socket) ((PriorityQueue<Socket>) ThreadUtilityFactory.getInstance().get("requestSockets")).poll();
	}

	protected abstract void addRequest(Socket socket);

	protected abstract void serveRequest();

	protected void shutDownServer() {
		shutDownServer();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		server = null;
	}
}
