/*
 * Copyright 2003
 * G2Gui Team
 * 
 * 
 * This file is part of G2Gui.
 *
 * G2Gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2Gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2Gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.helper;

import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * SplashShell
 *
 * @version $Id: Splash.java,v 1.1 2003/11/20 14:02:17 lemmster Exp $ 
 *
 */
public class Splash {
	private static Display display;
	private static Shell shell;
	private static ProgressBar bar;
//	private static Label label;
	
	public Splash( Display aDisplay, int barSize ) {
		display = aDisplay;
		
		shell = new Shell( display, SWT.NO_TRIM | SWT.NO_BACKGROUND | SWT.ON_TOP );
		shell.setLayout( new FormLayout() );

		final Image image = G2GuiResources.getImage("splashScreen");
		shell.addPaintListener( new PaintListener() {
				public void paintControl( PaintEvent e ) {
					e.gc.drawImage( image, 0, 0 );
				}
			} );
			
		//TODO label is disabled because no transparent control is possible and therefore the splash looks ugly...
		// one solution: create a new image with the underlying image area to use it as a bg for the label
		// remember that the label is uncommented in increaseSplashBar() and dispose() too
		/*		
		// set the location of the text
		label = new Label( shell, SWT.NONE );
		FormData formData = new FormData();
		formData.left = new FormAttachment( 0, 150 );
		formData.right = new FormAttachment( 100, -150 );
		formData.bottom = new FormAttachment( 100, -25 );
		label.setLayoutData( formData );
		*/
		
		// set the location of the bar
		bar = new ProgressBar( shell, SWT.NONE );
		bar.setMaximum( barSize );
		FormData formData = new FormData();
		formData.left = new FormAttachment( 0, 5 );
		formData.right = new FormAttachment( 100, -5 );
		formData.bottom = new FormAttachment( 100, -5 );
		bar.setLayoutData( formData );

		shell.pack();

		// set the size on the splash
		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Rectangle imageBounds = image.getBounds();
		shell.setBounds(
			displayBounds.x + ( ( displayBounds.width - imageBounds.width ) / 2 ),
			displayBounds.y + ( ( displayBounds.height - imageBounds.height ) / 2 ),
			imageBounds.width, imageBounds.height);

		shell.open();
	}
	
	/**
	 * This method should only be called from the <code>Thread</code> who created 
	 * this Splash <code>Display</code>.
	 * 
	 * @param aString The new text for the Label
	 */
	public static void increaseSplashBar( final String aString ) {
		if ( shell != null ) {
			bar.setSelection( bar.getSelection() + 1 );
			//label.setText( aString );
		}		
	}
	
	/**
	 * Dispose this Splash and all its Controls
	 * (calling only allowed from owning Thread)
	 */
	public static void dispose() {
		if ( shell != null ) {
			shell.dispose();
			bar.dispose();
			//label.dispose();
		}
	}

	/**
	 * Set the <code>Shell</code> of this Splash to visible
	 * (calling only allowed from owning Thread)
	 *
 	 * @param b true for visible/false otherwise
	 */
	public static void setVisible( boolean b ) {
		if ( shell != null )
			shell.setVisible( b );
	}
}

/*
$Log: Splash.java,v $
Revision 1.1  2003/11/20 14:02:17  lemmster
G2Gui cleanup

*/