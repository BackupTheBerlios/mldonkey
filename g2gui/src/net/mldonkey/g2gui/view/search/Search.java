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

import java.util.ArrayList;
import java.util.List;

import gnu.trove.TIntObjectIterator;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.NetworkInfoIntMap;
import net.mldonkey.g2gui.view.SearchTab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

/**
 * Search
 *
 * @author $user$
 * @version $Id: Search.java,v 1.1 2003/07/23 16:56:28 lemmstercvs01 Exp $ 
 *
 */
public abstract class Search {
	protected CoreCommunication core;
	protected SearchTab tab;
	private GridData gridData;
	private Label label;
	protected Text text;
	protected Combo combo;

	/**
	 * 
	 * @param core
	 */
	public Search( CoreCommunication core, SearchTab tab ) {
		this.core = core;
		this.tab = tab;
	}
	
	/**
	 * The string to display as the Tabname
	 * @return The string name
	 */
	public abstract String getTabName();

	/**
	 * @param tabFolder
	 * @return
	 */
	public abstract Control createTabFolderPage( TabFolder tabFolder );
	
	protected void createInputBox( Group group, String aString ) {
		/* describe the box */
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		label = new Label( group, SWT.NONE );
		label.setLayoutData( gridData );
		label.setText( aString );
				
		/* the box */
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		text = new Text( group, SWT.SINGLE | SWT.BORDER );
		text.setLayoutData( gridData );
	}
	
	protected void createNetworkBox( Group group, String aString ) {
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		label = new Label( group, SWT.NONE );
		label.setLayoutData( gridData );
		label.setText( aString );
				
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		combo = new Combo( group, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
		combo.setLayoutData( gridData );
	
		/* get all activated networks and display them in the combo */
		NetworkInfoIntMap temp = this.core.getNetworkInfoMap();
		TIntObjectIterator itr = temp.iterator();
		List items = new ArrayList();
		for ( int i = 0; i < temp.size(); i++ ) {
			itr.advance();
			NetworkInfo elem = ( NetworkInfo ) itr.value();
			if ( elem.isEnabled() )
				items.add( elem.getNetworkName() );
		}
		if ( items.size() > 1 )
			items.add( "All" );
	
		Object[] itemsArray = items.toArray();
		String[] strings = new String[ itemsArray.length ];
		for ( int i = 0; i < itemsArray.length; i++ ) {
			strings[ i ] = (String) itemsArray[ i ];
		}
		combo.setItems( strings );
		combo.setText( strings[ 0 ] );
	}
}

/*
$Log: Search.java,v $
Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/