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

import gnu.regexp.RE;
import gnu.regexp.REException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * ExecConsole : to spawn and watch an executable in a shell/console
 *
 * @version $Id: ExecConsole.java,v 1.5 2003/11/22 02:24:30 zet Exp $
 *
 */
public class ExecConsole implements Observer {
    private Shell shell;
    private StyledText outputConsole;
    private Process execProcess;
    private StreamMonitor stdoutMonitor;
    private StreamMonitor stderrMonitor;
    private Color highlightColor;
    private final int MAX_LINES = 5000;
    private final int STDOUT = 1;
    private final int STDERR = 2;
    private RE errorRE;

	/**
	 * Creates a new ExecConsole to launch the mldonkey core
	 */
    public ExecConsole() {
        createContents();
        runExec();
    }
  
    public void createContents() {
        shell = new Shell( SWT.CLOSE | SWT.TITLE | SWT.RESIZE | SWT.BORDER );
        shell.setImage( G2GuiResources.getImage( "ProgramIcon" ) );
        shell.setText( "Core" );
        shell.setLayout( WidgetFactory.createGridLayout( 1, 0, 0, 0, 0, false ) );
        shell.addDisposeListener( new DisposeListener() {
                public synchronized void widgetDisposed( DisposeEvent e ) {
                    PreferenceStore p = PreferenceLoader.getPreferenceStore();
                    PreferenceConverter.setValue( p, "coreExecutableWindowBounds", shell.getBounds() );
                }
            } );
        shell.addListener( SWT.Close,
                           new Listener() {
                public void handleEvent( Event event ) {
                    event.doit = false;
                    shell.setVisible( false );
                }
            } );
        if ( PreferenceLoader.contains( "coreExecutableWindowBounds" ) )
            shell.setBounds( PreferenceLoader.loadRectangle( "coreExecutableWindowBounds" ) );
        outputConsole =
            new StyledText( shell, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY );
        outputConsole.setLayoutData( new GridData( GridData.FILL_BOTH ) );
        outputConsole.setFont( PreferenceLoader.loadFont( "consoleFontData" ) );
        outputConsole.setBackground( PreferenceLoader.loadColour( "consoleBackground" ) );
        outputConsole.setForeground( PreferenceLoader.loadColour( "consoleForeground" ) );
        highlightColor = PreferenceLoader.loadColour( "consoleHighlight" );
        outputConsole.append( "availableProcessors: " + Runtime.getRuntime().availableProcessors()
                              + outputConsole.getLineDelimiter() );
        outputConsole.append( "maximumMem: " + Runtime.getRuntime().maxMemory()
                              + outputConsole.getLineDelimiter() );
        outputConsole.append( "totalMem: " + Runtime.getRuntime().totalMemory()
                              + outputConsole.getLineDelimiter() );
        outputConsole.append( "freeMem: " + Runtime.getRuntime().freeMemory()
                              + outputConsole.getLineDelimiter() + outputConsole.getLineDelimiter() );
        Menu popupMenu = new Menu( outputConsole );
        MenuItem copyItem = new MenuItem( popupMenu, SWT.PUSH );
        copyItem.setText( G2GuiResources.getString( "MISC_COPY" ) );
        copyItem.addListener( SWT.Selection,
                              new Listener() {
                public void handleEvent( Event event ) {
                    outputConsole.copy();
                }
            } );
        try {
            errorRE = new RE( "error", RE.REG_ICASE );
        }
        catch ( REException e ) {
            errorRE = null;
        }
    }

    public void runExec() {
        try {
            File executable = new File( PreferenceLoader.loadString( "coreExecutable" ) );
            String workingDirectory = executable.getParent();
            execProcess =
                Runtime.getRuntime().exec( executable.toString(), null, new File( workingDirectory ) );
            stdoutMonitor = new StreamMonitor( execProcess.getInputStream(), STDOUT );
            stdoutMonitor.addObserver( this );
            Thread stdoutThread = new Thread( stdoutMonitor );
            stdoutThread.start();
            stderrMonitor = new StreamMonitor( execProcess.getErrorStream(), STDERR );
            stderrMonitor.addObserver( this );
            Thread stderrThread = new Thread( stderrMonitor );
            stderrThread.start();
        }
        catch ( IOException e ) {
            System.out.println( "exec:" + e );
        }
    }
  
    public void update( final Observable o, Object obj ) {
        if ( obj instanceof String ) {
            final String newLine = ( String ) obj;
            if ( !outputConsole.isDisposed() )
                outputConsole.getDisplay().asyncExec( new Runnable() {
                        public void run() {
                            if ( outputConsole.isDisposed() )
                                return;
                            appendLine( ( StreamMonitor ) o, newLine );
                        }
                    } );
        }
    }
  
    public void appendLine( StreamMonitor streamMonitor, String newLine ) {
        if ( outputConsole.getLineCount() > MAX_LINES )
            outputConsole.replaceTextRange( 0, outputConsole.getOffsetAtLine( 5 ), "" );
        int start = outputConsole.getCharCount();
        outputConsole.append( newLine + outputConsole.getLineDelimiter() );
        if ( streamMonitor.getType() == STDERR )
            outputConsole.setStyleRange( 
            	new StyleRange( start, newLine.length(),
                                outputConsole.getDisplay().getSystemColor( SWT.COLOR_RED ),
                                outputConsole.getBackground() ) );
        else if ( errorRE.getMatch( newLine ) != null )
            outputConsole.setStyleRange( new StyleRange( start, newLine.length(), highlightColor,
                                                         outputConsole.getBackground() ) );
        outputConsole.update();

        // workaround for GTK2 bug, to focus the bottom
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=40800
        outputConsole.setSelection( outputConsole.getCharCount() );
        outputConsole.showSelection();
    }

    public Shell getShell() {
        return shell;
    }

    public void dispose() {
        stdoutMonitor.stop();
        stderrMonitor.stop();
        outputConsole.dispose();
        shell.dispose();
    }

    class StreamMonitor extends Observable implements Runnable {
        private boolean keepAlive = true;
        private InputStream inputStream;
        private int type;

        public StreamMonitor( InputStream inputStream, int type ) {
            this.inputStream = inputStream;
            this.type = type;
        }

        public void stop() {
            keepAlive = false;
        }

        public int getType() {
            return type;
        }

        public void run() {
            try {
                String line;
                BufferedReader in = new BufferedReader( new InputStreamReader( inputStream ) );
                while ( keepAlive && ( ( line = in.readLine() ) != null ) ) {
                    setChanged();
                    notifyObservers( line );
                }
            }
            catch ( IOException e ) {
                System.out.println( "streamMonitor:" + e );
            }
        }
    }
}

/*
$Log: ExecConsole.java,v $
Revision 1.5  2003/11/22 02:24:30  zet
widgetfactory & save sash postions/states between sessions

Revision 1.4  2003/09/20 22:07:40  zet
*** empty log message ***

Revision 1.3  2003/09/20 01:22:17  zet
*** empty log message ***

Revision 1.2  2003/09/18 09:54:45  lemmster
checkstyle

Revision 1.1  2003/09/03 14:49:07  zet
optionally spawn core from gui


*/
