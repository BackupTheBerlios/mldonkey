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
 * ( at your option ) any later version.
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
package net.mldonkey.g2gui.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.ClientInfoIntMap;
import net.mldonkey.g2gui.model.ClientMessage;
import net.mldonkey.g2gui.view.console.Console;
import net.mldonkey.g2gui.view.friends.FriendsTableContentProvider;
import net.mldonkey.g2gui.view.friends.FriendsTableLabelProvider;
import net.mldonkey.g2gui.view.friends.FriendsTableMenuListener;
import net.mldonkey.g2gui.view.friends.FriendsTableSorter;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transferTree.CustomTableViewer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

/**
 *
 * @version $Id: MessagesTab.java,v 1.18 2003/08/29 00:55:02 zet Exp $
 */
public class MessagesTab extends GuiTab implements Runnable {

	private CoreCommunication core;
	private CTabFolder cTabFolder;
	private Hashtable openTabs = new Hashtable();
	private CustomTableViewer tableViewer;
	private long lastTime = 0;
	private int mustRefresh = 0;
	/**
	 * @param gui
	 */
	public MessagesTab ( MainTab gui ) {
		super( gui );
		/* associate this tab with the corecommunication */
		this.core = gui.getCore();
		
		createButton("MessagesButton", 
			G2GuiResources.getString("TT_MessagesButton"),
			G2GuiResources.getString("TT_MessagesButtonToolTip"));
		
		core.getClientInfoIntMap().addObserver(this);
		createContents(this.subContent);
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.GuiTab#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected void createContents(Composite parent) {
		SashForm main = new SashForm( parent, SWT.HORIZONTAL );
		
		createLeftSash( main );
		createRightSash( main );
		core.addObserver( this );
		main.setWeights( new int[] {1,5});
		
	}
	/**
	 * @param main
	 */
	// what will this become on other networks?
	// can the concept of a friend be generalized? 
	// obviously we want to list their files, and what else?
	// simple and for messaging only atm 
	private void createLeftSash( Composite main ) {
		
		ViewForm friendsViewForm = new ViewForm( main, SWT.BORDER | (PreferenceLoader.loadBoolean("flatInterface") ? SWT.FLAT : SWT.NONE) );
		
		Composite friendsComposite = new Composite( friendsViewForm, SWT.NONE );
		friendsComposite.setLayout( new FillLayout() );
		
		CLabel friendsHeaderLabel = new CLabel(friendsViewForm, SWT.LEFT );
		friendsHeaderLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		friendsHeaderLabel.setBackground(new Color[]{friendsViewForm.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND),
												friendsViewForm.getBackground()},
												new int[] {100});
										
		friendsHeaderLabel.setText(G2GuiResources.getString("FR_FRIENDS"));
		friendsHeaderLabel.setImage(G2GuiResources.getImage("MessagesButtonSmallTrans"));
		createFriendsTable(friendsComposite);
		
