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
package net.mldonkey.g2gui.view.search;

import net.mldonkey.g2gui.model.ResultInfo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * ToolTip
 *
 * @version $Id: ToolTip.java,v 1.4 2003/12/04 08:47:29 lemmy Exp $ 
 *
 */
public abstract class ToolTip {
	protected Shell shell;
	protected CLabel title;
	protected Control parent, altNames;
	
	protected static Color foreGround;
	protected static Color backGround;
	
	/**
	 * 
	 * @param aParent
	 */
	public ToolTip( Composite aParent ) {
		parent = aParent;
		backGround = parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
		foreGround = parent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND);
	}

	/**
	 * Creates the tooltip on demand
	 * @param e
	 */
	public abstract void create( Event e );
	
	/**
	 * Dispose the tooltip on demand
	 */
	public void dispose() {
		// dispose the shell and all overlaying widgets
		if ( this.shell != null && !this.shell.isDisposed() ) {
			this.shell.dispose();
		}
	}
	
	/**
	 * Returns <code>true</code> if the receiver is visible and all
	 * of the receiver's ancestors are visible and <code>false</code>
	 * otherwise.
	 *
	 * @return the receiver's visibility state
	 */
	public boolean isVisisble() {
		if ( shell != null && !shell.isDisposed() )
			return this.shell.isVisible();
		else
			return false;
	}

	
	/**
	 * Returns <code>true</code> if the given point is inside the
	 * area specified by the receiver, and <code>false</code>
	 * otherwise.
	 *
	 * @param pt the point to test for containment
	 * @return <code>true</code> if the rectangle contains the point and <code>false</code> otherwise
	 */
	public boolean contains( int x, int y ) {
		if ( shell != null && !shell.isDisposed() )
			return this.shell.getBounds().contains( parent.toDisplay( x, y ) );
		else
			return false;
	}
	
	/**
	 * 
	 * @param e
	 * @return
	 */
	protected ResultInfo getResult( Event e ) {
		if ( e.widget == null ) return null;
		Table table = (Table) e.widget;
		if ( e.x == 0 && e.y == 0 ) return null;
		Item anItem = table.getItem( new Point( e.x, e.y ) );
		if ( anItem == null ) return null;
		ResultInfo result = (ResultInfo) anItem.getData();
		return result;
	}
	
	/**
	 * 
	 * @param aComposite
	 * @return
	 */
	protected CLabel createCLabel( Composite aComposite ) {
		CLabel aCLabel = new CLabel(aComposite, SWT.NONE);
		FontData[] fontDataArray = aCLabel.getFont().getFontData();
		for (int i = 0; i < fontDataArray.length; i++)
			fontDataArray[ i ].setStyle(SWT.BOLD);
		Font boldFont = new Font(null, fontDataArray);
		aCLabel.setFont(boldFont);
		aCLabel.setAlignment(SWT.LEFT);
		aCLabel.setForeground(foreGround);
		aCLabel.setBackground(backGround);
		aCLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
				GridData.HORIZONTAL_ALIGN_BEGINNING));
		return aCLabel;
	}
	
	/**
	 * 
	 * @param aComposite
	 * @return
	 */
	protected Control createSeparator( Composite aComposite ) {
		Label separator = new Label(aComposite, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return separator;
	}

	/**
	 * Sets the shells size and location
	 * @param e
	 */
	protected void setupShell( Event e ) {
		shell.pack();

		// calculate the location of the shell
		Point pt = parent.toDisplay( e.x, e.y );
		Rectangle displayBounds = shell.getDisplay().getBounds();
		Rectangle shellBounds = shell.getBounds();
		int x = Math.max( Math.min( pt.x, displayBounds.width - shellBounds.width ), 0 );
		int y = Math.max( Math.min( pt.y, displayBounds.height - shellBounds.height ), 0 );
		shell.setBounds( new Rectangle( x, y + 1, shellBounds.width, shellBounds.height ) );
		shell.open();
	}
}

/*
$Log: ToolTip.java,v $
Revision 1.4  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.3  2003/12/01 08:45:37  lemmy
cosmetic

Revision 1.2  2003/11/30 09:31:26  lemmy
ToolTip complete reworked (complete)

Revision 1.1  2003/11/29 13:03:54  lemmy
ToolTip complete reworked (to be continued)

*/