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
package net.mldonkey.g2gui.view;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.Information;

/**
 * InterFaceUI
 *
 * @author markus
 * @version $Id: InterFaceUI.java,v 1.1 2003/06/24 20:44:54 lemmstercvs01 Exp $ 
 *
 */
public interface InterFaceUI {
	/**
	 * Receive a notification 
	 * @param anInformation The Information which have changed
	 */
	void notify( Information anInformation );
	
	/**
	 * Adds this to the list of listeners
	 * @param mldonkey The CoreCommunication where to register
	 */
	void registerListener( CoreCommunication mldonkey );
}

/*
$Log: InterFaceUI.java,v $
Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.2  2003/06/19 11:42:13  lemmstercvs01
checkstyle applied, code cleanup

Revision 1.1  2003/06/18 13:49:10  lemmstercvs01
brocken!

Revision 1.1  2003/06/17 13:28:24  lemmstercvs01
initial commit

*/