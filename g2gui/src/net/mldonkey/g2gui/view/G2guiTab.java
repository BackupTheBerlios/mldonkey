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
package net.mldonkey.g2gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * G2guiTab
 *
 * @author $user$
 * @version $Id: G2guiTab.java,v 1.1 2003/06/24 20:44:54 lemmstercvs01 Exp $ 
 *
 */
public abstract class G2guiTab implements Listener{
	protected Button button;
	protected Composite content;
	protected IG2gui mainWindow;
	
	
	
	/**
	 * @param gui
	 */
	public G2guiTab(IG2gui gui) {
		this.mainWindow = gui;
		this.button = new Button(gui.getButtonRow(),SWT.FLAT);
		this.button.addListener(SWT.Selection,this);
		this.content = new Composite(gui.getPageContainer(),SWT.NONE);
		this.content.setLayout(new FillLayout());
		this.content.setVisible(false);
		gui.registerTab(this);	}

		
	protected abstract void createContents(Composite parent);
	
	/**
	 * @return
	 */
	public Button getButton() {
		return button;
	}

	/**
	 * @return
	 */
	public Composite getContent() {
		return content;
	}

	public void handleEvent(Event event) {
		mainWindow.setActive(this);
	}
}
/*
$Log: G2guiTab.java,v $
Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.2  2003/06/24 19:18:45  dek
checkstyle apllied

Revision 1.1  2003/06/24 18:25:43  dek
working on main gui, without any connection to mldonkey atm, but the princip works
test with:
public class guitest{
	public static void main(String[] args) {
	Gui g2gui = new Gui(null);
}

*/