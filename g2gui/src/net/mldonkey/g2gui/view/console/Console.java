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
package net.mldonkey.g2gui.view.console;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * ConsoleTab
 *
 * @author 
 * @version $Id: Console.java,v 1.5 2003/08/16 05:08:21 zet Exp $ 
 *
 */
public class Console extends Observable implements ControlListener  {	
	private Composite parent;
	private StyledText infoDisplay;	
	private Text input;
	private int clientId = 0;
	private Color highlightColor = null;

	/**
	 * @param gui the main gui, which takes care of all our tabs
	 */
	public Console ( Composite parent, int style ) {
		createContents( parent, style );
	} 
	
	/* ( non-Javadoc )
	 * @see net.mldonkey.g2gui.view.widgets.Gui.G2guiTab#createContents( org.eclipse.swt.widgets.Composite )
	 */
	protected void createContents( Composite parent, int style ) {	
		this.parent = parent;		
		parent.setLayout( null );
		parent.addControlListener( this );		
		/* Adding the Console-Display Text-field */
		infoDisplay = new StyledText( parent, style | SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY );
		input = new Text( parent, SWT.SINGLE | SWT.BORDER );					
		
		//Send command 
		input.addKeyListener( new KeyAdapter() {
			public void keyPressed( KeyEvent e ) {
				int numLinesDisplayed = infoDisplay.getClientArea().height / infoDisplay.getLineHeight();
				if (e.keyCode == SWT.PAGE_UP) {
					if ( infoDisplay.getTopIndex() > numLinesDisplayed )  
						infoDisplay.setTopIndex( infoDisplay.getTopIndex() - ( numLinesDisplayed ) );
					else
						infoDisplay.setTopIndex(0);
				}
				else if (e.keyCode == SWT.PAGE_DOWN) {
					infoDisplay.setTopIndex( infoDisplay.getTopIndex() + ( numLinesDisplayed ) );
				}
				else if ( e.character == SWT.CR ) {
					if (clientId > 0) infoDisplay.append( getTimeStamp() + "> " );
					int start = infoDisplay.getCharCount();
					String outText = input.getText();
					infoDisplay.append( outText + getLineDelimiter() );
					infoDisplay.setStyleRange(new StyleRange(start, outText.length(), highlightColor, infoDisplay.getBackground()));
					setChanged();
					notifyObservers( input.getText() );
					input.setText( "" );
				}
			}		
		} );	
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

	public void append(String message) {
		infoDisplay.append( message );
		infoDisplay.update();
		// workaround for GTK2 bug, to focus the bottom
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=40800
		infoDisplay.setSelection(infoDisplay.getCharCount());
		infoDisplay.showSelection();	
	}
	
	public String getLineDelimiter() {
		return infoDisplay.getLineDelimiter();
	}	
	public void setDisplayFont(Font font) {
		infoDisplay.setFont(font);
	}
	public void setInputFont(Font font) {
		input.setFont(font);
	}
	public void setDisplayForeground(Color color) {
		infoDisplay.setForeground(color);
	}
	public void setDisplayBackground(Color color) {
		infoDisplay.setBackground(color);
	}
	public void setInputForeground(Color color) {
		input.setForeground(color);
	}
	public void setInputBackground(Color color) {
		input.setBackground(color);
	}
	public void setFocus() {
		input.setFocus();
	}
	public void setHighlightColor(Color color) {
		highlightColor = color;
	}
	public void setClientId(int i) {
		clientId = i;
	}
	public int getClientId() {
		return clientId;
	}
	public String getTimeStamp() {
		SimpleDateFormat sdFormatter = new SimpleDateFormat("[HH:mm:ss] ");
		Date oToday = new Date();
		return sdFormatter.format(oToday);
	}
	public void dispose() {
		// If these were loaded from the PreferenceLoader, is this necessary? 
		
		infoDisplay.getBackground().dispose();
		infoDisplay.getForeground().dispose();
		input.getBackground().dispose();
		input.getForeground().dispose();
		highlightColor.dispose();
	}
	
}


/*
$Log: Console.java,v $
Revision 1.5  2003/08/16 05:08:21  zet
fix gtk crash when closing a message tab

Revision 1.4  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.3  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging

Revision 1.2  2003/08/10 00:38:17  zet
setFocus on activation

Revision 1.1  2003/08/08 20:16:13  zet
central PreferenceLoader, abstract Console


*/
