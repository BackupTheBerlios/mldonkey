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

import churchillobjects.rss4j.RssChannel;
import churchillobjects.rss4j.RssDocument;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * NewsFeed
 *
 * @version $Id: NewsFeed.java,v 1.3 2003/10/04 08:49:25 lemmster Exp $
 *
 */
public class NewsFeed extends News {
    private List aList;
    private String title;
    private Composite composite;
    private RssDocument aRssDocument;

    public NewsFeed( Control aControl, RssDocument aDoc, String aString ) {
        super( aControl );
        this.aRssDocument = aDoc;
        this.aList = new ArrayList();
        this.title = aString;
        this.create( aDoc );
    }

    /**
     * DOCUMENT ME!
     */
    private void create( RssDocument aDoc ) {
        composite = new Composite( ( Composite ) control, SWT.NONE );
        composite.setLayout( new GridLayout() );

        CTabItem cTabItem = new CTabItem( ( CTabFolder ) control, SWT.NONE );
        cTabItem.setControl( composite );
        cTabItem.setText( title );

        this.fill( aDoc );
    }

	/**
	 * DOCUMENT ME!
	 */
	private void disposeChildren() {
		Iterator itr = this.aList.iterator();
		while ( itr.hasNext() )
			( ( NewsChan ) itr.next() ).dispose();
		this.aList.clear();	
	}

    /**
     * DOCUMENT ME!
     *
     * @param aDoc DOCUMENT ME!
     */
    private void fill( RssDocument aDoc ) {
        Enumeration enum = aDoc.channels();
        while ( enum.hasMoreElements() ) {
            RssChannel aChannel = ( RssChannel ) enum.nextElement();
            aList.add( new NewsChan( composite, aChannel ) );
        }
    }

	/**
	 * DOCUMENT ME!
	 */
	protected void dispose() {
		this.disposeChildren();
		this.composite.dispose();
	}

    /**
     * DOCUMENT ME!
     *
     * @param aDoc DOCUMENT ME!
     */
    protected void update( RssDocument aDoc ) {
        this.disposeChildren();
        this.fill( aDoc );
        this.composite.layout();
    }

	/**
	 * @return
	 */
	public RssDocument getARssDocument() {
		return aRssDocument;
	}
}

/*
$Log: NewsFeed.java,v $
Revision 1.3  2003/10/04 08:49:25  lemmster
foobar

Revision 1.2  2003/09/29 14:05:45  lemmster
update & add still not working

Revision 1.1  2003/09/27 12:09:32  lemmster
initial commit

*/
