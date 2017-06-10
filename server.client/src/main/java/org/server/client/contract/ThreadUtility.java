package org.server.client.contract;

import java.util.Map;

public interface ThreadUtility {

	public void add(Object key, Object value);

	public void remove(Object key);

	public void add(Map<Object, Object> map);

	public void removeAll();

	public Object get(Object key);

	public Map<Object, Object> getMap();

}
