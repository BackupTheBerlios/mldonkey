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
 * @version $Id: PreferencePage.java,v 1.1 2003/10/01 20:56:27 lemmster Exp $ 
 *
 */
public abstract class PreferencePage extends FieldEditorPreferencePage {
	protected Label label;
	protected Composite composite;

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

		composite = ( Composite ) super.createContents( scrolledComposite );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		scrolledComposite.setContent( composite );

		scrolledComposite.setMinSize( composite.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

		composite.layout();

		return composite;
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
}

/*
$Log: PreferencePage.java,v $
Revision 1.1  2003/10/01 20:56:27  lemmster
add class hierarchy

*/