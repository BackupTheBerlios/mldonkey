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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.helper.DownloadSubmit;
import net.mldonkey.g2gui.view.StatusLine;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.UniformResourceLocator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.REMatch;

import java.util.List;
import java.util.ArrayList;

/**
 * LinkEntry
 *
 * @version $Id: LinkEntry.java,v 1.22 2004/03/02 23:39:29 psy Exp $
 *
 */
public class LinkEntry {
    private CoreCommunication core;
    private StatusLine statusLine;

    /**
     * @param statusLine
     * @param core
     * @param parent
     */
    public LinkEntry(StatusLine statusLine, CoreCommunication core, Composite parent) {
        this.statusLine = statusLine;
        this.core = core;
        createContents(parent);
    }

    /**
     * @param parent
     */
    public void createContents(Composite parent) {
        ViewForm linkEntryViewForm = WidgetFactory.createViewForm(parent);
        linkEntryViewForm.setLayoutData(new GridData(GridData.FILL_BOTH));

        CLabel linkEntryCLabel = WidgetFactory.createCLabel(linkEntryViewForm, "LE_HEADER",
                "UpArrowGreen");

        final Text linkEntryText = new Text(linkEntryViewForm, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
        linkEntryText.setLayoutData(new FillLayout());
        linkEntryText.setFont(PreferenceLoader.loadFont("consoleFontData"));
        linkEntryText.setForeground(PreferenceLoader.loadColour("consoleInputForeground"));
        linkEntryText.setBackground(PreferenceLoader.loadColour("consoleInputBackground"));

        linkEntryText.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if ((e.stateMask & SWT.CTRL) != 0) {
                        if ((e.character == SWT.LF) || (e.character == SWT.CR)) {
                            enterLinks(linkEntryText);
                        }
                    }
                }
            });

        ToolBar linkEntryToolBar = new ToolBar(linkEntryViewForm, SWT.RIGHT | SWT.FLAT);
        ToolItem sendItem = new ToolItem(linkEntryToolBar, SWT.NONE);
        sendItem.setText(G2GuiResources.getString("LE_BUTTON"));
        sendItem.setImage(G2GuiResources.getImage("UpArrowGreen"));
        sendItem.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    enterLinks(linkEntryText);
                }
            });

        linkEntryViewForm.setTopLeft(linkEntryCLabel);
        linkEntryViewForm.setContent(linkEntryText);
        linkEntryViewForm.setTopRight(linkEntryToolBar);

        if (SWT.getPlatform().equals("win32") && PreferenceLoader.loadBoolean("dragAndDrop")) {
            activateDropTarget(linkEntryText);
        }
    }

    /**
     * @param linkEntryText
     */
    public void enterLinks(Text linkEntryText) {
        String input = linkEntryText.getText();
        List linkList = new ArrayList();
        
        RE regex = null;
        try {
            regex = new RE("(ed2k://\\|file\\|[^\\|]+\\|(\\d+)\\|([\\dabcdef]+)\\|)" +
                    "|(sig2dat:///?\\|File:[^\\|]+\\|Length:.+?\\|UUHash:\\=.+?\\=)" +
                    "|(\\\"magnet:\\?xt=.+?\\\")" + "|(magnet:\\?xt=.+?\n)" +
                    ((linkEntryText.getLineCount() == 1) ? "|(magnet:\\?xt=.+)" : "") +
                    ((linkEntryText.getLineCount() == 1) ? "|(http://.+?\\.torrent.+)" : "") +
                    "|(\"http://.+?\\.torrent\\?[^>]+\")" + "|(http://.+?\\.torrent)", RE.REG_ICASE);
        } catch (REException e) {
            e.printStackTrace();
        }

        /* with our prepared regex we now extract all links */
        REMatch[] matches = regex.getAllMatches(input);

        /* put all matches in a list */
        for (int i = 0; i < matches.length; i++) {
            String link = RegExp.replaceAll(matches[ i ].toString(), "\"", "");
            link = RegExp.replaceAll(link, "\n", "");
            linkList.add(link);
        }
        /* submit those links */
        new DownloadSubmit(linkList, core);
        
        statusLine.update(G2GuiResources.getString("LE_LINKS_SENT") + " " + matches.length);
        linkEntryText.setText("");
    }

    /**
     * @param linkEntryText
     */
    private void activateDropTarget(final Text linkEntryText) {
        DropTarget dropTarget = new DropTarget(linkEntryText,
                DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_LINK);
        final UniformResourceLocator uRL = UniformResourceLocator.getInstance();
        final TextTransfer textTransfer = TextTransfer.getInstance();
        dropTarget.setTransfer(new Transfer[] { uRL, textTransfer });
        dropTarget.addDropListener(new DropTargetAdapter() {
                public void dragEnter(DropTargetEvent event) {
                    event.detail = DND.DROP_COPY;

                    for (int i = 0; i < event.dataTypes.length; i++) {
                        if (uRL.isSupportedType(event.dataTypes[ i ])) {
                            event.detail = DND.DROP_LINK;

                            break;
                        }
                    }
                }

                public void drop(DropTargetEvent event) {
                    if (event.data == null) {
                        return;
                    }

                    linkEntryText.append((String) event.data);
                }
            });
    }
}


/*
$Log: LinkEntry.java,v $
Revision 1.22  2004/03/02 23:39:29  psy
replaced raw-socket link-submission

Revision 1.21  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.20  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.19  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.18  2003/10/17 03:36:43  zet
use toolbar

Revision 1.17  2003/10/16 20:40:49  zet
cr+lf

Revision 1.16  2003/10/16 20:24:53  zet
ctrl+cr

Revision 1.15  2003/09/26 04:19:22  zet
add dropTarget

Revision 1.14  2003/09/20 01:36:19  zet
*** empty log message ***

Revision 1.13  2003/09/18 15:30:17  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.12  2003/09/18 11:37:24  lemmy
checkstyle

Revision 1.11  2003/08/30 23:37:51  zet
use preference colors/fonts

Revision 1.10  2003/08/29 22:11:47  zet
add CCLabel helper class

Revision 1.9  2003/08/29 19:30:50  zet
font colour

Revision 1.8  2003/08/28 18:41:51  zet
cleanup

Revision 1.7  2003/08/28 18:27:32  zet
configurable flat interface

Revision 1.6  2003/08/28 18:05:28  zet
remove unused parent

Revision 1.5  2003/08/28 18:01:46  zet
remove button

Revision 1.4  2003/08/28 17:07:03  zet
gif not png

Revision 1.3  2003/08/28 16:07:48  zet
update linkentry

Revision 1.2  2003/08/26 21:14:45  zet
ignore case

Revision 1.1  2003/08/25 12:24:09  zet
Toggleable link entry.  It should parse links from pasted HTML as well.


*/
