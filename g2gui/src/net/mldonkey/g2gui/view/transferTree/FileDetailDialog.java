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

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
  
/**
 * 
 * FileDetailDialog
 *
 *
 * @version $Id: FileDetailDialog.java,v 1.25 2003/09/04 18:14:56 zet Exp $ 
 *
 */
public class FileDetailDialog implements Observer {

	private Shell shell;
	private Display desktop = Display.getCurrent();
	private FileInfo fileInfo;
	private ChunkCanvas chunkCanvas;
	
	private Button fileActionButton, fileCancelButton;
	
	private CLabel clFileName, clHash, clSize, clAge,
				clSources, clChunks, clTransferred, clPercent,
				clLast, clPriority, clRate, clETA;
	
	int leftColumn = 100;
	int rightColumn = leftColumn * 3;
	int width = leftColumn + rightColumn + 30;
	int height = 420;
	
	private List renameList;
	private Text renameText;
	
	public FileDetailDialog (final FileInfo fileInfo) 
	{
		this.fileInfo = fileInfo;
		shell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL );
	
		shell.setBounds( (desktop.getBounds().width - width) / 2,
								  (desktop.getBounds().height - height) / 2,
								  width, height);
								  
		shell.setImage(G2GuiResources.getImage("ProgramIcon"));
		shell.setText( G2GuiResources.getString("TT_File") + " " + fileInfo.getId() 
						+ " " + G2GuiResources.getString("TT_Details").toLowerCase());						  
				
		GridLayout gridLayout = CGridLayout.createGL(1,5,5,0,5,false);
		shell.setLayout( gridLayout );
		shell.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		// General
		Group fileGeneral = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		fileGeneral.setText(G2GuiResources.getString("TT_DOWNLOAD_FD_FILE_INFO"));
		
		gridLayout = CGridLayout.createGL(4,5,2,0,0,false);
		fileGeneral.setLayout(gridLayout);
		
		clFileName = createLine(fileGeneral, G2GuiResources.getString("TT_DOWNLOAD_FD_FILENAME"), true);
		clHash = createLine(fileGeneral, G2GuiResources.getString("TT_DOWNLOAD_FD_HASH"), true);
		clSize = createLine(fileGeneral, G2GuiResources.getString("TT_DOWNLOAD_FD_SIZE"), false);
	 	clAge = createLine(fileGeneral, G2GuiResources.getString("TT_DOWNLOAD_FD_AGE"), false);
	
		fileGeneral.setLayoutData( new GridData(GridData.FILL_HORIZONTAL ) );

		// Transfer		
		Group fileTransfer = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		fileTransfer.setText(G2GuiResources.getString("TT_DOWNLOAD_FD_TRANSFER_INFO"));
		
		gridLayout = CGridLayout.createGL(4,5,2,0,0,false);
		fileTransfer.setLayout(gridLayout);
 
		clSources = createLine(fileTransfer, G2GuiResources.getString("TT_DOWNLOAD_FD_SOURCES"), false);
		clChunks = createLine(fileTransfer, G2GuiResources.getString("TT_DOWNLOAD_FD_CHUNKS"), false);
		clTransferred = createLine(fileTransfer, G2GuiResources.getString("TT_DOWNLOAD_FD_TRANSFERRED"), false);
		clPercent = createLine(fileTransfer, G2GuiResources.getString("TT_DOWNLOAD_FD_PERCENT"), false);
		clLast = createLine(fileTransfer, G2GuiResources.getString("TT_DOWNLOAD_FD_LAST"), false);
		clPriority = createLine(fileTransfer, G2GuiResources.getString("TT_DOWNLOAD_FD_PRIORITY"), false);
		clRate = createLine(fileTransfer, G2GuiResources.getString("TT_DOWNLOAD_FD_RATE"), false);
		clETA = createLine(fileTransfer, G2GuiResources.getString("TT_DOWNLOAD_FD_ETA"), false);
		
		fileTransfer.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
		
		// Chunk	
		Group chunkGroup = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		chunkGroup.setText(G2GuiResources.getString("TT_DOWNLOAD_FD_CHUNKS_INFO"));
		
		gridLayout = CGridLayout.createGL(1,5,5,0,0,false);
		chunkGroup.setLayout(gridLayout);
			
