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
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import gnu.trove.TIntObjectIterator;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.NetworkInfoIntMap;
import net.mldonkey.g2gui.model.SearchQuery;
import net.mldonkey.g2gui.view.SearchTab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

/**
 * Search
 *
 * @author $user$
 * @version $Id: Search.java,v 1.7 2003/07/30 19:32:02 lemmstercvs01 Exp $ 
 *
 */
public abstract class Search {
	protected CoreCommunication core;
	protected SearchTab tab;
	protected SearchQuery query;
	
	private GridData gridData;
	private Label label;
	protected Text text;
	protected Combo combo;
	
	protected ResourceBundle bundle = ResourceBundle.getBundle( "g2gui" );

	/**
	 * 
	 * @param core The parent corecommunication
	 * @param tab The parent searchtab
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
	 * @param tabFolder The tabfolder to create the control in
	 * @return a Control filled with the content of this obj
	 */
	public abstract Control createTabFolderPage( TabFolder tabFolder );

	/**
	 * create a searchquery, fill it and send it to mldonkey
	 */
	public abstract void performSearch();
	
	/**
	 * Creates a blank input field for search strings
	 * @param group The Group to display the box in
	 * @param aString The Box header
	 */
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
		text.addKeyListener( new KeyAdapter() {
			public void keyPressed( KeyEvent e ) {
				if ( e.character == SWT.CR ) {
					performSearch(); 
				}
			}		
		} );	
	}
	
	/**
	 * Creates a DropDown Box with all activated Networks inside 
	 * (SWT.READ_ONLY)
	 * @param group The Group to display the box in
	 * @param aString The Box header
	 */
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
			/* exclude disabled and networks without servers */
			if ( elem.isEnabled() && elem.hasServers() )
				items.add( elem );
		}
		/* if more than 1 network is enabled, we need a "All" field */
		int i = 0;
		String[] strings;
		Object[] data;
		if ( items.size() > 1 ) {
			strings = new String[ items.size() + 1 ];
			data = new Object[ items.size() + 1 ];
			strings[ i ] = bundle.getString( "S_ALL" );
			data[ i++ ] = null;
		}
		else {
			strings = new String[ items.size() ];
			data = new Object[ items.size() ];
		}
	
		Iterator itr2 = items.iterator();
		while ( itr2.hasNext() ) {
			NetworkInfo elem = ( NetworkInfo ) itr2.next();
			strings[ i ] = elem.getNetworkName();
			data[ i++ ] = elem.getNetworkType();
		}
		
		combo.setItems( strings );
		combo.setData( data );
		
		/* not get a default selection */
		if ( strings.length > 0 )
			combo.setText( strings[ 0 ] );
	}
}

/*
$Log: Search.java,v $
Revision 1.7  2003/07/30 19:32:02  lemmstercvs01
using hasServers() instead of direct compare

Revision 1.6  2003/07/28 08:19:20  lemmstercvs01
get NetworkInfo by Enum instead of NetworkName

Revision 1.5  2003/07/27 18:45:47  lemmstercvs01
lots of changes

Revision 1.4  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.3  2003/07/24 02:22:46  zet
doesn't crash if no core is running

Revision 1.2  2003/07/23 19:49:17  zet
press enter

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/