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
 * @version $Id: ChunkView.java,v 1.9 2003/07/20 11:47:04 dek Exp $ 
 *
 */
public class ChunkView extends Canvas {


	private String avail;

	private String chunks;

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
	 * creates a chunkview-Object for the given clientInfo
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
		this.avail =  clientInfo.getFileAvailability( fileInfo );		
		int length = 0;
		byte[] temp = {};
		Color red = getDisplay().getSystemColor( SWT.COLOR_RED );
		Color blue = getDisplay().getSystemColor( SWT.COLOR_BLUE );
		Color black = getDisplay().getSystemColor( SWT.COLOR_BLACK );
		Color yellow = getDisplay().getSystemColor( SWT.COLOR_YELLOW );		
		
		if ( avail != null ) {		
				length = avail.length();
		}				
		if ( image != null ) image.dispose();
		
		if ( length == 0 ) 		
			image = new Image( getDisplay(), 1, 1 );
		else
			image = new Image( getDisplay(), length, 1 );
			
		GC gc = new GC( image );
		
		gc.setBackground( getParent().getBackground( ) );		
		gc.fillRectangle( 0, 0, image.getBounds().width, image.getBounds().height );		
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
		this.chunks = fileInfo.getChunks();
		this.avail = fileInfo.getAvail();
		int length = 0;
		
		Color red = getDisplay().getSystemColor( SWT.COLOR_RED );
		Color blue = getDisplay().getSystemColor( SWT.COLOR_BLUE );
		Color black = getDisplay().getSystemColor( SWT.COLOR_BLACK );
		Color yellow = getDisplay().getSystemColor( SWT.COLOR_YELLOW );		
		
		if ( avail.length() != 0 ) 
				length = avail.length();
				
		byte[] temp = avail.getBytes();		
		
		if ( image != null ) image.dispose();
		image = new Image( getDisplay(), length, 12 );
		GC gc = new GC( image );
		for ( int i = 0; i < avail.length(); i++ ) {		
			int height = temp[ i ] / 10	;			
			if ( height < 1 ) height = 1;
			gc.setForeground( blue );	
			
			//this availability is so low, we can assume, it is not available:			
			if ( temp[ i ] == 0 ) {			
				gc.setForeground( red );
				height = 12;
			}
			
			if ( chunks.charAt( i ) == '2' ) {			
				gc.setForeground( black );
				height = 12;
			}
			if ( chunks.charAt( i ) == '3' ) {			
				gc.setForeground( yellow );
				height = 12;
			}
									
			gc.drawLine( i, 12, i, 12 - height );			
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
			gc.setBackground( getParent().getBackground( ) );		
			gc.fillRectangle( 0, 0, e.width, e.height );
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


	private boolean hasChanged() {
		boolean result = false;
		
		if ( type == isFileInfo ) {
			boolean part1 = !( chunks.hashCode() == fileInfo.getChunks().hashCode() );
			boolean part2 = !( avail.hashCode() == fileInfo.getAvail().hashCode() );
			result = part1 || part2;
		
		}		
		else if ( type == isClientInfo ) {
			String tempAvail = clientInfo.getFileAvailability( fileInfo );
			if ( avail != null  )				
				result = !( tempAvail.hashCode() == avail.hashCode() );
		}		
		return result;		
	}


	/**
	 * redraws this widget, with refreshed Information from FileInfo (if changed)
	 */
	public void refresh() {	
		if ( this.hasChanged() )
			createImage();
		
	}
}

/*
$Log: ChunkView.java,v $
Revision 1.9  2003/07/20 11:47:04  dek
foobar

Revision 1.8  2003/07/18 15:45:16  dek
still working on flicker...

Revision 1.7  2003/07/15 20:13:56  dek
sorting works now, chunk-display is kind of broken, when sorting with expanded tree-items...

Revision 1.6  2003/07/15 18:14:47  dek
Wow, nice piece of work already done, it works, looks nice, but still lots of things to do

Revision 1.5  2003/07/15 13:25:41  dek
right-mouse menu and some action to hopefully avoid flickering table

Revision 1.4  2003/07/14 19:26:40  dek
done some clean.up work, since it seems,as if this view becomes reality..

Revision 1.3  2003/07/13 20:28:51  dek
now, everytime a new chunkview is created, but now everything displayed is recent information

Revision 1.2  2003/07/13 20:12:39  dek
fixed Exception and applied checkstyle

Revision 1.1  2003/07/13 12:48:28  dek
chunk-bar begins to work

*/