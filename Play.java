package org.oscar.cryptobionic;

import com.jcraft.jsch.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import javax.imageio.*;
import java.util.HashMap;


public class Play{
    
	JSch jsch;// = new JSch();
	String host;
	String user;
	String password;
	Session session;
	UserInfo ui;
	Channel channel;
	
	public Play() {
		connect();
	}
	
	void killVLC()
	{
		try{
			String command = "killall vlc"; 
			channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(command); 
			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);
			channel.connect();
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
	}
	
	Image getPicture(String filename, String movie)
	{
		Image dummy = null;
		// filename is like Avatar.mkv
		// movie is like Avatar
		try {	
			String command = "/home/fabs/vlc-bin/getPicture.sh " + "\"" + filename + "\"" + " \"" + movie + "\""; 
			//System.out.println("command: " + command);
			channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);
			channel.connect();
			String picURL = readInputStream();
			URL url = new URL(picURL);
			Image image = ImageIO.read(url);
			return image;
	
		}catch(Exception e){
			System.out.println(e);
		} return dummy;
	}
	
	void connect() {
		try{
		jsch = new JSch();
		host = "oscar.zapto.org";
		user = "fabs";
		password = "cameron";
		session = jsch.getSession(user, host, 22);
		ui = new MyUserInfo();
		session.setUserInfo(ui);
		session.connect();
		} catch (Exception e) {
			System.err.println("Error Connecting");
		}
	}
	
	void playMovie(String movie)
	{
		System.out.println("Playing movie " + movie);
		try{
		
			String str2 = ":";
			if (movie.toLowerCase().contains(str2.toLowerCase()))
			{
				movie = movie.replace(": ", "/");
			}
			
			String command = "/home/fabs/vlc-bin/play.sh " + "\"" + movie + "\""; 
			System.out.println("command: " + command);
			channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(command); 
			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);
			channel.connect();
			
			
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
	}
	
	String listFiles(String name)
	{
		try{
			String command = "/home/fabs/vlc-bin/listFiles.sh " + "\"" + name + "\""; 
			channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(command); 
			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);
			channel.connect();
			
			return readInputStream();
			/*InputStream in = channel.getInputStream();
			String filelist;
			byte[] tmp = new byte[10000];
			while(true)
			{
				while(in.available()>0)
				{
					int i = in.read(tmp, 0, 10000);
					if(i < 0) break;
					return( new String(tmp, 0, i));
				}   
			 }
			*/
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		return "listFiles: Error receiving files in directory";
	}
	
	String getMovies()
	{
		try {	
		
			String command = "/home/fabs/vlc-bin/list.sh"; 
			channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);
			
			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);
			
			channel.connect();
			return readInputStream();
	
		}catch(Exception e){
			System.out.println(e);
		} return "getMovies: Error, data could not be retrieved.";
    } 

	
   String listDir(String name)
    {
	   try { 
		String command = "/home/fabs/vlc-bin/select.sh " + "\"" + name + "\""; 
		//System.out.println("listDIR: " + command);
        channel = session.openChannel("exec");
		((ChannelExec)channel).setCommand(command); 
		channel.setInputStream(null);
		((ChannelExec)channel).setErrStream(System.err);
		channel.connect();

		String movielist = null;
		
		return readInputStream();
		/*InputStream in = channel.getInputStream();
     	byte[] tmp = new byte[10000];
   	  	while(true)
      	{
   	    	while(in.available()>0)
       		{
       			int i = in.read(tmp, 0, 10000);
      			if(i < 0)break;
        		movielist = new String(tmp, 0, i);
				return movielist;
        	}   	        	
        	if(channel.isClosed())
        	{
          		//System.out.println("exit-status: "+channel.getExitStatus());
  	        	break;
 		    }
     	 }*/
      	} catch(Exception e){
  	    System.out.println(e);
    } return "listDir: Error, data could not be retrieved.";
     
    }
	
	String movieInfo(String folder, String movie)
	{
		try{
			System.out.println("passed : " + movie);
			//String command = "/home/fabs/vlc-bin/./checkMovieInfo.sh " + "\"" + movie + "\"";
			String command = "/home/fabs/vlc-bin/checkMovieInfo.sh " + "\"" + folder + "\"" + " \"" + movie + "\"";
			//System.out.println("command: " + command);
			channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(command); 
			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);
			channel.connect();
			
			/*InputStream in = channel.getInputStream();
			byte[] tmp = new byte[10000];
			while(true)
			{
				while(in.available()>0)
				{
					int i = in.read(tmp, 0, 10000);
					if(i < 0) break;
					return( new String(tmp, 0, i));
				}   
			 }
			*/
			//System.out.println(readInputStream());
			return readInputStream();
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}	
		return "movieInfo: Error in execution";
	}
	
	String readInputStream() {
		try{
		InputStream in = channel.getInputStream();
			byte[] tmp = new byte[10000];
			while(true)
			{
				while(in.available()>0)
				{
					int i = in.read(tmp, 0, 10000);
					if(i < 0) break;
					return( new String(tmp, 0, i));
				}   
			 }
			
		} catch (Exception e) {
			return("Error: " + e);
		}	
	}
	
	/*
	Image readImageInputStream() {
		try{
		InputStream in = channel.getInputStream();
		//Image image = ImageIO.read(in)
			byte[] tmp = new byte[10000];
			while(true)
			{
				while(in.available()>0)
				{
					int i = in.read(tmp, 0, 10000);
					if(i < 0) break;
					Image img = ImageIO.read(in);
					System.out.println("img: " + img);
					return img;
					//return( new String(tmp, 0, i));
				}   
			 }
			
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}	
		Image dummy = null;
		return dummy;
	}
	*/

