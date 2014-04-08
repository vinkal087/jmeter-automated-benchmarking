package com.test;

import java.io.File;

import com.iitk.ExecuteShellComand;

public class JmeterTest {

	public static void main2(String[] args) {
		String directoryname=args[0];
		new File(directoryname).mkdir();
		String jmeterCommand="C:\\Users\\vinkal\\Downloads\\apache-jmeter-2.11\\apache-jmeter-2.11\\bin\\jmeter.bat -n -t cseproj50.jmx -l "+directoryname+"/results.jtl -JNUMTHREADS="+10+" -JRAMP=1";
		ExecuteShellComand obj = new ExecuteShellComand();
		String output = obj.executeCommand(jmeterCommand);
		System.out.println(output);

	}

}
