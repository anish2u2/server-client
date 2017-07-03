package org.server.client.imple.server;

import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.PriorityQueue;

import org.server.client.abstracts.server.AbstractServer;
import org.server.client.contract.Reader;
import org.server.client.contract.RequestAware;
import org.server.client.contract.ThreadUtility;
import org.server.client.contract.Work;
import org.server.client.contract.Worker;
import org.server.client.contract.Writer;
import org.server.client.factory.contracts.StreamFactory;
import org.server.client.factory.imple.StreamFactoryHandler;
import org.server.client.logger.LoggerAPI;
import org.server.client.thread.ThreadUtilityFactory;
import org.server.client.thread.WorkerThread;

public class ServerImpl extends AbstractServer {

	private static final PriorityQueue<RequestAware> PRIORITY_QUEUE = new PriorityQueue<RequestAware>();

	private static StreamFactory streamFactory;

	static {
		streamFactory = StreamFactoryHandler.getInstance();
	}

	public Writer getWriter() throws Exception {
		ThreadUtility threadUtility = ThreadUtilityFactory.getInstance();
		RequestAware requestAware = threadUtility.get("currentThreadRequest") != null
				? (RequestAware) threadUtility.get("currentThreadRequest") : PRIORITY_QUEUE.poll();
		if (threadUtility.get("isRequestAwareObjectFetched") == null)
			synchronized (PRIORITY_QUEUE) {
				LoggerAPI.logInfo("Waiting for PRIORITY_QUEUE .. ");
				PRIORITY_QUEUE.wait();
				LoggerAPI.logInfo("PRIORITY_QUEUE has notified..");
				requestAware = PRIORITY_QUEUE.poll();
				threadUtility.add("currentThreadRequest", requestAware);
				threadUtility.add("isRequestAwareObjectFetched", true);
			}
		else {
			requestAware = (RequestAware) threadUtility.get("currentThreadRequest");
		}
		if (requestAware == null)
			requestAware = (RequestAware) threadUtility.get("currentThreadRequest");
		/*
		 * if (requestAware == null) synchronized (PRIORITY_QUEUE) {
		 * LoggerAPI.logInfo("Started waiting on Server object..");
		 * PRIORITY_QUEUE.wait(); LoggerAPI.logInfo(
		 * "Wait is end for the server object.. .."); requestAware =
		 * threadUtility.get("currentThreadRequest") != null ? (RequestAware)
		 * threadUtility.get("currentThreadRequest") : PRIORITY_QUEUE.peek(); }
		 * if (!requestAware.isNotifyCalled()) synchronized (requestAware) {
		 * LoggerAPI.logInfo("Started waiting on Request Aware Object object.."
		 * ); requestAware.wait(); LoggerAPI.logInfo(
		 * "SWait is end  on Request Aware Object .."); }
		 */
		// requestAware.notify();
		LoggerAPI.logInfo("Request found now gsending reader..");
		return requestAware.getRequestWriter();
	}

	public Reader getReader() throws Exception {
		ThreadUtility threadUtility = ThreadUtilityFactory.getInstance();
		RequestAware requestAware = threadUtility.get("currentThreadRequest") != null
				? (RequestAware) threadUtility.get("currentThreadRequest") : PRIORITY_QUEUE.poll();
		if (threadUtility.get("isRequestAwareObjectFetched") == null)
			synchronized (PRIORITY_QUEUE) {
				LoggerAPI.logInfo("Waiting for PRIORITY_QUEUE .. while fetching reader");
				PRIORITY_QUEUE.wait();
				LoggerAPI.logInfo("PRIORITY_QUEUE has notified.. while fetching reader");
				requestAware = PRIORITY_QUEUE.poll();
				threadUtility.add("currentThreadRequest", requestAware);
				threadUtility.add("isRequestAwareObjectFetched", true);
			}
		else {
			requestAware = (RequestAware) threadUtility.get("currentThreadRequest");
		}

		if (requestAware == null)
			requestAware = (RequestAware) threadUtility.get("currentThreadRequest");
		if (requestAware == null)
			LoggerAPI.logInfo("request aware object is null..");
		else {
			LoggerAPI.logInfo("Object request Aware.." + requestAware);
		}
		/*
		 * if (requestAware == null) { LoggerAPI.logInfo(
		 * "Request aware object is not found int the ThreadUtility..");
		 * synchronized (this) { LoggerAPI.logInfo(
		 * "Started waiting on Server object..In reader"); this.wait();
		 * LoggerAPI.logInfo("Wait is end for the server object..In Reader .."
		 * ); requestAware = threadUtility.get("currentThreadRequest") != null ?
		 * (RequestAware) threadUtility.get("currentThreadRequest") :
		 * PRIORITY_QUEUE.peek(); } }
		 * 
		 * if (!requestAware.isNotifyCalled()) synchronized (requestAware) {
		 * LoggerAPI.logInfo(
		 * "Started waiting on Request Aware Object object..reader");
		 * requestAware.wait(); LoggerAPI.logInfo(
		 * "Wait is end  on Request Aware Object .. reader"); }
		 */

		LoggerAPI.logInfo(" Returning back the reader..");
		return requestAware.getRequestReader();
	}

	protected void addRequest(final Socket socket) {
		LoggerAPI.logInfo("Adding request to queue..");
		PRIORITY_QUEUE.add(new RequestAware() {
			private Writer writer;
			private Reader reader;
			private boolean notify;

			private Date requestTime = new Date();

			public void onRequest() {
				try {
					socket.setKeepAlive(true);
				} catch (SocketException e) {
					e.printStackTrace();
				}
				writer = streamFactory.getWriteStream();
				reader = streamFactory.getReader();
				writer.setSocket(socket);
				reader.setSocket(socket);
			}

			public Date getDate() {
				return requestTime;
			}

			public Writer getRequestWriter() {
				return this.writer;
			}

			public Reader getRequestReader() {
				return this.reader;
			}

			public boolean isNotifyCalled() {
				return notify;
			}

			public void notifyMe() {
				notify = true;

			}

			@Override
			public int compareTo(RequestAware o) {
				return (this.getDate().after(o.getDate())) == true ? -1 : 1;
			}

		});
		LoggerAPI.logInfo("request added to queue..:" + socket.getPort() + " ");
	}

	public void serveRequest() {
		LoggerAPI.logInfo("Calling serve request..");
		Worker worker = WorkerThread.getWorker();
		worker.startWorking(new Work() {
			public void doWork() {
				try {
					LoggerAPI.logInfo("Starting work of serv request..");
					while (true) {
						// LoggerAPI.logInfo("Serving request.");
						if (!PRIORITY_QUEUE.isEmpty()) {
							LoggerAPI.logInfo("request queue is not empty..");
							/*
							 * RequestAware request = PRIORITY_QUEUE.poll();
							 * threadUtilityMap.put("currentThreadRequest",
							 * request); request.onRequest();
							 * request.notifyMe();
							 */
							PRIORITY_QUEUE.peek().onRequest();
							synchronized (PRIORITY_QUEUE) {
								PRIORITY_QUEUE.notifyAll();
							}
							/*
							 * synchronized (request) { request.notifyAll(); }
							 */

							LoggerAPI.logInfo("All threads are notified for handling this request..");
						}
						// LoggerAPI.logInfo("Going to sleep..");
						Thread.sleep(700);
						// LoggerAPI.logInfo("Waking up..");
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		LoggerAPI.logInfo("Serv request started..");
	}

	@Override
	public void shutDown() {
		shutDownServer();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		PRIORITY_QUEUE.remove();
		streamFactory = null;
	}
}
