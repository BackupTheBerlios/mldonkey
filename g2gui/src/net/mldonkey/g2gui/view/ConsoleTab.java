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

import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.model.ConsoleMessage;
import net.mldonkey.g2gui.view.console.Console;
import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;


/**
 * ConsoleTab
 *
 *
 * @version $Id: ConsoleTab.java,v 1.57 2003/12/04 08:47:27 lemmy Exp $
 *
 */
public class ConsoleTab extends GuiTab implements Observer, Runnable {
    private ConsoleMessage consoleMessage;
    private Console console;

    /**
     * @param mainWindow
     */
    public ConsoleTab(MainWindow mainWindow) {
        super(mainWindow, "ConsoleButton");
    }

    /* ( non-Javadoc )
     * @see net.mldonkey.g2gui.view.widgets.Gui.G2guiTab#createContents( org.eclipse.swt.widgets.Composite )
     */
    protected void createContents(Composite parent) {
        ViewFrame viewFrame = new ViewFrame(parent, "TT_ConsoleButton", "ConsoleButtonSmall", this);
        
        console = new Console(viewFrame.getChildComposite(), SWT.NONE);
        console.addObserver(this);

        loadPreferences();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.GuiTab#setInActive()
     */
    public void setInActive() {
        getCore().getConsoleMessage().deleteObserver(this);
        console.deleteObserver(this);
        super.setInActive();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.GuiTab#setActive()
     */
    public void setActive() {
        console.addObserver(this);
        getCore().getConsoleMessage().addObserver(this);
        super.setActive();
        console.setFocus();
    }

    /**
     * what to do, if this tab becomes active
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event) {
        super.handleEvent(event);

        if (getCore().isConnected()) {
            console.append(RegExp.replaceAll(getCore().getConsoleMessage().getConsoleMessage(), "\n",
                    console.getLineDelimiter()));
            getCore().getConsoleMessage().reset();
        }
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.GuiTab#updateDisplay()
     */
    public void updateDisplay() {
        loadPreferences();
        super.updateDisplay();
    }

    public void loadPreferences() {
        console.setDisplayFont(PreferenceLoader.loadFont("consoleFontData"));
        console.setInputFont(PreferenceLoader.loadFont("consoleFontData"));
        console.setHighlightColor(PreferenceLoader.loadColour("consoleHighlight"));
        console.setDisplayBackground(PreferenceLoader.loadColour("consoleBackground"));
        console.setDisplayForeground(PreferenceLoader.loadColour("consoleForeground"));
        console.setInputBackground(PreferenceLoader.loadColour("consoleInputBackground"));
        console.setInputForeground(PreferenceLoader.loadColour("consoleInputForeground"));
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update( java.util.Observable, java.lang.Object )
     */
    public void update(Observable o, Object arg) {
        if (o instanceof Console) {
            String[] command = new String[ 1 ];
            command[ 0 ] = (String) arg;
            (new EncodeMessage(Message.S_CONSOLEMSG, command)).sendMessage(getCore());
        } else if (o instanceof ConsoleMessage) {
            this.consoleMessage = (ConsoleMessage) arg;
            getContent().getDisplay().asyncExec(this);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        console.append(consoleMessage.getConsoleMessage());
        consoleMessage.reset();
    }
}


/*
$Log: ConsoleTab.java,v $
Revision 1.57  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.56  2003/11/29 19:28:38  zet
minor string move

Revision 1.55  2003/11/29 19:10:24  zet
small update.. continue later.
- mainwindow > tabs > viewframes(can contain gView)

Revision 1.54  2003/11/29 17:21:22  zet
minor cleanup

Revision 1.53  2003/11/29 17:01:00  zet
update for mainWindow

Revision 1.52  2003/11/28 01:06:21  zet
not much- slowly expanding viewframe - will continue later

Revision 1.51  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.50  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.49  2003/11/04 20:38:27  zet
update for transparent gifs

Revision 1.48  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.47  2003/09/18 15:30:27  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.46  2003/09/18 09:44:57  lemmy
checkstyle

Revision 1.45  2003/08/29 22:11:47  zet
add CCLabel helper class

Revision 1.44  2003/08/29 19:30:50  zet
font colour

Revision 1.43  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.42  2003/08/29 16:06:54  zet
optional shadow

Revision 1.41  2003/08/29 15:59:16  zet
optional shadow

Revision 1.40  2003/08/25 22:11:53  zet
update style bits

Revision 1.39  2003/08/23 15:21:37  zet
remove @author

Revision 1.38  2003/08/23 09:56:15  lemmy
use supertype instead of Core

Revision 1.37  2003/08/22 21:06:48  lemmy
replace $user$ with $Author: lemmy $

Revision 1.36  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.35  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.34  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging

Revision 1.33  2003/08/10 23:24:20  zet
move updatedisplay

Revision 1.32  2003/08/10 00:38:17  zet
setFocus on activation

Revision 1.31  2003/08/09 16:04:46  dek
added gnu.regexp for compiling with gcj
you can get it at:

ftp://ftp.tralfamadore.com/pub/java/gnu.regexp-1.1.4.tar.gz

Revision 1.30  2003/08/08 20:16:13  zet
central PreferenceLoader, abstract Console

Revision 1.29  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.28  2003/08/01 17:21:18  lemmy
reworked observer/observable design, added multiversion support

Revision 1.27  2003/07/27 22:39:36  zet
small buttons toggle (in popup) for main cool menu

Revision 1.26  2003/07/27 01:01:51  vnc
added better scrolling features

Revision 1.25  2003/07/26 20:21:48  zet
pgup/pgdn from input

Revision 1.24  2003/07/26 17:15:04  vnc
optimized console display appendings

Revision 1.23  2003/07/25 22:01:23  vnc
added workaround for eclipse-bug 40800 (focus bottom on GTK2)

Revision 1.22  2003/07/25 14:48:28  zet
replace string.split/replaceAll

Revision 1.21  2003/07/25 02:41:22  zet
console window colour config in prefs / try different fontfieldeditor / pref page  (any worse?)

Revision 1.20  2003/07/24 02:22:46  zet
doesn't crash if no core is running

Revision 1.19  2003/07/23 04:08:07  zet
looks better with icons

Revision 1.18  2003/07/22 18:10:15  zet
console linedelimiter

Revision 1.17  2003/07/18 04:34:22  lemmy
checkstyle applied

Revision 1.16  2003/07/17 14:58:44  lemmy
refactored

Revision 1.15  2003/07/15 14:43:30  dek
*** empty log message ***

Revision 1.14  2003/07/04 12:30:53  dek
checkstyle

Revision 1.13  2003/07/03 10:13:09  dek
saving font now works

Revision 1.12  2003/07/03 01:56:45  zet
attempt(?) to save window size/pos & table column widths between sessions

Revision 1.11  2003/07/02 19:26:55  dek
fixed:< no i don't ever want to become active> bug

Revision 1.10  2003/07/02 19:25:41  dek
transferTab is now default ;-)

Revision 1.9  2003/07/02 16:34:55  dek
minor checkstyle

Revision 1.8  2003/07/02 16:34:19  dek
Checkstyle, JavaDocs still have to be added

Revision 1.7  2003/06/30 21:40:09  dek
CoolBar created

Revision 1.6  2003/06/30 19:11:57  dek
at work: setting the font to the one selected in the preference-dialog

Revision 1.5  2003/06/27 11:12:53  dek
works now

Revision 1.4  2003/06/27 10:36:17  lemmy
changed notify to observer/observable

Revision 1.3  2003/06/25 21:28:16  dek
cosmetic change, made console-output read-only

Revision 1.2  2003/06/25 18:04:53  dek
Console-Tab reworked

Revision 1.1  2003/06/24 20:44:54  lemmy
refactored

Revision 1.1  2003/06/24 20:33:23  dek
first sketch

*/
