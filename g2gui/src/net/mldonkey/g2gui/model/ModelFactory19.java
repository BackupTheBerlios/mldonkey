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

/**
 * ModelFactory19
 *
 * @version $Id: ModelFactory19.java,v 1.3 2004/03/21 21:00:50 dek Exp $ 
 *
 */
public class ModelFactory19 extends ModelFactory18 {
	/**
	 * 
	 */
	public ModelFactory19() {
		super();
	}
	public ClientInfo getClientInfo() {		
		return new ClientInfo19( core );
	}

}

/*
$Log: ModelFactory19.java,v $
Revision 1.3  2004/03/21 21:00:50  dek
implemented gui-Proto 21-25 !!!!!

Revision 1.2  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.1  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

*/