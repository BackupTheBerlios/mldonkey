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
package net.mldonkey.g2gui.view.news;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import churchillobjects.rss4j.RssChannel;
import churchillobjects.rss4j.RssDocument;

/**
 * NewsFeed
 *
 * @version $Id: NewsFeed.java,v 1.2 2003/09/29 14:05:45 lemmster Exp $
 *
 */
public class NewsFeed extends News {
    private Map aMap;
    private RssDocument doc;
	private String title;

    public NewsFeed( Control aControl, RssDocument aDoc, String aString ) {
        super( aControl );
        this.doc = aDoc;
        this.title = aString;
        this.create();
    }

    /**
     * DOCUMENT ME!
     */
    private void create() {
		GridLayout gridLayout = new GridLayout();
//		gridLayout.numColumns = 3;
//		gridLayout.makeColumnsEqualWidth = true;
		
		Composite composite = new Composite( ( Composite ) control, SWT.NONE );
		composite.setLayout( gridLayout );

        CTabItem cTabItem = new CTabItem( ( CTabFolder ) control, SWT.NONE );
        cTabItem.setControl( composite );
        cTabItem.setText( title );

        Enumeration enum = doc.channels();
        while ( enum.hasMoreElements() ) {
            NewsChan aNewsChan = new NewsChan( composite, ( RssChannel ) enum.nextElement() );
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void dispose() {
        Iterator itr = this.aMap.values().iterator();
        while ( itr.hasNext() )
            ( ( NewsChan ) itr.next() ).dispose();
    }
}

/*
$Log: NewsFeed.java,v $
Revision 1.2  2003/09/29 14:05:45  lemmster
update & add still not working

Revision 1.1  2003/09/27 12:09:32  lemmster
initial commit

*/