		friendsViewForm.setTopLeft(friendsHeaderLabel);
		friendsViewForm.setContent(friendsComposite);
	}

	/**
	 * 
	 */
	public void createFriendsTable( Composite parent ) {
		tableViewer = new CustomTableViewer( parent , SWT.NONE);
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tableViewer.getTable().setHeaderVisible( false );
		tableViewer.setContentProvider(new FriendsTableContentProvider());
		tableViewer.setLabelProvider(new FriendsTableLabelProvider());
		
		FriendsTableMenuListener tableMenuListener = new FriendsTableMenuListener( tableViewer, core, this );
		tableViewer.addSelectionChangedListener( tableMenuListener );
		MenuManager popupMenu = new MenuManager( "" );
		popupMenu.setRemoveAllWhenShown( true );
		popupMenu.addMenuListener( tableMenuListener );			
		tableViewer.getTable().setMenu( popupMenu.createContextMenu( tableViewer.getTable() ) );
		tableViewer.setSorter(new FriendsTableSorter());
				
		tableViewer.setInput(core.getClientInfoIntMap().getFriendsList());
		setRightLabel();
		
		
		/*add default-action to menu (hack, but i didn't find this is 
		 * menuManager etc.)*/
		Menu menu = tableViewer.getTable().getMenu();
		menu.addMenuListener(new MenuListener(){
			public void menuHidden(MenuEvent e) {}

			public void menuShown(MenuEvent e) {
				Menu menu = tableViewer.getTable().getMenu();
				MenuItem[] items = menu.getItems();
				menu.setDefaultItem(items [( items.length - 1) ] );				
			}
			});
			
		/*add the double-click handler to open message-window on double-click*/
		tableViewer.getTable().addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e) {
				TableItem[] currentItems = tableViewer.getTable().getSelection();
								
				for ( int i = 0; i < currentItems.length;	 i ++ ) {					
					ClientInfo selectedClientInfo = ( ClientInfo ) currentItems[ i ].getData();
					openTab(selectedClientInfo);
					
				}
			}
			public void mouseDown(MouseEvent e) {}
			public void mouseUp(MouseEvent e) {}
			});
	}

	/**
	 * @param main
	 */
	private void createRightSash( Composite main ) {
		SashForm messagesSash = new SashForm ( main, SWT.HORIZONTAL );
		messagesSash.setLayout( new FillLayout() );
			
		cTabFolder = new CTabFolder( messagesSash, (PreferenceLoader.loadBoolean("flatInterface") ? SWT.FLAT : SWT.NONE) );
		cTabFolder.setBorderVisible(true);
		cTabFolder.setLayoutData( new FillLayout() );
		Display display = cTabFolder.getDisplay();
		cTabFolder.setSelectionBackground(new Color[]{display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND),
											cTabFolder.getBackground() },
							 			 	new int[] {75});
		
		cTabFolder.addCTabFolderListener( new CTabFolderAdapter() {
		public void itemClosed( CTabFolderEvent event ) {
				CTabItem item = ( CTabItem ) event.item;
				//Console console = (Console) item.getData("console");
				Composite consoleComposite = (Composite) item.getData("composite");
				Integer id = (Integer) item.getData("id");
				openTabs.remove(id);
				consoleComposite.dispose();
				item.dispose();
				setRightLabel();
			}
		} );
		
		// set the focus to the input line of the console when 
		// an item is selected
		cTabFolder.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			
			public void widgetSelected(SelectionEvent e) {
				CTabItem cTabItem = (CTabItem) e.item;
				Console console = (Console) cTabItem.getData("console");
				console.setFocus();
			}
		});
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(final Observable arg0, final Object arg1) {
		if (arg1 instanceof ClientMessage
			|| arg0 instanceof ClientInfoIntMap) {
			Shell shell = this.mainWindow.getShell();
			if(!shell.isDisposed() && shell !=null && shell.getDisplay()!=null) {
				shell.getDisplay().asyncExec(new Runnable() {
					public void run() {
						runUpdate(arg0, arg1);
					}
				});
				
			}
		} else if (arg0 instanceof Console) {
			messageToClient((Console) arg0, (String) arg1);
		} 
	}
	
	/**
	 * @param arg0
	 * @param arg1
	 */
	public void runUpdate(Observable arg0, Object arg1) {
		if (arg1 instanceof ClientMessage) {
			messageFromClient((ClientMessage) arg1);
		} else if (arg0 instanceof ClientInfoIntMap) {
			mustRefresh++;
			if (System.currentTimeMillis() > lastTime + 2000) {
				this.run();
			} else {
				if (mustRefresh == 1) {
					cTabFolder.getDisplay().timerExec(2500, this);
				}
			}
		}
	}
	
	public void run() {
		if (!cTabFolder.isDisposed()) {
			lastTime = System.currentTimeMillis();
			tableViewer.refresh();
			setRightLabel();
			mustRefresh=0;
		}
	}
	
	/**
	 * 
	 */
	public void setRightLabel() {
		setRightLabel(G2GuiResources.getString("FR_FRIENDS") + ": " + tableViewer.getTable().getItemCount() 
				+ ", " + G2GuiResources.getString("FR_TABS") + ": " + openTabs.size());
	}
	
	/**
	 * @param console
	 * @param messageText
	 */
	public void messageToClient(Console console, String messageText) {
		ClientMessage.sendMessage(core, console.getClientId(), messageText);
	}

	/**
	 * @param message
	 */
	
	public void sendTabMessage(int id, String textMessage) {
		ClientInfo clientInfo = core.getClientInfoIntMap().get(id);
		CTabItem cTabItem = (CTabItem) openTabs.get(new Integer(id));
		Console console = (Console) cTabItem.getData("console");
		console.append(textMessage + console.getLineDelimiter());
	}
	
	/**
	 * @param message
	 */
	public void messageFromClient(ClientMessage message) {
		mainWindow.getStatusline().update( "New message!" );
		
		if (openTabs.containsKey(new Integer(message.getId()))) {
			String textMessage;
			ClientInfo clientInfo = core.getClientInfoIntMap().get(message.getId());
			if (clientInfo == null) // Why don't we have clientInfo for these?
				textMessage = getTimeStamp() + message.getId() +  ": <unknown>> " + message.getText();
			else
				textMessage = getTimeStamp() + message.getId() +  ": " + clientInfo.getClientName() + "> " + message.getText();		
				sendTabMessage(message.getId(), textMessage );
			
		} else {
			
			// the core sends the clientInfo for this message's clientID AFTER
			// the message itself.. not very smart.  So, neither is this curious loop.
			// TODO: does this help at all? remove if not.
			
			ClientInfo clientInfo = null;
			
			for (int i = 0; (i < 6) && ( (clientInfo = core.getClientInfoIntMap().get(message.getId())) == null); i++)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
			
			String tabText;
			if (clientInfo == null) tabText = "" + message.getId() + ": <unknown>"; 
			else tabText = "" + clientInfo.getClientid() + ": " + clientInfo.getClientName();
			
			String textMessage = getTimeStamp() + tabText + "> " + message.getText();		
			
			CTabItem cTabItem = addCTabItem(message.getId(), "  " + tabText);

			if (cTabFolder.getItemCount() == 1) {
				setItemFocus(cTabItem);
			}

			sendTabMessage(message.getId(), textMessage);
		}
	}
	
	/**
	 * @param id
	 * @param tabText
	 * @return
	 */
	public CTabItem addCTabItem(int id, String tabText) {
		
		CTabItem tabItem = new CTabItem(cTabFolder, SWT.NONE);
		tabItem.setText(tabText);
		Composite consoleComposite = new Composite(cTabFolder, SWT.BORDER);
		consoleComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		Console console = new Console(consoleComposite, SWT.WRAP );
		setPreferences(console);
		console.setClientId(id);
		console.addObserver( this );
		tabItem.setControl( consoleComposite );
		tabItem.setData("id", new Integer(id));
		tabItem.setData("composite", consoleComposite);
		tabItem.setData("console", console);
		openTabs.put(new Integer(id), tabItem);
		setRightLabel();
		return tabItem;
	}
	
	/**
	 * @param clientInfo
	 */
	public void openTab(ClientInfo clientInfo) {
		if (!openTabs.containsKey(new Integer(clientInfo.getClientid())))  {
			String tabText = "  " + clientInfo.getClientid() + ": " + clientInfo.getClientName();
			setItemFocus(addCTabItem(clientInfo.getClientid(), tabText));
		} else {
			cTabFolder.setSelection((CTabItem) openTabs.get(new Integer(clientInfo.getClientid())));
		}
	}

	/**
	 * @return
	 */
	public String getTimeStamp() {
		SimpleDateFormat sdFormatter = new SimpleDateFormat("[HH:mm:ss] ");
		Date oToday = new Date();
		return sdFormatter.format(oToday);
	}
	
	/**
	 * @param cTabItem
	 */
	public void setItemFocus(CTabItem cTabItem) {
		cTabFolder.setSelection(cTabItem);
		Console console = (Console) cTabItem.getData("console");
		console.setFocus();
	}
	
	public void setActive() {
		super.setActive();
		if (cTabFolder.getSelection() != null) {
			setItemFocus(cTabFolder.getSelection());
		}
	}	
	
	/**
	 * @param console
	 */
	public void setPreferences(Console console) {
		console.setDisplayFont( PreferenceLoader.loadFont( "consoleFontData" ) );
		console.setInputFont( PreferenceLoader.loadFont( "consoleFontData" ) );
		console.setHighlightColor( PreferenceLoader.loadColour( "consoleHighlight" ) );
		console.setDisplayBackground ( PreferenceLoader.loadColour( "consoleBackground" ) );
		console.setDisplayForeground ( PreferenceLoader.loadColour( "consoleForeground" ) );
		console.setInputBackground ( PreferenceLoader.loadColour( "consoleInputBackground" ) );
		console.setInputForeground ( PreferenceLoader.loadColour( "consoleInputForeground" ) );
	}

}
/*
$Log: MessagesTab.java,v $
Revision 1.18  2003/08/29 00:55:02  zet
change timer

Revision 1.17  2003/08/28 23:00:07  zet
remove unused

Revision 1.16  2003/08/28 18:27:32  zet
configurable flat interface

Revision 1.15  2003/08/28 15:37:36  zet
remove ctabfolder

Revision 1.14  2003/08/26 15:37:32  zet
dispose composite

Revision 1.13  2003/08/25 23:04:02  zet
update statusline on incoming msg

Revision 1.12  2003/08/25 21:18:43  zet
localise/update friendstab

Revision 1.11  2003/08/23 15:21:37  zet
remove @author

Revision 1.10  2003/08/23 14:58:38  lemmster
cleanup of MainTab, transferTree.* broken

Revision 1.9  2003/08/23 09:47:52  lemmster
just rename

Revision 1.8  2003/08/22 21:06:48  lemmster
replace $user$ with $Author: zet $

Revision 1.7  2003/08/20 22:18:56  zet
Viewer updates

Revision 1.6  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.5  2003/08/18 06:00:01  zet
fix null pointer (I'm not even sure it is real..)

Revision 1.4  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.3  2003/08/17 10:11:03  dek
double-click on friend opens window for friend

Revision 1.2  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.1  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging


*/