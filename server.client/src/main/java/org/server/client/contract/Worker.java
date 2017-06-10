package org.server.client.contract;

public interface Worker {

	public void startWorking(Work work);

	public void stopWork();

	public void takeBreak();

}
