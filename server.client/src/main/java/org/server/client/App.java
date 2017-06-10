package org.server.client;

import java.io.File;
import java.io.FileInputStream;

import org.server.client.contract.Reader;
import org.server.client.contract.Reader.RESPONSE_TYPE;
import org.server.client.contract.Server;
import org.server.client.contract.Wifi;
import org.server.client.contract.Work;
import org.server.client.contract.Writer;
import org.server.client.factory.imple.WifiFactory;
import org.server.client.thread.ThreadUtilityFactory;
import org.server.client.thread.WorkerThread;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Exception {
		File logFile = new File("app.log");
		if (!logFile.exists())
			logFile.createNewFile();
		//System.setOut(new PrintStream(logFile));
		//System.setErr(new PrintStream(logFile));
		Wifi wifi = WifiFactory.getInstance();
		final Server server = wifi.getServer();
		server._init(null, 8987, 0);
		WorkerThread.getWorker().startWorking(new Work() {

			@Override
			public void doWork() {
				while (true) {
					Runtime runtime = Runtime.getRuntime();
					System.out.println("Total Memory:" + (runtime.totalMemory() / 1000000) + " M.B." + " free Memory:"
							+ (runtime.freeMemory() / 1000000) + " M.B. " + " memory consumed by app:"
							+ ((runtime.totalMemory() - runtime.freeMemory()) / 1000000) + " M.B.");
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
		while (true) {
			WorkerThread.getWorker().startWorking(new Work() {

				@Override
				public void doWork() {
					try {
						System.out.println("Statring work of main Thread..");
						// while (true) {

						Reader reader = server.getReader();
						System.out.println("Response from vernetwork client:" + reader.read(RESPONSE_TYPE.STRING));
						// writer.write("Hi this server checking multiple
						// clients....".getBytes());
						// writer.flushAndClose();

						Writer writer = server.getWriter();
						System.out.println("Got the writer now waiting ofr the reader..");
						FileInputStream inputStream = new FileInputStream(new File("D:/cdlsi_mitsubishi_logo.jpg"));
						byte[] buffer = new byte[(int) new File("D:/cdlsi_mitsubishi_logo.jpg").length()];
						inputStream.read(buffer);
						writer.write(buffer);
						inputStream.close();
						writer.flushAndClose();
						ThreadUtilityFactory.getInstance().removeAll();
						Thread.sleep(300);
						// }
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			try {
				synchronized (server) {
					server.wait();
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
