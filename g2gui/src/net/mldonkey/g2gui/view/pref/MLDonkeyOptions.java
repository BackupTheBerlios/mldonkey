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

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * MLDonkeyOptions
 *
 * @author $user$
 * @version $Id: MLDonkeyOptions.java,v 1.2 2003/07/07 18:31:08 dek Exp $ 
 *
 */
public class MLDonkeyOptions extends PreferencePage {
	/*have this page's contents been created? important for saving, as this might cause an NPE*/
	private boolean initialized = false;
	
	private Map options = new HashMap();
	private Map fields = new HashMap();
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
			fields.put( option.getKey(), temp );
		}
		
		this.initialized = true;
		return null;
	}
	/**
	 * @param option This Option should be displayed on this preferencePage
	 */
	public void addOption( OptionsInfo option ) {
		this.options.put( option.getKey(), option );
		/*if this option has no description, we set its name as description*/
		if ( option.getDescription().equals( "" ) )
			option.setDescription( option.getKey() );
	}
	
	/**
	 * Is called, when the OKbutton of the Tab is pressed
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {	
		/* only perform, if this tab has been initialized */
		if ( initialized ) {
			Iterator it = fields.keySet().iterator();
			while ( it.hasNext() ) {
				String optionName = ( String ) it.next();
				OptionsInfo option = ( OptionsInfo ) options.get( optionName );
				StringFieldEditor field = ( StringFieldEditor ) fields.get( optionName );
				/*has the value changed??*/
				if ( !field.getStringValue().equals( option.getValue() ) ) {
					/*Ok, option has changed, take it and send it to the mldonkey*/
					option.setValue( field.getStringValue() );
					option.send();
				}
								
			}			
				
		}
		 
		return super.performOk();		
	}
}

/*
$Log: MLDonkeyOptions.java,v $
Revision 1.2  2003/07/07 18:31:08  dek
saving options now also works

Revision 1.1  2003/07/07 17:38:14  dek
now, one can take a look at all Core-options, not saving yet, but is in work

*/