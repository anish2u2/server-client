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
				System.out.println("Waiting for PRIORITY_QUEUE .. ");
				PRIORITY_QUEUE.wait();
				System.out.println("PRIORITY_QUEUE has notified..");
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
		 * System.out.println("Started waiting on Server object..");
		 * PRIORITY_QUEUE.wait(); System.out.println(
		 * "Wait is end for the server object.. .."); requestAware =
		 * threadUtility.get("currentThreadRequest") != null ? (RequestAware)
		 * threadUtility.get("currentThreadRequest") : PRIORITY_QUEUE.peek(); }
		 * if (!requestAware.isNotifyCalled()) synchronized (requestAware) {
		 * System.out.println("Started waiting on Request Aware Object object.."
		 * ); requestAware.wait(); System.out.println(
		 * "SWait is end  on Request Aware Object .."); }
		 */
		// requestAware.notify();
		System.out.println("Request found now gsending reader..");
		return requestAware.getRequestWriter();
	}

	public Reader getReader() throws Exception {
		ThreadUtility threadUtility = ThreadUtilityFactory.getInstance();
		RequestAware requestAware = threadUtility.get("currentThreadRequest") != null
				? (RequestAware) threadUtility.get("currentThreadRequest") : PRIORITY_QUEUE.poll();
		if (threadUtility.get("isRequestAwareObjectFetched") == null)
			synchronized (PRIORITY_QUEUE) {
				System.out.println("Waiting for PRIORITY_QUEUE .. while fetching reader");
				PRIORITY_QUEUE.wait();
				System.out.println("PRIORITY_QUEUE has notified.. while fetching reader");
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
			System.out.println("request aware object is null..");
		else {
			System.out.println("Object request Aware.." + requestAware);
		}
		/*
		 * if (requestAware == null) { System.out.println(
		 * "Request aware object is not found int the ThreadUtility..");
		 * synchronized (this) { System.out.println(
		 * "Started waiting on Server object..In reader"); this.wait();
		 * System.out.println("Wait is end for the server object..In Reader .."
		 * ); requestAware = threadUtility.get("currentThreadRequest") != null ?
		 * (RequestAware) threadUtility.get("currentThreadRequest") :
		 * PRIORITY_QUEUE.peek(); } }
		 * 
		 * if (!requestAware.isNotifyCalled()) synchronized (requestAware) {
		 * System.out.println(
		 * "Started waiting on Request Aware Object object..reader");
		 * requestAware.wait(); System.out.println(
		 * "Wait is end  on Request Aware Object .. reader"); }
		 */

		System.out.println(" Returning back the reader..");
		return requestAware.getRequestReader();
	}

	protected void addRequest(final Socket socket) {
		System.out.println("Adding request to queue..");
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
		System.out.println("request added to queue..:" + socket.getPort() + " ");
	}

	public void serveRequest() {
		System.out.println("Calling serve request..");
		Worker worker = WorkerThread.getWorker();
		worker.startWorking(new Work() {
			public void doWork() {
				try {
					System.out.println("Starting work of serv request..");
					while (true) {
						// System.out.println("Serving request.");
						if (!PRIORITY_QUEUE.isEmpty()) {
							System.out.println("request queue is not empty..");
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

							System.out.println("All threads are notified for handling this request..");
						}
						// System.out.println("Going to sleep..");
						Thread.sleep(700);
						// System.out.println("Waking up..");
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		System.out.println("Serv request started..");
	}

	@Override
	public void shutDown() {
		shutDownServer();
	}

}
