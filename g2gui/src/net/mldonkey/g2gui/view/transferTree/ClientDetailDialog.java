/*
 * Copyright 2003
 * G2Gui Team
 * 
 * 
 * This file is part of G2Gui.
 *
 * G2Gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2Gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2Gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.transferTree;

import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumClientMode;
import net.mldonkey.g2gui.model.enum.EnumClientType;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * ClientDetailDialog
 *
 * @version $Id: ClientDetailDialog.java,v 1.24 2003/09/14 16:23:56 zet Exp $ 
 *
 */  
public class ClientDetailDialog implements Observer {

	private Shell shell;
	private Display desktop = Display.getDefault();
	private FileInfo fileInfo;
	private ClientInfo clientInfo;
	private ChunkCanvas chunkCanvas, chunkCanvas2;
	private CLabel clName, clRating, clActivity, clKind, clNetwork;
	
	int leftColumn = 100;
	int rightColumn = leftColumn * 3;
	int width = leftColumn + rightColumn + 30;
	int height = 420;
	
	private List renameList;
	private Text renameText;
	
	public ClientDetailDialog ( FileInfo fileInfo, final ClientInfo clientInfo, final CoreCommunication core ) 
	{
	
		this.fileInfo = fileInfo;
		this.clientInfo = clientInfo;
		shell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL );
	
		shell.setBounds( (desktop.getBounds().width - width) / 2,
								  (desktop.getBounds().height - height) / 2,
								  width, height);
								  
		shell.setImage(G2GuiResources.getImage("ProgramIcon"));
		
		shell.setText( G2GuiResources.getString("TT_Client") + " " + clientInfo.getClientid() 
						+ " " + G2GuiResources.getString("TT_Details").toLowerCase());						  
		
		GridLayout gridLayout = CGridLayout.createGL(1,5,5,0,5,false);
		
		shell.setLayout( gridLayout );
		shell.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		// General
		Group clientGeneral = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		clientGeneral.setText(G2GuiResources.getString("TT_DOWNLOAD_CD_CLIENT_INFO"));
		
		gridLayout = CGridLayout.createGL(4,5,2,0,0,false);
		clientGeneral.setLayout(gridLayout);
		
		clName = createLine(clientGeneral, G2GuiResources.getString("TT_DOWNLOAD_CD_NAME"), true);
		clNetwork = createLine(clientGeneral, G2GuiResources.getString("TT_DOWNLOAD_CD_NETWORK"), false);
		clRating = createLine(clientGeneral, G2GuiResources.getString("TT_DOWNLOAD_CD_RATING"), false);
		clActivity = createLine(clientGeneral, G2GuiResources.getString("TT_DOWNLOAD_CD_ACTIVITY"), false);
		clKind = createLine(clientGeneral, G2GuiResources.getString("TT_DOWNLOAD_CD_KIND"), false);
	
		clientGeneral.setLayoutData( new GridData(GridData.FILL_HORIZONTAL ) );

		// Chunk	
		Group chunkGroup = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		chunkGroup.setText(G2GuiResources.getString("TT_DOWNLOAD_CD_LOCAL_CHUNKS"));

		gridLayout = CGridLayout.createGL(1,5,5,0,0,false);
		chunkGroup.setLayout(gridLayout);
			
