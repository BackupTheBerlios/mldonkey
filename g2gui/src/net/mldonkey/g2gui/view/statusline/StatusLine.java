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
package net.mldonkey.g2gui.view.statusline;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Statusline, This class handles all the Information that should be visible all the time in a so called Status-line.
 * It is an own implementation of this widget, as the JFace-Statusline is way too complicated for our simple needs.
 * It has to be placed in a GridLayout, since it applies a GridData object for its appearance.
 *
 * @author $user$
 * @version $Id: StatusLine.java,v 1.2 2003/06/26 14:11:58 dek Exp $ 
 *
 */
public class StatusLine {
	private Composite statusline;
	private ArrayList fields;

	/**
	 * @param mainComposite the Composite where this status-line finds its place fpr living
	 */
	public StatusLine(Composite parent) {
		fields = new ArrayList();		
		this.statusline = new Composite(parent,SWT.NONE);			
		this.statusline.setLayout(new FillLayout());
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );	
			statusline.setLayoutData( gridData );		
			
	addField(new SimpleStatusLineItem(" Connection-Status",SWT.NONE));
	addField(new SimpleStatusLineItem(" Statistics",SWT.CENTER));
	addField(new SimpleStatusLineItem(" other Information",SWT.NONE));
	addField(new SimpleStatusLineItem(".....",SWT.NONE));
	}

	/**
	 * @param item the StatusBarItem which should be added to our nice little statusbar
	 */
	public void addField(StatusLineItem item) {
		Label newField = new Label(statusline,SWT.READ_ONLY|SWT.SINGLE|SWT.BORDER);	
		newField.setText(item.getContent());
		newField.setAlignment(item.getAlignment());
		this.fields.add(newField);
	}
	
	
	public StatusLineItem[] getItems(){		
		return (StatusLineItem[])fields.toArray();
	}

}

/*
$Log: StatusLine.java,v $
Revision 1.2  2003/06/26 14:11:58  dek
NPE fixed ;-)

Revision 1.1  2003/06/26 14:04:59  dek
Class statusline for easy information display

*/