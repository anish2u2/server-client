package org.server.client.contract;

import java.util.Comparator;
import java.util.Date;

public interface RequestAware extends Comparable<RequestAware>{

	public void onRequest();

	public Writer getRequestWriter();

	public Reader getRequestReader();

	public boolean isNotifyCalled();

	public void notifyMe();
	
	public Date getDate();
}
