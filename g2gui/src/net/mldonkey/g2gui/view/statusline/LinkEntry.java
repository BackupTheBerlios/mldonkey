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

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.REMatch;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.view.StatusLine;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * LinkEntry
 *
 * @version $Id: LinkEntry.java,v 1.7 2003/08/28 18:27:32 zet Exp $ 
 *
 */
public class LinkEntry {

	private Text linkEntryText;
	private Button linkEntryButton;
	private CoreCommunication core;
	private StatusLine statusLine;

	public LinkEntry(StatusLine statusLine, CoreCommunication core, Composite parent) {
		this.statusLine = statusLine;
		this.core = core;
		createContents(parent);
	}
	
	public void createContents(Composite parent) {
		
		ViewForm linkEntryViewForm = new ViewForm( parent, SWT.BORDER | (PreferenceLoader.loadBoolean("flatInterface") ? SWT.FLAT : SWT.NONE) );
		linkEntryViewForm.setLayoutData(new GridData(GridData.FILL_BOTH));	
			
		CLabel linkEntryCLabel = new CLabel(linkEntryViewForm, SWT.LEFT );	
		linkEntryCLabel.setText(G2GuiResources.getString("LE_HEADER"));
		linkEntryCLabel.setImage(G2GuiResources.getImage("UpArrowBlue"));
		linkEntryCLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		linkEntryCLabel.setBackground(new Color[]{linkEntryViewForm.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND),
										linkEntryViewForm.getBackground()},
										new int[] {100});	
			
			
		linkEntryText = new Text(linkEntryViewForm, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL );
		linkEntryText.setLayoutData(new FillLayout());
		linkEntryText.setFont(JFaceResources.getTextFont());

		
		ToolBar linkEntryToolBar = new ToolBar(linkEntryViewForm, SWT.RIGHT | SWT.FLAT );
		ToolItem sendItem = new ToolItem(linkEntryToolBar, SWT.NONE);
		sendItem.setText(G2GuiResources.getString("LE_BUTTON"));
		sendItem.setImage(G2GuiResources.getImage("UpArrowBlue"));
		sendItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected (SelectionEvent s) {
				enterLinks(linkEntryText.getText());
			}	
		});
		
		linkEntryViewForm.setTopLeft(linkEntryCLabel);
		linkEntryViewForm.setContent(linkEntryText);
		linkEntryViewForm.setTopRight(linkEntryToolBar);
		
	}

	// bleah. When do magnet links terminate? or ".torrent?value=x..."?  I don't know..
	public void enterLinks(String input) {
		RE regex = null;	
		try {
			 regex = new RE( "(ed2k://\\|file\\|[^\\|]+\\|(\\d+)\\|([\\dabcdef]+)\\|)" 
			+ "|(sig2dat:///?\\|File:[^\\|]+\\|Length:.+?\\|UUHash:\\=.+?\\=)"
			+ "|(\\\"magnet:\\?xt=.+?\\\")"
			+ "|(magnet:\\?xt=.+?\n)"
			+ (linkEntryText.getLineCount() == 1 ? "|(magnet:\\?xt=.+)" : "")
			+ (linkEntryText.getLineCount() == 1 ? "|(http://.+?\\.torrent.+)" : "")
			+ "|(\"http://.+?\\.torrent\\?[^>]+\")"
			+ "|(http://.+?\\.torrent)" , RE.REG_ICASE );
		} 
		catch ( REException e ) {			
			e.printStackTrace();
		}		
	
		REMatch[] matches = regex.getAllMatches(input);
		for (int i = 0; i < matches.length; i++) {
			String link = replaceAll(matches[i].toString(), "\"", "");
			link = replaceAll(link, "\n", "");
			Message dllLink = new EncodeMessage( Message.S_DLLINK, link );
			dllLink.sendMessage( core.getConnection() );
		}
		statusLine.update(G2GuiResources.getString("LE_LINKS_SENT") + " " + matches.length);
		linkEntryText.setText("");
	}

	private String replaceAll( String input, String toBeReplaced, String replaceWith ) {		
			RE regex = null;			
			try {
				 regex = new RE( toBeReplaced );
			}
			catch ( REException e ) {			
				e.printStackTrace();
			}		
			String result = regex.substituteAll( input, replaceWith );	
			return result;
	}


}
/*
$Log: LinkEntry.java,v $
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