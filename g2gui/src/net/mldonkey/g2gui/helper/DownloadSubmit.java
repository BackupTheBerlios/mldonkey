/*
 * Copyright 2003
 * g2gui Team
 * 
 * 
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.view.G2Gui;

import org.eclipse.swt.SWT;


/**
 * DownloadSubmit
 *
 * @author $user$
 * @version $Id: DownloadSubmit.java,v 1.11 2004/03/29 00:07:16 psy Exp $ 
 *
 */
public class DownloadSubmit implements Runnable {
	private List submitList;
	private CoreCommunication core;
	private String submitHost;
	private int submitPort = 4081;
	
	/* how many seconds should the torrent be served? */
	private final static int SERVETIME = 30;

	
	/**
	 * This contructor sets up the basic data for the submitter and
	 * starts a submitter-thread in the background. It waits a few seconds
	 * for the links to be submitted
	 * 
	 * @param aLink a List of links and/or torrent-files
	 * @param aSocket a raw established socket to the mldonkey core
	 */
	public DownloadSubmit( List aLink, CoreCommunication aCore ) {
		this( aLink, aCore, null, 0 );
	} 

	/**
	 * This contructor sets up the basic data for the submitter and
	 * starts a submitter-thread in the background. It waits a few seconds
	 * for the links to be submitted
	 * 
	 * @param aLink a List of links and/or torrent-files
	 * @param aSocket a raw established socket to the mldonkey core
	 * @param sHost which host/ip should we tell the core to get the torrent from?
	 * @param sPort which port should we tell the core to get the torrent from?
	 */
	public DownloadSubmit( List aLink, CoreCommunication aCore, String sHost, int sPort ) {
		if ( sPort > 0 )
			submitPort = sPort;
		if ( sHost != null )
			submitHost = sHost;

		submitList = aLink;
		core = aCore;

		/* set up the submitter as daemon-thread */
		Thread submitter = new Thread(this);
		submitter.setDaemon(true);
		submitter.start();
		
		int counter = 0;
		/* we sleep up to 20 seconds, waiting for the thread */ 
		while (submitter.isAlive() && counter++ < SERVETIME * 10) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e3) {
				if (G2Gui.debug) System.out.println("Interrupted sleep: " + e3);
			}
		}
	}
	
	/**
     * Send a link via raw socket without a core
     */
    public void run() {
    	for ( int i = 0; i < submitList.size(); i++ ) {

    		/* do a regular link-submit if its not a file */
    		if (!(new File((String) submitList.get(i)).exists())) {
    			sendLink((String) submitList.get(i));

			/* it seems we have to submit a .torrent file */
    		} else {
				System.out.println("We need to submit a torrent!");
				serveTorrent(new File( (String) submitList.get(i) ));
			}
		}
    }

    private String hexDecode(String string) {
    	try {
			return(URLDecoder.decode( string, "latin1" ));
		} catch (UnsupportedEncodingException e) {
			return(string);
		}
    }
    
    private String hexEncode(String string) {
    	try {
			/* decode the string completely and encode it again to prevent
			 * multiple encoding */
    		string = URLEncoder.encode( hexDecode(string), "latin1" );

			/* revert some important parts of the URL */
			string = RegExp.replaceAll(string, "%3A", ":");
			string = RegExp.replaceAll(string, "%2F", "/");
			string = RegExp.replaceAll(string, "%3a", ":");
			string = RegExp.replaceAll(string, "%2f", "/");
			string = RegExp.replaceAll(string, "\\+", "%20");
			
			return(string);

    	} catch (UnsupportedEncodingException e) {
			System.out.println("Encoding Exception: " + e);
    		return(string);
		}
    }
    
    /**
     * This method submits a link (ed2k://) to the core
     * @param link a single link
     */
    private void sendLink(String link) {
    	/* ed2k://-links are hexDecoded for nicer filenames,
     	 * http://-links are hexEncoded */
		if ( link.toLowerCase().startsWith("ed2k://") ) {
			link = hexDecode(link);
		} else {
			link = hexEncode(link);
		}
		if (G2Gui.debug) System.out.println("SENDING: " + link);
		
		if (link.toLowerCase().startsWith("http://") && !link.toLowerCase().endsWith(".torrent") ) {

			/* TODO do we have an opcode for http submits, 
			 * so we don't need this ugly console command? */
            String[] command = new String[] { "http " + link };
            new EncodeMessage(Message.S_CONSOLEMSG, command).sendMessage(core);

		} else {
			/* create a nice package and send it to the core */
			new EncodeMessage(Message.S_DLLINK, link ).sendMessage(core);
		}
    }

    /**
     * This method sets up a very rudimentary HTTP-server and tells the mldonkey core
     * to download a .torrent file from it
     * 
     * @param localfile our local torrent-file which should be served to the core
     */
    private void serveTorrent(File localfile) {
		ServerSocket server;
		/* search for a free port we can bind to */
		for (int i = 0; i <= 20; i++) {
			
			try {				
				server = new ServerSocket(submitPort+i);
				String link = "http://" + getMyIP() + ":" + server.getLocalPort() + "/g2gui-prepared.torrent";
				if (G2Gui.debug) System.out.println("INTERNAL HTTPD: " + link + " (waiting max " + SERVETIME + " seconds)");
								
				sendLink(link);				
				/* is it dangerous to send the link before the internal httpd has been set up?
				 * This could produce complications if the mld-core machine is really fast and
				 * the local gui-machine is very very slow.
				 * Multithreading this part is propably better in the future...	 */
				try {
					/* listen for incoming connections */
					Socket socket = server.accept();
					/* we got connected, proceed... */
					try {
						String requestedFile = "";
						
						if (G2Gui.debug) System.out.println("CONNECT: " + socket.toString());
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
						
						/* read the first line to see if its a HTTP-request */
						String string = in.readLine();
						if (string.startsWith("GET")) {
							
							/* which file does the other side request? */
							if (RegExp.split(string, ' ').length > 1)  
								requestedFile = RegExp.split(string, ' ')[1];

							if (G2Gui.debug) System.out.println("REQUEST: " + requestedFile + ", sending now: " + localfile);
							
							if (localfile.exists()) {
								out.println("HTTP/1.1 200 OK");
								out.println("Server: G2gui Inbuilt Torrent Proxy");
								out.println("Content-Type: application/x-bittorrent");
								out.println("Content-Length: " + localfile.length());
								/* after this empty line we can start the content-transmission */
								out.println("");
								
								/* read the file-contents */
								byte[] b = new byte[(int) localfile.length()];
								try {
									FileInputStream fis = new FileInputStream(localfile);
									fis.read(b);
									fis.close();
								} 
								catch (FileNotFoundException e) { }
								catch (IOException e) { }
								
								/* send the file-contents to the socket */
								FilterOutputStream contentout = new FilterOutputStream(socket.getOutputStream());
								try {
									contentout.write(b);
								}
								catch (IOException e) { }

								if (G2Gui.debug) System.out.println("Torrent file served sucessfully!");
							}
						}
		
					} finally {
						socket.close();
					}
				} finally {
					server.close();
				}
				/* when this break is reached, we sucessfully bound to a port */
				break;
			/* otherwise the for-loop will try with the next port */
			} catch (IOException e) {
				if (G2Gui.debug) System.out.println("Socket-binding exception: "+e);
			}
		}
		
    }
    
    /**
     * This method tries to determine our network-adapter IP, which is to be
     * used for serving a .torrent file
     * 
     * @return a string which contains our IP
     * @throws SocketException when IP-determination somehow went wrong
     */
    private String getMyIP() throws IOException  {
    	String myIP = "";
		
    	if ( submitHost != null ) {
    		if (G2Gui.debug) System.out.println("Overriding IP-autodetection!");
    		return submitHost;
    	}
    	
    	if (SWT.getPlatform().equals("win32")){
    		return getWin32IP();
    	}
    	/* get a list of network-devices */
    	Enumeration test = NetworkInterface.getNetworkInterfaces();			

		/* cycle through the devices */
		while ( test.hasMoreElements() ) {
			NetworkInterface netface = (NetworkInterface) test.nextElement(); 
			
			/* get a list of all IPs for each device */
			Enumeration ips = netface.getInetAddresses();
			while (ips.hasMoreElements()) {
				InetAddress addr = (InetAddress) ips.nextElement();
				String temp = addr.getHostAddress();
				
				/* check if this is a usable IP for serving our torrent */
				if ( RegExp.split(temp, ':').length > 1 || addr.isLoopbackAddress() ) {
					if (G2Gui.debug) System.out.println("BAD adress: " + temp);
				} else {
					if (G2Gui.debug) System.out.println("GOOD adress: " + temp);
					myIP = temp;
				}
			}
	    }	
		return myIP;
    }

	/**
	 * @return the Ip-adress determined by ipconfig.exe (only win32)
	 */
	private String getWin32IP() throws IOException {
		String ipAdress = "0.0.0.0";
		System.out.println("win32 IP-detection started");
		File executable = new File("ipconfig.exe");
		Process execProcess = Runtime.getRuntime().exec(executable.toString());

		BufferedReader in = new BufferedReader(new InputStreamReader(execProcess.getInputStream()));
		String buffer = "";
		/* now searching for IP-adress in STDOUT : */
		while (((buffer = in.readLine()) != null)) {			
			/* strip all leading " " */
			while (buffer.startsWith(" ")||buffer.startsWith("\t")) {				
				buffer = buffer.substring(1);
			}			
			/* is this the line we want? */
			if ( buffer.startsWith("IP") || buffer.startsWith("Ip") ) {				
				String[] parts = RegExp.split(buffer, ':');
				String tempIp=ipAdress;
				
				if ( parts.length == 2 ) {
					/* the second entry in this array is the IP-adress */
					tempIp = parts[1];
					
					/* now remove " " at beginning and end */
					while (tempIp.startsWith(" ")||tempIp.startsWith("\t")) {
						tempIp = tempIp.substring(1);
					}
					while (ipAdress.endsWith(" ")) {
						tempIp = tempIp.substring(0, ipAdress.length() - 1);
					}
				}				
				/*now check if we have found 0.0.0.0 or a real IP*/
				if ( !tempIp.equals("0.0.0.0") ){
					ipAdress=tempIp;
				}
			} 
			
		}
		if (ipAdress.equals("0.0.0.0")){	
			/*we were not able to determine IP adress is such a way*/
			throw new IOException();
						
		}
		return ipAdress;
	}

}

/*
$Log: DownloadSubmit.java,v $
Revision 1.11  2004/03/29 00:07:16  psy
added matching and submission for http-links for the FileTP network

Revision 1.10  2004/03/16 12:54:32  dek
win32 IP-detection using ipconfig

Revision 1.9  2004/03/06 17:12:51  psy
removed filename-checks, we don't really need them and they made problems with gcj hex-en-/de-coding

Revision 1.8  2004/03/04 18:00:33  psy
*** empty log message ***

Revision 1.7  2004/03/04 17:41:54  psy
*** empty log message ***

Revision 1.6  2004/03/04 17:39:49  psy
*** empty log message ***

Revision 1.5  2004/03/04 17:23:08  psy
fix error when torrent file was in root (/, C:\) directory

Revision 1.4  2004/03/02 23:39:29  psy
replaced raw-socket link-submission

Revision 1.3  2004/03/02 00:23:26  psy
more hex-encoding related stuff

Revision 1.2  2004/03/01 21:50:07  psy
replaced java's split()-method with our own one

Revision 1.1  2004/03/01 21:00:31  psy
* Moved linksubmitter from G2gui.java to DownloadSubmit.java
* added .torrent http-server

*/