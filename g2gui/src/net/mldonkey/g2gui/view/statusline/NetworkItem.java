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
import java.util.ResourceBundle;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.view.StatusLine;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
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
 * @version $Id: NetworkItem.java,v 1.11 2003/08/03 17:38:54 zet Exp $ 
 *
 */
public class NetworkItem implements Observer {
	private boolean connected;
	private static ResourceBundle res = ResourceBundle.getBundle( "g2gui" );
	private CoreCommunication core;
	private StatusLine statusline;
	private Composite composite;
	private GridLayout gridLayout;
	private Image image;
	private CLabel cLabel, anotherCLabel;
	private boolean enabled;


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
			cLabel.setText( network.getNetworkShortName() );
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
				cLabel.getImage().dispose(); // dispose the old image
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
	 * Creates a Transparent imageobject with a given .png|.gif Image-Object
	 * be aware, the the scr-image is disposed, so dont' use it any further
	 * 
	 * @param src the non-transparent image we want to process
	 * @param control where is our image laid in, to check for the background-color
	 * @return the transparent image
	 */
	private Image createTransparentImage( Image src, Control control ) {
		int width = src.getBounds().width;
		int height = src.getBounds().height;
		
		Image result = new Image( control.getDisplay(), new Rectangle( 0, 0, width, height ) );		
		GC gc = new GC( result );
		gc.setBackground( control.getBackground(  ) );
		gc.fillRectangle( 0, 0, width, height );							
		gc.drawImage( src, 0, 0 );
			
		src.dispose();		
		gc.dispose();		

		return result;
	} 
	
