package com.maca.proxy.interfaces;

import java.lang.reflect.Method;

import com.maca.events.ChangeListener;

import net.sf.cglib.proxy.MethodProxy;

public interface DynamicCollection extends Bean {

	public void addChangeListener(ChangeListener l);

	public void removeChangeListener(ChangeListener l);

	public void removeChangeListenerByFrom(Object from);

	public boolean hasChangeListenerFrom(Object from);

	public Object invoke(Method method, Object[] args, MethodProxy proxy) throws Throwable;

	public void fireChange();

}
