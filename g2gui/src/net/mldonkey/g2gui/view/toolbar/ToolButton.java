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
package net.mldonkey.g2gui.view.toolbar;

import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Listener;
/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 * 
 * A simple helper class made so that the coolbar can be redrawn
 * 
 */
public class ToolButton {
	
	private ToolItem toolItem;
	private Image bigActiveImage, smallActiveImage;
	private Image bigInactiveImage, smallInactiveImage;
	private String text, toolTipText;
	private ToolBar toolbar;
	private boolean useSmallButtons = false;
	private boolean active = false;
	private int toolItemStyle;
	private int eventType;

	
	private Listener listener;
	
	public ToolButton (ToolBar parent, int style, int index) {
		this.toolbar = parent;
		this.toolItemStyle = style;
		toolItem = new ToolItem(parent, style, index);
	}
	public ToolButton (ToolBar parent, int style) {
		// this(parent, style, ?)
		this.toolbar = parent;
		this.toolItemStyle = style;
		toolItem = new ToolItem(parent, style);
	}
	public void setText(String text) {
		this.text = text;
		toolItem.setText(text);
	}
	public void setToolTipText(String text) {
		this.toolTipText = text;
		toolItem.setToolTipText(text);
	}
	public void setImage(Image image) {
		toolItem.setImage(image);
	}
	public ToolBar getParent() {
		return toolItem.getParent();
	}
	public ToolItem getToolItem() {
		return toolItem;
	}
	public void setSmallActiveImage(Image image) {
		smallActiveImage = image;
	}
	public void setBigActiveImage(Image image) {
		bigActiveImage = image;
	}
	public void setSmallInactiveImage(Image image) {
		smallInactiveImage = image;
	}
	public void setBigInactiveImage(Image image) {
		bigInactiveImage = image;
	}
	public void setActive(boolean toggle) {
		active = toggle;
		resetImage();
	}
	public void useSmallButtons(boolean useSmall) {
		useSmallButtons = useSmall;
	}
	public void resetImage() {
		if (active) setImage(useSmallButtons ? smallActiveImage : bigActiveImage);
			else	setImage(useSmallButtons ? smallInactiveImage : bigInactiveImage);
	}
	public void resetItem(ToolBar newtoolbar) {
		toolItem.dispose();
		toolItem = new ToolItem(newtoolbar, toolItemStyle);
		setText(text);
		setToolTipText(toolTipText);
		addListener(eventType, listener);
		resetImage();
	}
	public void addListener(int eventType, Listener listener) {
		this.listener = listener;
		this.eventType = eventType;
		toolItem.addListener(eventType, listener);
	}
}

/*
$Log: ToolButton.java,v $
Revision 1.1  2003/07/27 22:39:36  zet
small buttons toggle (in popup) for main cool menu



*/