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
package net.mldonkey.g2gui.view.transferTree;




import net.mldonkey.g2gui.model.FileInfo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * ChunkView
 *
 * @author $user$
 * @version $Id: ChunkView.java,v 1.3 2003/07/13 20:28:51 dek Exp $ 
 *
 */
public class ChunkView extends Canvas {
	private FileInfo fileinfo;

	private Image image;
	
	/**
	 * creates a chunkview-Object for the given FileInfo
	 * @param parent here does the object live
	 * @param style this style do we prefer (not used atm)
	 * @param fileInfo  and the source of all the information
	 */
	public ChunkView( Composite parent, int style, FileInfo fileInfo ) {	
		super( parent, style );
		this.fileinfo = fileInfo;	
		createImage();		
		addDisposeListener( new DisposeListener() {
			public void widgetDisposed( DisposeEvent e ) {		
				ChunkView.this.widgetDisposed( e );		
				}
			} );
	
		
		addPaintListener( new PaintListener() {
			public void paintControl( PaintEvent e ) {
				ChunkView.this.paintControl( e );
				}
			} );
	}
	


	/**
	 * @param chunks
	 */
	private void createImage() {
		String chunks = fileinfo.getChunks();
		String avail = fileinfo.getAvail();
		int length = 0;
		
		Color red = getDisplay().getSystemColor( SWT.COLOR_RED );
		Color blue = getDisplay().getSystemColor( SWT.COLOR_BLUE );
		Color black = getDisplay().getSystemColor( SWT.COLOR_BLACK );
		Color yellow = getDisplay().getSystemColor( SWT.COLOR_YELLOW );
		
		
		if ( avail.length() != 0 ) 
				length = avail.length();
				
		byte[] temp = avail.getBytes();		
		
		if ( image != null ) image.dispose();
		image = new Image( getDisplay(), length, 127 );
		GC gc = new GC( image );
		for ( int i = 0; i < avail.length(); i++ ) {		
			short height = temp[ i ]	;
			gc.setForeground( blue );	
			
			//this availability is so low, we can assume, it is not available:			
			if ( temp[ i ] == 0 ) {			
				gc.setForeground( red );
				height = 127;
			}
			
			if ( chunks.charAt( i ) == '2' ) {			
				gc.setForeground( black );
				height = 127;
			}
									
			gc.drawLine( i, 127, i, 127 - height );			
		}	
		gc.dispose();	
		
	}

	/**
	 * @param e
	 */
	protected void widgetDisposed( DisposeEvent e ) {
		image.dispose();	
	}
	
	/**
	 * @param e
	 */
	protected void paintControl( PaintEvent e ) {
		GC gc = e.gc;
		int width = e.width;
		int height = e.height;
		if ( width >= 2 ) {
			width = e.width - 2;
		}
		if ( e.height >= 2 ) {
			height = e.height - 2;
		}
		if ( image != null ) {
			gc.drawImage(
				image,
				0,
				0,
				image.getBounds().width,
				image.getBounds().height,
				1,
				1,
				width,
				height );
		}
		gc.dispose();
	}



	/**
	 * redraws this widget, with refreshed Information from FileInfo (if changed)
	 */
	public void refresh() {		
		createImage();
		
		
	}
}

/*
$Log: ChunkView.java,v $
Revision 1.3  2003/07/13 20:28:51  dek
now, everytime a new chunkview is created, but now everything displayed is recent information

Revision 1.2  2003/07/13 20:12:39  dek
fixed Exception and applied checkstyle

Revision 1.1  2003/07/13 12:48:28  dek
chunk-bar begins to work

*/