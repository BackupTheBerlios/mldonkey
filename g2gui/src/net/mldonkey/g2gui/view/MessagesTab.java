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
import java.util.Iterator;
import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.ClientMessage;
import net.mldonkey.g2gui.view.console.Console;
import net.mldonkey.g2gui.view.friends.FriendsTableContentProvider;
import net.mldonkey.g2gui.view.friends.FriendsTableLabelProvider;
import net.mldonkey.g2gui.view.friends.FriendsTableMenuListener;
import net.mldonkey.g2gui.view.friends.FriendsTableSorter;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.CustomTableViewer;

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


/**
 *
 * @version $Id: MessagesTab.java,v 1.35 2003/11/26 07:42:22 zet Exp $
 */
public class MessagesTab extends GuiTab {
    private CoreCommunication core;
    private CTabFolder cTabFolder;
    private Hashtable openTabs = new Hashtable();
    private CustomTableViewer tableViewer;
    private CLabel friendsCLabel;
    private CLabel messagesCLabel;

    public MessagesTab(MainTab gui) {
        super(gui);

        /* associate this tab with the corecommunication */
        this.core = gui.getCore();
        createButton("MessagesButton");

        // core.getClientInfoIntMap().addObserver(this);
        createContents(this.subContent);
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.GuiTab#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected void createContents(Composite parent) {
        SashForm main = new SashForm(parent, SWT.HORIZONTAL);
        createLeftSash(main);
        createRightSash(main);
        core.addObserver(this);
        main.setWeights(new int[] { 1, 5 });
    }

    /**
     * @param main
     */
    private void createLeftSash(Composite main) {
        // what will this become on other networks?
        // can the concept of a friend be generalized? 
        // obviously we want to list their files, and what else?
        // simple and for messaging only atm 
        ViewForm friendsViewForm = WidgetFactory.createViewForm(main);
        Composite friendsComposite = new Composite(friendsViewForm, SWT.NONE);
        friendsComposite.setLayout(new FillLayout());
        friendsCLabel = WidgetFactory.createCLabel(friendsViewForm, "FR_FRIENDS",
                "MessagesButtonSmall");
        createFriendsTable(friendsComposite);
        friendsViewForm.setTopLeft(friendsCLabel);
        friendsViewForm.setContent(friendsComposite);
    }

    /**
     * @param parent
     */
    public void createFriendsTable(Composite parent) {
        tableViewer = new CustomTableViewer(parent, SWT.NONE | SWT.MULTI);
        tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        tableViewer.getTable().setHeaderVisible(false);
        tableViewer.setContentProvider(new FriendsTableContentProvider(friendsCLabel));
        tableViewer.setLabelProvider(new FriendsTableLabelProvider());

        FriendsTableMenuListener tableMenuListener = new FriendsTableMenuListener(tableViewer,
                core, this);
        tableViewer.addSelectionChangedListener(tableMenuListener);

        MenuManager popupMenu = new MenuManager("");
        popupMenu.setRemoveAllWhenShown(true);
        popupMenu.addMenuListener(tableMenuListener);

        tableViewer.getTable().setMenu(popupMenu.createContextMenu(tableViewer.getTable()));
        tableViewer.setSorter(new FriendsTableSorter());
        tableViewer.setInput(core.getClientInfoIntMap().getFriendsWeakMap());

        /*add default-action to menu (hack, but i didn't find this is menuManager etc.)*/
        Menu menu = tableViewer.getTable().getMenu();
        menu.addMenuListener(new MenuListener() {
                public void menuHidden(MenuEvent e) {
                }

                public void menuShown(MenuEvent e) {
                    Menu aMenu = tableViewer.getTable().getMenu();
                    MenuItem[] items = aMenu.getItems();
                    aMenu.setDefaultItem(items[ (items.length - 1) ]);
                }
            });

        /*add the double-click handler to open message-window on double-click*/
        tableViewer.getTable().addMouseListener(new MouseListener() {
                public void mouseDoubleClick(MouseEvent e) {
                    TableItem[] currentItems = tableViewer.getTable().getSelection();

                    for (int i = 0; i < currentItems.length; i++) {
                        ClientInfo selectedClientInfo = (ClientInfo) currentItems[ i ].getData();
                        openTab(selectedClientInfo);
                    }
                }

                public void mouseDown(MouseEvent e) {
                }

                public void mouseUp(MouseEvent e) {
                }
            });
    }

