package com.maca.proxy.impls;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import com.maca.utils.MacaUtils;
import com.maca.proxy.interfaces.AbstractBean;
import com.maca.proxy.interfaces.Bean;
import com.maca.proxy.interfaces.DynamicCollection;
import com.maca.proxy.interfaces.DynamicMap;
import com.rits.cloning.Cloner;

import net.sf.cglib.proxy.MethodProxy;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DynamicMapImpl extends AbstractBean<Map> implements DynamicMap {

	private Map proxy;

	private boolean changed = false;

	public DynamicMapImpl(Map source) {
		super(source);
		this.createProxy();
	}

	@Override
	public Object invoke(Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		Map beforeMap = (Map) MacaUtils.newInstance(this.source.getClass());
		beforeMap.putAll(this.proxy);
		args = this.convertArgs(args, this.proxy.values().toArray());
		Object result = methodProxy.invoke(this.proxy, args);
		if (this.isDifferent(beforeMap, this.proxy)) {
			this.syncData();
			this.fireChange(beforeMap);
		}
		return result;
	}

	private void fireChange(Map beforeMap) {
		for (Object e : beforeMap.entrySet()) {
			Map.Entry entry = (Entry) e;
			Object oldValue = entry.getValue();
			Object newValue = this.proxy.get(entry.getKey());
			if (newValue != oldValue) {
				if (newValue == null) {
					handleRemoved(oldValue);
				}
				firePropertyChange(null, entry.getKey().toString(), oldValue, newValue);
			}
		}
		for (Object e : this.proxy.entrySet()) {
			Map.Entry entry = (Entry) e;
			if (!beforeMap.containsKey(entry.getKey())) {
				firePropertyChange(null, (String) entry.getKey().toString(), null, entry.getValue());
			}
		}
	}

	protected void handleRemoved(Object removed) {
		if (removed != null && removed instanceof Bean) {
			if (removed instanceof DynamicCollection) {
				((DynamicCollection) removed).removeChangeListenerByFrom(this);
			}
			((Bean) removed).removeAllPropertyChangeListenerFrom(this);
		}
	}

	private void syncData() {
		this.source.clear();
		for (Object e : this.proxy.entrySet()) {
			Map.Entry entry = (Entry) e;
			Object value = entry.getValue();
			Object sourceValue = value;
			if (value instanceof Bean) {
				sourceValue = ((Bean) value).source();
			} else {
				value = this.convert2DynamicObject(value);
				entry.setValue(value);
			}
			if (value instanceof Bean) {
				this.bindBean(entry.getKey().toString(), (Bean) value);
			}
			this.source.put(entry.getKey(), sourceValue);
		}
	}

	protected boolean isDifferent(Map beforeMap, Map afterMap) {
		if (beforeMap.size() != afterMap.size()) {
			return true;
		} else {
			for (Object e : beforeMap.entrySet()) {
				Map.Entry entry = (Entry) e;
				if (entry.getValue() != afterMap.get(entry.getKey())) {
					return true;
				}
			}
		}
		return false;
	}

	private void createProxy() {
		proxy = (Map) MacaUtils.newInstance(this.source.getClass());
		for (Object e : source.entrySet()) {
			Map.Entry entry = (Entry) e;
			Object result = null;
			if (entry.getValue() != null) {
				result = convert2DynamicObject(entry.getValue());
				if (result instanceof Bean) {
					bindBean(entry.getKey().toString(), (Bean) result);
				}
			}
			proxy.put(entry.getKey(), result);
		}
	}

	@Override
	public boolean changed() {
		return changed;
	}

	@Override
	public Object cloneSource() {
		Cloner cloner = new Cloner();
		Map target = (Map) MacaUtils.newInstance(this.source().getClass());
		for (Object e : this.source().entrySet()) {
			Map.Entry entry = (Entry) e;
			if (entry.getValue() instanceof Bean) {
				target.put(entry.getKey(), ((Bean) entry.getValue()).cloneSource());
			} else {
				target.put(entry.getKey(), cloner.deepClone(entry.getValue()));
			}
		}
		return target;
	}

}
