package com.iitk;

import java.util.ResourceBundle;

public interface VMDetails {
	ResourceBundle bundle=ResourceBundle.getBundle("VMDetails");
	String VM_IP_ADDRESS=bundle.getString("VM_IP_ADDRESS");
	String VM_USERNAME=bundle.getString("VM_USERNAME");
	String VM_PASSWORD=bundle.getString("VM_PASSWORD");
	String VM_FOLDER_PATH=bundle.getString("VM_FOLDER_PATH");
	String VM_EXECUTE_COMMAND=bundle.getString("VM_EXECUTE_COMMAND");
	String VM_APACHE_MEMORY=bundle.getString("VM_APACHE_MEMORY");
	String VM_MEMORY=bundle.getString("VM_MEMORY");
	String VM_NO_OF_CORES=bundle.getString("VM_NO_OF_CORES");
	String VM_PYTHONSCRIPT_DIRECTORYPATH=bundle.getString("VM_PYTHONSCRIPT_DIRECTORYPATH");
	String VM_SUDO_PASSWORD=bundle.getString("VM_SUDO_PASSWORD");
	boolean STOP_APACHE_AFTER_ONE_CYCLE=Boolean.parseBoolean(bundle.getString("STOP_APACHE_AFTER_ONE_CYCLE"));
}
