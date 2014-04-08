package com.iitk;

import java.util.ResourceBundle;

public interface Jmeter {
	ResourceBundle bundle=ResourceBundle.getBundle("Jmeter");
	String JMETER_FULL_PATH=bundle.getString("JMETER_FULL_PATH");
	String JMETER_HOME=bundle.getString("JMETER_HOME");
	int WAIT_TIME_AFTER_JMETER_CYCLE=Integer.parseInt(bundle.getString("WAIT_TIME_AFTER_JMETER_CYCLE"));
	
}