		chunkGroup.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) ) ;
					
		chunkCanvas = new ChunkCanvas( chunkGroup, SWT.NO_BACKGROUND, null, fileInfo, null );
		fileInfo.addObserver( chunkCanvas );
		GridData canvasGD = new GridData(GridData.FILL_HORIZONTAL);
		canvasGD.heightHint = 28;
		chunkCanvas.setLayoutData(canvasGD);
		
		// Client Chunk	
		Group chunkGroup2 = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		chunkGroup2.setText(G2GuiResources.getString("TT_DOWNLOAD_CD_CLIENT_CHUNKS"));
		
		gridLayout = CGridLayout.createGL(1,5,5,0,0,false);
		chunkGroup2.setLayout(gridLayout);
		
		chunkGroup2.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) ) ;
				
		chunkCanvas2 = new ChunkCanvas( chunkGroup2, SWT.NO_BACKGROUND, clientInfo, fileInfo, null );
		clientInfo.addObserver( chunkCanvas );
		GridData canvasGD2 = new GridData(GridData.FILL_HORIZONTAL);
		canvasGD2.heightHint = 28;
		chunkCanvas2.setLayoutData(canvasGD2);


		Composite buttonComposite = new Composite(shell, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonComposite.setLayout(CGridLayout.createGL(2,0,0,5,0,false));

		final Button addFriendButton = new Button( buttonComposite, SWT.NONE );
		addFriendButton.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END));
		addFriendButton.setText(G2GuiResources.getString( "TT_DOWNLOAD_MENU_ADD_FRIEND" ));
		
		if (clientInfo.getClientType() == EnumClientType.FRIEND)
			addFriendButton.setEnabled(false);

		addFriendButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected (SelectionEvent s) {
				ClientInfo.addFriend(core, clientInfo.getClientid());
				addFriendButton.setText(G2GuiResources.getString( "BTN_OK" ));
				addFriendButton.setEnabled(false);
			}	
		});

		Button closeButton = new Button( buttonComposite, SWT.NONE );
		closeButton.setFocus();
		closeButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		closeButton.setText(G2GuiResources.getString( "BTN_CLOSE" ));
		closeButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected (SelectionEvent s) {
						shell.dispose();
			}	
		});

		updateLabels();
		fileInfo.addObserver(this);
		clientInfo.addObserver(this);
		shell.pack();
		shell.open();
		
	}

	private void renameFile() {
		
		String newName = "";
		if ( !renameText.getText().equals("") && !renameText.getText().equals( fileInfo.getName() )) {
			newName = renameText.getText();
		} else if ( renameList.getSelection()[0] != null && !renameList.getSelection()[0].equals(fileInfo.getName()) ) {
			newName = renameList.getSelection()[0];
		}
			
		if (!newName.equals("")) {
			fileInfo.setName(newName);
		}
		
	}

	private CLabel createLine(Composite composite, String label, boolean longlabel) {
		
		Label aLabel = new Label(composite, SWT.NONE);
		aLabel.setText(label);
		GridData lGD = new GridData(); 
		lGD.widthHint = leftColumn;
		aLabel.setLayoutData(lGD);
		
		CLabel aCLabel = new CLabel(composite, SWT.NONE);
		GridData clGD = new GridData();
		if (longlabel) {
			clGD.widthHint = rightColumn;
			clGD.horizontalSpan = 3;
		} else {
			clGD.widthHint = leftColumn;
		}
			
		aCLabel.setLayoutData(clGD); 
		return aCLabel;	 
		
	}
	
	public void updateLabels() {
		
		updateLabel(clName, clientInfo.getClientName());
		updateLabel(clRating, "" + clientInfo.getClientRating());
		updateLabel(clActivity, clientInfo.getClientActivity());
		updateLabel(clKind, getClientConnection(clientInfo));
		updateLabel(clNetwork, clientInfo.getClientnetworkid().getNetworkName());
		
	}
	
	public void updateLabel(CLabel cLabel, String string) {
		if (!cLabel.isDisposed()) {
			cLabel.setText(string);
			if (string.length() > 10) 
				cLabel.setToolTipText(string);
			else cLabel.setToolTipText("");
		}
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof FileInfo) {
			if (clName.isDisposed()) return;
			clName.getDisplay().asyncExec(new Runnable() {
				public void run() {
					updateLabels();
				}
			});
		}
	}
	
	public void dispose() {
		chunkCanvas.dispose();
		clientInfo.deleteObserver(this);
		fileInfo.deleteObserver(this);
	}

	public String getClientConnection(ClientInfo clientInfo) {
		if ( clientInfo.getClientKind().getClientMode() == EnumClientMode.FIREWALLED ) 
			return G2GuiResources.getString( "TT_Firewalled" ).toLowerCase();			
		else
			return G2GuiResources.getString( "TT_Direct" ).toLowerCase();	
	}
	

}
/*
$Log: ClientDetailDialog.java,v $
Revision 1.24  2003/09/14 16:23:56  zet
multi network avails

Revision 1.23  2003/09/14 03:37:43  zet
changedProperties

Revision 1.22  2003/09/13 22:26:44  zet
weak sets & !rawrate

Revision 1.21  2003/08/31 15:37:45  zet
check if already a friend

Revision 1.20  2003/08/31 02:35:32  zet
setFocus

Revision 1.19  2003/08/31 02:16:55  zet
*** empty log message ***

Revision 1.18  2003/08/31 01:48:25  zet
*** empty log message ***

Revision 1.17  2003/08/31 01:46:33  zet
localise

Revision 1.14  2003/08/31 00:08:59  zet
add buttons

Revision 1.13  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.12  2003/08/23 15:21:37  zet
remove @author

Revision 1.11  2003/08/23 15:13:00  zet
remove reference to static MainTab methods

Revision 1.10  2003/08/23 01:00:10  zet
*** empty log message ***

Revision 1.9  2003/08/22 22:54:04  vaste
new todo (close button)

Revision 1.8  2003/08/22 21:22:58  lemmster

*/
