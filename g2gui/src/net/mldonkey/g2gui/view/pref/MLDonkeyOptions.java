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
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.model.OptionsInfo;
import net.mldonkey.g2gui.model.enum.EnumTagType;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * MLDonkeyOptions
 *
 * @author  $Author: dek $ 
 * @version $Id: MLDonkeyOptions.java,v 1.9 2003/08/17 21:22:21 dek Exp $ 
 *
 */
public class MLDonkeyOptions extends FieldEditorPreferencePage {
	private Composite parent;


	List options = new ArrayList();
	
	
	/**
	 * @param title
	 * @param style
	 */
	protected MLDonkeyOptions(String title, int style) {
		super(title, style);
				// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		/*
		 * I don't know why this method is called even before super.createContents 
		 * returned the parent for these controls??? this is why i check for null and recall
		 * it in this.createContents()
		 */
		if (parent != null){		
			Iterator it = options.iterator();
			while ( it.hasNext() ) {
				OptionsInfo temp = ( OptionsInfo ) it.next();
				if ( temp.getOptionType() == EnumTagType.BOOL ) {
					/*create a boolean-editor and add to page*/
					BooleanFieldEditor bool =
						new BooleanFieldEditor(
												temp.getKey(),
												temp.getDescription(),
												parent) ;	
					bool.getLabelControl( parent ).setToolTipText( temp.getKey() );				
					bool.setPreferenceStore( this.getPreferenceStore() );				
					addField( bool );
					bool.fillIntoGrid( parent, 2 );	
					bool.load();
				} else {
					StringFieldEditor string =
						new StringFieldEditor( temp.getKey(),
										 temp.getDescription(),
										 parent );
					string.getLabelControl( parent ).setToolTipText( temp.getKey() );
					string.setPreferenceStore( this.getPreferenceStore() );						
					addField( string );
					string.fillIntoGrid( parent,2 );	
					string.load();
				}			
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite myparent) {
		parent = (Composite) super.createContents(myparent);
		createFieldEditors();
		
		return parent;
	}


	/**
	 * @param option
	 */
	public void addOption(OptionsInfo option) {
		options.add(option);
	}



	
}

/*
$Log: MLDonkeyOptions.java,v $
Revision 1.9  2003/08/17 21:22:21  dek
reworked options, finally, it makes full use of the jFace framework ;-)

Revision 1.8  2003/07/26 17:54:14  zet
fix pref's illegal setParent, redo graphs, other

Revision 1.7  2003/07/26 03:07:12  zet
scrollbars

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