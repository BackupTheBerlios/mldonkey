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

import net.mldonkey.g2gui.helper.VersionInfo;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
/**
 * About
 *
 * @version $Id: About.java,v 1.28 2004/01/13 18:53:00 psy Exp $ 
 *
 */
public class About extends Dialog {

	private Link activeLink;
	private final Cursor handCursor = new Cursor( Display.getDefault(), SWT.CURSOR_HAND );
	private List linklist = new ArrayList();	
	private Color background = Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND );

	/**
	 * @param shell were this dialog lives
	 */
	public About( Shell shell ) {
		super( shell );
		setDefaultImage( G2GuiResources.getImage( "ProgramIcon" ) );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell( newShell );
		newShell.setText( "G2gui " + VersionInfo.getVersion() );
	}
	/**
	 * @param parent the Control on which the contents should be created
	 * @return The Control on which the contents are created
	 */
	public Control createContents( Composite parent ) {
		GridLayout layout;
		
		initializeDialogUnits( parent );		
		
		layout = new GridLayout( 1, false );
			layout.marginHeight = 0;
			layout.marginWidth = 0;
		parent.setLayout( layout );
		
		Composite dialogArea = ( Composite )createDialogArea( parent );
		layout = new GridLayout( 1, false );
			layout.marginHeight = 0;
			layout.marginWidth = 0;
		dialogArea.setLayout( layout );			
		
		Composite upperPart = new Composite ( dialogArea, SWT.NONE );
			upperPart.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			upperPart.setLayout( new GridLayout() );		
		createUpperPart( upperPart );
		upperPart.layout();
		
		Label bar1 = new Label( dialogArea, SWT.SEPARATOR | SWT.HORIZONTAL );
			bar1.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			
		Composite lowerPart = new Composite ( dialogArea, SWT.NONE );
			lowerPart.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			lowerPart.setLayout( new GridLayout() );		
		createLowerPart( lowerPart );
		lowerPart.layout();
		
		Label bar2 = new Label( dialogArea, SWT.SEPARATOR | SWT.HORIZONTAL );
			bar2.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			
		
		createButtonBar( parent );	
		return parent;	
		
	}

	private void createLowerPart( Composite parent ) {
		parent.setLayoutData( new GridData( GridData.BEGINNING ) );
		StyledText about = new StyledText( parent, SWT.MULTI | SWT.READ_ONLY );
		about.setCaret( null );
		about.setBackground( background );
		G2GuiResources.getString( "ABOUT_DEVEL" );
		
		String develHeader = G2GuiResources.getString( "ABOUT_DEVEL" ) + "\n";
		String devels = "Dek, lemmy, vnc\n\n";

		String contributorHeader = G2GuiResources.getString( "ABOUT_CONTRIB" ) + "\n";
		String contributors = "housetier, mitch, vaste, z\n\n";
		
		
		String thankHeader = G2GuiResources.getString( "ABOUT_THANK" ) + "\n";
		String thank = 
		  "  * The mldonkey core developers! (GPL)\n"
		+ "  * The KDE project for icons (GPL)\n"
		+ "  * The creators of Noia, Lush and Nuvola KDE icon sets (GPL)\n"
		+ "  * The emule-plus project for icons (CCL)\n"
		+ "  * The Gnu/Trove and Regex project (GPL)\n"
		+ "  * The whole Eclipse and SWT team (CPL)\n\n";
		
		
		String moreThanksHeader = G2GuiResources.getString( "ABOUT_MORE_THANK" ) + "\n";
		String moreThanks = 
		  "  * JCraft's extssh2 for use in eclipse\n"
		+ "  * The creators of phpWiki\n"
		+ "  * The Freenode IRC network";		
		
		
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
		info.setText(  "G2gui is (c) 2003,2004 by the G2gui team, \n" 
					 + "all of our own java code is released under \n" 
					 + "the  " );	
					 	
		Link link = new Link( "General Public License v2" );		
		link.addToStyledText( info );	
		link.setURL( "http://www.opensource.org/licenses/gpl-license.php" );
		this.linklist.add( link );
		
		Label swt = new Label( parent, SWT.NONE );
				gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
				gd.horizontalSpan = 2;
				swt.setLayoutData( gd );
				swt.setText( "g2gui " + VersionInfo.getVersion() + "\n" + "swt-" + SWT.getPlatform() + "-" + SWT.getVersion() );
		
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
	
	
	protected void createButtonsForButtonBar( Composite parent ) {
		createButton( parent, IDialogConstants.OK_ID, G2GuiResources.getString( "ABOUT_LEAVE" ), true );
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
	 * @version $Id: About.java,v 1.28 2004/01/13 18:53:00 psy Exp $ 
	 *
	 */
	public class Link {

		private String url;
		private int length = 0;
		private int offset = 0;
		private String linkText;
		private Color blue = Display.getCurrent().getSystemColor( SWT.COLOR_BLUE );

		/**
		 * @param parent the StyledText, in which the link is.
		 * @param link the text of the link
		 * @param offset the start of the link relative to the beginning
		 *  		of the text in the widget
		 * @param length the length of the text
		 */
		public Link( String link, int offset, int length ) {
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
Revision 1.28  2004/01/13 18:53:00  psy
credits update

Revision 1.27  2003/12/04 08:47:31  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.26  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.25  2003/11/09 13:24:02  lemmy
typo in credits

Revision 1.24  2003/11/08 15:16:31  vnc
little credits cleanup

Revision 1.23  2003/10/27 00:17:26  zet
set titlebar text

Revision 1.22  2003/10/19 03:55:37  zet
add swt info

Revision 1.21  2003/10/09 09:59:19  lemmy
get the version number from the centralized class VersionInfo

Revision 1.20  2003/10/08 22:24:23  zet
include a version #

Revision 1.19  2003/09/23 05:30:40  lemmy
fixed typo

Revision 1.18  2003/09/14 12:13:22  dek
removed Todo


Revision 1.16  2003/09/03 15:02:52  zet
+the

Revision 1.15  2003/09/02 14:27:03  dek
i18n of About Dialog (only bold headers, as i want other stuff hardcoded)

Revision 1.14  2003/09/01 14:44:36  dek
has now icon in upper-left-corner

Revision 1.13  2003/09/01 14:31:12  dek
no change in look / function, but converted to JFace Dialog..

Revision 1.12  2003/09/01 12:42:32  dek
lemmy now in lowercase ;-)

Revision 1.11  2003/08/31 15:48:23  zet
add core devs

Revision 1.10  2003/08/31 12:40:51  dek
set position of about-dialog in middle of app

Revision 1.9  2003/08/31 12:27:31  dek
background color is now still ok, even if one has searched ;-)

Revision 1.8  2003/08/30 22:35:53  dek
added OK-Button

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