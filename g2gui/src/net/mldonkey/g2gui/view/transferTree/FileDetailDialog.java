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

import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.view.MainTab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
 * @author z
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FileDetailDialog implements Observer {

	private Shell shell;
	private Display desktop = MainTab.getShell().getDisplay();
	private FileInfo fileInfo;
	private ChunkCanvas chunkCanvas;
	
	private CLabel clFileName, clHash, clSize, clAge,
				clSources, clChunks, clTransferred, clPercent,
				clLast, clPriority, clRate, clETA;
	
	int leftColumn = 100;
	int rightColumn = leftColumn * 3;
	int width = leftColumn + rightColumn + 30;
	int height = 420;
	
	private List renameList;
	private Text renameText;
	
	public FileDetailDialog (FileInfo fileInfo) 
	{
		this.fileInfo = fileInfo;
		shell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.MIN | SWT.MAX);
	
		shell.setBounds( (desktop.getBounds().width - width) / 2,
								  (desktop.getBounds().height - height) / 2,
								  width, height);
								  
		shell.setImage(MainTab.getImageFromRegistry("TransfersButton"));
		shell.setText( "File " + fileInfo.getId() + " details");						  
								  
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 5;
		
		shell.setLayout( gridLayout );
		shell.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		// General
		Group fileGeneral = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		fileGeneral.setText("File information");
		GridLayout generalGridLayout = new GridLayout();
		generalGridLayout.numColumns = 4;
		generalGridLayout.marginWidth = 5;
		generalGridLayout.marginHeight = 2;
		generalGridLayout.horizontalSpacing = 0;
		generalGridLayout.verticalSpacing = 0;
		
		fileGeneral.setLayout(generalGridLayout);
		
		clFileName = createLine(fileGeneral, "Filename:", true);
		clHash = createLine(fileGeneral, "Hash:", true);
		clSize = createLine(fileGeneral, "Size:", false);
	 	clAge = createLine(fileGeneral, "Age:", false);
	
		fileGeneral.setLayoutData( new GridData(GridData.FILL_HORIZONTAL ) );


		// Transfer		
		Group fileTransfer = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		fileTransfer.setText("Transfer information");
		GridLayout transferGridLayout = new GridLayout();
		transferGridLayout.numColumns = 4;
		transferGridLayout.marginWidth = 5;
		transferGridLayout.marginHeight = 2;
		transferGridLayout.horizontalSpacing = 0;
		transferGridLayout.verticalSpacing = 0;
	
		fileTransfer.setLayout(transferGridLayout);
 
		clSources = createLine(fileTransfer, "Sources:", false);
		clChunks = createLine(fileTransfer, "Chunks:", false);
		clTransferred = createLine(fileTransfer, "Transferred:", false);
		clPercent = createLine(fileTransfer, "Percent:", false);
		clLast = createLine(fileTransfer, "Last seen:", false);
		clPriority = createLine(fileTransfer, "Priority", false);
		clRate = createLine(fileTransfer, "Rage:", false);
		clETA = createLine(fileTransfer, "ETA:", false);
		
		fileTransfer.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
		
		// Chunk	
		Group chunkGroup = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		chunkGroup.setText("Chunks");
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
		
		// Rename
		Group renameGroup = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		renameGroup.setText("Alternate filenames");
		
		GridLayout renameGridLayout = new GridLayout();
		renameGridLayout.numColumns = 1;
		renameGridLayout.marginWidth = 5;
		renameGridLayout.marginHeight = 2;
		renameGroup.setLayout(renameGridLayout);
	
		renameGroup.setLayoutData(  new GridData(GridData.FILL_HORIZONTAL));
				
		renameList = new List(renameGroup, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		for (int i = 0; i < fileInfo.getNames().length; i++) 
			renameList.add(fileInfo.getNames()[ i ]);
		
		GridData listGD = new GridData();
		listGD.heightHint = 80;
		listGD.widthHint = width - 50;
		renameList.setLayoutData(listGD); 
	
		Composite rename = new Composite(shell, SWT.NONE);
		GridLayout renameCGridLayout = new GridLayout();
		renameCGridLayout.numColumns = 2;
		renameCGridLayout.marginWidth = 0;
		renameCGridLayout.marginHeight = 0;
		renameCGridLayout.horizontalSpacing = 4;

		rename.setLayout( renameCGridLayout );
		rename.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
				
		renameText = new Text(rename, SWT.BORDER);
		renameText.setText(fileInfo.getName());
		
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.widthHint = 50;
		renameText.setLayoutData(data);
		
		Button renameButton = new Button(rename, SWT.NONE);
		renameButton.setText("Rename");
		
		renameText.addKeyListener( new KeyAdapter() {
			public void keyPressed( KeyEvent e ) {
				if ( e.character == SWT.CR ) {
					renameFile();
					renameText.setText("");
				}
			}		
		} );	
				
		renameButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
	
		renameButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected (SelectionEvent s) {
				renameFile();
			}
		});

		updateLabels();
		fileInfo.addObserver(this);
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
		
		updateLabel(clFileName, fileInfo.getName());
		updateLabel(clHash, fileInfo.getMd4().toUpperCase());
		updateLabel(clSize, fileInfo.getStringSize());
		updateLabel(clAge, fileInfo.getStringAge());
	
		updateLabel(clSources, Integer.toString(fileInfo.getSources()));
		updateLabel(clChunks, Integer.toString(fileInfo.getNumChunks())
							+ " of " + Integer.toString(fileInfo.getChunks().length()));
		updateLabel(clTransferred, fileInfo.getStringDownloaded());
		updateLabel(clPercent, Double.toString(fileInfo.getPerc()) + "%");
		updateLabel(clLast, fileInfo.getStringOffset());
		updateLabel(clPriority, fileInfo.getStringPriority());
		updateLabel(clRate, String.valueOf(fileInfo.getRate()) + " KB/s");
		updateLabel(clETA, fileInfo.getStringETA());
		
		
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
		fileInfo.deleteObserver(this);
		
	}

}
