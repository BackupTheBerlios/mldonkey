package net.mldonkey.g2gui.view.pref;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


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


/**
 * Edonkey
 *
 * @author $user$
 * @version $Id: Edonkey.java,v 1.2 2003/06/26 14:09:20 dek Exp $ 
 *
 */
public class Edonkey extends PreferencePage {
	private PreferenceStore preferenceStore;
	/**
	 * @param preferenceStore
	 */
	public Edonkey( PreferenceStore preferenceStore_ ) {
		super( "eDonkey" );
		this.preferenceStore = preferenceStore_;

	}

	protected Control createContents( Composite arg0 ) {
		return null;
	}


}

/*
$Log: Edonkey.java,v $
Revision 1.2  2003/06/26 14:09:20  dek
checkstyle

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/23 18:57:05  dek
jface

*/