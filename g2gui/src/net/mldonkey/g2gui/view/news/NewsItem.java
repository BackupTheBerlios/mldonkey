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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import churchillobjects.rss4j.RssChannelItem;

/**
 * NewsItem
 *
 * @version $Id: NewsItem.java,v 1.2 2003/09/29 14:05:45 lemmster Exp $
 *
 */
public class NewsItem extends News {
    private Font boldFont;
    private Font italicFont;
    private Composite myComposite;
    private RssChannelItem chanItem;

    public NewsItem( Control aControl, RssChannelItem chanItem ) {
		super ( aControl );
        this.chanItem = chanItem;
        this.create();
    }

    /**
     * DOCUMENT ME!
     */
    public void create() {
    	this.dispose();
 
		boldFont = new Font( control.getDisplay(), new FontData( "Arial", 10, SWT.BOLD ) );
		italicFont = new Font( control.getDisplay(), new FontData( "Arial", 10, SWT.ITALIC ) );
    	
        myComposite = new Composite( ( Composite ) control, SWT.NONE );
        myComposite.setLayout( new GridLayout() );
        myComposite.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_CENTER ) );

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        Group aGroup = new Group( myComposite, SWT.NONE );
        aGroup.setLayoutData( new GridData( GridData.FILL_BOTH ) );
        aGroup.setLayout( gridLayout );

        GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 2;
        Text aText = new Text( aGroup, SWT.NONE | SWT.READ_ONLY );
        aText.setLayoutData( gridData );
        aText.setFont( boldFont );
        aText.setText( chanItem.getItemTitle() );
 
        aText = new Text( aGroup, SWT.NONE | SWT.READ_ONLY );
        aText.setText( "Read more..." );
        aText.setFont( italicFont );
        aText.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_CENTER ) );

        aText = new Text( aGroup, SWT.NONE | SWT.READ_ONLY );
        aText.setText( "Comments..." );
        aText.setFont( italicFont );
        aText.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_CENTER ) );
    }

    /**
     * DOCUMENT ME!
     */
    public void dispose() {
        if ( ( this.myComposite == null ) || this.myComposite.isDisposed() )
            return;
        boldFont.dispose();
        italicFont.dispose();
        myComposite.dispose();
    }
}

/*
$Log: NewsItem.java,v $
Revision 1.2  2003/09/29 14:05:45  lemmster
update & add still not working

Revision 1.1  2003/09/27 12:09:32  lemmster
initial commit

*/
