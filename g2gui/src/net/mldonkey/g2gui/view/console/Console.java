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

import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

import net.mldonkey.g2gui.view.resource.G2GuiResources;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

/**
 * ConsoleTab
 *
 *
 * @version $Id: Console.java,v 1.16 2003/10/07 15:46:54 dek Exp $
 *
 */
public class Console extends Observable implements ControlListener {
    private Composite parent;
    private StyledText infoDisplay;
    private Text input;
    private int clientId = 0;
    private Color highlightColor = null;
    private final int MAX_LINES = 1000;
    private ArrayList commandHistory = new ArrayList();
    private int numOfCommands = 0;
    private int recentCommand = 0;

	/**
	 * @param parent The parent composite to draw in
	 * @param style The style we should look like
	 */
    public Console( Composite parent, int style ) {
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
        infoDisplay =
            new StyledText( parent, style | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY );
        Menu popupMenu = new Menu( infoDisplay );
        MenuItem copyItem = new MenuItem( popupMenu, SWT.PUSH );
        copyItem.setText( G2GuiResources.getString( "MISC_COPY" ) );
        copyItem.addListener( SWT.Selection,
                              new Listener() {
                public void handleEvent( Event event ) {
                    infoDisplay.copy();
                }
            } );

        MenuItem selectAll = new MenuItem( popupMenu, SWT.PUSH );
        selectAll.setText( G2GuiResources.getString( "MISC_SELECT_ALL" ) );
        selectAll.addListener( SWT.Selection,
                               new Listener() {
                public void handleEvent( Event event ) {
                    infoDisplay.selectAll();
                }
            } );

        MenuItem clearItem = new MenuItem( popupMenu, SWT.PUSH );
        clearItem.setText( G2GuiResources.getString( "MISC_CLEAR" ) );
        clearItem.addListener( SWT.Selection,
                               new Listener() {
                public void handleEvent( Event event ) {
                    infoDisplay.replaceTextRange( 0, infoDisplay.getText().length(), "" );
                }
            } );
        infoDisplay.setMenu( popupMenu );
        input = new Text( parent, SWT.SINGLE | SWT.BORDER );

        //Send command 
        input.addKeyListener( new KeyAdapter() {
                public void keyPressed( KeyEvent e ) {
                    int numLinesDisplayed =
                              infoDisplay.getClientArea().height / infoDisplay.getLineHeight();
                    if ( e.keyCode == SWT.PAGE_UP ) {
                        if ( infoDisplay.getTopIndex() > numLinesDisplayed )
                            infoDisplay.setTopIndex( infoDisplay.getTopIndex() - ( numLinesDisplayed ) );
                        else
                            infoDisplay.setTopIndex( 0 );
                    }
                    else if ( e.keyCode == SWT.PAGE_DOWN )
                        infoDisplay.setTopIndex( infoDisplay.getTopIndex() + ( numLinesDisplayed ) );
                    else if ( e.character == SWT.CR ) {
                        if ( clientId > 0 )
                            infoDisplay.append( getTimeStamp() + "> " );
                        int start = infoDisplay.getCharCount();
                        String outText = input.getText();
                        infoDisplay.append( outText + getLineDelimiter() );
                        infoDisplay.setStyleRange( new StyleRange( start, outText.length(), highlightColor,
                                                                   infoDisplay.getBackground() ) );
                        setChanged();
                        notifyObservers( input.getText() );
                        commandHistory.add( numOfCommands, input.getText() );
                        /*
                         * Add the command to history and increase counter for
                         * command history
                         */
						numOfCommands++;
						recentCommand = numOfCommands - 1;
                        
                        input.setText( "" );
                    }
                    /*next two cases are for brwosing through command-history*/                     
                   else if ( e.keyCode == SWT.ARROW_UP ) {
					if ( numOfCommands != 0 ) {	
						/*additional +" " is nescessary, because ARROW_UP also steps one char back*/					
						input.setText( ( String ) commandHistory.get( recentCommand ) + " " );
						/*set the cursor at the end of the line*/
						input.setSelection( input.getText().length() );
						if ( recentCommand > 0 )					                	
							recentCommand--; 	
					}			
                   }
				   else if ( e.keyCode == SWT.ARROW_DOWN ) {				   
				   	if ( recentCommand < commandHistory.size() - 1 ) {						
						recentCommand++;
						input.setText( ( String ) commandHistory.get( recentCommand ) );
						/*set the cursor at the end of the line*/
						input.setSelection( input.getText().length() );
				   	}
				   }
				   
                }
            } );
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ControlListener#controlMoved( org.eclipse.swt.events.ControlEvent )
     */
    public void controlMoved( ControlEvent e ) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ControlListener#controlResized( org.eclipse.swt.events.ControlEvent )
     */
    public void controlResized( ControlEvent e ) {
        if ( ( this.infoDisplay != null ) && ( this.input != null ) ) {
            Rectangle rect = this.parent.getClientArea();
            int inputheight = this.input.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).y;
            this.infoDisplay.setBounds( 0, 0, rect.width, ( rect.height - inputheight ) );
            this.input.setBounds( 0, ( rect.height - inputheight ), rect.width, inputheight );
        }
    }

    /**
     *
     * @param String message 
     */
    public void append( String message ) {
        if ( infoDisplay.getLineCount() > MAX_LINES )
            infoDisplay.replaceTextRange( 0, infoDisplay.getOffsetAtLine( 5 ), "" );
        infoDisplay.append( message );
        infoDisplay.update();

        // workaround for GTK2 bug, to focus the bottom
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=40800
        infoDisplay.setSelection( infoDisplay.getCharCount() );
        infoDisplay.showSelection();
    }

    public String getLineDelimiter() {
        return infoDisplay.getLineDelimiter();
    }

    public void setDisplayFont( Font font ) {
        infoDisplay.setFont( font );
    }
   
    public void setInputFont( Font font ) {
        input.setFont( font );
    }

    public void setDisplayForeground( Color color ) {
        infoDisplay.setForeground( color );
    }

    public void setDisplayBackground( Color color ) {
        infoDisplay.setBackground( color );
    }

    public void setInputForeground( Color color ) {
        input.setForeground( color );
    }

    public void setInputBackground( Color color ) {
        input.setBackground( color );
    }

    public void setFocus() {
        input.setFocus();
    }

    public void setHighlightColor( Color color ) {
        highlightColor = color;
    }

    public void setClientId( int i ) {
        clientId = i;
    }

    public int getClientId() {
        return clientId;
    }

    public String getTimeStamp() {
        SimpleDateFormat sdFormatter = new SimpleDateFormat( "[HH:mm:ss] " );
        Date oToday = new Date();
        return sdFormatter.format( oToday );
    }
}

/*
$Log: Console.java,v $
Revision 1.16  2003/10/07 15:46:54  dek
added Command-History to Console

Revision 1.15  2003/09/20 01:22:17  zet
*** empty log message ***

Revision 1.14  2003/09/18 09:54:45  lemmster
checkstyle

Revision 1.13  2003/09/16 16:19:03  zet
localise

Revision 1.12  2003/09/16 02:34:16  zet
selectall/clear menuitems

Revision 1.11  2003/09/03 14:49:07  zet
optionally spawn core from gui

Revision 1.10  2003/08/26 14:28:26  zet
add Copy menuitem

Revision 1.9  2003/08/25 22:11:53  zet
update style bits

Revision 1.8  2003/08/23 15:21:37  zet
remove @author

Revision 1.7  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: dek $

Revision 1.6  2003/08/18 06:00:01  zet
fix null pointer (I'm not even sure it is real..)

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
