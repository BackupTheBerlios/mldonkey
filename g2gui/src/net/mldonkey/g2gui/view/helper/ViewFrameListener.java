/*
 * Copyright 2003
 * g2gui Team
 *
 *
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view.helper;

import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.actions.BestFitColumnAction;
import net.mldonkey.g2gui.view.viewers.actions.FilterAction;
import net.mldonkey.g2gui.view.viewers.actions.NetworkFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.SortByColumnAction;
import net.mldonkey.g2gui.view.viewers.filters.GViewerFilter;
import net.mldonkey.g2gui.view.viewers.filters.NetworkGViewerFilter;
import net.mldonkey.g2gui.view.viewers.filters.StateGViewerFilter;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;


/**
 * ViewFrameListener
 *
 * @version $Id: ViewFrameListener.java,v 1.7 2003/12/17 13:06:03 lemmy Exp $
 *
 */
public abstract class ViewFrameListener implements IMenuListener, DisposeListener {
    protected ViewFrame viewFrame;
    protected Control control;
    protected GView gView;

    public ViewFrameListener(ViewFrame viewFrame) {
        this.viewFrame = viewFrame;
		this.viewFrame = viewFrame;
		this.control = viewFrame.getControl();
		this.gView = viewFrame.getGView();
        
        if (this.gView != null) 
		    this.gView.addDisposeListener(this);
    }

    public abstract void menuAboutToShow(IMenuManager menuManager);
    
    protected void createNetworkFilterSubMenu(MenuManager menu) {
        NetworkInfo[] networks = viewFrame.getCore().getNetworkInfoMap().getNetworks();

        for (int i = 0; i < networks.length; i++) {
            NetworkInfo network = networks[ i ];

            if (network.isEnabled() && network.isSearchable())
                menu.add(new NetworkFilterAction(viewFrame.getGView(), network));
        }
    }

    public void createSortByColumnSubMenu(IMenuManager menuManager) {
        if (viewFrame.getGView() == null) return;
        
        MenuManager sortSubMenu = new MenuManager(G2GuiResources.getString("MISC_SORT"));

        for (int i = 0; i < viewFrame.getGView().getTable().getColumnCount(); i++)
            sortSubMenu.add(new SortByColumnAction(viewFrame.getGView(), i));

        menuManager.add(sortSubMenu);
    }
    
    protected void createBestFitColumnSubMenu(IMenuManager menuManager) {
    	if (viewFrame.getGView() == null) return;
    	
    	MenuManager favoriteSubMenu = new MenuManager(G2GuiResources.getString("MISC_BESTFIT"));

    	for (int i = 0; i < viewFrame.getGView().getTable().getColumnCount(); i++)
    		favoriteSubMenu.add(new BestFitColumnAction(viewFrame.getGView(), i));

    	menuManager.add(favoriteSubMenu);
    }

    public void widgetDisposed(DisposeEvent arg0) {
    }

    /**
     * Sets the state of the table filters from the pref file
     * @param states
     */
    protected void setFilterState( Enum[] states ) {
    	// set the last state from preferences
    	boolean temp = false;
    	GViewerFilter aFilter = new StateGViewerFilter(gView);
    	for ( int i = 0; i < states.length; i++ ) {
    		if ( PreferenceLoader.loadBoolean( states[ i ].getPrefName(this) ) ) {
    			aFilter.add( states[ i ] );
    			temp = true;
    		}
    	}
    	// just add the filter if we really added enums to it
    	if ( temp ) gView.addFilter( aFilter );
    }
    
    /**
     * Saves the state of the table filters to the pref file
     * @param states
     */
    protected void saveFilterState( Enum[] states ) {
    	for ( int i = 0; i < states.length; i++ )
    		PreferenceLoader.setValue( states[ i ].getPrefName(this), FilterAction.isFiltered( gView, states[ i ] ) ); 
    }
    
    /**
     * Saves the bestfit column to the pref file
     */
    protected void saveBestFit() {
    	if ( gView != null && gView.getColumnControlListenerIsOn() != -1 )
    		PreferenceLoader.setValue( this.getClass().getName() + "BestFit", gView.getColumnControlListenerIsOn() );
    }

    /**
     * reads the bestfit column from the pref file and sets them on the table
     */
    protected void setBestFit() {
    	int i = PreferenceLoader.getInt( this.getClass().getName() + "BestFit" );
    	if ( i != -1 )
    		new BestFitColumnAction( gView, i ).run();
    }

    /**
     * Saves the state of the networkfilters to the pref store
     */
    protected void saveNetworkFilterState() {
    	NetworkInfo[] networks = viewFrame.getGView().getCore().getNetworkInfoMap().getNetworks();
    	for ( int i = 0; i < networks.length; i++ )
    		PreferenceLoader.setValue( networks[ i ].getNetworkType().getPrefName(this),
    									FilterAction.isFiltered( gView, networks[ i ] ) ); 
    }
    
    /**
     * Reads the state of the networkfilters from the pref store and sets it on the table
     */
    protected void setNetworkFilterState() {
    	// set the last state from preferences
    	boolean temp = false;
    	GViewerFilter aFilter = new NetworkGViewerFilter(gView);
    	NetworkInfo[] networks = viewFrame.getGView().getCore().getNetworkInfoMap().getNetworks();
    	for ( int i = 0; i < networks.length; i++ ) {
    		if ( PreferenceLoader.loadBoolean( networks[ i ].getNetworkType().getPrefName(this) ) ) {
    			aFilter.add( networks[ i ].getNetworkType() );
    			temp = true;
    		}
    	}
    	// just add the filter if we really added enums to it
    	if ( temp ) gView.addFilter( aFilter );
    }
}


/*
$Log: ViewFrameListener.java,v $
Revision 1.7  2003/12/17 13:06:03  lemmy
save all panelistener states correctly to the prefstore

Revision 1.6  2003/12/07 19:40:19  lemmy
[Bug #1156] Allow a certain column to be 100% by pref

Revision 1.5  2003/11/29 17:02:27  zet
more viewframes.. will continue later.

Revision 1.4  2003/11/29 01:51:53  zet
a few more viewframe changes.. will continue later.

Revision 1.3  2003/11/28 01:06:21  zet
not much- slowly expanding viewframe - will continue later

Revision 1.2  2003/11/24 01:33:27  zet
move some classes

Revision 1.1  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

*/
