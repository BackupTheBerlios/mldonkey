/*
 * Copyright 2003
 * G2Gui Team
 * 
 * 
 * This file is part of G2Gui.
 *
 * G2Gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2Gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2Gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.model;

import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * Parent
 *
 * @author $user$
 * @version $Id: Parent.java,v 1.2 2003/08/01 17:21:19 lemmstercvs01 Exp $ 
 *
 */
public abstract class Parent extends Observable implements SimpleInformation {
	/**
	 * The CoreCommunication parent
	 */
	protected CoreCommunication parent;
	/**
	 *  Reads a State object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public abstract void readStream( MessageBuffer messageBuffer );
	/**
	 * Creates a new SimpleInformation
	 * @param core The parent CoreCommunication
	 */
	public Parent( CoreCommunication core ) {
		this.parent = core;
	}
}

/*
$Log: Parent.java,v $
Revision 1.2  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.1  2003/07/06 08:49:34  lemmstercvs01
better oo added

*/