		chunkGroup.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) ) ;
					
		chunkCanvas = new ChunkCanvas( chunkGroup, SWT.NO_BACKGROUND, null, fileInfo );
		fileInfo.addObserver( chunkCanvas );
		GridData canvasGD = new GridData(GridData.FILL_HORIZONTAL);
		canvasGD.heightHint = 28;
		chunkCanvas.setLayoutData(canvasGD);
		
		// Rename
		Group renameGroup = new Group(shell, SWT.SHADOW_ETCHED_OUT );
		renameGroup.setText(G2GuiResources.getString("TT_DOWNLOAD_FD_ALTERNATIVE_FILENAMES"));
		
		gridLayout = CGridLayout.createGL(1,5,2,0,0,false);
		renameGroup.setLayout(gridLayout);
	
		renameGroup.setLayoutData(  new GridData(GridData.FILL_HORIZONTAL));
				
		Arrays.sort(fileInfo.getNames(), String.CASE_INSENSITIVE_ORDER);		
		renameList = new List(renameGroup, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		for (int i = 0; i < fileInfo.getNames().length; i++) 
			renameList.add(fileInfo.getNames()[ i ]);
		
		GridData listGD = new GridData();
		listGD.heightHint = 80;
		listGD.widthHint = width - 50;
		renameList.setLayoutData(listGD); 
		renameList.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected (SelectionEvent s) {
				String lItem = renameList.getSelection()[0];
				renameText.setText(lItem);
			}
		});
	
		Composite rename = new Composite(shell, SWT.NONE);
		
		gridLayout = CGridLayout.createGL(2,0,0,4,0,false);
		rename.setLayout( gridLayout );
		rename.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
				
		renameText = new Text(rename, SWT.BORDER);
		renameText.setText(fileInfo.getName());
		
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.widthHint = 50;
		renameText.setLayoutData(data);
		
		Button renameButton = new Button(rename, SWT.NONE);
		renameButton.setText(G2GuiResources.getString("TT_DOWNLOAD_FD_RENAME_BUTTON"));
		
		
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


		Label s = new Label(shell, SWT.SEPARATOR|SWT.HORIZONTAL);
		s.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Composite buttonComposite = new Composite(shell, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonComposite.setLayout(CGridLayout.createGL(3,0,0,5,0,false));

		if (fileInfo.getState().getState() == EnumFileState.PAUSED
			|| fileInfo.getState().getState() == EnumFileState.DOWNLOADING) {
				
			fileCancelButton = new Button( buttonComposite, SWT.NONE );	
			fileCancelButton.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END));			
			fileCancelButton.setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_CANCEL" ) );
			fileCancelButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected (SelectionEvent s) {
					
					MessageBox reallyCancel =
							new MessageBox( 
								fileCancelButton.getShell(),
								SWT.YES | SWT.NO | SWT.ICON_QUESTION );
			
					reallyCancel.setMessage( G2GuiResources.getString( "TT_REALLY_CANCEL" ) );
					int answer = reallyCancel.open();
					if ( answer == SWT.YES ) {
						fileInfo.setState(EnumFileState.CANCELLED);
						fileCancelButton.setEnabled(false);
						fileActionButton.setEnabled(false);
					}
				}	
			});
		}

		fileActionButton = new Button( buttonComposite, SWT.NONE );
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		if (fileInfo.getState().getState() == EnumFileState.DOWNLOADED) {
			gridData.horizontalSpan = 2;
			gridData.grabExcessHorizontalSpace = true;
		}
		
		fileActionButton.setLayoutData(gridData); 
		
		if (fileInfo.getState().getState() == EnumFileState.PAUSED)
			fileActionButton.setText(G2GuiResources.getString( "TT_DOWNLOAD_MENU_RESUME" ));
		else if (fileInfo.getState().getState() == EnumFileState.DOWNLOADING)
			fileActionButton.setText("  " + G2GuiResources.getString( "TT_DOWNLOAD_MENU_PAUSE" ) + "  ");
		else if (fileInfo.getState().getState() == EnumFileState.DOWNLOADED)
			fileActionButton.setText(G2GuiResources.getString( "TT_DOWNLOAD_MENU_COMMIT" ));
	
		fileActionButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected (SelectionEvent s) {
				if (fileInfo.getState().getState() == EnumFileState.PAUSED) {
					fileInfo.setState(EnumFileState.DOWNLOADING);
					fileActionButton.setText(G2GuiResources.getString( "TT_DOWNLOAD_MENU_PAUSE" ));
				}
				else if (fileInfo.getState().getState() == EnumFileState.DOWNLOADING) {
					fileInfo.setState(EnumFileState.PAUSED);
					fileActionButton.setText(G2GuiResources.getString( "TT_DOWNLOAD_MENU_RESUME" ));
				}
				else if (fileInfo.getState().getState() == EnumFileState.DOWNLOADED) {
					if (renameText.getText().equals(""))
						fileInfo.saveFileAs( fileInfo.getName() );
					else
						fileInfo.saveFileAs( renameText.getText() );
						
					fileActionButton.setText(G2GuiResources.getString( "BTN_OK" ));
					fileActionButton.setEnabled(false);
				}
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
		shell.pack();
		shell.open();
		
	}

	private void renameFile() {
		
		String newName = "";
		if ( !renameText.getText().equals("") && !renameText.getText().equals( fileInfo.getName() )) {
			newName = renameText.getText();
		} else if ( renameList.getSelection().length > 0 && !renameList.getSelection()[0].equals(fileInfo.getName()) ) {
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
							+ " / " + Integer.toString(fileInfo.getChunks().length()));
		updateLabel(clTransferred, fileInfo.getStringDownloaded());
		updateLabel(clPercent, Double.toString(fileInfo.getPerc()) + "%");
		updateLabel(clLast, fileInfo.getStringOffset());
		updateLabel(clPriority, fileInfo.getStringPriority());
		if (fileInfo.getState().getState() == EnumFileState.PAUSED)
			updateLabel(clRate, G2GuiResources.getString( "TT_Paused" ));
		else 
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
			if (clFileName.isDisposed())
				return;

				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						updateLabels();
					}
				});
		}
	}
	
	public void dispose() {
		chunkCanvas.dispose();
		fileInfo.deleteObserver(this);
		
	}

}
/*
$Log: FileDetailDialog.java,v $
Revision 1.25  2003/09/04 18:14:56  zet
cancel messagebox

Revision 1.24  2003/08/31 02:35:32  zet
setFocus

Revision 1.23  2003/08/31 02:21:33  zet
commit

Revision 1.22  2003/08/31 02:16:50  zet
cancel button

Revision 1.21  2003/08/31 01:54:25  zet
add spaces

Revision 1.20  2003/08/31 01:48:25  zet
*** empty log message ***

Revision 1.19  2003/08/31 01:46:33  zet
localise

Revision 1.17  2003/08/31 00:08:59  zet
add buttons

Revision 1.16  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.15  2003/08/23 15:21:37  zet
remove @author

Revision 1.14  2003/08/23 15:13:00  zet
remove reference to static MainTab methods

Revision 1.13  2003/08/23 01:08:17  zet
*** empty log message ***

Revision 1.12  2003/08/23 01:00:02  zet
*** empty log message ***

Revision 1.11  2003/08/22 22:49:22  vaste
new todos (name + close button)

Revision 1.10  2003/08/22 21:22:58  lemmster
fix $Log: FileDetailDialog.java,v $
fix Revision 1.25  2003/09/04 18:14:56  zet
fix cancel messagebox
fix
fix Revision 1.24  2003/08/31 02:35:32  zet
fix setFocus
fix
fix Revision 1.23  2003/08/31 02:21:33  zet
fix commit
fix
fix Revision 1.22  2003/08/31 02:16:50  zet
fix cancel button
fix
fix Revision 1.21  2003/08/31 01:54:25  zet
fix add spaces
fix
fix Revision 1.20  2003/08/31 01:48:25  zet
fix *** empty log message ***
fix
fix Revision 1.19  2003/08/31 01:46:33  zet
fix localise
fix
fix Revision 1.17  2003/08/31 00:08:59  zet
fix add buttons
fix
fix Revision 1.16  2003/08/28 22:44:30  zet
fix GridLayout helper class
fix
fix Revision 1.15  2003/08/23 15:21:37  zet
fix remove @author
fix
fix Revision 1.14  2003/08/23 15:13:00  zet
fix remove reference to static MainTab methods
fix
fix Revision 1.13  2003/08/23 01:08:17  zet
fix *** empty log message ***
fix
fix Revision 1.12  2003/08/23 01:00:02  zet
fix *** empty log message ***
fix
fix Revision 1.11  2003/08/22 22:49:22  vaste
fix new todos (name + close button)
fix

*/