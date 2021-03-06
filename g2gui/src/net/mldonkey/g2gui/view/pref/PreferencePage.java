/*
 * Copyright 2003
 * G2Gui Team
 * 
 * 
 * This file is part of G2Gui.
 *
 * G2Gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2Gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2Gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.pref;

import net.mldonkey.g2gui.view.resource.G2GuiResources;

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
import org.eclipse.swt.widgets.Label;

/**
 * GuiPreferences
 *
 * @version $Id: PreferencePage.java,v 1.4 2004/03/01 21:12:21 psy Exp $ 
 *
 */
public abstract class PreferencePage extends FieldEditorPreferencePage {
	/**
	 * @param title
	 * @param style
	 */
	protected PreferencePage( String title, int style ) {
		super( title, style );
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#
	 * createContents( org.eclipse.swt.widgets.Composite )
	 */
	protected Control createContents( Composite aParent ) {
		GridLayout gridLayout = new GridLayout( 1, false );
		Group group = new Group( aParent, SWT.NONE );
		group.setLayout( gridLayout );
		group.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Label label = new Label( aParent, SWT.NONE );
		label.setText( G2GuiResources.getString( "PREF_RESTART" ) );

		ScrolledComposite scrolledComposite =
			new ScrolledComposite( group, SWT.H_SCROLL | SWT.V_SCROLL ) {
				public Point computeSize( int wHint, int hHint, boolean changed ) {
					return new Point( SWT.DEFAULT, SWT.DEFAULT );
				}
			};
		scrolledComposite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		scrolledComposite.setLayout( new FillLayout() );
		scrolledComposite.setExpandHorizontal( true );
		scrolledComposite.setExpandVertical( true );

		Composite aComposite = ( Composite ) super.createContents( scrolledComposite );
		aComposite.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		scrolledComposite.setContent( aComposite );

		scrolledComposite.setMinSize( aComposite.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

		aComposite.layout();

		return aComposite;
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.PreferencePage#
	 * setPreferenceStore( org.eclipse.jface.preference.IPreferenceStore )
	 */
	public void setPreferenceStore( IPreferenceStore store ) {
		super.setPreferenceStore( store );
		store = PreferenceLoader.setDefaults( store );
	}

	/**
	 * 
	 * @param e
	 */
	protected void setupEditor( FieldEditor e ) {
		e.setPreferencePage( this );
		e.setPreferenceStore( getPreferenceStore() );
		e.load();
		addField( e );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected abstract void createFieldEditors();

	/**
	 * Are we in advanced mode?
	 * @return true if we are in advanced mode
	 */
	protected boolean advancedMode() {
		return getPreferenceStore().getBoolean( "advancedMode" );
	}
	

}

/*
$Log: PreferencePage.java,v $
Revision 1.4  2004/03/01 21:12:21  psy
removed download-table font config (use global one instead)
started re-arranging the preferences (to be continued...)

Revision 1.3  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.2  2003/10/12 16:27:01  lemmy
minor changes

Revision 1.1  2003/10/01 20:56:27  lemmy
add class hierarchy

*/