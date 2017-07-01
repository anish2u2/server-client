package org.server.client.thread;

import java.util.HashMap;
import java.util.Map;

import org.server.client.contract.ThreadUtility;

public class ThreadUtilityFactory implements ThreadUtility {

	private static final ThreadLocal<Map<Object, Object>> THREAD_LOCAL = new ThreadLocal<Map<Object, Object>>();

	private static ThreadUtility THREAD_UTILITY;

	private ThreadUtilityFactory() {
		THREAD_LOCAL.set(new HashMap<Object, Object>());
	}

	public static ThreadUtility getInstance() {
		if (THREAD_UTILITY == null)
			THREAD_UTILITY = new ThreadUtilityFactory();
		return THREAD_UTILITY;
	}

	public void add(Object key, Object value) {
		if (THREAD_LOCAL.get() == null) {
			THREAD_LOCAL.set(new HashMap<Object, Object>());
		}
		THREAD_LOCAL.get().put(key, value);

	}

	public void remove(Object key) {
		if (THREAD_LOCAL.get() != null) {
			THREAD_LOCAL.get().remove(key);
		}
	}

	public void add(Map<Object, Object> map) {
		if (THREAD_LOCAL.get() != null) {
			THREAD_LOCAL.get().putAll(map);
		} else {
			THREAD_LOCAL.set(map);
		}
	}

	public void removeAll() {
		THREAD_LOCAL.remove();
	}

	public Object get(Object key) {
		if (THREAD_LOCAL.get() != null)
			return THREAD_LOCAL.get().get(key);
		return null;
	}

	public Map<Object, Object> getMap() {
		return THREAD_LOCAL.get();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		THREAD_LOCAL.remove();
	}
}
