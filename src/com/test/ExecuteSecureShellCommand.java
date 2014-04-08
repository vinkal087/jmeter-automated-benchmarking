package com.test;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ExecuteSecureShellCommand
{
  private static final String user = "vipin"; // TODO: Username of ssh account on remote machine
  private static final String host = "172.27.20.100"; // TODO: Hostname of the remote machine (eg: inst.eecs.berkeley.edu)
  private static final String password = "vipin"; // TODO: Password associated with your ssh account
  private static final String command = "ps -aux | grep '/home/vipin/benchmarking'\n"; // Remote command you want to invoke
  
  
  
  
  public static void main2(String args[]) throws JSchException, InterruptedException, FileNotFoundException
  {
    JSch jsch = new JSch();
    
    // TODO: You will probably want to use your client ssl certificate instead of a password
    // jsch.addIdentity(new File(new File(new File(System.getProperty("user.home")), ".ssh"), "id_rsa").getAbsolutePath());

    Session session = jsch.getSession(user, host, 22);

    // TODO: You will probably want to use your client ssl certificate instead of a password
    session.setPassword(password);

    // Not recommended - skips host check
    session.setConfig("StrictHostKeyChecking", "no");

    // session.connect(); - ten second timeout
    session.connect(10*1000);

    Channel channel = session.openChannel("shell");

    // TODO: You will probably want to use your own input stream, instead of just reading a static string.
    InputStream is = new ByteArrayInputStream(command.getBytes());
    channel.setInputStream(is);
    
    // Set the destination for the data sent back (from the server)
    // TODO: You will probably want to send the response somewhere other than System.out
    //channel.
    ByteArrayOutputStream os=new ByteArrayOutputStream();
    FileOutputStream fos=new FileOutputStream("abc.txt");
    channel.setOutputStream(fos);
  //  String tmp=new String(os.toByteArray());
   // System.out.println(tmp);
   // System.out.println(os.toString());
    
   // channel.setOutputStream(System.out);

    // channel.connect(); - fifteen second timeout
    channel.connect(15 * 1000);
    
    // Wait three seconds for this demo to complete (ie: output to be streamed to us).
    Thread.sleep(3*1000);
    
    // Disconnect (close connection, clean up system resources)
    channel.disconnect();
    session.disconnect();
  }
} 