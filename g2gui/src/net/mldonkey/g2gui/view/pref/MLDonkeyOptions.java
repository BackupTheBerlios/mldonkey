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
 * ( at your option ) any later version.
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.model.OptionsInfo;
import net.mldonkey.g2gui.model.enum.EnumTagType;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
/**
 * MLDonkeyOptions
 *
 *
 * @version $Id: MLDonkeyOptions.java,v 1.31 2003/08/29 15:35:58 dek Exp $ 
 *
 */
public class MLDonkeyOptions extends FieldEditorPreferencePage {
	private ScrolledComposite sc;
	private Composite parent;
	private List options = new ArrayList();
	private final int inputFieldLength = 20;
	/**
	 * @param title
	 * @param style
	 */
	protected MLDonkeyOptions( String title, int style ) {
		super( title, style );		
	}
	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	 
	private void setupEditor( FieldEditor e , String optionHelp ) {
		e.setPreferencePage( this );
		e.setPreferenceStore( getPreferenceStore() );
		e.getLabelControl( parent ).setToolTipText( optionHelp );
		
		e.load();
		addField( e );
	}
	
	protected void createFieldEditors() {
		Collections.sort( options, new optionsComparator() );
		Iterator it = options.iterator();
		while ( it.hasNext() ) {
			parent = getFieldEditorParent();
			OptionsInfo temp = ( OptionsInfo ) it.next();
			if ( temp.getOptionType() == EnumTagType.BOOL || isBoolean( temp.getValue() ) ) {
				String description = temp.getDescription();
				if ( description.equals( "" ) )
					description = temp.getKey();
				String optionHelp = temp.getOptionHelp();
				if ( optionHelp.equals( "" ) )
					optionHelp = temp.getKey();
				/*create a boolean-editor and add to page*/
				setupEditor( new BooleanFieldEditor( 
						temp.getKey(), description, BooleanFieldEditor.SEPARATE_LABEL, parent ), optionHelp );
			} 
			else if ( temp.getOptionType() == EnumTagType.INT || isInteger( temp.getValue() ) ) {
				String description = temp.getDescription();
				if ( description.equals( "" ) )
					description = temp.getKey();
				String optionHelp = temp.getOptionHelp();
				if ( optionHelp.equals( "" ) )
					optionHelp = temp.getKey();
				/*create a IntegerFieldEditor and add to page
				 * 
				 * this is kind of hacky, but i don't see another way to get the stuff
				 * with the different-sized inputs done...
				 */
				setupEditor( new IntegerFieldEditor( temp.getKey(), description, parent ) {
					/* ( non-Javadoc )
					 * @see org.eclipse.jface.preference.StringFieldEditor#doFillIntoGrid( org.eclipse.swt.widgets.Composite, int )
					 */
					protected void doFillIntoGrid( Composite parent, int numColumns ) {
						getLabelControl( parent );
						Text textField = getTextControl( parent );
						GridData gd = new GridData();
						gd.horizontalSpan = numColumns - 1;
						GC gc = new GC( textField );
						try {
							Point extent = gc.textExtent( "X" );
							gd.widthHint = inputFieldLength * extent.x;
						} finally {
							gc.dispose();
						}
						textField.setLayoutData( gd );
					}
				}, optionHelp );
			} 
			else {
				String description = temp.getDescription();
				if ( description.equals( "" ) )
					description = temp.getKey();
				String optionHelp = temp.getOptionHelp();
				if ( optionHelp.equals( "" ) )
					optionHelp = temp.getKey();
				// with a very long string, the pref pages looks bad. limit to inputFieldLength?
				setupEditor( new StringFieldEditor( temp.getKey(), description, inputFieldLength, parent ), optionHelp );
			}
		}

	}

	/**
	 * @param string
	 * @return
	 */
	private boolean isBoolean( String string ) {
		if ( string.equalsIgnoreCase( "true" ) || string.equalsIgnoreCase( "false" ) )
			return true;
		else return false;
	}
	
