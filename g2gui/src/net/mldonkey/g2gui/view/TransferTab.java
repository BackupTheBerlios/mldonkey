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


import gnu.trove.TIntObjectIterator;

import java.text.DecimalFormat;
import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transferTree.CustomTableViewer;
import net.mldonkey.g2gui.view.transferTree.DownloadTableTreeContentProvider;
import net.mldonkey.g2gui.view.transferTree.DownloadTableTreeViewer;
import net.mldonkey.g2gui.view.transferTree.clientTable.TableContentProvider;
import net.mldonkey.g2gui.view.transferTree.clientTable.TableLabelProvider;
import net.mldonkey.g2gui.view.transferTree.clientTable.TableMenuListener;
import net.mldonkey.g2gui.view.transferTree.clientTable.TableSorter;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Main
 *
 * @author $user$
 * @version $Id: TransferTab.java,v 1.31 2003/08/21 10:12:10 dek Exp $ 
 *
 */
public class TransferTab extends GuiTab  {
	private boolean active;
	private CoreCommunication mldonkey;
	private Label label;
	private long timestamp = 0;
	private Font font = new Font(null, "Helvetica", 12, SWT.BOLD); // fix later
	public static DecimalFormat decimalFormat = new DecimalFormat( "0.0" );
	private DownloadTableTreeViewer downloadTableTreeViewer = null;
	private CustomTableViewer clientTableViewer = null;
		
	/**
	 * @param gui where this tab belongs to
	 */
	public TransferTab( MainTab gui ) {
		super( gui );
		this.mldonkey = gui.getCore();		
		
		createButton("TransfersButton", 
						G2GuiResources.getString("TT_TransfersButton"),
						G2GuiResources.getString("TT_TransfersButtonToolTip"));
		createContents(this.subContent);
		
	}
	
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.G2guiTab#createContents(org.eclipse.swt.widgets.Composite)
	 */	 
	protected void createContents( Composite parent ) {
		SashForm mainSashForm = new SashForm( parent, SWT.VERTICAL );
		Composite downloadComposite;
		
		
		if (PreferenceLoader.loadBoolean("advancedMode")) {
					
			SashForm downloadSashForm = new SashForm( mainSashForm, SWT.HORIZONTAL );
			
		
			
			
			downloadComposite = new Composite( downloadSashForm, SWT.BORDER );
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			gridLayout.marginHeight = 0;
			gridLayout.marginWidth = 0;
			downloadComposite.setLayout( gridLayout );
		
			Composite downloadClients = new Composite(downloadSashForm, SWT.BORDER );
			gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			gridLayout.marginHeight = 0;
			gridLayout.marginWidth = 0;
			gridLayout.verticalSpacing = 0;
			downloadClients.setLayout( gridLayout );
			
			downloadClients.addControlListener(new ControlAdapter() {
				public void controlResized(ControlEvent e) {
						Composite c = (Composite) e.widget;
						int width = c.getBounds().width;
						if (width > 0 && downloadTableTreeViewer != null) 
							downloadTableTreeViewer.updateClientsTable(true);
				}
			});
		
			createClientTableViewer(downloadClients, downloadSashForm);
		
			downloadSashForm.setWeights( new int[] {100,0});
		
		} else {
			downloadComposite = new Composite( mainSashForm, SWT.BORDER );
			downloadComposite.setLayout(new FillLayout());
		}
		
				
		// When we have uploaders:		
		Composite upload = new Composite( mainSashForm, SWT.BORDER );
		upload.setLayout(new FillLayout());
		Label l = new Label(upload, SWT.CENTER);
		l.setText("Try \"uploaders\" or \"upstats\" command in console until gui protocol sends upload information.");
		
		mainSashForm.setWeights( new int[] {1441,0});
		downloadTableTreeViewer = new DownloadTableTreeViewer ( downloadComposite, clientTableViewer, mldonkey, this );
		
		if (headerBar) mldonkey.getFileInfoIntMap().addObserver( this );
		
	}
	
	public void createClientTableViewer(Composite parent, final SashForm parentSash) {

		final String[] COLUMN_LABELS = { "TT_CT_STATE", "TT_CT_NAME", "TT_CT_NETWORK", "TT_CT_KIND" };
		final int[] COLUMN_ALIGNMENT = { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT };
		final int[] COLUMN_DEFAULT_WIDTHS = { 200, 100, 75, 75 };
		clientTableViewer = new CustomTableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI);
		Table table = clientTableViewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		clientTableViewer.getTable().setLinesVisible( PreferenceLoader.loadBoolean("displayGridLines") );
		clientTableViewer.getTable().setHeaderVisible( true );
		
