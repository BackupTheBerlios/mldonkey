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
import java.util.ResourceBundle;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.transferTree.DownloadTableTreeContentProvider;
import net.mldonkey.g2gui.view.transferTree.DownloadTableTreeViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Main
 *
 * @author $user$
 * @version $Id: TransferTab.java,v 1.23 2003/08/06 17:10:50 zet Exp $ 
 *
 */
public class TransferTab extends GuiTab  {
	private boolean active;
	private CoreCommunication mldonkey;
	private Label label;
	private long timestamp = 0;
	private Font font = new Font(null, "Helvetica", 12, SWT.BOLD); // fix later
	public static ResourceBundle res = ResourceBundle.getBundle("g2gui");
	public static DecimalFormat decimalFormat = new DecimalFormat( "#.#" );
	private DownloadTableTreeViewer downloadTableTreeViewer;
		
	/**
	 * @param gui where this tab belongs to
	 */
	public TransferTab( MainTab gui ) {
		super( gui );
		this.mldonkey = gui.getCore();		
		
		createButton("TransfersButton", 
						bundle.getString("TT_TransfersButton"),
						bundle.getString("TT_TransfersButtonToolTip"));
		createContents(this.content);
		
	}
	
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.G2guiTab#createContents(org.eclipse.swt.widgets.Composite)
	 */	 
	protected void createContents( Composite parent ) {
		SashForm main = new SashForm( parent, SWT.VERTICAL );
		
		Composite downloadComposite = new Composite( main, SWT.BORDER );
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		downloadComposite.setLayout( gridLayout );
			
			
		// all this junk for right alignment? 	
		Composite downloadHeader = new Composite(downloadComposite, SWT.NONE);
		downloadHeader.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 10;
		downloadHeader.setLayout(layout);
		downloadHeader.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));

		Composite c = new Composite(downloadHeader, SWT.NONE);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		c.setLayoutData(data);

		// fix font/color later
		Label transferHeader = new Label(c, SWT.NONE);
		transferHeader.setText(res.getString("TT_Transfer_Header"));
		transferHeader.setFont(font);
		transferHeader.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		transferHeader.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		GridLayout layout2 = new GridLayout();
		layout2.marginWidth = layout2.marginHeight = 0;
		c.setLayout(layout2);

		label = new Label(downloadHeader, SWT.NONE);
		label.setText("");
		label.setFont(font);
		label.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		label.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
	
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData(data);
	
		// Bottom sash			
		Composite upload = new Composite( main, SWT.BORDER );
		main.setWeights( new int[] {11,1});
		// old non viewer
		// new DownloadTable ( download, mldonkey, this );
		downloadTableTreeViewer = new DownloadTableTreeViewer ( downloadComposite, mldonkey, this );
		
		mldonkey.getFileInfoIntMap().addObserver( this );
		
	}

	/** (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) {

		// no need to update so often
		if (System.currentTimeMillis() > timestamp + 5000) {

			timestamp = System.currentTimeMillis();
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
						totalRate += fileInfo.getRate();
						if (DownloadTableTreeContentProvider.isInteresting(fileInfo)) 
							totalFiles++;
						if (fileInfo.getState().getState() == EnumFileState.QUEUED) 
							totalQueued++;
					}
				} 
				// localise this
				String totalQueuedString = (totalQueued > 0 ? " (" + totalQueued + " queued)" : "");
				runLabelUpdate(totalFiles + " files, " + decimalFormat.format(totalRate) + " KB/s" + totalQueuedString);
			}
		}
	}
	
	public void runLabelUpdate(final String text) 
		{
			Shell shell = MainTab.getShell();
			if(!shell.isDisposed() && shell !=null && shell.getDisplay()!=null) {
				shell.getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (!label.isDisposed()) {
							label.setText(text);
							label.getParent().layout();
						}
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
	}
}

/*
$Log: TransferTab.java,v $
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