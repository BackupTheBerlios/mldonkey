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

import java.util.List;
import java.util.ArrayList;

import net.mldonkey.g2gui.comm.CoreCommunication;

import org.eclipse.jface.window.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;


/**
 * Gui
 *
 * @author $user$
 * @version $Id: Gui.java,v 1.3 2003/06/25 18:04:53 dek Exp $ 
 *
 */
public class Gui extends ApplicationWindow implements IG2gui, Listener {
	private CoreCommunication mldonkey;
	private Composite mainComposite,
					buttonRow,
					pageContainer;
	private List registeredTabs = new ArrayList();
	private G2guiTab activeTab;
	
		/**
		 * Layout for the page container.
		 * This class is from the JFace-Package, i hope they don't
		 * mind
		 */
		private class PageLayout extends Layout {
			public void layout( Composite composite, boolean force ) {
				Rectangle rect = composite.getClientArea();
				Control[] children = composite.getChildren();
				for ( int i = 0; i < children.length; i++ ) {
					children[ i ].setSize( rect.width, rect.height );
				}
			}
			public Point computeSize(
				Composite composite,
				int wHint,
				int hHint,
				boolean force ) {
				if ( wHint != SWT.DEFAULT && hHint != SWT.DEFAULT )
					return new Point( wHint, hHint );
				
				int x = 100;
				int y = 200;

				Control[] children = composite.getChildren();
				for ( int i = 0; i < children.length; i++ ) {
					Point size =
						children[ i ].computeSize( SWT.DEFAULT, SWT.DEFAULT, force );
					x = Math.max( x, size.x );
					y = Math.max( y, size.y );
				}
				if ( wHint != SWT.DEFAULT )
					x = wHint;
				if ( hHint != SWT.DEFAULT )
					y = hHint;
				return new Point( x, y );
			}
		}



	/**
	 * @param arg0 You know, what a shell is, right?
	 */
	public Gui(CoreCommunication core) {
		super( null );	
		this.mldonkey = core;	
		this.setBlockOnOpen( true );
		this.addStatusLine();		
		this.open();		
		Display.getCurrent().dispose();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite parent ) {
		parent.setSize(640,480);
		GridData gridData;		
		mainComposite = new Composite( parent, SWT.NONE );
		
		GridLayout mainLayout = new GridLayout();
			mainLayout.numColumns = 1;
			mainComposite.setLayout( mainLayout );
			
		buttonRow = new Composite( mainComposite, SWT.NONE );
			RowLayout buttonRowLayout = new RowLayout();
			buttonRowLayout.justify = true;
			buttonRowLayout.pack = false;
			buttonRowLayout.spacing = 5;			
			buttonRow.setLayout( buttonRowLayout );
						
				gridData = new GridData();
				gridData.grabExcessHorizontalSpace = true;
				gridData.horizontalAlignment = GridData.CENTER;
			buttonRow.setLayoutData( gridData );
		
		pageContainer = new Composite( mainComposite, SWT.NONE);
		pageContainer.setLayout( new PageLayout() );						
		gridData = new GridData( GridData.FILL_BOTH );			
			pageContainer.setLayoutData( gridData );
		

			
		addTabs();
						
		
		return super.createContents( parent );
	}

	/**
	 * Here do we add the tabs, they must extend G2GuiTab. They are responsible 
	 * for the content and their button.
	 */
	private void addTabs() {
		new TransferTab(this );
		new ConsoleTab( this );
	}
	

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.widgets.Gui.IG2gui#registerTab(net.mldonkey.g2gui.view.widgets.Gui.G2guiTab)
	 */
	public void registerTab( G2guiTab newTab ) {
		registeredTabs.add( newTab );		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event ) {
		System.out.println( event.widget.toString() );
		
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.widgets.Gui.IG2gui#getButtonRow()
	 */
	public Composite getButtonRow() {
	
		return buttonRow;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.widgets.Gui.IG2gui#getPageContainer()
	 */
	public Composite getPageContainer() {		
		return pageContainer;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.widgets.Gui.IG2gui#setActive(net.mldonkey.g2gui.view.widgets.Gui.G2guiTab)
	 */
	public void setActive( G2guiTab activatedTab ) {
		if ( activeTab != null ) activeTab.getContent().setVisible( false );
		activatedTab.getContent().setVisible( true );
		pageContainer.layout();
		activeTab = activatedTab;	
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.IG2gui#getCore()
	 */
	public CoreCommunication getCore() {		
		return mldonkey;
	}
}

/*
$Log: Gui.java,v $
Revision 1.3  2003/06/25 18:04:53  dek
Console-Tab reworked

Revision 1.2  2003/06/24 20:58:36  dek
removed border from Content-Composite

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.3  2003/06/24 19:18:45  dek
checkstyle apllied

Revision 1.2  2003/06/24 18:34:59  dek
removed senseless lines

Revision 1.1  2003/06/24 18:25:43  dek
working on main gui, without any connection to mldonkey atm, but the princip works
test with:
public class guitest{
	public static void main(String[] args) {
	Gui g2gui = new Gui(null);
}

*/