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

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FilterOutputStream;

import java.net.Socket;
import java.net.SocketException;
import java.net.ServerSocket;
import java.net.URLDecoder;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.List;

import net.mldonkey.g2gui.view.*;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.RegExp;
/**
 * DownloadSubmit
 *
 * @author $user$
 * @version $Id: DownloadSubmit.java,v 1.2 2004/03/01 21:50:07 psy Exp $ 
 *
 */
public class DownloadSubmit implements Runnable {
	private List submitList;
	private Socket socket;
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
	public DownloadSubmit( List aLink, Socket aSocket ) {
		this( aLink, aSocket, null, 0 );
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
	public DownloadSubmit( List aLink, Socket aSocket, String sHost, int sPort ) {
		if ( sPort > 0 )
			submitPort = sPort;
		if ( sHost != null )
			submitHost = sHost;

		submitList = aLink;
		socket = aSocket;

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
    			serveLink((String) submitList.get(i));

			/* it seems we have to submit a .torrent file */
    		} else {
				System.out.println("We need to submit a torrent!");
				serveTorrent(new File( (String) submitList.get(i) ));
			}
		}
    }

    /**
     * This method submits a link (ed2k:// and http://*.torrent) to the core
     * @param link a single link
     */
    private void serveLink(String link) {
    	String decoded;

    	/* simple hex-decoding of URI */
		try {
			decoded = URLDecoder.decode( link, "UTF-8" );
		} catch (UnsupportedEncodingException e) {
			decoded = link;
		}
    	/* create a nice package and send it to the core */
		EncodeMessage linkMessage = new EncodeMessage(Message.S_DLLINK, decoded );
	    try {
	        Message.writeStream(socket, linkMessage.getHeader(), linkMessage.getContent());
	    } 
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	    linkMessage = null;
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
				server = new ServerSocket(submitPort + i);

				String link = "http://" + getMyIP() + ":" + server.getLocalPort() + "/" + localfile.getName() + ".torrent";
				if (G2Gui.debug) System.out.println("INTERNAL HTTPD: " + link + " (waiting max " + SERVETIME + " seconds)");
				
				serveLink(link);
				/* is it dangerous to send the link before the internal httpd has been set up?
				 * This could produce complications if the mld-core machine is really fast and
				 * the local gui-machine is very very slow.
				 * Multithreading this part is propably better in the future...	 */
				try {
					/* listen for incoming connections */
					Socket socket = server.accept();
					/* we got connected, proceed... */
					try {
						String GETfile = "";
						
						if (G2Gui.debug) System.out.println("CONNECT: " + socket.toString());
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
						
						/* read the first line to see if its a HTTP-request */
						String string = in.readLine();
						if (string.startsWith("GET")) {
							
							/* which file does the other side request? */
							if (RegExp.split(string, ' ').length > 1)  
								GETfile = RegExp.split(string, ' ')[1];
							
							/* remove our added trailing ".torrent" */
							GETfile = GETfile.substring(0, GETfile.length() - ".torrent".length());
							
							/* locate our local file */
							File file = new File(localfile.getParent() + new File(GETfile).getAbsoluteFile());
							if (G2Gui.debug) System.out.println("REQUEST: " + GETfile + ", we should have it at: " + file);
							
							if (file.exists()) {
								out.println("HTTP/1.1 200 OK");
								out.println("Server: G2gui Inbuilt Torrent Proxy");
								out.println("Content-Type: application/x-bittorrent");
								out.println("Content-Length: " + file.length());
								/* after this empty line we can start the content-transmission */
								out.println("");
								
								/* read the file-contents */
								byte[] b = new byte[(int) file.length()];
								try {
									FileInputStream fis = new FileInputStream(file);
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
				if (G2Gui.debug) System.out.println("Socket-binding exception: " + e);
			}
		}
		if (G2Gui.debug) System.out.println("Torrent file served sucessfully!");
    }
    
    /**
     * This method tries to determine our network-adapter IP, which is to be
     * used for serving a .torrent file
     * 
     * @return a string which contains our IP
     * @throws SocketException when IP-determination somehow went wrong
     */
    private String getMyIP() throws SocketException {
    	String myIP = "";
		
    	if ( submitHost != null ) {
    		if (G2Gui.debug) System.out.println("Overriding IP-autodetection!");
    		return submitHost;
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

}

/*
$Log: DownloadSubmit.java,v $
Revision 1.2  2004/03/01 21:50:07  psy
replaced java's split()-method with our own one

Revision 1.1  2004/03/01 21:00:31  psy
* Moved linksubmitter from G2gui.java to DownloadSubmit.java
* added .torrent http-server

*/