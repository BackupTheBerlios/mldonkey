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
 * @version $Id: Edonkey.java,v 1.6 2003/07/03 08:58:34 dek Exp $ 
 *
 */
public class Edonkey extends PreferencePage {
	private boolean connected;
	private PreferenceStore preferenceStore;
	

	/**
	 * @param preferenceStore_ where this class saves its settings
	 * @param connected are we connected to remote mldonkey?
	 */
	public Edonkey( PreferenceStore preferenceStore_ , boolean connected ) {
		super( "eDonkey" );
		this.preferenceStore = preferenceStore_;
		this.connected = connected;

	}

	protected Control createContents( Composite arg0 ) {
		return null;
	}


}

/*
$Log: Edonkey.java,v $
Revision 1.6  2003/07/03 08:58:34  dek
how the hell do i store a font on disk...

Revision 1.4  2003/07/02 15:55:14  dek
checkstyle

Revision 1.3  2003/06/27 18:05:46  dek
Client name is now an option, not saveable yet, but it's displayed ;-)

Revision 1.2  2003/06/26 14:09:20  dek
checkstyle

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/23 18:57:05  dek
jface

*/