	private boolean isInteger( String string ) {
		try {
			int value = Integer.valueOf( string ).intValue();
			if ( value >= 0 && value <= Integer.MAX_VALUE ) {				
				return true;
			} else {				
				return false;
			}
		} catch ( NumberFormatException e ) {
				return false;
		}	
	}
	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents( org.eclipse.swt.widgets.Composite )
	 */
	protected Control createContents( Composite myparent ) {
		
		computeSize();
		
		sc = new ScrolledComposite( myparent, SWT.H_SCROLL | SWT.V_SCROLL ) {		
			public Point computeSize( int wHint, int hHint, boolean changed ) 
			/* This method prevents the window from becoming huge (as in hight and width) 
			 * when reopening "General" (or equivalents)
			 */ 
				{ return new Point( SWT.DEFAULT, SWT.DEFAULT ); }
		};
		
		sc.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			sc.setLayout( new FillLayout() );
		
		Composite parent = ( Composite ) super.createContents( sc );
		parent.setLayoutData( new GridData( GridData.FILL_BOTH ) );		
		
		sc.setExpandHorizontal( true );
		sc.setExpandVertical( true );
		sc.setContent( parent );
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;		
		
		sc.setMinSize( parent.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
		parent.layout();
		
		return parent; 
	}
	/**
	 * @param option this option should appear on this preference-page
	 */
	public void addOption( OptionsInfo option ) {
		options.add( option );
	}
	
	public class optionsComparator implements Comparator {
		/**
		 * returns wich option is larger than the other one
		 * @param o1 first Option to compare
		 * @param o2 second Option to compare
		 * @return which option is larger, 1|2, returns 0 when equal
		 */
		public int compare( Object o1, Object o2 ) {
			//TODO sorting options by kind of value?? see comment for more: 
			/* Should boolean-Options appear ontop of each other
			 * instead of beeing scatterd all over the list? so this should happen here
			 */
			
						
			OptionsInfo optionsInfo1 = ( OptionsInfo ) o1;
			OptionsInfo optionsInfo2 = ( OptionsInfo ) o2;
			String optionDescription1 = ( optionsInfo1.getDescription().equals( "" ) ? optionsInfo1.getKey() : optionsInfo1.getDescription() );
			String optionDescription2 = ( optionsInfo2.getDescription().equals( "" ) ? optionsInfo2.getKey() : optionsInfo2.getDescription() );
			return optionDescription1.compareToIgnoreCase( optionDescription2 );
		}
		
	}
} 
/*
$Log: MLDonkeyOptions.java,v $
Revision 1.31  2003/08/29 15:35:58  dek
re-added tooltip-Help

Revision 1.30  2003/08/29 15:19:52  dek
some more JFace-cleaning up, i didn't even believe jface would make things sooo easy..:-)

Revision 1.29  2003/08/26 14:55:12  dek
added check for Integer-Valued Options, just because there are already
IntFieldEditors...

Revision 1.28  2003/08/26 14:12:01  zet
decrease inputfieldlength

Revision 1.27  2003/08/26 09:09:44  dek
readded max length to input-fields, as my holy grail was not so holy as i thought..

Revision 1.25  2003/08/25 13:21:35  dek
checkstyle

Revision 1.24  2003/08/25 13:14:57  dek
now integegerFields have the same width as StringInputFields

Revision 1.23  2003/08/24 18:41:46  zet
try to remove horizontal scrollbars from prefs

Revision 1.22  2003/08/24 14:28:31  lemmster
organize imports

Revision 1.21  2003/08/24 11:30:57  dek
prefDialog is not resizable any more, and we have IntEditors for int-values

Revision 1.20  2003/08/23 15:21:37  zet
remove @author

Revision 1.19  2003/08/19 22:06:56  zet
with the new options, heighthint doesn't seem to be needed

Revision 1.18  2003/08/19 16:54:29  zet
limit string length so long option strings fit on screen

Revision 1.17  2003/08/19 16:39:50  zet
fix null pointer

Revision 1.16  2003/08/19 16:33:37  zet
sort the options...

Revision 1.15  2003/08/19 13:08:03  dek
advanced-Options included

Revision 1.14  2003/08/18 22:28:58  zet
scrolledcomposite height

Revision 1.13  2003/08/18 15:48:13  zet
computeSize

Revision 1.12  2003/08/18 14:51:58  dek
some more jface-work

Revision 1.11  2003/08/18 10:36:17  dek
added scoll-bars for long options-list

Revision 1.10  2003/08/17 21:24:55  dek
reworked options, finally, it makes full use of the jFace framework ;- )

Revision 1.9  2003/08/17 21:22:21  dek
reworked options, finally, it makes full use of the jFace framework ;- )

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