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

import net.mldonkey.g2gui.model.ServerInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.helper.CCLabel;
import net.mldonkey.g2gui.view.helper.HeaderBarMouseAdapter;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.server.ServerPaneListener;
import net.mldonkey.g2gui.view.server.ServerTableView;
import net.mldonkey.g2gui.view.viewers.GPaneListener;
import net.mldonkey.g2gui.view.viewers.filters.StateGViewerFilter;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

/**
 * ServerTab
 *
 *
 * @version $Id: ServerTab.java,v 1.47 2003/11/04 20:38:27 zet Exp $ 
 *
 */
public class ServerTab extends TableGuiTab implements Runnable, DisposeListener {
	private MenuManager popupMenu;
	private String statusText = "";
	private ServerInfoIntMap servers;

	/**
	 * @param gui The main gui tab
	 */
	public ServerTab( MainTab gui ) {
		super( gui );
		/* associate this tab with the corecommunication */
		this.core = gui.getCore();
		/* Set our name on the coolbar */
		createButton( "ServersButton", 
						G2GuiResources.getString( "TT_ServersButton" ),
						G2GuiResources.getString( "TT_ServersButtonToolTip" ) );

		/* create the tab content */
		this.createContents( this.subContent );
		updateDisplay();			
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.GuiTab#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected void createContents( Composite parent ) {
		ViewForm viewForm = 
			new ViewForm( parent, SWT.BORDER
				| ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );
		
		CLabel ccLabel = CCLabel.createCL( viewForm, "TT_ServersButton", "ServersButtonSmall" );
			
		Composite composite = new Composite( viewForm, SWT.NONE );
		composite.setLayout( new FillLayout() );
	
		viewForm.setContent( composite );
		viewForm.setTopLeft( ccLabel );
		
		GPaneListener aListener = new ServerPaneListener(this, core);
		createPaneToolBar( viewForm, aListener);

		gView = new ServerTableView( composite, core );

		/* fill the table with content */
		servers = core.getServerInfoIntMap();
		gView.getViewer().setInput( servers );
		servers.clearAdded();

		int itemCount = gView.getTable().getItemCount();
		this.statusText = G2GuiResources.getString( "SVT_SERVERS" ) + itemCount;
		
		popupMenu = new MenuManager( "" );
		popupMenu.setRemoveAllWhenShown( true );
		popupMenu.addMenuListener( aListener );
		( (ServerPaneListener) aListener ).initialize();
		ccLabel.addMouseListener( new HeaderBarMouseAdapter( ccLabel, popupMenu ) );
	}

	private void createPaneToolBar( ViewForm aViewForm, IMenuListener menuListener ) {
		ToolBar toolBar = new ToolBar(aViewForm, SWT.RIGHT | SWT.FLAT);

		super.createPaneToolBar( toolBar, menuListener );   

		aViewForm.setTopRight( toolBar );
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) {
		if ( gView.getTable().isDisposed() ) return;
		this.content.getDisplay().asyncExec( this );
	}		

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		/* still running? */
		if (gView.getTable().isDisposed() ) return;
		
		synchronized ( this.servers.getRemoved() ) {
			( (TableViewer) gView.getViewer() ).remove( this.servers.getRemoved().toArray() );
			this.servers.clearRemoved();
		}
		synchronized ( this.servers.getAdded() ) {
			( (TableViewer) gView.getViewer() ).add( this.servers.getAdded().toArray() );
			this.servers.clearAdded();
		}
		synchronized ( this.servers.getModified() ) {
			( (TableViewer) gView.getViewer() ).update( this.servers.getModified().toArray(), null );
			this.servers.clearModified();
		}
		int itemCount = gView.getTable().getItemCount();	
		this.setStatusLine();

		/* refresh the table if "show connected servers only" is true and the filter is activated */
		if ( PreferenceLoader.loadBoolean( "displayAllServers" )
		&& this.servers.getConnected() != itemCount ) {
			if ( StateGViewerFilter.matches( gView, EnumState.CONNECTED ) )
				gView.refresh();
		}
	}
	
	/**
	 * unregister ourself at the observable
	 */
	public void setInActive() {
		this.core.getServerInfoIntMap().deleteObserver( this );
		super.setInActive();
	}
	
	/**
	 * register ourself at the observable
	 */
	public void setActive() {
		/* if we become active, refresh the table */
		this.servers = this.core.getServerInfoIntMap();
		gView.getShell().getDisplay().asyncExec( this );
		this.setStatusLine();
		
		this.core.getServerInfoIntMap().addObserver( this );
		super.setActive();
	}
	
	/**
	 * Sets the statusline to the current value
	 */
	private void setStatusLine() {
		if ( !this.isActive() ) return;
		this.statusText = G2GuiResources.getString( "SVT_SERVERS" ) + servers.getConnected();
		this.mainWindow.getStatusline().update( this.statusText );
		this.mainWindow.getStatusline().updateToolTip( "" );
	}
	
	/**
	 * @return The text this tab wants to display in the statusline
	 */
	public String getStatusText() {
		return this.statusText;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.DisposeListener#
	 * widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed( DisposeEvent e ) {
		this.core.getServerInfoIntMap().deleteObserver( this );
	}
	
	/**
	 * Updates this tab on preference close
	 */
	public void updateDisplay() {
		( ( ServerTableView ) gView ).updateDisplay();
		super.updateDisplay();
	}
}

