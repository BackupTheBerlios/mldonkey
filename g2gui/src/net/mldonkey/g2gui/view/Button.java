/*
 * Created on 01.07.2003
 *
  */
package net.mldonkey.g2gui.view;

import java.util.List;
import java.util.Vector;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * @author mitch
 *
*/
public class Button extends Canvas implements MouseListener,MouseTrackListener, PaintListener{

	List selectionListeners;
	Image image;
	String text;

	/**
	 * @param arg0
	 * @param arg1
	 */
	public Button(Composite arg0, int arg1) {
		super(arg0, arg1);
		selectionListeners = new Vector();
		addPaintListener(this);
		addMouseListener(this);
		addMouseTrackListener(this);
		// TODO Auto-generated constructor stub
	}
	
	public void addSelectionListener(SelectionListener listener) {
		selectionListeners.add(listener);
	}
	public void removeSelectionListener(SelectionListener listener) {
		selectionListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Event ev = new Event();
		ev.widget = this;
		for(int i=0;i<selectionListeners.size();i++) {
			SelectionListener l = (SelectionListener)selectionListeners.get(i);
			l.widgetSelected(new SelectionEvent(ev));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseEnter(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseExit(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseHover(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */
	public void paintControl(PaintEvent arg0) {
		GC gc = arg0.gc;
		int x = (int)(getBounds().width/2-image.getBounds().width/2);
		int y = (int)(getBounds().height/2-image.getBounds().height/2);
		gc.drawImage(image,x,y);		
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @return
	 */
	public String getText() {
		return text;
	}

}
