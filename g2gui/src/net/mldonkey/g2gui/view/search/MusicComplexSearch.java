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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.SearchTab;

/**
 * MusicComplexSearch
 *
 * @version $Id: MusicComplexSearch.java,v 1.1 2003/09/03 22:15:27 lemmster Exp $ 
 *
 */
public class MusicComplexSearch extends ComplexSearch {
	/**
	 * @param core
	 * @param tab
	 */
	public MusicComplexSearch( CoreCommunication core, SearchTab tab ) {
		super( core, tab );
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.ComplexSearch#getTabName()
	 */
	protected String getName() {
		return "Music";
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.ComplexSearch#createContent(org.eclipse.swt.widgets.Control)
	 */
	protected Control createContent(Control aControl) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		Composite composite = new Composite( ( Composite ) aControl, SWT.NONE );
		composite.setLayout( gridLayout );
		
		this.createInputBox( composite, "Title" );
		this.createInputBox( composite, "Artist" );
		this.createInputBox( composite, "Album" );
		
		String[] items = { "mp3", "ogg", "wav", "midi" };
		this.createExtensionAndResultBox( composite, items );

		this.createMaxMinSizeSlider( composite );

		this.createSearchButton( composite );
  
		return composite;
	}
}

/*
$Log: MusicComplexSearch.java,v $
Revision 1.1  2003/09/03 22:15:27  lemmster
advanced search introduced; not working and far from complete. just to see the design

*/