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



import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.transferTree.DownloadTable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Main
 *
 * @author $user$
 * @version $Id: TransferTab.java,v 1.18 2003/07/18 09:58:00 dek Exp $ 
 *
 */
public class TransferTab extends GuiTab  {
	private boolean active;
	private CoreCommunication mldonkey;
	/**
	 * @param gui where this tab belongs to
	 */
	public TransferTab( MainTab gui ) {
		super( gui );
		this.mldonkey = gui.getCore();		
		this.toolItem.setText( "testTab" );		
		createContents( this.content );
	}
	
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.G2guiTab#createContents(org.eclipse.swt.widgets.Composite)
	 */	 
	protected void createContents( Composite parent ) {
		SashForm main = new SashForm( parent, SWT.VERTICAL );
		Composite download = new Composite( main, SWT.BORDER );
			download.setLayout( new FillLayout() );
		Composite upload = new Composite( main, SWT.BORDER );
		new DownloadTable( download, mldonkey, this );
	}

	/** (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) {
		// TODO Auto-generated method stub
	}
}

/*
$Log: TransferTab.java,v $
Revision 1.18  2003/07/18 09:58:00  dek
checkstyle

Revision 1.17  2003/07/17 14:58:37  lemmstercvs01
refactored

Revision 1.3  2003/07/15 18:14:47  dek
Wow, nice piece of work already done, it works, looks nice, but still lots of things to do

Revision 1.2  2003/07/14 19:26:40  dek
done some clean.up work, since it seems,as if this view becomes reality..

Revision 1.1  2003/07/11 17:53:51  dek
Tree for Downloads - still unstable

*/