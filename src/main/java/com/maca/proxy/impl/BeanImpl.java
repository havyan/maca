/**
 * 
 */
package com.maca.proxy.impl;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maca.events.ChangeAdapter;
import com.maca.events.ChangeEvent;
import com.maca.events.ChangeListener;
import com.maca.events.PropertyChangeListenerProxy;
import com.maca.log.Logger;
import com.maca.proxy.DynamicObjectFactory2;
import com.maca.proxy.interfaces.AbstractBean;
import com.maca.proxy.interfaces.Bean;
import com.maca.proxy.interfaces.DynamicCollection;
import com.maca.utils.MacaUtils;
import com.rits.cloning.Cloner;

/**
 * @author HWYan
 * 
 */
public class BeanImpl extends AbstractBean<Object> implements ChangeListener {

	private List<String> changes = new ArrayList<String>();

	private Map<String, Bean> complexes = new HashMap<String, Bean>();

	public BeanImpl() {
		this(null);
	}

	public BeanImpl(Object source) {
		super(source);
	}

	@Override
	public void setProperty(String propertyName, Object value) {
		Logger.debug("begin set propety name @" + propertyName);
		if (source != null) {
			Object current = MacaUtils.getProperty(source, propertyName);
			if ((current != null && value != null && !current.equals(value)) || current != value) {
				Bean bean = null;
				if (value instanceof Bean) {
					bean = (Bean) value;
					value = bean.getSource();
				}
				MacaUtils.setProperty(source, propertyName, value);
				changes.add(propertyName);
				removeBean(propertyName);
				if (bean != null) {
					addBean(propertyName, bean);
				}
				value = MacaUtils.getProperty(source, propertyName);
				firePropertyChange(null, propertyName, current, value);
			}
		}
		Logger.debug("end set propety name @" + propertyName);
	}

	@Override
	public Object getProperty(String propertyName) {
		if (source != null) {
			Object value = MacaUtils.getProperty(source, propertyName);
			Bean complex = complexes.get(propertyName);
			if (complex != null && complex.getSource() == value) {
				return complex;
			} else {
				if (value != null && !value.getClass().isPrimitive() && !Modifier.isFinal(value.getClass().getModifiers())) {
					return addBean(propertyName, value);
				}
				return value;
			}
		}
		return null;
	}
	
	protected void removeBean(String propertyName) {
		Bean bean = complexes.remove(propertyName);
		if (bean != null) {
			if (bean instanceof DynamicCollection) {
				DynamicCollection dynamicCollection = (DynamicCollection) bean;
				dynamicCollection.removeChangeListenerByFrom(this);
			}
			bean.removeAllPropertyChangeListenerFrom(this);
		}
	}

	protected Bean addBean(String propertyName, Object value) {
		Bean bean = null;
		if (value instanceof Bean) {
			bean = (Bean) value;
		} else {
			bean = (Bean) DynamicObjectFactory2.createDynamicObject(value);
		}
		if (bean instanceof DynamicCollection) {
			DynamicCollection dynamicCollection = (DynamicCollection) bean;
			if (!dynamicCollection.hasChangeListenerFrom(this)) {
				dynamicCollection.addChangeListener(new ChangeAdapter(this) {
					public void change(ChangeEvent e) {
						firePropertyChange(null, propertyName, null, e.getSource());
					}
				});
			}
		}
		if (!bean.hasPropertyChangeListenerFrom(this)) {
			bean.addPropertyChangeListener(new PropertyChangeListenerProxy(this) {
				public void propertyChange(PropertyChangeEvent e) {
					List<Object> chain = MacaUtils.getChain(e);
					if (!chain.contains(BeanImpl.this.source)) {
						firePropertyChange(chain, propertyName + "." + e.getPropertyName(), e.getOldValue(), e.getNewValue());
					}
				}
			});
		}
		complexes.put(propertyName, bean);
		return bean;
	}

	@Override
	public void change(ChangeEvent e) {

	}

	@Override
	public boolean isChanged() {
		if (changes.size() > 0) {
			return true;
		} else {
			boolean changed = false;
			for (Map.Entry<String, Bean> entry : complexes.entrySet()) {
				changed = changed || entry.getValue().isChanged();
			}
			return changed;
		}
	}

	@Override
	public Object cloneSource() {
		Cloner cloner = new Cloner();
		Object target = cloner.shallowClone(this.source);
		for (Map.Entry<String, Bean> entry: this.complexes.entrySet()) {
			MacaUtils.setProperty(target, entry.getKey(), null);
		}
		target = cloner.deepClone(target);
		for (Map.Entry<String, Bean> entry: this.complexes.entrySet()) {
			MacaUtils.setProperty(target, entry.getKey(), entry.getValue().cloneSource());
		}
		return target;
	}

}
