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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.toolbar.ToolButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;


/**
 * G2guiTab
 *
 *
 * @version $Id: GuiTab.java,v 1.42 2003/11/29 19:10:24 zet Exp $
 *
 */
public abstract class GuiTab implements Listener, Observer {
    /**
     * whether this tab is active
     */
    private boolean active;

    /**
     * The parent Composite
     */
    protected Composite contentComposite;

    /**
     * The main Window
     */
    protected MainWindow mainWindow;

    /**
     * The ToolItem
     */
    protected ToolButton toolButton;

    /**
     * A list of this tab's contained ViewFrames
     */
    protected List viewFrameList;

    /**
     * @param mainWindow
     * @param resButtonString
     */
    public GuiTab(MainWindow mainWindow, String resButtonString) {
        this.mainWindow = mainWindow;

        this.contentComposite = new Composite(mainWindow.getPageContainer(), SWT.NONE);
        this.contentComposite.setLayout(new FillLayout());
        this.contentComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        this.contentComposite.setVisible(false);

        this.createButton(resButtonString);

        this.mainWindow.registerTab(this);

        createContents(this.contentComposite);
    }

    /**
     * Creates the content for the tab
     * @param parent The parent composite
     */
    protected abstract void createContents(Composite parent);

    /**
     * @return The text to display in the statusline for this tab
     */
    public String getStatusText() {
        return "";
    }

    /**
     * @return The tooltip to display in the statusline for this tab
     */
    public String getStatusTip() {
        return "";
    }

    /**
     * To call if the MainWindow dispose
     */
    public void dispose() {
        this.mainWindow.getCore().deleteObserver(this);
    }

    /**
     * is called from the gui, when this tab is set to background
     * (because another tab was activated)
     */
    public void setInActive() {
        this.active = false;
        this.toolButton.setActive(false);
        toggleAllViewFramesActive(false);
    }

    /**
     * is called when this tab is set to foreground
     */
    public void setActive() {
        this.active = true;
        this.mainWindow.setActive(this);
        this.toolButton.setActive(true);
        toggleAllViewFramesActive(true);

        /* does the mainwindow already have a statusline */
        if (this.mainWindow.getStatusline() != null) {
            this.mainWindow.getStatusline().update(this.getStatusText());
            this.mainWindow.getStatusline().updateToolTip(this.getStatusTip());
        }
    }

    /**
     * @param true if active, false otherwise
     */
    public void toggleAllViewFramesActive(boolean b) {
        if (viewFrameList == null) return;
        for (int i = 0; i < viewFrameList.size(); ++i) {
            ViewFrame viewFrame = (ViewFrame) viewFrameList.get(i);

            if (viewFrame.getGView() != null)
                viewFrame.getGView().setActive(b);
        }
    }

    /**
     * Update all viewFrame's gView's 
     */
    public void allViewFramesUpdateDisplay() {
        if (viewFrameList == null) return;
        for (int i = 0; i < viewFrameList.size(); ++i) {
            ViewFrame viewFrame = (ViewFrame) viewFrameList.get(i);

            if (viewFrame.getGView() != null)
                viewFrame.getGView().updateDisplay();
        }
    }

    /**
     * @param viewFrame
     */
    public void addViewFrame(ViewFrame viewFrame) {
        if (viewFrameList == null) 
            viewFrameList = new ArrayList();
        
        viewFrameList.add(viewFrame);
    }
    
    /**
     * @return viewFrameList
     */
    public List getViewFrameList() {
        return viewFrameList;
    }
    
    /**
     * @return wether this tab is actually beeing displayed or sleeping in the background..
     */
    public boolean isActive() {
        return active;
    }

    /**
     * what to do, when we are selected: bring us in front of the whole thing
     * @param event the Button-press event from the CoolBar
     */
    public void handleEvent(Event event) {
        setActive();
    }

    /**
     * @return the Composite, where all the Tab is layed out onto
     */
    public Composite getContent() {
        return contentComposite;
    }

    /**
     * updateDisplay
     */
    public void updateDisplay() {
        getContent().layout();
        allViewFramesUpdateDisplay();
    }

    /**
     * @param buttonName
     */
    public void createButton(String buttonName) {
        toolButton = new ToolButton(mainWindow.getCoolBar().getMainTools(), SWT.PUSH);
        toolButton.addListener(SWT.Selection, this);
        toolButton.setText(G2GuiResources.getString("TT_" + buttonName));
        toolButton.setToolTipText(G2GuiResources.getString("TT_" + buttonName + "ToolTip"));
        toolButton.setBigActiveImage(G2GuiResources.getImage(buttonName + "Active"));
        toolButton.setBigInactiveImage(G2GuiResources.getImage(buttonName));
        toolButton.setSmallActiveImage(G2GuiResources.getImage(buttonName + "SmallActive"));
        toolButton.setSmallInactiveImage(G2GuiResources.getImage(buttonName + "Small"));
        toolButton.useSmallButtons(this.mainWindow.getCoolBar().isToolbarSmallButtons());
        toolButton.setActive(false);
        toolButton.resetImage();
        this.mainWindow.getCoolBar().getMainToolButtons().add(toolButton);
    }

