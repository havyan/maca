/**
 * 
 */
package com.maca.proxy.interfaces;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

import com.maca.events.PropertyChangeListenerProxy;
import com.maca.proxy.DynamicObject;

/**
 * @author HWYan
 * 
 */
public interface Bean extends DynamicObject {

	public void setProperty(String propertyName, Object value);

	public Object getProperty(String propertyName);

	public void addPropertyChangeListener(PropertyChangeListener l);

	public void removePropertyChangeListener(PropertyChangeListener l);

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

	public void removePropertyChangeListenerFrom(Object from);

	public PropertyChangeListener[] getPropertyChangeListeners();

	public PropertyChangeListener[] getPropertyChangeListeners(String propertyName);

	public PropertyChangeListenerProxy[] getPropertyChangeListenersFrom(Object from);

	public PropertyChangeListenerProxy[] getPropertyChangeListenersFrom(Object from, String propertyName);

	public Map<String, PropertyChangeListenerProxy[]> getPropertyChangeListenersMapFrom(Object from);

	public void removeAllPropertyChangeListenerFrom(Object from);

	public boolean hasPropertyChangeListenerFrom(Object from);

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

	public void firePropertyChange(List<Object> chain, String propertyName, Object oldValue, Object newValue);
	
	public void fireChange();
	
	public Object cloneSource();

}