public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{
    public String getPassword(){ return passwd; }
    public boolean promptYesNo(String str)
    {
       int foo = 0;
       return foo==0;
    }
  
    String passwd;
    JTextField passwordField=(JTextField)new JPasswordField(20);

    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){ return true; }
    public boolean promptPassword(String message){
      Object[] ob={passwordField}; 
      int result=1;
        //JOptionPane.showConfirmDialog(null, ob, message,
			//						  JOptionPane.OK_CANCEL_OPTION);
      //if(result==JOptionPane.OK_OPTION){
        //passwd=passwordField.getText();
        passwd="cameron";
        return true;
      //}
      //else{ 
        //return false; 
      //}
    }
    public void showMessage(String message){
      JOptionPane.showMessageDialog(null, message);
    }
    final GridBagConstraints gbc = 
      new GridBagConstraints(0,0,1,1,1,1,
                             GridBagConstraints.NORTHWEST,
                             GridBagConstraints.NONE,
                             new Insets(0,0,0,0),0,0);
    private Container panel;
    public String[] promptKeyboardInteractive(String destination,
                                              String name,
                                              String instruction,
                                              String[] prompt,
                                              boolean[] echo){
      panel = new JPanel();
      panel.setLayout(new GridBagLayout());

      gbc.weightx = 1.0;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.gridx = 0;
      panel.add(new JLabel(instruction), gbc);
      gbc.gridy++;

      gbc.gridwidth = GridBagConstraints.RELATIVE;

      JTextField[] texts=new JTextField[prompt.length];
      for(int i=0; i<prompt.length; i++){
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.weightx = 1;
        panel.add(new JLabel(prompt[i]),gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        if(echo[i]){
          texts[i]=new JTextField(20);
        }
        else{
          texts[i]=new JPasswordField(20);
        }
        panel.add(texts[i], gbc);
        gbc.gridy++;
      }

      if(JOptionPane.showConfirmDialog(null, panel, 
                                       destination+": "+name,
                                       JOptionPane.OK_CANCEL_OPTION,
                                       JOptionPane.QUESTION_MESSAGE)
         ==JOptionPane.OK_OPTION){
        String[] response=new String[prompt.length];
        for(int i=0; i<prompt.length; i++){
          response[i]=texts[i].getText();
        }
	return response;
      }
      else{
        return null;  // cancel
      }
    }
}

}

