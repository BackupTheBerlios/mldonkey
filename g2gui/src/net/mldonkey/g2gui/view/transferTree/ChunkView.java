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
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * ChunkView
 *
 * @author $user$
 * @version $Id: ChunkView.java,v 1.11 2003/07/29 04:10:55 zet Exp $ 
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

	/** to make transition from emule easier 
	 * http://www.emule-project.net/faq/progress.htm
	 */
	private void createClientInfoImage() {
		this.avail =  clientInfo.getFileAvailability( fileInfo );	
		this.chunks = fileInfo.getChunks();	
		int length = 0;
		
		Color red = getDisplay().getSystemColor( SWT.COLOR_RED );
		Color blue = new Color (null, 0, 150, 255);
		Color black = getDisplay().getSystemColor( SWT.COLOR_BLACK );
		Color yellow = getDisplay().getSystemColor( SWT.COLOR_YELLOW );		
		Color silver = new Color (null, 226, 225, 221);
		
		Color fromColor = black;
		Color toColor;
		
		if ( avail != null ) {		
				length = avail.length();
		}				
		if ( image != null ) image.dispose();
		
		if ( length == 0 ) 		
			image = new Image( getDisplay(), 1, 18 );
		else
			image = new Image( getDisplay(), length, 18 );
			
		GC gc = new GC( image );
					
		for ( int i = 0; i < length; i++ ) {
			toColor = blue;
			//this availability is so low, we can assume, it is not available:			
			if ( avail.charAt( i ) == '0' ) {			
				toColor = silver;				
			}
			else if ( avail.charAt( i ) == '1' ) {
				toColor = blue;				
			}
			else if ( avail.charAt( i ) == '2' ) {
				toColor = yellow;				
			}	
			
			if ( chunks.charAt( i ) == '2' ) {
				toColor = new Color (null, 107, 81, 9);		
			}
			gc.setBackground (toColor);
			gc.setForeground (fromColor);
			gc.fillGradientRectangle(i, 0, 1, 9, true);
			
			gc.setForeground(toColor);
			gc.setBackground(fromColor);
			gc.fillGradientRectangle(i, 9, 1, 9, true);				
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
		Color black = getDisplay().getSystemColor( SWT.COLOR_BLACK );
		Color yellow = getDisplay().getSystemColor( SWT.COLOR_YELLOW );	
		
		Color fromColor = black;
		Color toColor;
			
		if ( avail.length() != 0 ) 
				length = avail.length();
				
		int numChunkSources;	
		int highestNumSources = 0;
		float factor = 1f;
	
		for (int i = 0; i < avail.length(); i++) 
		{
			numChunkSources = avail.charAt( i );
			if (numChunkSources > highestNumSources)
				highestNumSources = numChunkSources;
			
		}
		if (highestNumSources > 0) 
			factor = 10f / highestNumSources; 
	
		if ( image != null ) image.dispose();
		image = new Image( getDisplay(), length, 18 );
		GC gc = new GC( image );
		
		for ( int i = 0; i < avail.length(); i++ ) {
					
			numChunkSources = avail.charAt( i ) ;
			
			int colorIntensity = 255 - ( int ) ((float) numChunkSources * factor) * 25;
			toColor = new Color(null, 0, colorIntensity, 255);	
				
			if ( numChunkSources == 0 )		
				toColor = red;
				
			if ( chunks.charAt( i ) == '2' ) 			
				toColor = new Color (null, 107, 81, 9) ;
			else if ( chunks.charAt( i ) == '3' ) 		
				toColor = yellow;
						
			gc.setBackground (toColor);
			gc.setForeground (fromColor);
			gc.fillGradientRectangle(i, 0, 1, 9, true);
			
			gc.setForeground(toColor);
			gc.setBackground(fromColor);
			gc.fillGradientRectangle(i, 9, 1, 9, true);	
			
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
			
		if ( image != null ) {
			
			ImageData imageData = image.getImageData();
			imageData = imageData.scaledTo(getBounds().width, getBounds().height);
			Image newImage = new Image(null, imageData);
			
			GC t = new GC( newImage );
				
			int ht = newImage.getBounds().height - 1;
			int wt = newImage.getBounds().width-1 ;
			// progress bar				
			if ( type == isFileInfo ) {
				int pix =  ( int ) ( ( fileInfo.getPerc() / 100 ) * ( double ) wt ) ;
				t.setBackground( new Color(null, 15, 136, 0 ));
				t.setForeground( new Color(null, 41, 187, 26));
				t.fillGradientRectangle(0,0,pix,4,false);
			}
			// spacer in background colour	
			t.setForeground(getParent().getBackground());
			t.drawLine(0,0,newImage.getBounds().width, 0);
			t.drawLine(0,newImage.getBounds().height-1, newImage.getBounds().width, newImage.getBounds().height-1);
			
			// round the corners
			t.drawLine(0,1,0,1);
			t.drawLine(0,ht-1,0,ht-1);
			t.drawLine(wt,1,wt,1);
			t.drawLine(wt,ht-1,wt,ht-1);	
	
			gc.drawImage(
				newImage,
				e.x,
				e.y,
				e.width,
				e.height,
				e.x,
				e.y,
				e.width,
				e.height );
			
			t.dispose();
			newImage.dispose();
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
		if ( this.hasChanged() ) {		
			createImage();
			this.redraw();
		}
		
	}
}

/*
$Log: ChunkView.java,v $
Revision 1.11  2003/07/29 04:10:55  zet
chunks - half done - commit before I lose it again..

Revision 1.10  2003/07/21 17:38:50  dek
checkstyle

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