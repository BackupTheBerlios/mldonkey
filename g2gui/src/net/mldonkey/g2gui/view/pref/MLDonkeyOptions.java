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



import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.mldonkey.g2gui.model.OptionsInfo;
import net.mldonkey.g2gui.model.enum.EnumTagType;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * MLDonkeyOptions
 *
 * @author $user$
 * @version $Id: MLDonkeyOptions.java,v 1.6 2003/07/10 13:56:07 dek Exp $ 
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
			String description = option.getDescription();
			String optionName = option.getKey();
			String value = option.getValue();
			EnumTagType type = option.getOptionType();
			
			/*This is some handling for all the Options, that do _not_
			 * have a detailed description of their type inside:
			 */
			if ( description.equals( "" ) ||  description == null )
				description = optionName;				
			if ( value.equals( "false" ) || value.equals( "true" ) )
				type = EnumTagType.BOOL;			
			if ( type == null )
				type = EnumTagType.STRING;
				
			
			/*First check the Option, what kind of option it is: string or boolean
			 * to known, which widget to use*/	
			if ( type.equals( EnumTagType.BOOL ) ) {			
				ExtendedBooleanFieldEditor temp = new ExtendedBooleanFieldEditor(
											optionName, 
											description, 
											parent );
				temp.setSelection( new Boolean( value ).booleanValue() );
				temp.setToolTipText( optionName );				
				fields.put( optionName, temp );
			}
			else  {			
			   ExtendedStringFieldEditor temp = new ExtendedStringFieldEditor(
										   optionName, 
										   description, 
										   parent );
			   temp.setStringValue( value );
			   temp.setToolTipText( optionName );				
			   fields.put( optionName, temp );
			}		
			 
			 
		}
		
		if ( options.size() == 0 ) {
			Label noOptions = new Label ( parent, SWT.NONE );
			noOptions.setText( "please select a subentry from the list" );
			this.noDefaultAndApplyButton();
		}
		
		parent.layout();
		this.initialized = true;
		return null;
	}
	/**
	 * @param option This Option should be displayed on this preferencePage
	 */
	public void addOption( OptionsInfo option ) {
		this.options.put( option.getKey(), option );
		

	}
	
	
	/** is called, when "Restore Defaults" Button is pressed.
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {		
		super.performDefaults();
		Iterator it = fields.keySet().iterator();
		/*iterate oer all fields, and restore default-value if option has changed*/
		while ( it.hasNext() ) {
			String optionName = ( String ) it.next();
			OptionsInfo option = ( OptionsInfo ) options.get( optionName );
			IValueEditor field = ( IValueEditor ) fields.get( optionName );
			/*has the value changed??*/
			if ( field.hasChanged() ) {
				/*Ok, option has changed, reset to default*/					
				field.restoreDefault();
				field.resetChangedStatus();
			}
								
		}
		
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
				IValueEditor field = ( IValueEditor ) fields.get( optionName );
				/*has the value changed??*/
				if ( field.hasChanged() ) {
					/*Ok, option has changed, take it and send it to the mldonkey*/					
					option.setValue( field.getValue() );
					option.send();
					field.resetChangedStatus();
				}
								
			}			
				
		}
		 
		return super.performOk();		
	}
}

/*
$Log: MLDonkeyOptions.java,v $
Revision 1.6  2003/07/10 13:56:07  dek
empty-pages have no Default/apply-buttons anymore

Revision 1.5  2003/07/09 09:16:05  dek
general Options

Revision 1.4  2003/07/09 08:30:37  dek
removed setter

Revision 1.3  2003/07/08 16:59:23  dek
now the booleanValues are checkBoxes

Revision 1.2  2003/07/07 18:31:08  dek
saving options now also works

Revision 1.1  2003/07/07 17:38:14  dek
now, one can take a look at all Core-options, not saving yet, but is in work

*/