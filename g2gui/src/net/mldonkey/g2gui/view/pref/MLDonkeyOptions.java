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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.mldonkey.g2gui.model.OptionsInfo;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * MLDonkeyOptions
 *
 * @author $user$
 * @version $Id: MLDonkeyOptions.java,v 1.1 2003/07/07 17:38:14 dek Exp $ 
 *
 */
public class MLDonkeyOptions extends PreferencePage {
	private Map options = new HashMap();
	private List fields = new ArrayList();
	/**
	 * 
	 * @param title the Title of this preferencePage, is displayed in the tree on the left side
	 */
	public MLDonkeyOptions( String title ) {
		super( title );		
	}
	
	/** (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite parent ) {
		Iterator it = options.keySet().iterator();
		while ( it.hasNext() ) {			
			OptionsInfo option = ( OptionsInfo ) options.get( it.next() );
			StringFieldEditor temp = new StringFieldEditor(
										 option.getKey(), 
										 option.getDescription(), 
										 parent );
			temp.setStringValue( option.getValue() );
			temp.getTextControl( parent ).setToolTipText( option.getKey() );
			temp.getLabelControl( parent ).setToolTipText( option.getKey() );
			fields.add( temp );
		}
		
		/*
		 * this.clientNameField = new StringFieldEditor( "client_name", "Client Name", shell );
			clientNameField.setEnabled( connected, shell );
			clientNameField.setStringValue( clientName );			
			clientNameField.getTextControl( shell ).setToolTipText( "Small name of client" );
			clientNameField.getLabelControl( shell ).setToolTipText( "Small name of client" );			
		 */
		return null;
	}
	/**
	 * @param option This Option should be displayed on this preferencePage
	 */
	public void addOption( OptionsInfo option ) {
		this.options.put( option.getKey(), option );
		
	}
}

/*
$Log: MLDonkeyOptions.java,v $
Revision 1.1  2003/07/07 17:38:14  dek
now, one can take a look at all Core-options, not saving yet, but is in work

*/