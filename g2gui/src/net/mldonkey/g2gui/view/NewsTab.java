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
package net.mldonkey.g2gui.view;

import java.util.Observable;

import net.mldonkey.g2gui.view.helper.CCLabel;
import net.mldonkey.g2gui.view.news.NewsManager;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * NewsTab
 *
 * @version $Id: NewsTab.java,v 1.1 2003/09/27 12:09:32 lemmster Exp $
 *
 */
public class NewsTab extends GuiTab {

    /**
     * @param gui
     */
    public NewsTab( MainTab gui ) {
        super( gui );
		/* Set our name on the coolbar */
		createButton( "NewsButton", 
							"News" ,
							"Your RSS newsfeeds" );
		createContents( this.subContent );					
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.GuiTab#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected void createContents( Composite parent ) {
		ViewForm viewForm = new ViewForm( parent, SWT.BORDER | ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );
		viewForm.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		NewsManager newsManager = new NewsManager( viewForm );

		CLabel cLabel = CCLabel.createCL( viewForm, "TT_NewsButton", "NewsButtonSmallTitlebar" );

		viewForm.setTopLeft( cLabel );
		viewForm.setContent( newsManager.getContent() );
    }
    
    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update( Observable o, Object arg ) { }
}

/*
$Log: NewsTab.java,v $
Revision 1.1  2003/09/27 12:09:32  lemmster
initial commit

*/
