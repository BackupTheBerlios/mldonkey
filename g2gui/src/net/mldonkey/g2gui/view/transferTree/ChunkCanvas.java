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

import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.view.MainTab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * ChunkView
 *
 * @author $user$
 * @version $Id: ChunkCanvas.java,v 1.1 2003/08/04 19:22:08 zet Exp $ 
 *
 */
public class ChunkCanvas extends Canvas implements Observer {

	private String avail;

	private String chunks;

	private ClientInfo clientInfo;

	private TableColumn column;

	private FileInfo fileInfo;

	private Image image;
	private ImageData imageData;
	private ImageData resizedImageData;
	
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
	
	
	private final int initialHeight = 18;
	private int minWidth = 1;
	
	/**
	 * creates a chunkview-Object for the given clientInfo
	 * @param parent here does the object live
	 * @param style this style do we prefer (not used atm)
	 * @param clientInfo the source of this chunkviews information
	 * @param fileInfo for this fileInfo we want to display the Information
	 * @param column the column in which the widget appears
	 */
	public ChunkCanvas( Table parent, int style, ClientInfo clientInfo, FileInfo fileInfo, int column ) {			
		super( parent, style );
		this.column = parent.getColumn( column );
		
		this.clientInfo = clientInfo;
		this.fileInfo = fileInfo;
		this.type = (clientInfo == null ? isFileInfo : isClientInfo);
		
		createImage();		
	
		addDisposeListener( new DisposeListener() {
			public void widgetDisposed( DisposeEvent e ) {		
				ChunkCanvas.this.widgetDisposed( e );		
			}
		} );
		addPaintListener( new PaintListener() {
			public void paintControl( PaintEvent e ) {
				ChunkCanvas.this.paintControl( e );
			}
		} );
		addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {
				ChunkCanvas.this.resizeImage(e);
			} 
			public void controlMoved(ControlEvent e) {
			}
		} );
	
	}
	/**
	 * creates a chunkview-Object for the given FileInfo
	 * @param parent here does the object live
	 * @param style this style do we prefer (not used atm)
	 * @param fileInfo  and the source of all the information
	 * @param column the column in which the widget appears
	 */
	public ChunkCanvas( Table parent, int style, final FileInfo fileInfo, int column ) {	
		this(parent, style, null, fileInfo, column);
	}

	private void createImage() {
		switch ( type ) {
			case isClientInfo :	
				createClientInfoImage();	
				return;
				
			case isFileInfo :
				createFileInfoImage();				
				return;
				
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
		
		if ( avail != null ) 
			length = avail.length();
		
		if (length == 0) return;
			
		Display thisDisplay = MainTab.getShell().getDisplay();
		
		Color red = thisDisplay.getSystemColor( SWT.COLOR_RED );
		Color black = thisDisplay.getSystemColor( SWT.COLOR_BLACK );
		Color yellow = thisDisplay.getSystemColor( SWT.COLOR_YELLOW );	
		Color blue = new Color (null, 0, 150, 255);
		Color silver = new Color (null, 226, 225, 221);
		Color darkGray = new Color (null, 107, 81, 9);
		
		Color fromColor = black;
		Color toColor;
						
		if ( image != null ) image.dispose();
		
		if ( length > minWidth ) minWidth = length;
		image = new Image( thisDisplay, minWidth, initialHeight );
		
		GC imageGC = new GC( image );
					
		for ( int i = 0; i < length; i++ ) {
			toColor = blue;
			
			// we have it
			if ( chunks.charAt( i ) == '2' ) {
				toColor = darkGray;	
			} // doesn't have it
			else if ( avail.charAt( i ) == '0' ) {			
				toColor = silver;				
			} // they have it
			else if ( avail.charAt( i ) == '1' ) {
				toColor = blue;				
			} // ???
			else if ( avail.charAt( i ) == '2' ) {
				toColor = yellow;				
			}	
			
			imageGC.setBackground (toColor);
			imageGC.setForeground (fromColor);
			imageGC.fillGradientRectangle(i, 0, 1, initialHeight / 2, true);
			
			imageGC.setForeground(toColor);
			imageGC.setBackground(fromColor);
			imageGC.fillGradientRectangle(i, initialHeight / 2, 1, initialHeight / 2, true);				
		}	
		
		imageGC.dispose();	
		blue.dispose();
		silver.dispose();
		darkGray.dispose();
		
		imageData = image.getImageData();

	}

	/**
	 * @param chunks
	 */
	private void createFileInfoImage() {
	
		
		this.chunks = fileInfo.getChunks();
		this.avail = fileInfo.getAvail();
		int length = 0;
		
		Display thisDisplay = MainTab.getShell().getDisplay();
		
		Color red = thisDisplay.getSystemColor( SWT.COLOR_RED );
		Color black = thisDisplay.getSystemColor( SWT.COLOR_BLACK );
		Color yellow = thisDisplay.getSystemColor( SWT.COLOR_YELLOW );	
		Color darkGray = new Color (null, 107, 81, 9) ;
		
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

		if ( length > minWidth ) minWidth = length;
		image = new Image( thisDisplay, minWidth, initialHeight );
		
		GC imageGC = new GC( image );
		
		for ( int i = 0; i < avail.length(); i++ ) {
					
			numChunkSources = avail.charAt( i ) ;
			
			int colorIntensity = 255 - ( int ) ((float) numChunkSources * factor) * 25;
			toColor = new Color(null, 0, colorIntensity, 255);	
				
			if ( numChunkSources == 0 )		
				toColor = red;
				
			if ( chunks.charAt( i ) == '2' ) 			
				toColor = darkGray;
			else if ( chunks.charAt( i ) == '3' ) 		
				toColor = yellow;
						
			imageGC.setBackground (toColor);
			imageGC.setForeground (fromColor);
			imageGC.fillGradientRectangle(i, 0, 1, initialHeight / 2, true);
			
			imageGC.setForeground(toColor);
			imageGC.setBackground(fromColor);
			imageGC.fillGradientRectangle(i, initialHeight / 2, 1, initialHeight / 2, true);	
			
		}	
		darkGray.dispose();
		imageGC.dispose();	
		
		imageData = image.getImageData();

	}

	/**
	 * @param e
	 */
	protected void widgetDisposed( DisposeEvent e ) {
	
		if (image != null && !image.isDisposed()) image.dispose();
		if (type==isFileInfo) {
			fileInfo.deleteObserver(this);
		} else { 
			clientInfo.deleteObserver(this);
		}
	}
	
	
	protected void resizeImage( ControlEvent e ) {
		if ( image != null && imageData != null ) {
			if (getClientArea().width > minWidth && getClientArea().height > 0) {
				resizedImageData = imageData.scaledTo(
					getClientArea().width, getClientArea().height);
			}
		}
	}
	/**
	 * @param e
	 */
	protected void paintControl( PaintEvent e ) {

		GC canvasGC = e.gc;
				
		if ( image != null ) {
			
			int srcWidth = resizedImageData.width;			
			int srcHeight = resizedImageData.height;	
			int destWidth = e.width;
			int destHeight = e.height;			
			
			Image bufferImage = new Image(null, resizedImageData);			
						
			GC bufferGC = new GC( bufferImage );
				
			int ht = srcHeight - 1;
			int wt = srcWidth - 1;
			// progress bar				
			if ( type == isFileInfo ) {
				int pix =  ( int ) ( ( fileInfo.getPerc() / 100 ) * ( double ) wt ) ;
				bufferGC.setBackground( new Color(null, 15, 136, 0 ));
				bufferGC.setForeground( new Color(null, 41, 187, 26));
				bufferGC.fillGradientRectangle(0,0,pix,4,false);
			}
			// spacer in background colour	
			bufferGC.setForeground(getParent().getBackground());
			bufferGC.drawLine(0,0,srcWidth-1, 0);
				
			// round the corners
			bufferGC.drawLine(0,1,0,1);
			bufferGC.drawLine(0,srcHeight-1,0,srcHeight-1);
			bufferGC.drawLine(srcWidth-1,1,srcWidth-1,1);
			bufferGC.drawLine(srcWidth-1,srcHeight-1,srcWidth-1,srcHeight-1);	
			bufferGC.dispose();
			
			canvasGC.drawImage(
				bufferImage,
				e.x,
				e.y,
				e.width,
				e.height,
				e.x,
				e.y,
				e.width,
				e.height );
			
			bufferGC.dispose();

		} else { 
			
			canvasGC.setBackground(getParent().getBackground());
			canvasGC.fillRectangle(e.x, e.y, e.width, e.height);
					
		}
		
		canvasGC.dispose();
	}


	private boolean hasChanged() {
		boolean result = false;
		
		if ( type == isFileInfo ) {
			boolean part1 = chunks.hashCode() != fileInfo.getChunks().hashCode();
			boolean part2 = avail.hashCode() != fileInfo.getAvail().hashCode();
			result = part1 || part2;
		
		}		
		else if ( type == isClientInfo ) {
			String tempAvail = clientInfo.getFileAvailability( fileInfo );
			if ( avail == null && tempAvail != null) 
				result = true;
			else if ( avail != null && tempAvail != null )				
				 result = tempAvail.hashCode() != avail.hashCode();
				
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

	// runs in gui thread	
	public void update(Observable o, Object obj) {
		final Shell shell = MainTab.getShell();
		if (!shell.isDisposed()
			&& shell != null
			&& shell.getDisplay() != null) {
			shell.getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (!shell.isDisposed()) {
						if (!isDisposed())
							refresh();
					}
				}
			});
		}
	}
	
	
	
}

/*
$Log: ChunkCanvas.java,v $
Revision 1.1  2003/08/04 19:22:08  zet
trial tabletreeviewer



*/