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




import net.mldonkey.g2gui.model.ClientInfo;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * ChunkView
 *
 * @author $user$
 * @version $Id: ChunkView.java,v 1.4 2003/07/14 19:26:40 dek Exp $ 
 *
 */
public class ChunkView extends Canvas {


	private ClientInfo clientInfo;

	private TableColumn column;

	private FileInfo fileInfo;

	private Image image;
	
	/**
	 * this type of Chunkview,<br>
	 *   "1" for fileInfo<br>
	 *   "2" for clientInfo<br>
	 */
	private short type;
	
	/**
	 * short identification to mark this chunkview as a FileInfo Chunkview<br>
	 * value is 1
	 */
	private final short isFileInfo = 1;
	/**
	 * short identification to mark this chunkview as a ClientInfo Chunkview<br>
	 * value is 2
	 */
	private final short isClientInfo = 2;
	
	/**
	 * creates a chunkview-Object for the given FileInfo
	 * @param parent here does the object live
	 * @param style this style do we prefer (not used atm)
	 * @param fileInfo  and the source of all the information
	 * @param column the column in which the widget appears
	 */
	public ChunkView( Table parent, int style, FileInfo fileInfo, int column ) {			
		super( parent, style );
		this.column = parent.getColumn( column );
		this.fileInfo = fileInfo;	
		this.type = isFileInfo;
		createFileInfoImage();		
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
	 * creates a chunkview-Object for the given FileInfo
	 * @param parent here does the object live
	 * @param style this style do we prefer (not used atm)
	 * @param clientInfo the source of this chunkviews information
	 * @param fileInfo for this fileInfo we want to display the Information
	 * @param column the column in which the widget appears
	 */
	public ChunkView( Table parent, int style, ClientInfo clientInfo, FileInfo fileInfo, int column ) {			
		super( parent, style );
		this.column = parent.getColumn( column );
		this.clientInfo = clientInfo;
		this.fileInfo = fileInfo;
		this.type = isClientInfo;
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
	

	private void createImage() {
		switch ( type ) {
			case isClientInfo :	
				createClientInfoImage();	
				break;
				
			case isFileInfo :
				createFileInfoImage();				
				break;
				
			default:
			/*do nothing, since no other case should exist...*/
			break;
		}
		
	}

	/**
	 * 
	 */
	private void createClientInfoImage() {
		
		String avail = clientInfo.getFileAvailability( fileInfo );
		int length = 0;
		byte[] temp = {};
		Color red = getDisplay().getSystemColor( SWT.COLOR_RED );
		Color blue = getDisplay().getSystemColor( SWT.COLOR_BLUE );
		Color black = getDisplay().getSystemColor( SWT.COLOR_BLACK );
		Color yellow = getDisplay().getSystemColor( SWT.COLOR_YELLOW );		
		
		if ( avail != null ){		
				length = avail.length();
		}
				
		if ( image != null ) image.dispose();
		if (length==0) 		
			image = new Image( getDisplay(), 1, 1 );
		else
			image = new Image( getDisplay(), length, 1 );
			
		GC gc = new GC( image );
		for ( int i = 0; i < length; i++ ) {
			
			gc.setForeground( blue );			
			//this availability is so low, we can assume, it is not available:			
			if ( avail.charAt( i ) == '0' ) {			
				gc.setForeground( red );				
			}
			else if ( avail.charAt( i ) == '1' ) {
				gc.setForeground( blue );				
			}
			else if ( avail.charAt( i ) == '2' ) {
				gc.setForeground( black );				
			}					
			gc.drawLine( i, 0, i, 1  );			
		}	
		gc.dispose();	
		
		
	}

	/**
	 * @param chunks
	 */
	private void createFileInfoImage() {
		String chunks = fileInfo.getChunks();
		String avail = fileInfo.getAvail();
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
	
		int srcWidth = image.getBounds().width;			
		int srcHeight = image.getBounds().height;	
		int destWidth = e.width;
		int destHeight = e.height;	
		
		if ( destWidth < column.getWidth() ) {
			/*we don't have to get the whole source-image, but only the displayed stuff*/
			float showPercent = ( ( float ) destWidth ) / column.getWidth();
			srcWidth = Math.round( srcWidth * showPercent ) ;			
		}
			
		if ( destWidth >= 2 ) {
			destWidth = e.width - 2;
		}
		if ( e.height >= 2 ) {
			destHeight = e.height - 2;
		}
		if ( image != null ) {
			gc.drawImage(
				image,
				0,
				0,
				srcWidth,
				srcHeight,
				1,
				1,
				destWidth,
				destHeight );
		}
		gc.dispose();
	}



	/**
	 * redraws this widget, with refreshed Information from FileInfo (if changed)
	 */
	public void refresh() {		
		createFileInfoImage();
		
		
	}
}

/*
$Log: ChunkView.java,v $
Revision 1.4  2003/07/14 19:26:40  dek
done some clean.up work, since it seems,as if this view becomes reality..

Revision 1.3  2003/07/13 20:28:51  dek
now, everytime a new chunkview is created, but now everything displayed is recent information

Revision 1.2  2003/07/13 20:12:39  dek
fixed Exception and applied checkstyle

Revision 1.1  2003/07/13 12:48:28  dek
chunk-bar begins to work

*/