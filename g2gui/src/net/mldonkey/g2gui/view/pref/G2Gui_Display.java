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
 * (  at your option  ) any later version.
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
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
/**
 * G2Gui_Display
 *
 *
 * @version $Id: G2Gui_Display.java,v 1.23 2003/08/29 17:33:20 zet Exp $
 */
public class G2Gui_Display extends FieldEditorPreferencePage {
	private Composite parent;
	/**
	 * 
	 * @param string The name of the OptionsPage
	 * @param i Style: SWT.XXXX
	 */
	public G2Gui_Display( String string, int i ) {
		super( string, i );
	}
	protected void setupEditor( FieldEditor e ) {
		e.setPreferencePage( this );
		e.setPreferenceStore( getPreferenceStore() );
		e.load();
		addField( e );
	}
	/* (  non-Javadoc  )
	 * @see org.eclipse.jface.preference.PreferencePage#setPreferenceStore(  org.eclipse.jface.preference.IPreferenceStore  )
	 */
	public void setPreferenceStore( IPreferenceStore store ) {
		super.setPreferenceStore( PreferenceLoader.setDefaults( store ) );
	}
	
	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents( org.eclipse.swt.widgets.Composite )
	 */
	protected Control createContents( Composite myparent ) {
		
		Group group = new Group( myparent, SWT.NONE );
			GridLayout gl = new GridLayout( 1, false );
				gl.horizontalSpacing = 10;
				gl.verticalSpacing = 10;
			group.setLayout( gl );
			group.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			group.setText( getTitle() );		
		
		ScrolledComposite sc = new ScrolledComposite( group, SWT.H_SCROLL | SWT.V_SCROLL ) {		
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
	/* (   non-Javadoc   )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		boolean advanced = getPreferenceStore().getBoolean( "advancedMode" );

		parent = getFieldEditorParent();

		if ( advanced ) {
			setupEditor( 
				new ExtendedColorFieldEditor( 
					"consoleBackground",
					G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_OBK" ),
					parent ) );

			setupEditor( 
				new ExtendedColorFieldEditor( 
					"consoleForeground",
					G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_OFG" ),
					parent ) );

			setupEditor( 
				new ExtendedColorFieldEditor( 
					"consoleHighlight",
					G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_HL" ),
					parent ) );

			setupEditor( 
				new ExtendedColorFieldEditor( 
					"consoleInputBackground",
					G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_IBK" ),
					parent ) );

			setupEditor( 
				new ExtendedColorFieldEditor( 
					"consoleInputForeground",
					G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_IFG" ),
					parent ) );

			setupEditor( 
				new ExtendedFontFieldEditor2( 
					"consoleFontData",
					G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_FONT" ),
					"Sample",
					parent ) );

			setupEditor( 
				new BooleanFieldEditor( 
					"displayAllServers",
					G2GuiResources.getString( "PREF_DISPLAY_CONN_SERV" ),
					parent ) );

		}
		setupEditor( 
			new BooleanFieldEditor( 
				"searchFilterPornography",
				G2GuiResources.getString( "PREF_SEARCH_FILTER_PORNOGRAPHY" ),
				parent ) );

		setupEditor( 
			new BooleanFieldEditor( 
				"searchFilterProfanity",
				G2GuiResources.getString( "PREF_SEARCH_FILTER_PROFANITY" ),
				parent ) );

		setupEditor( 
			new BooleanFieldEditor( 
				"flatInterface",
				G2GuiResources.getString( "PREF_DISPLAY_FLAT_INTERFACE" ),
				parent ) );

		setupEditor( 
			new BooleanFieldEditor( 
				"displayChunkGraphs",
				G2GuiResources.getString( "PREF_DISPLAY_CHUNK" ),
				parent ) );

		setupEditor( 
			new BooleanFieldEditor( 
				"displayGridLines",
				G2GuiResources.getString( "PREF_DISPLAY_GRID" ),
				parent ) );

		setupEditor( 
			new BooleanFieldEditor( 
				"tableCellEditors",
				G2GuiResources.getString( "PREF_DISPLAY_CELLEDITORS" ),
				parent ) );

		setupEditor( 
			new BooleanFieldEditor( 
				"maintainSortOrder",
				G2GuiResources.getString( "PREF_DISPLAY_SORT_ORDER" ),
				parent ) );

	}
}
/*
$Log: G2Gui_Display.java,v $
Revision 1.23  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.22  2003/08/29 17:13:29  dek
all content is now within a group, do you like it, or should i revert changes?

Revision 1.21  2003/08/29 14:59:34  dek
cleaning up, jFaced the whole thing even more, removed not needed methods & calls..
now the whole thing is kind of small ;-)

Revision 1.20  2003/08/28 18:27:32  zet
configurable flat interface

Revision 1.19  2003/08/26 22:44:03  zet
basic filtering

Revision 1.18  2003/08/23 15:21:37  zet
remove @author

Revision 1.17  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.16  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: zet $

Revision 1.15  2003/08/19 22:02:15  zet
localise

Revision 1.14  2003/08/19 13:16:10  dek
advanced-Mode introduced

Revision 1.13  2003/08/18 14:51:58  dek
some more jface-work

Revision 1.12  2003/08/18 00:24:44  zet
cleanup

Revision 1.11  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.10  2003/08/17 16:57:55  zet
*** empty log message ***

Revision 1.9  2003/08/14 12:59:47  zet
fix typo

Revision 1.8  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.7  2003/08/11 11:27:46  lemmstercvs01
display only connected servers added

Revision 1.6  2003/08/08 21:11:15  zet
set defaults in central location



*/