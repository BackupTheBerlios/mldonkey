/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.statusline;

import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.ServerTab;
import net.mldonkey.g2gui.view.StatusLine;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;


/**
 * NetworkItem
 *
 * @author $user$
 * @version $Id: NetworkItem.java,v 1.16 2003/08/18 01:42:24 zet Exp $ 
 *
 */
public class NetworkItem implements Observer {
	private boolean connected;
	private CoreCommunication core;
	private StatusLine statusline;
	private Composite composite;
	private GridLayout gridLayout;
	private Image image;
	private CLabel cLabel, anotherCLabel;
	private MenuItem item;
	private boolean enabled;
	private GuiTab serverTab;


	/**
	 * @param statusline the Statusline in which this item should appear
	 * @param line
	 * @param mldonkey
	 */
	public NetworkItem( StatusLine statusline, CoreCommunication mldonkey ) {
		this.composite = statusline.getStatusline();
		this.statusline = statusline;
		this.core = mldonkey;
		
		this.createContent();

		this.core.getNetworkInfoMap().addObserver( this );
		
		/* get the servertab */
		GuiTab[] tabs = statusline.getMainTab().getTabs();
		for ( int i = 0; i < tabs.length; i++ ) {
			GuiTab tab = tabs[ i ];
			if ( tab instanceof ServerTab ) {
				this.serverTab = tab;
			}
		}
	}

	/**
	 * 
	 */
	private void createContent() {
		NetworkInfo[] networks = core.getNetworkInfoMap().getNetworks();

		composite = new Composite( composite, SWT.BORDER );
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = networks.length;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		composite.setLayout( gridLayout );
		
		/* sets the enabled/disabled image for each known network */
		for ( int i = 0; i < networks.length; i++ ) {
			NetworkInfo network = networks[ i ];
			cLabel = new CLabel( composite, SWT.NONE );
			cLabel.setData( network );
			cLabel.setMenu( this.createRightClickMenu() );
//			cLabel.setText( network.getNetworkShortName() );
			/* set the network name and status as tooltip */
			cLabel.setToolTipText( network.getNetworkName() + " " + new Boolean( network.isEnabled() ).toString() );
			/* by default, the network is not connected */
			cLabel.setData( "CONNECTED", new Boolean( false ) );

			/* get the clicked label */			
			cLabel.addMouseListener( new MouseListener() {
				public void mouseDown( MouseEvent e ) {
					if ( e.button != 3 ) return;
					anotherCLabel = ( CLabel ) e.widget;
				}
				public void mouseDoubleClick( MouseEvent e ) { }
				public void mouseUp( MouseEvent e ) { }
			} );

			/* on dispose() deregister on the model */			
			cLabel.addDisposeListener( new DisposeListener () {
				public void widgetDisposed(DisposeEvent e) {
					core.getNetworkInfoMap().deleteObserver( NetworkItem.this );
				}
			} );

			if ( core.getProtoToUse() >= 18 ) {
				/* get the servers the network is connected to */
				int numConnected = network.getConnectedServers();
				if ( numConnected > 0 ) { //TODO get min # of servers
					/* we are connected, the "CONNECTED" to true */
					cLabel.setData( "CONNECTED", new Boolean( true ) );
					/* alter the tooltip text, to reflect that we are connected */
					cLabel.setToolTipText( network.getNetworkName() + ": connected to " + numConnected + " servers" );
				}
			}
			/* now create the image */
			cLabel.setImage( this.createNetworkImage( network ) );
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) {
		// ClassCastException without this
		if (!(arg instanceof NetworkInfo)) return;
		
		final NetworkInfo network = ( NetworkInfo ) arg;
		composite.getDisplay().asyncExec( new Runnable () {
			public void run() {
				/* check for widget disposed */
				if ( composite.isDisposed() ) return;

				/* get all labels */
				Control[] controls = composite.getChildren();

				/* find the corresponding label */	
				cLabel = getLabelByNetwork( controls, network );
				cLabel.getImage().dispose(); // dispose the old image // nullpointer on this
				cLabel.setToolTipText( network.getNetworkName() + " " + new Boolean( network.isEnabled() ).toString() );

				if ( core.getProtoToUse() >= 18 ) {
					/* get the servers the network is connected to */
					int numConnected = network.getConnectedServers();
					if ( numConnected > 0 ) { //TODO get min # of servers
						/* we are connected, the "CONNECTED" to true */
						cLabel.setData( "CONNECTED", new Boolean( true ) );
						/* alter the tooltip text, to reflect that we are connected */
						cLabel.setToolTipText( network.getNetworkName() + ": connected to " + numConnected + " servers" );
					}
				}
				cLabel.setImage( createNetworkImage( network ) );
			}
		} );	
	}
	
	private CLabel getLabelByNetwork( Control[] controls, NetworkInfo network ) {
		for ( int i = 0; i < controls.length; i++ ) {
			cLabel = ( CLabel ) controls[ i ];
			if ( cLabel.getData() == network ) {
				return cLabel;
			}
		}
		return null;
	}
		
	/**
	 * set the proper image for the network
	 * @param networkInfo The network info to create the image for
	 * @return an image representing the network status
	 */
	private Image createNetworkImage( NetworkInfo networkInfo ) {
		/* enabled or disabled image */
		enabled = ( ( NetworkInfo ) cLabel.getData() ).isEnabled();
	
		// we have icons for these:
		if ( networkInfo.getNetworkType() == NetworkInfo.Enum.DONKEY 
			|| networkInfo.getNetworkType() == NetworkInfo.Enum.FT
			|| networkInfo.getNetworkType() == NetworkInfo.Enum.GNUT
			|| networkInfo.getNetworkType() == NetworkInfo.Enum.GNUT2
			|| networkInfo.getNetworkType() == NetworkInfo.Enum.SOULSEEK 
			|| networkInfo.getNetworkType() == NetworkInfo.Enum.DC ) {
	
			if (enabled){	
			
				connected = ( ( Boolean ) cLabel.getData( "CONNECTED" ) ).booleanValue();
				if ( connected || core.getProtoToUse() < 18 ) // connected to servers
					return G2GuiResources.getImage(networkInfo.getNetworkShortName() + "Connected");
				else // not connected to servers 
					return G2GuiResources.getImage(networkInfo.getNetworkShortName() + "Disconnected"); 
			} else 	
				return G2GuiResources.getImage(networkInfo.getNetworkShortName() + "Disabled");					
	
		// but not for these:
		} else {
		
			if ( enabled ) { 
				connected = ( ( Boolean ) cLabel.getData( "CONNECTED" ) ).booleanValue();
				if ( connected || core.getProtoToUse() < 18 ) 
					return G2GuiResources.getImage("UnknownConnected");
				else 
					return G2GuiResources.getImage("UnknownDisconnected");
			}
			else 
				return G2GuiResources.getImage("UnknownDisabled");
	
		}		
	}
	
	/**
	 * Creates the "button 3" menu
	 * @return The menu added to the table
	 */
	private Menu createRightClickMenu() {
		Shell shell = composite.getShell();
		Menu menu = new Menu( shell , SWT.POP_UP );
	
		/* change the menu for bittorrent (doesnt have servers) */
		if ( ( ( NetworkInfo ) cLabel.getData() ).hasServers() ) {
		
			/* manage Server */
			item = new MenuItem( menu, SWT.PUSH );
			item.setText( "manage server" );
			item.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent e ) {
					statusline.getMainTab().setActive( serverTab );
					NetworkInfo network = ( NetworkInfo ) anotherCLabel.getData();
					( ( ServerTab ) serverTab ).setFilter( network.getNetworkType() );
				}
			} );
	
