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

import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.model.ConsoleMessage;
import net.mldonkey.g2gui.model.Information;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * ConsoleTab
 *
 * @author $user$
 * @version $Id: ConsoleTab.java,v 1.1 2003/06/24 20:44:54 lemmstercvs01 Exp $ 
 *
 */
public class ConsoleTab extends G2guiTab implements InterFaceUI {
	private Core mldonkey;		
	private Text infoDisplay;
	private CoreCommunication core;
	private String consoleMessage;
	 Text input;

	/**
	 * @param gui
	 */
	public ConsoleTab(IG2gui gui) {
		super(gui);
		this.button.setText("Console");
		createContents( this.content );
		mldonkey = Main.getMldonkey();
		registerListener( mldonkey );
	}
	
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.widgets.Gui.G2guiTab#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected void createContents(Composite parent) {		
		
		registerListener( Main.getMldonkey() );
				/*
				 * Now we set the Layout for this very special Tab 
				 */		
				GridLayout gridLayout = new GridLayout();
					gridLayout.numColumns = 1;
					gridLayout.verticalSpacing = 10;
				parent.setLayout(gridLayout);
			
				GridData gridData = new GridData(GridData.FILL_BOTH);			
					gridData.horizontalAlignment = GridData.FILL;
					gridData.verticalAlignment = GridData.BEGINNING;					
				/*
				 * Adding the Console-Display Text-field
				 */			
				infoDisplay = new Text(parent,SWT.MULTI|SWT.H_SCROLL|SWT.V_SCROLL);	
					infoDisplay.setLayoutData(gridData);
   			
				gridData = new GridData(GridData.FILL_BOTH);			
					gridData.horizontalAlignment = GridData.FILL;
					gridData.verticalAlignment = GridData.BEGINNING;
					gridData.grabExcessHorizontalSpace = true;
			
				final Text input = new Text(parent, SWT.SINGLE | SWT.BORDER);
					gridData.horizontalAlignment = GridData.FILL;
					input.setLayoutData(gridData);
					//Send command to core
					input.addKeyListener(new KeyAdapter() {
						  public void keyPressed(KeyEvent e) {
							if (e.character == SWT.CR) {
								infoDisplay.setText(infoDisplay.getText()+input.getText()+"\n");
								String[] command = new String[1];
								command[0]=input.getText();
								(new EncodeMessage(Message.S_CONSOLEMSG,command)).sendMessage(mldonkey.getConnection());
								input.setText("");
							}
						  }
				});
			
		
			}

	/* (non-Javadoc)
	 * @see net.mldonkey.snippets.InterFaceUI#notify(net.mldonkey.g2gui.model.Information)
	 */
	public void notify(Information anInformation) {
		if (anInformation instanceof ConsoleMessage )
		{
			ConsoleMessage aConsoleMessage = (ConsoleMessage)anInformation;
			consoleMessage = aConsoleMessage.getConsoleMessage();
			
			content.getDisplay().syncExec( new Runnable () {
				public void run() {	
					infoDisplay.append(consoleMessage);
					infoDisplay.update();
					if (infoDisplay.getSize().y<infoDisplay.getLineCount())					
					content.layout();			
				}
			});
			
			
			
		}
		/* else: we are not responsible for this Information to be displayed*/
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.widgets.InterFaceUI#registerListener(net.mldonkey.g2gui.comm.CoreCommunication)
	 */
	public void registerListener(CoreCommunication mldonkey) {
		mldonkey.registerListener(this);
		
	}
	


}

/*
$Log: ConsoleTab.java,v $
Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/24 20:33:23  dek
first sketch

*/