		for (int i = 0; i < COLUMN_LABELS.length; i++) {
			MainTab.getStore().setDefault(COLUMN_LABELS[ i ], COLUMN_DEFAULT_WIDTHS[ i ]);
			TableColumn tableColumn = new TableColumn(table, COLUMN_ALIGNMENT[ i ]);
			tableColumn.setText ( G2GuiResources.getString( COLUMN_LABELS[ i ] )  );
			tableColumn.setWidth(MainTab.getStore().getInt( COLUMN_LABELS[ i ] ));
			final int columnIndex = i;
			tableColumn.addDisposeListener(new DisposeListener() {
				public synchronized void widgetDisposed( DisposeEvent e ) {
					TableColumn thisColumn = ( TableColumn ) e.widget;
					MainTab.getStore().setValue(COLUMN_LABELS [ columnIndex ] , thisColumn.getWidth() );
				}
			} );
			tableColumn.addListener( SWT.Selection, new Listener() {
				public void handleEvent( Event e ) {
					TableSorter oldTS = (TableSorter) clientTableViewer.getSorter();
					TableSorter newTS = new TableSorter();
					newTS.setLastSort(oldTS.getLastSort());
					newTS.setLastColumnIndex(oldTS.getLastColumnIndex());
					newTS.setColumnIndex( columnIndex );
					clientTableViewer.setSorter ( newTS );
				}	
			} ); 
		}
			
		clientTableViewer.setContentProvider(new TableContentProvider());
		clientTableViewer.setLabelProvider(new TableLabelProvider());
		TableMenuListener tableMenuListener = new TableMenuListener( clientTableViewer, mldonkey );
		clientTableViewer.addSelectionChangedListener( tableMenuListener );
		MenuManager popupMenu = new MenuManager( "" );
		popupMenu.setRemoveAllWhenShown( true );
		popupMenu.addMenuListener( tableMenuListener );			
		clientTableViewer.getTable().setMenu( popupMenu.createContextMenu( clientTableViewer.getTable() ) );
		clientTableViewer.setSorter(new TableSorter());
		
		Composite bottomBar = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		bottomBar.setLayout( gridLayout );
		bottomBar.setLayoutData( new GridData(GridData.FILL_HORIZONTAL));
		
		Label separator1 = new Label(bottomBar,SWT.SEPARATOR|SWT.HORIZONTAL);
		separator1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Button hideButton = new Button(bottomBar, SWT.NONE);
		hideButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		hideButton.setText(">>>");
		hideButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected (SelectionEvent s) {
				parentSash.setWeights(new int[]{10,0});
				if (downloadTableTreeViewer != null) 
					downloadTableTreeViewer.updateClientsTable(false);
			}
		});
		
	}

	/** (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) {
		
		float totalRate = 0;
		int totalFiles = 0;
		int totalQueued = 0;

		// add precision later		
		if (o instanceof FileInfoIntMap) {
			synchronized(o) {
				FileInfoIntMap files = (FileInfoIntMap)o;
				TIntObjectIterator it = files.iterator();
				while(it.hasNext()) {
					it.advance();
					FileInfo fileInfo = (FileInfo) it.value();
					totalRate += fileInfo.getRawRate();
					if (DownloadTableTreeContentProvider.isInteresting(fileInfo)) 
						totalFiles++;
					if (fileInfo.getState().getState() == EnumFileState.QUEUED) 
						totalQueued++;
				}
			} 
			// localise this
			String totalQueuedString = (totalQueued > 0 ? " (" + totalQueued + " queued)" : "");
			runLabelUpdate(totalFiles + " files, " + decimalFormat.format(totalRate / 1000f) + " KB/s" + totalQueuedString);
		}
	
	}
	
	public void runLabelUpdate(final String text) 
		{
			Shell shell = MainTab.getShell();
			if(!shell.isDisposed() && shell !=null && shell.getDisplay()!=null) {
				shell.getDisplay().asyncExec(new Runnable() {
					public void run() {
							setRightLabel(text);
					}
				});
			}
		}	
	
	
	public void dispose() {
		super.dispose();
		font.dispose();
	}
	public void updateDisplay() {
		downloadTableTreeViewer.updateDisplay();
		if (clientTableViewer != null)
			clientTableViewer.getTable().setLinesVisible( PreferenceLoader.loadBoolean("displayGridLines") );
		super.updateDisplay();
		mldonkey.getFileInfoIntMap().deleteObserver( this );
		if (headerBar) mldonkey.getFileInfoIntMap().addObserver( this );
		
	}
}

/*
$Log: TransferTab.java,v $
Revision 1.31  2003/08/21 10:12:10  dek
removed empty expression

Revision 1.30  2003/08/20 22:18:56  zet
Viewer updates

Revision 1.29  2003/08/20 18:35:16  zet
uploaders label

Revision 1.28  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.27  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.26  2003/08/15 22:05:58  zet
*** empty log message ***

Revision 1.25  2003/08/11 00:30:10  zet
show queued files

Revision 1.24  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.23  2003/08/06 17:10:50  zet
check for shell.isdisposed

Revision 1.22  2003/08/04 19:22:08  zet
trial tabletreeviewer

Revision 1.21  2003/07/27 22:39:36  zet
small buttons toggle (in popup) for main cool menu

Revision 1.20  2003/07/24 02:22:46  zet
doesn't crash if no core is running

Revision 1.19  2003/07/23 04:08:07  zet
looks better with icons

Revision 1.18  2003/07/18 09:58:00  dek
checkstyle

Revision 1.17  2003/07/17 14:58:37  lemmstercvs01
refactored

Revision 1.3  2003/07/15 18:14:47  dek
Wow, nice piece of work already done, it works, looks nice, but still lots of things to do

Revision 1.2  2003/07/14 19:26:40  dek
done some clean.up work, since it seems,as if this view becomes reality..

Revision 1.1  2003/07/11 17:53:51  dek
Tree for Downloads - still unstable

*/