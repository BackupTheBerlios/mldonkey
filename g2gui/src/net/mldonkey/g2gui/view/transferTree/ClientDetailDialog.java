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

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumClientMode;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.MainTab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
  
/**
 * @author z
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ClientDetailDialog implements Observer {

	private Shell shell;
	private Display desktop = MainTab.getShell().getDisplay();
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
	
	public ClientDetailDialog (FileInfo fileInfo, ClientInfo clientInfo ) 
	{
		this.fileInfo = fileInfo;
		this.clientInfo = clientInfo;
		shell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL );
	
		shell.setBounds( (desktop.getBounds().width - width) / 2,
								  (desktop.getBounds().height - height) / 2,
								  width, height);
								  
		shell.setImage(MainTab.getImageFromRegistry("TransfersButton"));
		
		shell.setText( "Client " + clientInfo.getClientid() + " details");						  
								  
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 5;
		
		shell.setLayout( gridLayout );
		shell.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		// General
		Group clientGeneral = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		clientGeneral.setText("Client information");
		GridLayout generalGridLayout = new GridLayout();
		generalGridLayout.numColumns = 4;
		generalGridLayout.marginWidth = 5;
		generalGridLayout.marginHeight = 2;
		generalGridLayout.horizontalSpacing = 0;
		generalGridLayout.verticalSpacing = 0;
		
		clientGeneral.setLayout(generalGridLayout);
		
		clName = createLine(clientGeneral, "Name:", true);
		clNetwork = createLine(clientGeneral, "Network:", false);
		clRating = createLine(clientGeneral, "Rating:", false);
		clActivity = createLine(clientGeneral, "Activity:", false);
		clKind = createLine(clientGeneral, "Kind:", false);
	
		clientGeneral.setLayoutData( new GridData(GridData.FILL_HORIZONTAL ) );

		// Chunk	
		Group chunkGroup = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		chunkGroup.setText("Local file chunks");
		GridLayout chunkGridLayout = new GridLayout();
		chunkGridLayout.numColumns = 1;
		chunkGridLayout.marginWidth = 5;
		chunkGroup.setLayout(chunkGridLayout);
			
		chunkGroup.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) ) ;
					
		chunkCanvas = new ChunkCanvas( chunkGroup, SWT.NO_BACKGROUND, null, fileInfo );
		fileInfo.addObserver( chunkCanvas );
		GridData canvasGD = new GridData(GridData.FILL_HORIZONTAL);
		canvasGD.heightHint = 28;
		chunkCanvas.setLayoutData(canvasGD);
		
		// Client Chunk	
		Group chunkGroup2 = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		chunkGroup2.setText("Client file chunks");
		GridLayout chunkGridLayout2 = new GridLayout();
		chunkGridLayout2.numColumns = 1;
		chunkGridLayout2.marginWidth = 5;
		chunkGroup2.setLayout(chunkGridLayout);
		
		chunkGroup2.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) ) ;
				
		chunkCanvas2 = new ChunkCanvas( chunkGroup2, SWT.NO_BACKGROUND, clientInfo, fileInfo );
		clientInfo.addObserver( chunkCanvas );
		GridData canvasGD2 = new GridData(GridData.FILL_HORIZONTAL);
		canvasGD2.heightHint = 28;
		chunkCanvas2.setLayoutData(canvasGD2);

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
		updateLabel(clActivity, getClientActivity(clientInfo));
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
			if (MainTab.getShell().isDisposed())
				return;

			Shell shell = MainTab.getShell();
			if (shell != null && shell.getDisplay() != null) {
				shell.getDisplay().asyncExec(new Runnable() {
					public void run() {
						updateLabels();
					}
				});
			}
		}
	}
	
	public void dispose() {
		chunkCanvas.dispose();
		clientInfo.deleteObserver(this);
		fileInfo.deleteObserver(this);
	}

	public String getClientConnection(ClientInfo clientInfo) {
		if (clientInfo.getClientKind().getClientMode()
			== EnumClientMode.FIREWALLED)
			return "firewalled";
		else
			return "direct";
	}

	public String getClientActivity(ClientInfo clientInfo) {
		if (clientInfo.getState().getState()
			== EnumState.CONNECTED_DOWNLOADING)
			return "transferring";
		else
			return "rank: " + clientInfo.getState().getRank();
	}


}
