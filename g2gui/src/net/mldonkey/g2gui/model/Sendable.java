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
 * Sendable
 *
 *
 * @version $Id: Sendable.java,v 1.4 2003/12/04 08:47:25 lemmy Exp $ 
 *
 */
public interface Sendable extends Information {
	/**
 	 * sends out the sendable to the mldonkey core
 	 */
	void send();
}

/*
$Log: Sendable.java,v $
Revision 1.4  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.3  2003/08/23 15:21:37  zet
remove @author

Revision 1.2  2003/08/22 21:03:15  lemmy
replace $user$ with $Author: lemmy $

Revision 1.1  2003/07/06 08:49:33  lemmy
better oo added

*/