/*
 * Created on 14.06.2003
 */
package net.mldonkey.g2gui.model;

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * @author markus
 */
public interface InfoList {
	/**
	 * Update an InformationObject from a MessageStream
	 * @param messageBuffer
	 */
	public void update( MessageBuffer messageBuffer); 
	/**
	 * Reads an InformationObject from a MessageStream
	 * @param messageBuffer
	 */
	public void readStream( MessageBuffer messageBuffer );
}
