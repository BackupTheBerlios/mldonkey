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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.view.resource.G2GuiResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
/**
 * About
 *
 * @version $Id: About.java,v 1.7 2003/08/30 18:20:50 dek Exp $ 
 *
 */
public class About {
//	TODO filling about-Dialog with content

	private Link activeLink;
	private final Cursor handCursor = new Cursor( Display.getDefault(), SWT.CURSOR_HAND );
	private List linklist = new ArrayList();	
	private Shell myShell;
	private Color background = Display.getCurrent().getShells()[1].getBackground();	
	

	/**
	 * @param shell were this dialog lives
	 */
	public About( Shell shell ) {
		this.myShell = new Shell( shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
		Composite parent = new Composite( myShell, SWT.NONE );
		myShell.setLayout( new FillLayout() );
		myShell.setText( "About" );
		GridLayout layout = new GridLayout( 1, false );
			layout.marginHeight = 0;
			layout.marginWidth = 0;
		parent.setLayout( layout );		
		createContent( parent );
		parent.layout();
	}

	/**
	 * @param parent
	 */
	private void createContent( Composite parent ) {
		
		Composite upperPart = new Composite ( parent, SWT.NONE );
			upperPart.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			upperPart.setLayout( new GridLayout() );		
		createUpperPart( upperPart );
		upperPart.layout();
		
		Label bar = new Label( parent, SWT.SEPARATOR | SWT.HORIZONTAL );
			bar.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			
		Composite lowerPart = new Composite ( parent, SWT.NONE );
			lowerPart.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			lowerPart.setLayout( new GridLayout() );		
		createLowerPart( lowerPart );
		lowerPart.layout();
		
	}

	private void createLowerPart( Composite parent ) {
		parent.setLayoutData( new GridData( GridData.BEGINNING ) );
		StyledText about = new StyledText( parent, SWT.MULTI | SWT.READ_ONLY );
		about.setCaret( null );
		about.setBackground( background );
		
		String develHeader = "Active Devs and Management:\n";
		String devels = "Dek, Lemmy, VNC, Z\n\n";
		
		String contributorHeader = "Contributors:\n";
		String contributors = "housetier, mitch, vaste\n\n";
		
		String thankHeader = "We thank following projects for code and libs:\n";
		String thank = "  * The KDE project for icons (GPL)\n"
		+ "  * The creator of the Noia KDE icon set for his/her icons (GPL)\n"
		+ "  * The creator of the lush KDE icon set for his/her icons (GPL)\n"
		+ "  * The creator of the Nuvola KDE icon set for his/her icons (GPL)\n"
		+ "  * The emule-plus project for icons (CCL)\n"
		+ "  * The Gnu/Trove and Regex project (GPL)\n"
		+ "  * The whole Eclipse and SWT team (CPL)\n\n";
		
		String moreThanksHeader = "We thank furthermore:\n";
		String moreThanks = " * The dude who made the java ssh2 for use in eclipse\n"
		+ "  * The dude who invented Wikis and the creator of phpWiki\n"
		+ "  * The Freenode IRC network\n\n";

		
		Color fg = parent.getForeground();
		Color bg = parent.getBackground();		
		StyleRange[] ranges = 
		{	new StyleRange( 0, 
				develHeader.length(), fg, bg, SWT.BOLD ), 			
			new StyleRange( develHeader.length() + devels.length(), 
				contributorHeader.length(), fg, bg, SWT.BOLD ), 
			new StyleRange( develHeader.length() + devels.length() + contributorHeader.length() + contributors.length(), 
				thankHeader.length(), fg, bg, SWT.BOLD ), 
			new StyleRange( develHeader.length() + devels.length() + contributorHeader.length() + contributors.length() + thankHeader.length() + thank.length(), 
				moreThanksHeader.length(), fg, bg, SWT.BOLD )	
		};
		about.setText(  develHeader + devels  
						+ contributorHeader + contributors  
						+ thankHeader + thank  
						+ moreThanksHeader + moreThanks );	

		
		about.setStyleRanges( ranges );
	}

	private void createUpperPart( Composite parent ) {
		( ( GridLayout ) parent.getLayout() ).numColumns = 2;			
		
		CLabel icon = new CLabel( parent, SWT.NONE );
			icon.setImage( G2GuiResources.getImage( "G2GuiLogo" ) );
		icon.setLayoutData( new GridData( GridData.CENTER ) );
		
		StyledText info = new StyledText( parent, SWT.MULTI | SWT.READ_ONLY );
		info.setCaret( null );		
		info.setBackground( background );
		
		GridData gd = new GridData( GridData.FILL_BOTH );
			gd.grabExcessHorizontalSpace = true;
			gd.verticalAlignment = GridData.CENTER;
			gd.horizontalAlignment = GridData.CENTER;
		info.setLayoutData( gd );		
		info.setText(  "G2gui is (c) 2003 by G2gui team, \n" 
					 + "all of our own java code is released under \n" 
					 + "the  " );	
					 	
		Link link = new Link( "General Public License v2" );		
		link.addToStyledText( info );	
		link.setURL( "http://www.opensource.org/licenses/gpl-license.php" );
		this.linklist.add( link );
		
		info.addMouseMoveListener( new MouseMoveListener() {
			public void mouseMove( MouseEvent e ) {
				if ( isLink( ( StyledText ) e.widget, e.x, e.y ) ) { 				
					( ( StyledText )e.widget ).setCursor( handCursor );
					( ( StyledText )e.widget ).setToolTipText( activeLink.url );
				}
				else {			
					( ( StyledText )e.widget ).setCursor( null );
					( ( StyledText )e.widget ).setToolTipText( null );
				}
			}			
			 } );
		info.addMouseListener( new MouseListener() {
			public void mouseDoubleClick( MouseEvent e ) { }
			public void mouseDown( MouseEvent e ) {
				if ( activeLink != null )
					Program.launch( activeLink.url );
			}
			public void mouseUp( MouseEvent e ) { }
		} );
	}

	/**
	 * open the dialog
	 */
	protected void open() {
		myShell.pack();
		myShell.open();		
	}
	
	private boolean isLink( StyledText parent, int x, int y ) {
		this.activeLink = null;
		int offset;
		try {
			offset = parent.getOffsetAtLocation( new Point( x, y ) );
		} catch ( RuntimeException e ) {
			return false;			
		}
		Iterator it = linklist.iterator();
		while ( it.hasNext() ) {
			Link temp = ( Link ) it.next();
			if ( ( temp.offset < offset ) && ( offset  < temp.offset + temp.length ) ) {
				this.activeLink = temp;				
				return true	;	
			}	 
		}		
		return false;
	}
	
	
	/**
	 * Link
	 *
	 * @author $user$
	 * @version $Id: About.java,v 1.7 2003/08/30 18:20:50 dek Exp $ 
	 *
	 */
	public class Link {

		private String url;
		private int length = 0;
		private int offset = 0;
		private String linkText;
		private StyledText parent = null;
		private Color blue = Display.getCurrent().getSystemColor( SWT.COLOR_BLUE );

		/**
		 * @param parent the StyledText, in which the link is.
		 * @param link the text of the link
		 * @param offset the start of the link relative to the beginning
		 *  		of the text in the widget
		 * @param length the length of the text
		 */
		public Link( StyledText parent, String link, int offset, int length ) {
				this.parent = parent;
				this.linkText = link;
				this.offset = offset;
				this.length = length;			
		}
		/**
		 * @param url the url this link points at
		 */
		public void setURL( String url ) {
			this.url = url;
			
		}
		/**
		 * @param parent where to add the link (at the end of the text)
		 */
		public void addToStyledText( StyledText parent ) {
			this.offset = parent.getText().length();
			this.parent = parent;
			parent.setText( parent.getText() + linkText );	
			parent.setStyleRange( new StyleRange( offset, length, blue, parent.getBackground() ) );		
		}
		/**
		 * @param link the Link-Text
		 */
		public Link( String link ) {
			this.linkText = link;
			this.length = link.length();
		}
	}
}
/*
$Log: About.java,v $
Revision 1.7  2003/08/30 18:20:50  dek
minor changes & checkstyle

Revision 1.6  2003/08/30 18:02:24  dek
nearly finished

Revision 1.5  2003/08/30 17:35:12  dek
added url too tooltip of link

Revision 1.4  2003/08/30 17:29:31  dek
added link to GPL in about-dialog

Revision 1.3  2003/08/26 09:46:41  dek
about-dialog is now child of mainShell, instead of creating its own..

Revision 1.2  2003/08/26 09:19:31  dek
added todo-item

Revision 1.1  2003/08/25 20:27:56  dek
first sketch of an about-dialog, feel free to extend ;-)

*/