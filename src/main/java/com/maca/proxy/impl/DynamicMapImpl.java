package com.maca.proxy.impl;

import java.util.Map;
import java.util.Map.Entry;

import com.maca.proxy.interfaces.AbstractBean;
import com.maca.proxy.interfaces.Bean;
import com.maca.proxy.interfaces.DynamicMap;
import com.maca.utils.MacaUtils;
import com.rits.cloning.Cloner;

public class DynamicMapImpl extends AbstractBean<Map<?, ?>> implements DynamicMap {

	public DynamicMapImpl(Map<?, ?> source) {
		super(source);
	}

	@Override
	public boolean isChanged() {
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object cloneSource() {
		Cloner cloner = new Cloner();
		Map target = (Map) MacaUtils.newInstance(this.getSource().getClass());
		for (Entry<?, ?> entry: this.getSource().entrySet()) {
			if (entry.getValue() instanceof Bean) {
				target.put(entry.getKey(), ((Bean)entry.getValue() ).cloneSource());
			} else {
				target.put(entry.getKey(), cloner.deepClone(entry.getValue()));
			}
		}
		return target;
	}

}
