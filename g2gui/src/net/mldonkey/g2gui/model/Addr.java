/*
 * Created on 14.06.2003
 */
package net.mldonkey.g2gui.model;

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * @author markus
 */
public class Addr implements Information {
	/**
	 * Address Type
	 */
	private boolean addressType;
	/**
	 * IP Address (if type is true)
	 */
	private int ipAddress;
	/**
	 * Name Address (if type is false
	 */
	private String nameAddress;

	/**
	 * @return a boolean
	 */
	public boolean isAddressType() {
		return addressType;
	}

	/**
	 * @return an int
	 */
	public int getIpAddress() {
		return ipAddress;
	}

	/**
	 * @return a string
	 */
	public String getNameAddress() {
		return nameAddress;
	}

	/**
	 * @param b a boolean
	 */
	public void setAddressType( byte b ) {
		if ( b == 0 ) 
			addressType = true;
		if ( b == 1 )
			addressType = false;
	}

	/**
	 * @param i an int
	 */
	public void setIpAddress(int i) {
		ipAddress = i;
	}

	/**
	 * @param string a string
	 */
	public void setNameAddress( String string ) {
		nameAddress = string;
	}
	
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * int8  	 Address Type: 0 = Address is IP, 1 = Address is Name 
 		 * int32  	 IP address (present only if Address Type = 0) 
 		 * String  	 Name address (present only if Address Type = 1) 
		 */
		this.setAddressType( messageBuffer.readByte() );
		if ( this.isAddressType())
			this.setIpAddress( messageBuffer.readInt32() );
		else
			this.setNameAddress( messageBuffer.readString() );
	}

}
