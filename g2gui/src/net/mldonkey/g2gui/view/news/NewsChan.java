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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import churchillobjects.rss4j.RssChannel;
import churchillobjects.rss4j.RssChannelItem;

/**
 * NewsFeed
 *
 * @version $Id: NewsChan.java,v 1.2 2003/09/29 14:05:45 lemmster Exp $
 *
 */
public class NewsChan extends News {
    private RssChannel channel;
    private List aList;
    private Group group;

    public NewsChan( Control aControl, RssChannel channel ) {
		super( aControl );
        this.update( channel );
    }

    /**
     * DOCUMENT ME!
     */
    private void create() {
    	System.out.println( channel.getChannelTitle() );
		group = new Group( ( Composite ) control, SWT.NONE );
		group.setLayout( new GridLayout() );
		group.setText( channel.getChannelTitle() );

        Enumeration items = channel.items();
        while ( items.hasMoreElements() )
            aList.add( new NewsItem( group, ( RssChannelItem ) items.nextElement() ) );
    }

    /**
     * DOCUMENT ME!
     *
     * @param aChannel DOCUMENT ME!
     */
    public void update( RssChannel aChannel ) {
        this.channel = aChannel;
        this.aList = Collections.synchronizedList( new ArrayList() );
        this.create();
    }

    /**
     * DOCUMENT ME!
     */
    public void dispose() {
        for ( int i = 0; i < aList.size(); i++ )
            ( ( NewsItem ) aList.get( i ) ).dispose();
        this.group.dispose();
    }
}

/*
$Log: NewsChan.java,v $
Revision 1.2  2003/09/29 14:05:45  lemmster
update & add still not working

Revision 1.1  2003/09/27 12:09:32  lemmster
initial commit

*/
