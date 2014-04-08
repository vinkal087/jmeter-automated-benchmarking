package com.iitk;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class RemoteMachineStartJmeter {
	
	private Session executeCommand(String command,String username,String password,String ipaddress)  throws JSchException, InterruptedException, IOException{
		command+="\n";
		System.out.println(command);
		JSch jsch = new JSch();
	    
	 

	    Session session = jsch.getSession(username, ipaddress, 22);
	    session.setPassword(password);
	    session.setConfig("StrictHostKeyChecking", "no");

	    
	    session.connect(10*1000);
	    
	    Channel channel = session.openChannel("shell");

	    // TODO: You will probably want to use your own input stream, instead of just reading a static string.
	    InputStream is = new ByteArrayInputStream(command.getBytes());
	    channel.setInputStream(is);
	 //   ByteArrayOutputStream os=new ByteArrayOutputStream();
	    FileOutputStream fos=new FileOutputStream("abc.txt");
	    channel.setOutputStream(fos);
	 //   channel.setOutputStream(System.out);
	   
	    // channel.connect(); - fifteen second timeout
	    channel.connect(15 * 1000);
	    
	    // Wait three seconds for this demo to complete (ie: output to be streamed to us).
	    Thread.sleep(3*1000);
	    
	    // Disconnect (close connection, clean up system resources)
	    
	    channel.disconnect();
	    fos.flush();

	    fos.close();
	    return session;
	  //  session.disconnect();
		
	}
	
	ResourceBundle bundle=ResourceBundle.getBundle("Remote");
	
	public Session startJmeterRemoteI(int i,int threads,int rampup) throws JSchException, InterruptedException, IOException{
		String ipaddress=bundle.getString("REMOTE_MACHINE_IP_ADDRESS_"+i);
		String username=bundle.getString("REMOTE_MACHINE_USERNAME_"+i);
		String password=bundle.getString("REMOTE_MACHINE_PASSWORD_"+i);
		String jmeterHome=bundle.getString("REMOTE_MACHINE_JMETER_HOME_"+i);
		String command="sh "+jmeterHome+"/bin/"+"jmeter-server -Djava.rmi.server.hostname="+ipaddress+" -JNUMTHREADS="+threads+" -JRAMP="+rampup;
		return executeCommand(command, username, password, ipaddress);
		
	}
	
}
