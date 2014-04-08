package com.iitk;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class GetProcessRunning {

	
	public void executeCommand(String command)  throws JSchException, InterruptedException, IOException{
		command+="\n";
		System.out.println(command);
		JSch jsch = new JSch();
	    
	    // TODO: You will probably want to use your client ssl certificate instead of a password
	    // jsch.addIdentity(new File(new File(new File(System.getProperty("user.home")), ".ssh"), "id_rsa").getAbsolutePath());

	    Session session = jsch.getSession(VMDetails.VM_USERNAME, VMDetails.VM_IP_ADDRESS, 22);
	    session.setPassword(VMDetails.VM_PASSWORD);
	    session.setConfig("StrictHostKeyChecking", "no");

	    // session.connect(); - ten second timeout
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
	    session.disconnect();
		
	}
	
	public List<String> parsePID(String searchString) throws IOException{
		//FileInputStream fis=new FileInputStream(new File("abc.txt"));
		FileReader fr=new FileReader(new File("abc.txt"));
		BufferedReader br=new BufferedReader(fr);
		List<String> ls=new ArrayList<String>();
		String s;
		while((s=br.readLine())!=null){
			System.out.println("test::"+s);
			if(!s.contains(searchString))continue;
			String[] arr=s.split("[ ]+");
			ls.add(arr[1]);
			
		}
		br.close();
		return ls;
		
	}
}
