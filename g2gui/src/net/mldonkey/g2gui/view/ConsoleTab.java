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

import java.io.IOException;

import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.comm.*;
import net.mldonkey.g2gui.model.*;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;


/**
 * ConsoleTab
 *
 * @author $user$
 * @version $Id: ConsoleTab.java,v 1.18 2003/07/22 18:10:15 zet Exp $ 
 *
 */
public class ConsoleTab extends GuiTab implements Observer, ControlListener, Runnable {	
	private String[] consoleFont;
	private PreferenceStore preferenceStore;
	private ConsoleMessage consoleMessage;
	private CoreCommunication core;
	private Composite parent;
	private Text infoDisplay;	
	private Text input;
	private Font font;

	/**
	 * @param gui the main gui, which takes care of all our tabs
	 */
	public ConsoleTab( MainTab gui ) {
		super( gui );
		this.core = gui.getCore();		
		this.toolItem.setText( "Console" );		
		createContents( this.content );
		toolItem.setImage( inActiveIm );
	} 
	
	/* ( non-Javadoc )
	 * @see net.mldonkey.g2gui.view.widgets.Gui.G2guiTab#createContents( org.eclipse.swt.widgets.Composite )
	 */
	protected void createContents( Composite parent ) {	
		this.parent = parent;		
		parent.setLayout( null );
		parent.addControlListener( this );		
		/* Adding the Console-Display Text-field, and creating the saved font */
		infoDisplay = new Text( parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY );
		infoDisplay.setFont( loadFont() );
				
		input = new Text( parent, SWT.SINGLE | SWT.BORDER );					
		//Send command to core
		input.addKeyListener( new KeyAdapter() {
			public void keyPressed( KeyEvent e ) {
				if ( e.character == SWT.CR ) {
					infoDisplay.setText( infoDisplay.getText() + input.getText() + infoDisplay.getLineDelimiter() );
					String[] command = new String[ 1 ] ;
					command[ 0 ] = input.getText();
					( new EncodeMessage( Message.S_CONSOLEMSG, command ) ).sendMessage( ( ( Core ) core ).getConnection() );
					input.setText( "" );
				}
		  	}		
		} );	
	}
	
	/**
	 * 
	 * @return
	 */
	private Font loadFont() {
		this.preferenceStore = new PreferenceStore( "g2gui.pref" );
			try { preferenceStore.load(); } catch ( IOException e ) { }		
		this.consoleFont = preferenceStore.getString( "consoleFont" ).split( ":" );			
		if ( preferenceStore.getString( "consoleFont" ).equals( "" ) )
			consoleFont = null;						
		if ( consoleFont != null ) {
			font = new Font( null,
					 new FontData( consoleFont[ 0 ], 
					 		Integer.parseInt( consoleFont[ 1 ] ), 
					 		Integer.parseInt( consoleFont[ 2 ] ) ) ) ;
		}
		else font = infoDisplay.getFont();
		return font;
	}
	
	/**
	 * what to do, if this tab becomes active
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event ) {
		super.handleEvent( event );	
		infoDisplay.append( core.getConsoleMessage().getConsoleMessage().replaceAll("\n", infoDisplay.getLineDelimiter()) );
		core.getConsoleMessage().reset();
		infoDisplay.setFont( loadFont() );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ControlListener#controlMoved( org.eclipse.swt.events.ControlEvent )
	 */
	public void controlMoved( ControlEvent e ) { }

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ControlListener#controlResized( org.eclipse.swt.events.ControlEvent )
	 */
	public void controlResized( ControlEvent e ) {
		if ( ( this.infoDisplay != null ) && ( this.input != null ) ) {
			Rectangle rect = this.parent.getClientArea ();	
			int inputheight = this.input.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).y;		
			this.infoDisplay.setBounds( 0, 0, rect.width, ( rect.height - inputheight ) );
			this.input.setBounds( 0, ( rect.height - inputheight ), rect.width, inputheight );			
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update( java.util.Observable, java.lang.Object )
	 */
	public void update( Observable o, Object arg ) {
		/* are we responsable for this object */
		if ( arg instanceof ConsoleMessage ) {	
			this.consoleMessage = ( ConsoleMessage )arg;
			content.getDisplay().syncExec( this );
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {				
		String message = consoleMessage.getConsoleMessage();	
		consoleMessage.reset();	
		infoDisplay.append( message );
		infoDisplay.update();			
	}
}

/*
$Log: ConsoleTab.java,v $
Revision 1.18  2003/07/22 18:10:15  zet
console linedelimiter

Revision 1.17  2003/07/18 04:34:22  lemmstercvs01
checkstyle applied

Revision 1.16  2003/07/17 14:58:44  lemmstercvs01
refactored

Revision 1.15  2003/07/15 14:43:30  dek
*** empty log message ***

Revision 1.14  2003/07/04 12:30:53  dek
checkstyle

Revision 1.13  2003/07/03 10:13:09  dek
saving font now works

Revision 1.12  2003/07/03 01:56:45  zet
attempt(?) to save window size/pos & table column widths between sessions

Revision 1.11  2003/07/02 19:26:55  dek
fixed:< no i don't ever want to become active> bug

Revision 1.10  2003/07/02 19:25:41  dek
transferTab is now default ;-)

Revision 1.9  2003/07/02 16:34:55  dek
minor checkstyle

Revision 1.8  2003/07/02 16:34:19  dek
Checkstyle, JavaDocs still have to be added

Revision 1.7  2003/06/30 21:40:09  dek
CoolBar created

Revision 1.6  2003/06/30 19:11:57  dek
at work: setting the font to the one selected in the preference-dialog

Revision 1.5  2003/06/27 11:12:53  dek
works now

Revision 1.4  2003/06/27 10:36:17  lemmstercvs01
changed notify to observer/observable

Revision 1.3  2003/06/25 21:28:16  dek
cosmetic change, made console-output read-only

Revision 1.2  2003/06/25 18:04:53  dek
Console-Tab reworked

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/24 20:33:23  dek
first sketch

*/