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
package net.mldonkey.g2gui.view.main;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
/**
 * About
 *
 * @version $Id: About.java,v 1.1 2003/08/25 20:27:56 dek Exp $ 
 *
 */
public class About {
	private Shell shell;
	

	/**
	 * @param shell 
	 */
	public About() {
		this.shell = new Shell( shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
		Composite parent = new Composite( shell, SWT.NONE );
		shell.setLayout( new FillLayout() );
		shell.setText( "About" );
		parent.setLayout( new GridLayout( 1, false ) );
		createContent( parent );
		parent.layout();

	}

	/**
	 * @param parent
	 */
	private void createContent( Composite parent ) {
		CLabel icon = new CLabel( parent, SWT.NONE );
		icon.setImage( G2GuiResources.getImage( "ProgramIcon" ) );
		icon.setText( "About G2gui" );
		Label bar = new Label( parent, SWT.SEPARATOR | SWT.HORIZONTAL );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		bar.setLayoutData( gd );
		Label textToAdd = new Label( parent, SWT.NONE );
		textToAdd.setText( 
			" Here is place for info to add about us\n"
				+ " this is only a first sketch of an about-dialog\n"
				+ "feel free to extend ;- )" );		
	}

	/**
	 * 
	 */
	protected void open() {
		shell.pack();
		shell.open();		
	}
}
/*
$Log: About.java,v $
Revision 1.1  2003/08/25 20:27:56  dek
first sketch of an about-dialog, feel free to extend ;-)

*/