/*
 * Created on 14.06.2003
 */
package net.mldonkey.g2gui.model;

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * @author markus
 */
public class ConsoleMessage implements Information {
	/**
	 * String the core wants to display on the console
	 */
	private String consoleMessage;
	
	/**
	 * @return a string
	 */
	public String getConsoleMessage()
	{
		return consoleMessage;
	}

	/**
	 * @param string a string
	 */
	public void setConsoleMessage( String string )
	{
		consoleMessage = string;
	}
	
	/**
	 * Reads a ConsoleMessage object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setConsoleMessage( messageBuffer.readString() );
	}

}