	/**
	 * set the proper image for the network
	 * @param networkInfo The network info to create the image for
	 * @return an image representing the network status
	 */
	private Image createNetworkImage( NetworkInfo networkInfo ) {
		/* enabled or disabled image */
		enabled = ( ( NetworkInfo ) cLabel.getData() ).isEnabled();
	
		/* donkey */
		if ( networkInfo.getNetworkType() == NetworkInfo.Enum.DONKEY ) {
			if ( enabled ) { // enabled
				connected = ( ( Boolean ) cLabel.getData( "CONNECTED" ) ).booleanValue();
				if ( connected || core.getProtoToUse() < 18 ) // connected to servers
					image = new Image( composite.getDisplay(),
									"icons/edonkey2000_connected.png" );
				else // not connected to servers 
					image = new Image( composite.getDisplay(),
									"icons/edonkey2000_disconnected.png" );
			}
			else // disabled
				image = new Image( composite.getDisplay(),
								"icons/edonkey2000_disabled.png" );
			
			/* generate the image transparent and return the image */
			image = this.createTransparentImage( image, composite );
			return image;
		}
	
		/* overnet */
/*		if ( networkInfo.getNetworkType() == NetworkInfo.Enum.OV ) {
			if ( enabled ) { // enabled
				connected = ( ( Boolean ) label.getData( "CONNECTED") ).booleanValue();
				if ( connected || core.getProtoToUse() < 18 ) {
					image = new Image( composite.getDisplay(),
									"icons/overnet_connected.png" );
				}
				else {
					image = new Image( composite.getDisplay(),
									"icons/overnet_disconnected.png" );
				}
			}
			else { // disabled
				image = new Image( composite.getDisplay(),
								"icons/overnet_disabled.png" );
			}
			
			/* generate the image transparent and return the image */
/*			image = this.createTransparentImage( image, composite );
			return image;
		}

		/* fasttrack */
		if ( networkInfo.getNetworkType() == NetworkInfo.Enum.FT ) {
			if ( enabled ) { // enabled
				connected = ( ( Boolean ) cLabel.getData( "CONNECTED") ).booleanValue();
				if ( connected || core.getProtoToUse() < 18 ) {
					image = new Image( composite.getDisplay(),
									"icons/kazaa_connected.png" );
				}
				else {
					image = new Image( composite.getDisplay(),
									"icons/kazaa_disconnected.png" );
				}
			}
			else { // disabled
				image = new Image( composite.getDisplay(),
								"icons/kazaa_disabled.png" );
			}
			/* generate the image transparent and return the image */
			image = this.createTransparentImage( image, composite );
			return image;
		}

		/* gnutella2 */
		if ( networkInfo.getNetworkType() == NetworkInfo.Enum.GNUT2 ) {
			if ( enabled ) { // enabled
				connected = ( ( Boolean ) cLabel.getData( "CONNECTED") ).booleanValue();
				if ( connected || core.getProtoToUse() < 18 ) {
					image = new Image( composite.getDisplay(),
									"icons/gnutella_connected.png" );
				}
				else {
					image = new Image( composite.getDisplay(),
									"icons/gnutella_disconnected.png" );
				}
			}
			else { // disabled
				image = new Image( composite.getDisplay(),
								"icons/gnutella_disabled.png" );
			}
			/* generate the image transparent and return the image */
			image = this.createTransparentImage( image, composite );
			return image;
		}

		/* gnutella */
		if ( networkInfo.getNetworkType() == NetworkInfo.Enum.GNUT ) {
			if ( enabled ) { // enabled
				connected = ( ( Boolean ) cLabel.getData( "CONNECTED") ).booleanValue();
				if ( connected || core.getProtoToUse() < 18 ) {
					image = new Image( composite.getDisplay(),
									"icons/gnutella_connected.png" );
				}
				else {
					image = new Image( composite.getDisplay(),
									"icons/gnutella_disconnected.png" );
				}
			}
			else { // disabled
				image = new Image( composite.getDisplay(),
								"icons/gnutella_disabled.png" );
			}
			
			/* generate the image transparent and return the image */
			image = this.createTransparentImage( image, composite );
			return image;
		}

		/* soulseek */
		if ( networkInfo.getNetworkType() == NetworkInfo.Enum.GNUT ) {
			if ( enabled ) { // enabled
				connected = ( ( Boolean ) cLabel.getData( "CONNECTED") ).booleanValue();
				if ( connected || core.getProtoToUse() < 18 ) {
					image = new Image( composite.getDisplay(),
									"icons/soulseek_connected.png" );
				}
				else {
					image = new Image( composite.getDisplay(),
									"icons/soulseek_disconnected.png" );
				}
			}
			else { // disabled
				image = new Image( composite.getDisplay(),
								"icons/soulseek_disabled.png" );
			}
					
			/* generate the image transparent and return the image */
			image = this.createTransparentImage( image, composite );
			return image;
		}

		/* bittorrent */
/*		if ( networkInfo.getNetworkType() == NetworkInfo.Enum.BT ) {
			if ( enabled ) { // enabled
				connected = ( ( Boolean ) label.getData( "CONNECTED") ).booleanValue();
				if ( connected || core.getProtoToUse() < 18 ) {
					image = new Image( composite.getDisplay(),
									"icons/bittorrent_connected.png" );
				}
				else {
					image = new Image( composite.getDisplay(),
									"icons/bittorrent_disconnected.png" );
				}
			}
			else { // disabled
				image = new Image( composite.getDisplay(),
								"icons/bittorrent_disabled.png" );
			}
			
			/* generate the image transparent and return the image */
/*			image = this.createTransparentImage( image, composite );
			return image;
		}

		/* direct connect */
/*		if ( networkInfo.getNetworkType() == NetworkInfo.Enum.DC ) {
			if ( enabled ) { // enabled
				connected = ( ( Boolean ) label.getData( "CONNECTED") ).booleanValue();
				if ( connected || core.getProtoToUse() < 18 ) {
					image = new Image( composite.getDisplay(),
									"icons/directConnect_connected.png" );
				}
				else {
					image = new Image( composite.getDisplay(),
									"icons/directConnect_disconnected.png" );
				}
			}
			else { // disabled
				image = new Image( composite.getDisplay(),
								"icons/directConnect_disabled.png" );
			}
			
			/* generate the image transparent and return the image */
/*			image = this.createTransparentImage( image, composite );
			return image;
		}

		/* open napster */
/*		if ( networkInfo.getNetworkType() == NetworkInfo.Enum.OPENNP ) {
			if ( enabled ) { // enabled
				connected = ( ( Boolean ) label.getData( "CONNECTED") ).booleanValue();
				if ( connected || core.getProtoToUse() < 18 ) {
					image = new Image( composite.getDisplay(),
									"icons/openNap_connected.png" );
				}
				else {
					image = new Image( composite.getDisplay(),
									"icons/openNap_disconnected.png" );
				}
			}
			else { // disabled
				image = new Image( composite.getDisplay(),
								"icons/openNap_disabled.png" );
			}
			
			/* generate the image transparent and return the image */
/*			image = this.createTransparentImage( image, composite );
			return image;
		} 
		/* default (unknown network) */
//		else {
			if ( enabled ) { // enabled
				connected = ( ( Boolean ) cLabel.getData( "CONNECTED" ) ).booleanValue();
				if ( connected || core.getProtoToUse() < 18 ) {
					image = new Image( composite.getDisplay(),
									"icons/unknown_connected.png" );
				}
				else {
					image = new Image( composite.getDisplay(),
									"icons/unknown_disconnected.png" );
				}
			}
			else { // disabled
				image = new Image( composite.getDisplay(),
								"icons/unknown_disabled.png" );
			}
			
			/* generate the image transparent and return the image */
			image = this.createTransparentImage( image, composite );
			return image;
//		}
	}
	
	/**
	 * Creates the "button 3" menu
	 * @return The menu added to the table
	 */
	private Menu createRightClickMenu() {
		Shell shell = composite.getShell();
		Menu menu = new Menu( shell , SWT.POP_UP );
	
		MenuItem item;
		final MenuItem stateItem;
	
		/* change the menu for bittorrent (doesnt have servers) */
		if ( ( ( NetworkInfo ) cLabel.getData() ).hasServers() ) {
		
			/* disconnect Server */
			item = new MenuItem( menu, SWT.PUSH );
			item.setText( "manage server" );
			item.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent e ) {
					//TODO open servertab
				}
			} );
	
			new MenuItem( menu, SWT.SEPARATOR );
		}
	
		/* disable/enable the network */
		stateItem = new MenuItem( menu, SWT.PUSH );
		stateItem.setText( "enable" );
		stateItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				NetworkInfo networkInfo = ( NetworkInfo ) anotherCLabel.getData();
				networkInfo.setEnabled();
			}
		} );
			
		/* disable or enable? */
		menu.addListener( SWT.Show, new Listener () {
			public void handleEvent( Event event ) {
				NetworkInfo networkInfo = ( NetworkInfo ) anotherCLabel.getData();
				if ( networkInfo.isEnabled() )
					stateItem.setText( "Disable" );
				else
					stateItem.setText( "Enable" );						
			}
		} );
		return menu;
	}
}

/*
$Log: NetworkItem.java,v $
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