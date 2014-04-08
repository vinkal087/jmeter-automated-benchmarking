package com.iitk;

import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

public class SSHExecute extends Thread {
		
	private static SSHExec ssh;
	private static SSHExec connectToSSH(){
		
		ConnBean cb = new ConnBean(VMDetails.VM_IP_ADDRESS, VMDetails.VM_USERNAME,VMDetails.VM_PASSWORD);
		ssh = SSHExec.getInstance(cb);
		ssh.connect();
		return ssh;
	}
	
	public  void executeRemotely(String command) throws TaskExecFailException{
		CustomTask sampleTask = new ExecCommand(command);
		System.out.println(command);
		Result rs=ssh.exec(sampleTask);
		System.out.println(rs.isSuccess);
		System.out.println("outut:"+rs.sysout.toString());
		String s=rs.sysout.toUpperCase();
		System.out.println(s);
	}
	
	String command;
	public SSHExecute(String s) {
		// TODO Auto-generated constructor stub
		command=s;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		connectToSSH();
		super.run();
		CustomTask sampleTask = new ExecCommand(command);
		Result rs;
		try {
			rs = ssh.exec(sampleTask);
			System.out.println(rs.isSuccess);
			System.out.println("outut:"+rs.sysout.toString());
			String s=rs.sysout.toUpperCase();
			System.out.println(s);
		} catch (TaskExecFailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void stop1(){
		ssh.disconnect();
	}
	
	@Override
	public void interrupt() {
		// TODO Auto-generated method stub
		super.interrupt();
		ssh.disconnect();
	}
	
	
}