    /**
     * @param main
     */
    private void createRightSash(Composite main) {
        ViewForm messagesViewForm = WidgetFactory.createViewForm(main);
        messagesCLabel = WidgetFactory.createCLabel(messagesViewForm, "FR_TABS",
                "MessagesButtonSmall");

        ToolBar messagesToolBar = new ToolBar(messagesViewForm, SWT.RIGHT | SWT.FLAT);
        ToolItem sendItem = new ToolItem(messagesToolBar, SWT.NONE);
        sendItem.setToolTipText(G2GuiResources.getString("FR_TABS_CLOSE_TOOLTIP"));
        sendItem.setImage(G2GuiResources.getImage("X"));
        sendItem.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    closeAllTabs();
                }
            });
        cTabFolder = new CTabFolder(messagesViewForm, SWT.NONE);
        messagesViewForm.setTopLeft(messagesCLabel);
        messagesViewForm.setTopRight(messagesToolBar);
        messagesViewForm.setContent(cTabFolder);
        cTabFolder.setBorderVisible(false);
        cTabFolder.setLayoutData(new FillLayout());
        cTabFolder.setSelectionBackground(new Color[] {
                cTabFolder.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND),
                cTabFolder.getBackground()
            }, new int[] { 75 });
        cTabFolder.setSelectionForeground(cTabFolder.getDisplay().getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
        cTabFolder.addCTabFolderListener(new CTabFolderAdapter() {
                public void itemClosed(CTabFolderEvent event) {
                    CTabItem item = (CTabItem) event.item;

                    //Console console = (Console) item.getData("console");
                    Composite consoleComposite = (Composite) item.getData("composite");
                    Integer id = (Integer) item.getData("id");
                    openTabs.remove(id);
                    consoleComposite.dispose();
                    item.dispose();
                    setTabsLabel();
                }
            });

        // set the focus to the input line of the console when 
        // an item is selected
        cTabFolder.addSelectionListener(new SelectionListener() {
                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    CTabItem cTabItem = (CTabItem) e.item;
                    Console console = (Console) cTabItem.getData("console");
                    setTabsLabel();
                    console.setFocus();
                }
            });
    }

    public void closeAllTabs() {
        Iterator iterator = openTabs.keySet().iterator();

        while (iterator.hasNext()) {
            Integer id = (Integer) iterator.next();
            CTabItem cTabItem = (CTabItem) openTabs.get(id);
            Composite consoleComposite = (Composite) cTabItem.getData("composite");
            consoleComposite.dispose();
            cTabItem.dispose();
        }

        openTabs.clear();
        setTabsLabel();
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(final Observable arg0, final Object arg1) {
        if (arg1 instanceof ClientMessage) {
            if (!cTabFolder.isDisposed())
                cTabFolder.getDisplay().asyncExec(new Runnable() {
                        public void run() {
                            messageFromClient((ClientMessage) arg1);
                        }
                    });

        } else if (arg0 instanceof Console)
            messageToClient((Console) arg0, (String) arg1);
    }

    

    public void setTabsLabel() {
        String extra = "";

        if (cTabFolder.getSelection() != null)
            extra = " -> " + cTabFolder.getSelection().getText();

        messagesCLabel.setText(G2GuiResources.getString("FR_TABS") + ": " + openTabs.size() +
            extra);
    }

    /**
     *
     * @param console
     * @param messageText
     */
    public void messageToClient(Console console, String messageText) {
        ClientMessage.sendMessage(core, console.getClientId(), messageText);
    }

    /**
     * @param id
     * @param textMessage
     */
    public void sendTabMessage(int id, String textMessage) {
        CTabItem cTabItem = (CTabItem) openTabs.get(new Integer(id));
        Console console = (Console) cTabItem.getData("console");
        console.append(textMessage + console.getLineDelimiter());
    }

    /**
     * @param message
     */
    public void messageFromClient(ClientMessage message) {
        mainWindow.getStatusline().update("New message!");

        if (openTabs.containsKey(new Integer(message.getId()))) {
            String textMessage;
            ClientInfo clientInfo = core.getClientInfoIntMap().get(message.getId());

            if (clientInfo == null)
                textMessage = getTimeStamp() + message.getId() + ": <unknown>> " +
                    message.getText();
            else
                textMessage = getTimeStamp() + message.getId() + ": " + clientInfo.getClientName() +
                    "> " + message.getText();

            sendTabMessage(message.getId(), textMessage);
        } else {
            // the core sends the clientInfo for this message's clientID AFTER
            // the message itself.. not very smart.  So, neither is this curious loop.
            // TODO: does this help at all? remove if not.
            ClientInfo clientInfo = null;

            for (int i = 0;
                    (i < 6) &&
                    ((clientInfo = core.getClientInfoIntMap().get(message.getId())) == null);
                    i++)
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

            String tabText;

            if (clientInfo == null)
                tabText = "" + message.getId() + ": <unknown>";
            else
                tabText = "" + clientInfo.getClientid() + ": " + clientInfo.getClientName();

            String textMessage = getTimeStamp() + tabText + "> " + message.getText();
            CTabItem cTabItem = addCTabItem(message.getId(), "  " + tabText);

            if (cTabFolder.getItemCount() == 1)
                setItemFocus(cTabItem);

            sendTabMessage(message.getId(), textMessage);
            setTabsLabel();
        }
    }

    /**
     * @param id
     * @param tabText
     *
     * @return tabItem
     */
    public CTabItem addCTabItem(int id, String tabText) {
        CTabItem tabItem = new CTabItem(cTabFolder, SWT.NONE);
        tabItem.setText(tabText);

        Composite consoleComposite = new Composite(cTabFolder, SWT.BORDER);
        consoleComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        Console console = new Console(consoleComposite, SWT.WRAP);
        setPreferences(console);
        console.setClientId(id);
        console.addObserver(this);
        tabItem.setControl(consoleComposite);
        tabItem.setData("id", new Integer(id));
        tabItem.setData("composite", consoleComposite);
        tabItem.setData("console", console);
        openTabs.put(new Integer(id), tabItem);

        return tabItem;
    }

    /**
     * @param clientInfo
     */
    public void openTab(ClientInfo clientInfo) {
        if (!openTabs.containsKey(new Integer(clientInfo.getClientid()))) {
            String tabText = "  " + clientInfo.getClientid() + ": " + clientInfo.getClientName();
            setItemFocus(addCTabItem(clientInfo.getClientid(), tabText));
        } else
            cTabFolder.setSelection((CTabItem) openTabs.get(new Integer(clientInfo.getClientid())));

        setTabsLabel();
    }

    /**
     * @return String timestamp
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

        if (cTabFolder.getSelection() != null)
            setItemFocus(cTabFolder.getSelection());
    }

    /**
     * @param console
     */
    public void setPreferences(Console console) {
        console.setDisplayFont(PreferenceLoader.loadFont("consoleFontData"));
        console.setInputFont(PreferenceLoader.loadFont("consoleFontData"));
        console.setHighlightColor(PreferenceLoader.loadColour("consoleHighlight"));
        console.setDisplayBackground(PreferenceLoader.loadColour("consoleBackground"));
        console.setDisplayForeground(PreferenceLoader.loadColour("consoleForeground"));
        console.setInputBackground(PreferenceLoader.loadColour("consoleInputBackground"));
        console.setInputForeground(PreferenceLoader.loadColour("consoleInputForeground"));
    }
}


/*
$Log: MessagesTab.java,v $
Revision 1.35  2003/11/26 07:42:22  zet
small changes

Revision 1.34  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.33  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.32  2003/11/04 20:38:27  zet
update for transparent gifs

Revision 1.31  2003/10/22 01:36:59  zet
add column selector to server/search (might not be finished yet..)

Revision 1.30  2003/09/20 14:39:48  zet
move transfer package

Revision 1.29  2003/09/20 01:26:36  zet
*** empty log message ***

Revision 1.28  2003/09/18 09:44:57  lemmster
checkstyle

Revision 1.27  2003/09/03 14:49:07  zet
optionally spawn core from gui

Revision 1.26  2003/09/01 17:04:20  zet
*** empty log message ***

Revision 1.25  2003/08/30 23:38:14  zet
show selected tabtext in tab header

Revision 1.24  2003/08/30 14:25:57  zet
multi select

Revision 1.23  2003/08/29 23:34:14  zet
close all tabs

Revision 1.22  2003/08/29 22:11:47  zet
add CCLabel helper class

Revision 1.21  2003/08/29 19:33:24  zet
color

Revision 1.20  2003/08/29 19:30:50  zet
font colour

Revision 1.19  2003/08/29 17:33:20  zet
remove headerbar

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
