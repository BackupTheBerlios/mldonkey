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
 * @version $Id: ConsoleTab.java,v 1.8 2003/07/02 16:34:19 dek Exp $ 
 *
 */
public class ConsoleTab extends G2guiTab implements Observer, ControlListener, Runnable {	
	private String consoleFont;
	private PreferenceStore preferenceStore;
	private ConsoleMessage consoleMessage;
	private CoreCommunication core;
	private Composite parent;
	private Text infoDisplay;	
	private Text input;

	/**
	 * @param gui
	 */
	public ConsoleTab( IG2gui gui ) {
		super( gui );
		this.core = gui.getCore();		
		this.toolItem.setText( "Console" );		
		createContents( this.content );
		( ( Core ) core ).addObserver( this );
		this.preferenceStore = new PreferenceStore( "g2gui.pref" );
		this.consoleFont = preferenceStore.getString( "ConsoleFont" );		
	} 
	
	/* ( non-Javadoc )
	 * @see net.mldonkey.g2gui.view.widgets.Gui.G2guiTab#createContents( org.eclipse.swt.widgets.Composite )
	 */
	protected void createContents( Composite parent ) {		
		this.parent = parent;
		parent.setLayout( null );
		parent.addControlListener( this );		
			/*
			 * Adding the Console-Display Text-field
			 */			
			infoDisplay = new Text( parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY );
		//infoDisplay.setFont( new Font( null, consoleFont, 0, 0 ) );
			input = new Text( parent, SWT.SINGLE | SWT.BORDER );					
			//Send command to core
			input.addKeyListener( new KeyAdapter() {
				public void keyPressed( KeyEvent e ) {
					if ( e.character == SWT.CR ) {
						infoDisplay.setText( infoDisplay.getText() + input.getText() + "\n" );
						String[] command = new String[ 1 ] ;
						command[ 0 ] = input.getText();
						( new EncodeMessage( Message.S_CONSOLEMSG, command ) ).sendMessage( ( ( Core ) core ).getConnection() );
						input.setText( "" );
					}
		  		}		
			} );	
	}
	
	public void handleEvent( Event event ) {
		mainWindow.setActive( this );
		
		infoDisplay.append( core.getConsoleMessage().getConsoleMessage() );
		core.getConsoleMessage().reset();
	}
	
	/*
	 * Everything below here is a private kind of LayoutManager, only for this tab, 
	 * as none of the present ones did what i wanted it to do	
	 * 
	 */
	/* ( non-Javadoc )
	 * @see org.eclipse.swt.events.ControlListener#controlMoved( org.eclipse.swt.events.ControlEvent )
	 */
	public void controlMoved( ControlEvent e ) {		
		/*do nothing*/		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.swt.events.ControlListener#controlResized( org.eclipse.swt.events.ControlEvent )
	 */
	public void controlResized( ControlEvent e ) {
		if ( ( this.infoDisplay != null ) && ( this.input!=null ) ){
			Rectangle rect = this.parent.getClientArea ();	
			int inputheight = this.input.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).y;		
			this.infoDisplay.setBounds( 0, 0, rect.width, ( rect.height - inputheight ) );
			this.input.setBounds( 0, ( rect.height - inputheight ), rect.width, inputheight );			
		}
	}

	/* ( non-Javadoc )
	 * @see java.util.Observer#update( java.util.Observable, java.lang.Object )
	 */
	public void update( Observable o, Object arg ) {
		if ( arg instanceof ConsoleMessage ) {	
			this.consoleMessage = ( ConsoleMessage )arg;
			content.getDisplay().syncExec( this );
		}
		/* else: we are not responsible for this Information to be displayed*/
	}

	/* ( non-Javadoc )
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