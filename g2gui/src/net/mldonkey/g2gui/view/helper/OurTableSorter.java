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

import net.mldonkey.g2gui.view.pref.PreferenceLoader;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

/**
 * OurTableViewerSorter
 *
 * @version $Id: OurTableSorter.java,v 1.1 2003/10/21 17:00:45 lemmster Exp $ 
 *
 */
public abstract class OurTableSorter extends ViewerSorter implements DisposeListener {
	final PreferenceStore prefStore = PreferenceLoader.getPreferenceStore();

	/* the last column we sorted */
	private int lastColumnIndex = -1;
	/* the column we should sort */
	protected int columnIndex;
	/* the sort order (ascending/descending) */
	protected boolean lastSort;
	/* the name we read to the prefstore */
	protected String sorterName;

	/**
	 * Sets the default values for the prefstore and reads the current values
	 */
	public OurTableSorter( String aSorterName, int aColumnIndex ) {
		this.lastSort = true;
		this.sorterName = aSorterName;
	
		prefStore.setDefault( sorterName + "Column", aColumnIndex );
		prefStore.setDefault( sorterName + "Order", lastSort );
		
		this.columnIndex = prefStore.getInt( sorterName +  "Column" );
		this.lastSort = prefStore.getBoolean( sorterName + "Order" );
	}

	/**
	 * Returns a negative, zero, or positive number depending on whether
	 * the first element is less than, equal to, or greater than
	 * the second element.
	 * <p>
	 * The default implementation of this method is based on
	 * comparing the elements' categories as computed by the <code>category</code>
	 * framework method. Elements within the same category are further
	 * subjected to a case insensitive compare of their label strings, either
	 * as computed by the content viewer's label provider, or their
	 * <code>toString</code> values in other cases. Subclasses may override.
	 * </p>
	 *
	 * @param viewer the viewer
	 * @param obj1 the first element
	 * @param obj2 the second element
	 * @return a negative number if the first element is less  than the
	 *  second element; the value <code>0</code> if the first element is
	 *  equal to the second element; and a positive number if the first
	 *  element is greater than the second element
	 */
	public abstract int compare( Viewer viewer, Object obj1, Object obj2 );

	/**
	 * DOCUMENT ME!
	 * 
	 * @param aString1 DOCUMENT ME!
	 * @param aString2 DOCUMENT ME!
	 * @return DOCUMENT ME!
	 */
	public int compareStrings( String aString1, String aString2 ) {
		if ( aString1.equals( "" ) )
			return 1;
		if ( aString2.equals( "" ) )
			return -1;
		return
			( lastSort ? aString1.compareToIgnoreCase( aString2 )
					   : aString2.compareToIgnoreCase( aString1 ) );
	}

	/**
	 * Sets the column index
	 * @param i The column index to sort
	 */
	public void setColumnIndex( int i ) {
		columnIndex = i;
		if ( columnIndex == lastColumnIndex )
			lastSort = !lastSort;
		else {
			if ( i == 2 )
				lastSort = false;
			else
				lastSort = true;
		}
		lastColumnIndex = columnIndex;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.DisposeListener#
	 * widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed( DisposeEvent e ) {
		prefStore.setValue( sorterName + "Column", columnIndex );
		prefStore.setValue( sorterName + "Order", lastSort );
	}
}

/*
$Log: OurTableSorter.java,v $
Revision 1.1  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

*/