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
package net.mldonkey.g2gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolItem;

/**
 * G2guiTab
 *
 * @author $user$
 * @version $Id: G2guiTab.java,v 1.7 2003/07/02 19:14:10 dek Exp $ 
 *
 */
public abstract class G2guiTab implements Listener {	
	protected Composite content;
	protected IG2gui mainWindow;
	protected ToolItem toolItem;
	
	
	
	/**
	 * @param gui
	 */
	public G2guiTab( IG2gui gui ) {
		this.mainWindow = gui;		
		this.content = new Composite( gui.getPageContainer(), SWT.NONE );
		this.content.setLayout( new FillLayout() );
		this.content.setVisible( false );
		toolItem = new ToolItem( ( ( Gui )gui ).getMainTools(), SWT.PUSH );		
		this.toolItem.addListener( SWT.Selection, this );
		gui.registerTab( this );
		
	}
		
	protected abstract void createContents( Composite parent );
		

	/**
	 * @return
	 */
	public Composite getContent() {
		return content;
	}

	public void handleEvent( Event event ) {
		mainWindow.setActive( this );
	}
}
/*
$Log: G2guiTab.java,v $
Revision 1.7  2003/07/02 19:14:10  dek
default-window is now transfer-Tab

Revision 1.6  2003/07/02 16:37:29  dek
minor checkstyle

Revision 1.5  2003/07/02 16:37:12  dek
Checkstyle, JavaDocs still have to be added

Revision 1.4  2003/07/01 21:21:27  dek
*** empty log message ***

Revision 1.3  2003/06/30 21:40:09  dek
CoolBar created

Revision 1.2  2003/06/26 14:08:03  dek
statusline created

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.2  2003/06/24 19:18:45  dek
checkstyle apllied

Revision 1.1  2003/06/24 18:25:43  dek
working on main gui, without any connection to mldonkey atm, but the princip works
test with:
public class guitest{
	public static void main( String[] args ) {
	Gui g2gui = new Gui( null );
}

*/