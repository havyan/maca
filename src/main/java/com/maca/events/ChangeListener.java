package com.maca.events;

import java.util.EventListener;

public interface ChangeListener extends EventListener {

	void change(ChangeEvent e);

}