			new MenuItem( menu, SWT.SEPARATOR );
		}
	
		/* disable/enable the network */
		final MenuItem stateItem = new MenuItem( menu, SWT.PUSH );
		stateItem.setText( "enable" );
		stateItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				NetworkInfo networkInfo = ( NetworkInfo ) anotherCLabel.getData();
				networkInfo.setEnabled();
			}
		} );
			
		/* disable or enable? */
		menu.addListener( SWT.Show, new Listener () {
			private MenuItem item = NetworkItem.this.item;
			public void handleEvent( Event event ) {
				NetworkInfo networkInfo = ( NetworkInfo ) anotherCLabel.getData();
				if ( networkInfo.isEnabled() ) {
					stateItem.setText( "Disable" );
					item.setEnabled( true );
				}
				else {
					stateItem.setText( "Enable" );						
					item.setEnabled( false );
				}
			}
		} );
		return menu;
	}
}

/*
$Log: NetworkItem.java,v $
Revision 1.16  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.15  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.14  2003/08/11 11:26:20  lemmstercvs01
added some images

Revision 1.13  2003/08/10 12:59:01  lemmstercvs01
"manage servers" in NetworkItem implemented

Revision 1.12  2003/08/04 19:22:08  zet
trial tabletreeviewer

Revision 1.11  2003/08/03 17:38:54  zet
check instanceof

Revision 1.10  2003/08/02 19:47:54  lemmstercvs01
finally working

Revision 1.9  2003/08/02 09:55:16  lemmstercvs01
observers changed

Revision 1.8  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.7  2003/07/31 14:11:06  lemmstercvs01
reworked

Revision 1.6  2003/07/17 14:58:56  lemmstercvs01
refactored

Revision 1.5  2003/07/06 13:19:39  dek
small change

Revision 1.4  2003/07/04 17:48:57  dek
refactor

Revision 1.3  2003/06/28 09:50:00  lemmstercvs01
hasNext() optimized

Revision 1.2  2003/06/27 13:37:28  dek
tooltips added

Revision 1.1  2003/06/27 13:21:12  dek
added connected Networks

*/