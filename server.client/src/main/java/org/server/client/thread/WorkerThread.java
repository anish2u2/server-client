package org.server.client.thread;

import java.util.Map;
import java.util.WeakHashMap;

import org.server.client.contract.ThreadUtility;
import org.server.client.contract.Work;
import org.server.client.contract.Worker;

public class WorkerThread extends Thread implements Worker {

	private static final Map<Object, Object> THREAD_RESOURCES = new WeakHashMap<Object, Object>();
	private static int THREAD_COUNT;
	private int threadNumber;
	private Work work;
	private boolean isStopCommanExecuted;

	private WorkerThread() {
		THREAD_COUNT++;
		threadNumber = THREAD_COUNT;
		THREAD_RESOURCES.put(THREAD_COUNT, this);
		System.out.println("Numer of Thread:" + THREAD_RESOURCES.size());
	}

	public static Worker getWorker() {
		Worker worker = findFreeWorkerFromThreadResources();
		if (worker == null) {
			worker = new WorkerThread();
			System.out.println("Thread is busy so created new thread...");
		}
		return worker;
	}

	private static Worker findFreeWorkerFromThreadResources() {
		for (Object key : THREAD_RESOURCES.keySet()) {
			System.out.println("Checking thread...");
			if (((WorkerThread) THREAD_RESOURCES.get(key)).isCurretWorkCompletedByThisWorker()) {
				return (WorkerThread) THREAD_RESOURCES.get(key);
			}
		}
		return null;
	}

	public void startWorking(Work work) {
		this.work = work;
		this.start();
	}

	public void stopWork() {
		isStopCommanExecuted = true;
		releaseResources();
	}

	public void takeBreak() {
		try {
			Thread.sleep(3000);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isCurretWorkCompletedByThisWorker() {
		ThreadUtility threadUtility = ThreadUtilityFactory.getInstance();
		if (threadUtility.get("isCurrentWorkDone") == null) {
			System.out.println("Curent work done is null..");
			return false;
		}
		System.out.println("Returning current work done flage..");
		return (Boolean) threadUtility.get("isCurrentWorkDone");
	}

	@Override
	public void run() {
		// while (true) {
		if (work != null && !isStopCommanExecuted) {
			System.out.println("Starting work.");
			synchronized (this) {
				work.doWork();
				this.notify();
			}
			work = null;
		}
		if (isStopCommanExecuted)
			return;
		// }
	}

	private void releaseResources() {
		work = null;
		THREAD_RESOURCES.remove(threadNumber);
		THREAD_RESOURCES.clear();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		for (Object key : THREAD_RESOURCES.keySet()) {
			((Worker) THREAD_RESOURCES.get(key)).stopWork();
		}
		THREAD_RESOURCES.clear();
	}
}