    /**
     * @return mainWindow
     */
    public MainWindow getMainWindow() {
        return mainWindow;
    }

    /**
     * @return CoreCommunication
     */
    public CoreCommunication getCore() {
        return mainWindow.getCore();
    }
}


/*
$Log: GuiTab.java,v $
Revision 1.42  2003/11/29 19:10:24  zet
small update.. continue later.
- mainwindow > tabs > viewframes(can contain gView)

Revision 1.41  2003/11/29 17:21:22  zet
minor cleanup

Revision 1.40  2003/11/29 17:01:00  zet
update for mainWindow

Revision 1.39  2003/11/29 01:51:53  zet
a few more viewframe changes.. will continue later.

Revision 1.38  2003/11/24 01:33:27  zet
move some classes

Revision 1.37  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.36  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.35  2003/10/31 13:20:31  lemmster
added PaneGuiTab and TableGuiTab
added "dropdown" button to all PaneGuiTabs (not finished yet, continue on monday)

Revision 1.34  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.33  2003/09/18 09:44:57  lemmster
checkstyle

Revision 1.32  2003/09/01 00:44:21  zet
use hotimage

Revision 1.31  2003/08/31 20:32:50  zet
active button states

Revision 1.30  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.29  2003/08/29 15:43:43  zet
try gradient headerbar

Revision 1.28  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.27  2003/08/24 18:28:24  zet
thinner header bar

Revision 1.26  2003/08/23 15:21:37  zet
remove @author

Revision 1.25  2003/08/23 14:58:38  lemmster
cleanup of mainWindow, transferTree.* broken

Revision 1.24  2003/08/23 01:57:36  zet
use JFace headerfont

Revision 1.23  2003/08/22 21:06:48  lemmster
replace $user$ with $Author: zet $

Revision 1.22  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.21  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.20  2003/08/16 13:55:32  dek
foo

Revision 1.18  2003/08/10 22:13:51  zet
pageheader separator fixes

Revision 1.17  2003/08/09 14:41:26  zet
dispose font

Revision 1.16  2003/08/08 20:16:13  zet
central PreferenceLoader, abstract Console

Revision 1.15  2003/08/08 08:26:37  dek
i think, the Tab-Caption looks better this way, don't you?

Revision 1.14  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.13  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.12  2003/07/31 14:10:44  lemmstercvs01
statusline.update() changed

Revision 1.11  2003/07/29 09:38:24  lemmstercvs01
set the statusline

Revision 1.10  2003/07/27 22:54:05  zet
coolbar small buttons

Revision 1.9  2003/07/27 22:39:36  zet
small buttons toggle (in popup) for main cool menu

Revision 1.8  2003/07/26 17:54:14  zet
fix pref's illegal setParent, redo graphs, other

Revision 1.7  2003/07/26 00:51:43  zet
stats graph continues to observe when inactive

Revision 1.6  2003/07/25 22:11:09  zet
use prefconverter

Revision 1.5  2003/07/25 03:00:05  zet
*** empty log message ***

Revision 1.4  2003/07/25 02:41:22  zet
console window colour config in prefs / try different fontfieldeditor / pref page  (any worse?)

Revision 1.3  2003/07/23 04:08:07  zet
looks better with icons

Revision 1.2  2003/07/17 15:10:35  lemmstercvs01
foobar

Revision 1.1  2003/07/17 14:58:37  lemmstercvs01
refactored

Revision 1.11  2003/07/15 14:43:30  dek
*** empty log message ***

Revision 1.10  2003/07/15 13:26:57  dek
checkstyle

Revision 1.9  2003/07/14 19:26:41  dek
done some clean.up work, since it seems,as if this view becomes reality..

Revision 1.8  2003/07/03 01:56:45  zet
attempt(?) to save window size/pos & table column widths between sessions

Revision 1.7  2003/07/02 19:14:10  dek
default-window is now transfer-Tab

Revision 1.6  2003/07/02 16:37:29  dek
minor checkstyle

Revision 1.5  2003/07/02 16:37:12  dek
Checkstyle, JavaDocs still have to be added

Revision 1.4  2003/07/01 21:21:27  dek
*** empty log message ***

Revision 1.3  2003/06/30 21:40:09  dek
CoolBar created

Revision 1.2  2003/06/26 14:08:03  dek
statusline created

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.2  2003/06/24 19:18:45  dek
checkstyle apllied

Revision 1.1  2003/06/24 18:25:43  dek
working on main gui, without any connection to mldonkey atm, but the princip works
test with:
public class guitest{
        public static void main( String[] args ) {
        Gui g2gui = new Gui( null );
}

*/
