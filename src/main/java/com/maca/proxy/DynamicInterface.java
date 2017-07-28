package com.maca.proxy;

import net.sf.cglib.proxy.NoOp;

public interface DynamicInterface extends NoOp {

	public Object source();

	public void source(Object source);

}
