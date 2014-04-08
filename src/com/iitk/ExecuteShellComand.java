package com.iitk;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import com.jcraft.jsch.JSchException;

import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;
 
public class ExecuteShellComand{
	
 
	private static void jmeterExecute(String[] args) throws TaskExecFailException{
		ExecuteShellComand obj = new ExecuteShellComand();
		if(args.length<6){
			System.out.println("Arguments should be 5");
			System.exit(0);
		}
		
		int initial=Integer.parseInt(args[0]);
		int increment=Integer.parseInt(args[2]);
		int maxval=Integer.parseInt(args[1]);
		String basedirectoryname=args[3]+"_jmeter";
		new File(basedirectoryname).mkdir();
		int remoteMachines=Integer.parseInt(args[4]);
		for(int i=initial;i<maxval;i+=increment){
			
			SSHExecute sshExecute=new SSHExecute(VMDetails.VM_EXECUTE_COMMAND+" "+VMDetails.VM_APACHE_MEMORY+" "+VMDetails.VM_MEMORY+" "+VMDetails.VM_NO_OF_CORES+" "+i+"into"+remoteMachines+" "+VMDetails.VM_PYTHONSCRIPT_DIRECTORYPATH);
			sshExecute.start();
			try {
				Thread.sleep(3*1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//executeRemotely();
			String directoryname=basedirectoryname+"/threads_jmeter_"+i+"into"+remoteMachines;
			new File(directoryname).mkdir();
			String jmeterCommand=Jmeter.JMETER_FULL_PATH+" -n -t "+args[5]+" -l "+directoryname+"/results.jtl -JNUMTHREADS="+i+" -JRAMP=1";
			
			if(remoteMachines>0){
				jmeterCommand+=" -r";
			}
			System.out.println("Executing.."+jmeterCommand);
			String output = obj.executeCommand(jmeterCommand);
			System.out.println(output);
			//Thread.sleep(60*1000);
			String csvCommand="java -jar "+Jmeter.JMETER_HOME+"/lib/ext/CMDRunner.jar --tool Reporter --generate-csv "+directoryname+"/Result.csv"+" --input-jtl "+directoryname+"/results.jtl --plugin-type AggregateReport";
			System.out.println("Executing.."+csvCommand);
			output=obj.executeCommand(csvCommand);
			sshExecute.stop();
			try {
				killProcess();
				String serverFoldername=VMDetails.VM_PYTHONSCRIPT_DIRECTORYPATH+"/"+VMDetails.VM_APACHE_MEMORY+"Apache_"+VMDetails.VM_MEMORY+"VM_"+VMDetails.VM_NO_OF_CORES+"CoreVM";
				String fullpath=serverFoldername+"/"+"testcase_"+i+"into"+remoteMachines;
				String processRawDataCommand="java -cp "+VMDetails.VM_PYTHONSCRIPT_DIRECTORYPATH+" com/iitk/SARRAM "+fullpath+"/sarr.log"+" "+fullpath+"/sarr.dat";
				GetProcessRunning gpr=new GetProcessRunning();
				gpr.executeCommand(processRawDataCommand);	
				processRawDataCommand="java -cp "+VMDetails.VM_PYTHONSCRIPT_DIRECTORYPATH+" com/iitk/SARPALL "+fullpath+"/sarP.log"+" "+fullpath;
				gpr.executeCommand(processRawDataCommand);
				if(VMDetails.STOP_APACHE_AFTER_ONE_CYCLE){
					System.out.println("Restarting Apache");
					processRawDataCommand="echo "+VMDetails.VM_SUDO_PASSWORD+"| sudo -S service apache2 restart";
					gpr.executeCommand(processRawDataCommand);
				}
				Thread.sleep(Jmeter.WAIT_TIME_AFTER_JMETER_CYCLE*1000);
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void killProcess() throws JSchException, InterruptedException, IOException{
		GetProcessRunning gpr=new GetProcessRunning();
	//	String command="ps -aux | grep '"+VMDetails.VM_PYTHONSCRIPT_DIRECTORYPATH+"'";
		String command="ps -aux | grep sar";
		gpr.executeCommand(command);
		System.out.println("command executed");
		//List<String> ls=gpr.parsePID(VMDetails.VM_PYTHONSCRIPT_DIRECTORYPATH);
		List<String> ls=gpr.parsePID("sar");
		command="kill -9 ";
		Iterator<String> itr=ls.iterator();
		while (itr.hasNext()) {
			String string = (String) itr.next();
			command+=string+" ";
			
		}
		System.out.println(command);
		gpr.executeCommand(command);
		
	}
	public static void main(String[] args) throws TaskExecFailException, InterruptedException, JSchException, IOException {
 		
		//connectToSSH();
		//executeRemotely(VMDetails.VM_EXECUTE_COMMAND+" "+VMDetails.VM_APACHE_MEMORY+" "+VMDetails.VM_MEMORY+" "+VMDetails.VM_NO_OF_CORES+" "+4+"into"+4+" "+VMDetails.VM_PYTHONSCRIPT_DIRECTORYPATH);
		/*String command=VMDetails.VM_EXECUTE_COMMAND+" "+VMDetails.VM_APACHE_MEMORY+" "+VMDetails.VM_MEMORY+" "+VMDetails.VM_NO_OF_CORES+" "+4+"into"+4+" "+VMDetails.VM_PYTHONSCRIPT_DIRECTORYPATH;
		SSHExecute sshExecute=new SSHExecute(command);
		sshExecute.start();
		
		
		//Thread.sleep(10000);
		killProcess();
		sshExecute.stop();*/
	
		
				
		//ssh.disconnect();
		jmeterExecute(args);
 
	}
 
	public String executeCommand(String command) {
 
		StringBuffer output = new StringBuffer();
 
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));
 
                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		return output.toString();
 
	}
 
}