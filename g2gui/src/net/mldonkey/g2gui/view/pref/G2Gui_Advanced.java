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
import org.eclipse.jface.preference.IntegerFieldEditor;
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
 * @version $Id: G2Gui_Advanced.java,v 1.3 2003/09/19 14:25:55 zet Exp $
 */
public class G2Gui_Advanced extends FieldEditorPreferencePage {

	private Composite parent;

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string The name of the OptionsPage
	 * @param i Style: SWT.XXXX
	 */
	public G2Gui_Advanced( String string, int i ) {
		super( string, i );
	}
	
	/**
	 * DOCUMENT ME!
	 * 
	 * @param e DOCUMENT ME!
	 */
	protected void setupEditor( FieldEditor e ) {
		e.setPreferencePage( this );
		e.setPreferenceStore( getPreferenceStore() );
		e.load();
		addField( e );
	}

	/* (  non-Javadoc  )
	 * @see org.eclipse.jface.preference.PreferencePage#
	 * setPreferenceStore(  org.eclipse.jface.preference.IPreferenceStore  )
	 */
	public void setPreferenceStore( IPreferenceStore store ) {
		super.setPreferenceStore( PreferenceLoader.setDefaults( store ) );
	}
	
	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#
	 * createContents( org.eclipse.swt.widgets.Composite )
	 */
	protected Control createContents( Composite myparent ) {
		
		Group group = new Group( myparent, SWT.NONE );
			GridLayout gl = new GridLayout( 1, false );
			group.setLayout( gl );
			group.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			
		
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
		
		if ( SWT.getPlatform().equals("win32") ) {
			setupEditor( 
				new BooleanFieldEditor( 
					"minimizeOnClose",
					G2GuiResources.getString( "PREF_DISPLAY_MIN_CLOSE" ),
					parent ) );
		
		}		
	
		if ( advanced ) {
		
			setupEditor( 
				new BooleanFieldEditor( 
					"displayAllServers",
					G2GuiResources.getString( "PREF_DISPLAY_CONN_SERV" ),
					parent ) );
						
			setupEditor( 
				new BooleanFieldEditor( 
					"displayNodes",
					G2GuiResources.getString( "PREF_DISPLAY_NODES" ),
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

		if ( advanced ) setupEditor( 
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

		IntegerFieldEditor updateDelayEditor = new IntegerFieldEditor ( 
				"updateDelay",
				G2GuiResources.getString( "PREF_DISPLAY_UPDATE_DELAY" ),
				parent ) ;
		updateDelayEditor.setValidRange( 0, 600 );
		setupEditor( updateDelayEditor );
	}
}
/*
$Log: G2Gui_Advanced.java,v $
Revision 1.3  2003/09/19 14:25:55  zet
min to systray option

Revision 1.2  2003/09/18 10:23:48  lemmster
checkstyle

Revision 1.1  2003/09/15 22:06:19  zet
split preferences


*/