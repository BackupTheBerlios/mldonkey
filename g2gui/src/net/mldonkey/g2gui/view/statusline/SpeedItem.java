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
package net.mldonkey.g2gui.view.statusline;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.*;

/**
 * SpeedItem
 *
 * @author $user$
 * @version $Id: SpeedItem.java,v 1.11 2003/08/02 12:56:57 zet Exp $ 
 *
 */
public class SpeedItem implements Observer {	

	private CoreCommunication core;
	private Composite composite;
	private StatusLine statusline;
	private CLabel cLabelDown, cLabelUp;
	private boolean needsLayout = true;

	/**
	 * @param string
	 * @param i
	 * @param mldonkey
	 */
	public SpeedItem( StatusLine statusline, CoreCommunication mldonkey ) {
		this.composite = statusline.getStatusline();
		this.statusline = statusline;
		this.core = mldonkey;
		
		this.createContent();

		mldonkey.getClientStats().addObserver( this );	
	}

	/**
	 * 
	 */
	private void createContent() {
		composite = new Composite( composite, SWT.BORDER );
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		composite.setLayout( gridLayout );
	
		/* down rate */	
		cLabelDown = new CLabel( composite, SWT.RIGHT );
		cLabelDown.setImage( new Image( composite.getDisplay(), "icons/down.png" ) );
		cLabelDown.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		
		/* up rate */
		cLabelUp = new CLabel( composite, SWT.NONE );
		cLabelUp.setImage( new Image( composite.getDisplay(), "icons/up.png" ) );
		cLabelUp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) {
		final ClientStats stats = ( ClientStats ) arg;
		final boolean doOnce = needsLayout;
		needsLayout = false; 
		if ( !composite.isDisposed() )				
		composite.getDisplay().asyncExec( new Runnable () {
			public void run() {
				if ( composite.isDisposed() ) return;
				/* first the text */
				cLabelDown.setText( "DL: " + stats.getTcpDownRate() + " kb/s");
				cLabelUp.setText( "UL: " + stats.getTcpUpRate() + "kb/s" );
				/* not the tooltip */
				cLabelDown.setToolTipText( "UDP-DL: " + stats.getUdpDownRate() );
				cLabelUp.setToolTipText( "UDP-UL: " + stats.getUdpUpRate() );
				if (doOnce) // hack
					cLabelUp.getParent().getParent().layout();
			}
		} );
	}
}

/*
$Log: SpeedItem.java,v $
Revision 1.11  2003/08/02 12:56:57  zet
size statusline

Revision 1.10  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.9  2003/07/31 14:11:06  lemmstercvs01
reworked

Revision 1.8  2003/07/17 14:58:56  lemmstercvs01
refactored

Revision 1.7  2003/06/28 09:37:18  lemmstercvs01
syncExec() -> asyncExec()

Revision 1.6  2003/06/27 13:37:28  dek
tooltips added

Revision 1.5  2003/06/27 13:21:12  dek
added connected Networks

Revision 1.4  2003/06/27 11:34:00  lemmstercvs01
foobar

Revision 1.3  2003/06/27 11:07:52  lemmstercvs01
CoreCommunications implements addObserver(Observer)

Revision 1.2  2003/06/27 10:36:17  lemmstercvs01
changed notify to observer/observable

Revision 1.1  2003/06/26 21:11:10  dek
speed is shown

*/