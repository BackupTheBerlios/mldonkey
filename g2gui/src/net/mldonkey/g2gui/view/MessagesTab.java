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
import java.util.List;
import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.ClientInfoIntMap;
import net.mldonkey.g2gui.model.ClientMessage;
import net.mldonkey.g2gui.view.console.Console;
import net.mldonkey.g2gui.view.friends.TableContentProvider;
import net.mldonkey.g2gui.view.friends.TableLabelProvider;
import net.mldonkey.g2gui.view.friends.TableMenuListener;
import net.mldonkey.g2gui.view.friends.TableSorter;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class MessagesTab extends GuiTab {

	private CoreCommunication core;
	
	private CTabFolder cTabFolder;
	private Hashtable openTabs = new Hashtable();
	private Composite friendsComposite;
	private TableViewer tableViewer;
	/**
	 * @param gui
	 */
	public MessagesTab ( MainTab gui ) {
		super( gui );
		/* associate this tab with the corecommunication */
		this.core = gui.getCore();
		
		createButton("MessagesButton", 
			bundle.getString("TT_MessagesButton"),
			bundle.getString("TT_MessagesButtonToolTip"));
		
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
		friendsComposite = new Composite( main, SWT.BORDER );
		friendsComposite.setLayout( new FillLayout() );
		
		createFriendsList();
	}

	public void createFriendsList() {
		List friendsArray = core.getClientInfoIntMap().getFriendsList();
		tableViewer = new TableViewer(friendsComposite, SWT.NONE);

		tableViewer.setContentProvider(new TableContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());
		
		TableMenuListener tableMenuListener = new TableMenuListener( tableViewer, core, this );
		tableViewer.addSelectionChangedListener( tableMenuListener );
		MenuManager popupMenu = new MenuManager( "" );
		popupMenu.setRemoveAllWhenShown( true );
		popupMenu.addMenuListener( tableMenuListener );
		tableViewer.getTable().setMenu( popupMenu.createContextMenu( tableViewer.getTable() ) );
		tableViewer.setSorter(new TableSorter());
		
		tableViewer.setInput(friendsArray);
		setRightLabel();
		
	}

	/**
	 * @param main
	 */
	private void createRightSash( Composite main ) {
		SashForm messagesSash = new SashForm ( main, SWT.HORIZONTAL );
		messagesSash.setLayout( new FillLayout() );
			
		cTabFolder = new CTabFolder( messagesSash, SWT.NONE );
		cTabFolder.setBorderVisible(true);
		cTabFolder.setLayoutData( new FillLayout() );
		Display display = cTabFolder.getDisplay();
		cTabFolder.setSelectionBackground(new Color[]{display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND),
											cTabFolder.getBackground() },
							 			 	new int[] {75});
		
		cTabFolder.addCTabFolderListener( new CTabFolderAdapter() {
			public void itemClosed( CTabFolderEvent event ) {
				CTabItem item = ( CTabItem ) event.item;
				Console console = (Console) item.getData("console");
				Composite consoleComposite = (Composite) item.getData("composite");
				Integer id = (Integer) item.getData("id");
				openTabs.remove(id);
				consoleComposite.dispose();
				item.dispose();
				setRightLabel();
			}
		} );
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(final Observable arg0, final Object arg1) {
		if (arg1 instanceof ClientMessage
			|| arg0 instanceof ClientInfoIntMap) {
			Shell shell = MainTab.getShell();
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
	
	public void runUpdate(Observable arg0, Object arg1) {
		if (arg1 instanceof ClientMessage) {
			messageFromClient((ClientMessage) arg1);
		} else if (arg0 instanceof ClientInfoIntMap) {
			tableViewer.refresh();
			setRightLabel();
		}
		
	}
	
	public void setRightLabel() {
		setRightLabel("Friends: " + tableViewer.getTable().getItemCount() + ", Tabs: " + openTabs.size());
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
	
	
	public void messageFromClient(ClientMessage message) {
		
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
			else tabText = "  " + clientInfo.getClientid() + ": " + clientInfo.getClientName();
			
			String textMessage = getTimeStamp() + tabText + "> " + message.getText();		
			
			addCTabItem(message.getId(), tabText);
			sendTabMessage(message.getId(), textMessage);
		}
	}
	
	public void addCTabItem(int id, String tabText) {
		
		CTabItem tabItem = new CTabItem(cTabFolder, SWT.NONE);
		tabItem.setText(tabText);
		Composite consoleComposite = new Composite(cTabFolder, SWT.BORDER);
		consoleComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		Console console = new Console(consoleComposite, SWT.WRAP );
		console.setDisplayFont( PreferenceLoader.loadFont( "consoleFontData" ) );
		console.setDisplayBackground ( PreferenceLoader.loadColour( "consoleBackground" ) );
		console.setDisplayForeground ( PreferenceLoader.loadColour( "consoleForeground" ) );
		console.setInputBackground ( PreferenceLoader.loadColour( "consoleInputBackground" ) );
		console.setInputForeground ( PreferenceLoader.loadColour( "consoleInputForeground" ) );
		console.setClientId(id);
		console.addObserver( this );
		tabItem.setControl( consoleComposite );
		tabItem.setData("id", new Integer(id));
		tabItem.setData("composite", consoleComposite);
		tabItem.setData("console", console);
		openTabs.put(new Integer(id), tabItem);
		setRightLabel();
	}
	
	public void openTab(ClientInfo clientInfo) {
		if (!openTabs.containsKey(new Integer(clientInfo.getClientid())))  {
			String tabText = "  " + clientInfo.getClientid() + ": " + clientInfo.getClientName();
				addCTabItem(clientInfo.getClientid(), tabText);
		}
	}

	public String getTimeStamp() {
		SimpleDateFormat sdFormatter = new SimpleDateFormat("[HH:mm:ss] ");
		Date oToday = new Date();
		return sdFormatter.format(oToday);
	}

}
/*
$Log: MessagesTab.java,v $
Revision 1.1  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging


*/