/*
$Log: ServerTab.java,v $
Revision 1.47  2003/11/04 20:38:27  zet
update for transparent gifs

Revision 1.46  2003/10/31 16:02:17  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.45  2003/10/31 13:20:31  lemmster
added PaneGuiTab and TableGuiTab
added "dropdown" button to all PaneGuiTabs (not finished yet, continue on monday)

Revision 1.44  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to gView... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class gView do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.43  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.42  2003/10/28 11:07:32  lemmster
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.41  2003/10/22 01:36:59  zet
add column selector to server/search (might not be finished yet..)

Revision 1.40  2003/10/21 17:06:27  lemmster
fix manage servers from statusline

Revision 1.39  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

Revision 1.38  2003/10/12 14:56:19  zet
*** empty log message ***

Revision 1.37  2003/10/11 20:19:18  zet
Don't TableColumn.setWidth(0) on gtk

Revision 1.36  2003/09/27 12:33:32  dek
server-Table has now same show-Gridlines-behaviour as download-Table

Revision 1.35  2003/09/23 11:46:25  lemmster
displayTableColors for servertab

Revision 1.34  2003/09/22 20:02:34  lemmster
right column align for integers [bug #934]

Revision 1.33  2003/09/20 14:39:48  zet
move transfer package

Revision 1.32  2003/09/18 09:44:57  lemmster
checkstyle

Revision 1.31  2003/09/16 02:12:30  zet
match the menus with transfertab

Revision 1.30  2003/09/14 13:44:22  lemmster
set statusline only on active

Revision 1.29  2003/09/14 13:24:30  lemmster
add header button to servertab

Revision 1.28  2003/09/14 10:01:24  lemmster
save column width [bug #864]

Revision 1.27  2003/09/14 09:42:51  lemmster
save column width

Revision 1.26  2003/09/14 09:40:31  lemmster
save column width

Revision 1.25  2003/09/11 13:39:19  lemmster
check for disposed

Revision 1.24  2003/08/29 22:11:47  zet
add CCLabel helper class

Revision 1.23  2003/08/29 19:30:50  zet
font colour

Revision 1.22  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.21  2003/08/29 16:06:54  zet
optional shadow

Revision 1.20  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.19  2003/08/25 20:38:26  vnc
GTK layout cleanups, slight typo refactoring

Revision 1.18  2003/08/23 15:21:37  zet
remove @author

Revision 1.17  2003/08/23 15:01:32  lemmster
update the header

Revision 1.16  2003/08/23 14:58:38  lemmster
cleanup of MainTab, transferTree.* broken

Revision 1.15  2003/08/23 10:33:36  lemmster
updateDisplay() only on displayAllServers change

Revision 1.14  2003/08/23 09:46:18  lemmster
superclass TableMenuListener added

Revision 1.13  2003/08/23 08:30:07  lemmster
added defaultItem to the table

Revision 1.12  2003/08/20 22:18:56  zet
Viewer updates

Revision 1.11  2003/08/20 21:34:22  lemmster
additive filters

Revision 1.10  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.9  2003/08/11 19:25:04  lemmstercvs01
bugfix at CleanTable

Revision 1.8  2003/08/10 12:59:01  lemmstercvs01
"manage servers" in NetworkItem implemented

Revision 1.7  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.6  2003/08/07 13:25:37  lemmstercvs01
ResourceBundle added

Revision 1.5  2003/08/07 12:35:31  lemmstercvs01
cleanup, more efficient

Revision 1.4  2003/08/06 20:56:49  lemmstercvs01
cleanup, more efficient

Revision 1.3  2003/08/06 17:38:38  lemmstercvs01
some actions still missing. but it should work for the moment

Revision 1.2  2003/08/05 15:35:24  lemmstercvs01
minor: column width changed

Revision 1.1  2003/08/05 13:50:10  lemmstercvs01
initial commit

*/