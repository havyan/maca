package com.maca.log;

import java.util.Date;

public class DefaultMacaLogger implements MacaLogger {

	public void error(Throwable e) {
		e.printStackTrace();
	}

	public void info(Exception e) {
		System.out.println(new Date().toString() + "[Info]: " + e.getMessage());
	}

	public void info(String msg) {
		System.out.println(new Date().toString() + "[Info]: " + msg);
	}

	public void error(String msg) {

	}

	public void warn(String msg) {

	}

	public void debug(String msg) {
		System.out.println(new Date().toString() + "[Debug]: " + msg);
	}

}
