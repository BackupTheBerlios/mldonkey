/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.pref;

/**
 * IValueEditor
 *
 * @author $user$
 * @version $Id: IValueEditor.java,v 1.1 2003/07/08 16:59:23 dek Exp $ 
 *
 */
public interface IValueEditor {
	
	/**
	 * @return the Value of this editField as String
	 */
	String getValue();	
	/**
	 * @return has this Field been edited?
	 */
	boolean hasChanged();
	
	/**
	 * resets the receivers hasChangedStatus to false
	 */
	void resetChangedStatus();

}

/*
$Log: IValueEditor.java,v $
Revision 1.1  2003/07/08 16:59:23  dek
now the booleanValues are checkBoxes

*/