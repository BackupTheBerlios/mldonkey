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
package net.mldonkey.g2gui.view;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.statusline.NetworkItem;
import net.mldonkey.g2gui.view.statusline.SpeedItem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Statusline, This class handles all the Information that should be visible all the time in a 
 * so called Status-line. It is an own implementation of this widget, as the JFace-Statusline
 * is way too complicated for our simple needs. It has to be placed in a GridLayout, since it
 * applies a GridData object for its appearance.
 *
 * @author $user$
 * @version $Id: StatusLine.java,v 1.8 2003/08/18 01:42:24 zet Exp $ 
 *
 */
public class StatusLine {
	private CoreCommunication core;
	private Composite composite;
	private Label label;
	private MainTab mainTab;
	private GridLayout gridLayout;

	/**
	 * Creates a new StatusLine obj
	 * @param parent The parent obj Composite to display in
	 * @param mldonkey The CoreCommunication to connect with
	 */
	public StatusLine( MainTab mainTab ) {
		this.mainTab = mainTab;
		this.core = mainTab.getCore();
		this.composite = new Composite( mainTab.getMainComposite(), SWT.NONE );	
		
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
			
		this.composite.setLayout( gridLayout);
		composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );	
			
		// why is this one a different height? 
		/* the left field */
		new NetworkItem( this, this.core );

		/* the middle field */
		Composite middle = new Composite( composite, SWT.BORDER );
		middle.setLayout( new FillLayout() );	
		middle.setLayoutData( new GridData ( GridData.FILL_BOTH ) );

		label = new Label(middle, SWT.NONE);
		label.setText ( "" );
		
		/* the right field */
		new SpeedItem( this, this.core );
		
	}

	/**
	 * Updates a String at a specific position
	 * @param aString The new String to display
	 */
	public void update( String aString ) {		
		if ( !composite.isDisposed() )
			label.setText( aString );					
	}

	/**
	 * 	Sets the tooltip of the Statusbar Item
	 * @param aString The tooltip to show
	 */
	public void updateToolTip( String aString ) {
		if ( !composite.isDisposed() )
			label.setToolTipText( aString );					
	}
	/**
	 * @return the Composite in which the statusline is created
	 */
	public Composite getStatusline() {
		return composite;
	}
	/**
	 * @return The maintab we are started from
	 */
	public MainTab getMainTab() {
		return mainTab;
	}
}

/*
$Log: StatusLine.java,v $
Revision 1.8  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.7  2003/08/10 12:59:01  lemmstercvs01
"manage servers" in NetworkItem implemented

Revision 1.6  2003/08/02 19:32:43  zet
marginwidths

Revision 1.5  2003/08/02 12:56:47  zet
size statusline

Revision 1.4  2003/07/31 14:10:58  lemmstercvs01
reworked

Revision 1.3  2003/07/29 09:41:56  lemmstercvs01
still in progress, removed networkitem for compatibility

Revision 1.2  2003/07/18 04:34:22  lemmstercvs01
checkstyle applied

Revision 1.1  2003/07/17 14:58:37  lemmstercvs01
refactored

Revision 1.7  2003/06/28 09:50:12  lemmstercvs01
ResourceBundle added

Revision 1.6  2003/06/28 09:39:30  lemmstercvs01
added isDisposed() check

Revision 1.5  2003/06/27 13:37:28  dek
tooltips added

Revision 1.4  2003/06/27 13:21:12  dek
added connected Networks

Revision 1.3  2003/06/26 21:11:10  dek
speed is shown

Revision 1.2  2003/06/26 14:11:58  dek
NPE fixed ;-)

Revision 1.1  2003/06/26 14:04:59  dek
Class statusline for easy information display

*/