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
package net.mldonkey.g2gui.view.server;

import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.viewers.table.GTableView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * ServerTableViewer
 *
 * @version $Id: ServerTableView.java,v 1.5 2003/12/04 08:47:30 lemmy Exp $ 
 *
 */
public class ServerTableView extends GTableView {
	private boolean oldValue = PreferenceLoader.loadBoolean( "displayTableColors" );
	
	public static final int NETWORK = 0;
	public static final int NAME = 1;
	public static final int DESCRIPTION = 2;
	public static final int ADDRESS = 3;
	public static final int PORT = 4;
	public static final int SCORE = 5;
	public static final int USERS = 6;
	public static final int FILES = 7;
	public static final int STATE = 8;
	public static final int FAVORITE = 9;
	

	/**
	 * @param composite
	 * @param core
	 */
	public ServerTableView( ViewFrame viewFrame ) {
		super( viewFrame );
		preferenceString = "server";
		
		/* proto <= 16 does not support favorites */					
		if ( core.getProtoToUse() <= 16 ) {
			columnLabels = new String[] { "SVT_NETWORK", "SVT_NAME", "SVT_DESC", "SVT_ADDRESS",
												"SVT_PORT", "SVT_SERVERSCORE", "SVT_USERS",	"SVT_FILES",
												"SVT_STATE" };	
			columnDefaultWidths = new int[] { 70, 160, 160, 120, 50, 55, 55, 60, 80 };
			columnAlignment = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT,
											SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT };
		}
		else {
			columnLabels = new String[] {	"SVT_NETWORK", "SVT_NAME", "SVT_DESC",
												"SVT_ADDRESS", "SVT_PORT", "SVT_SERVERSCORE",
												"SVT_USERS", "SVT_FILES", "SVT_STATE", "SVT_FAVORITES" };	
			columnDefaultWidths = new int[] { 70, 160, 160, 120, 50, 55, 55, 60, 80, 50 };
			columnAlignment = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT,
											SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT, SWT.LEFT };
		}
		
		tableContentProvider = new ServerTableContentProvider( this );
		tableLabelProvider = new ServerTableLabelProvider( this );
		gSorter = new ServerTableSorter( this );
		tableMenuListener = new ServerTableMenuListener( this );
		

		this.createContents( viewFrame.getChildComposite() );
	}

	protected void createContents( Composite parent ) {
		super.createContents( parent );
		sViewer.addSelectionChangedListener((ServerTableMenuListener) tableMenuListener);
		
		// add a menulistener to make the first menu item bold
	    addMenuListener();
	}
	
	/* (non-Javadoc)
	 * Method declared in Viewer.
	 * This implementatation additionaly unmaps all the elements.
	 */
	public void setInput( Object object ) {
	    sViewer.setInput( object );
	}
	
	/**
	 * Updates this table on preference close
	 */
	public void updateDisplay() {
	    super.updateDisplay();
		// displayTableColors changed?
		boolean temp2 = PreferenceLoader.loadBoolean( "displayTableColors" );
		if ( oldValue != temp2 ) {
			( ( ServerTableLabelProvider ) getTableViewer().getLabelProvider() ).setColors( temp2 );
			getTableViewer().refresh();
			this.oldValue = temp2;
		}
	}
}

/*
$Log: ServerTableView.java,v $
Revision 1.5  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.4  2003/11/29 01:51:53  zet
a few more viewframe changes.. will continue later.

Revision 1.3  2003/11/09 23:09:57  lemmy
remove "Show connected Servers only"
added filter saving in searchtab

Revision 1.2  2003/11/06 13:52:33  lemmy
filters back working

Revision 1.1  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.1  2003/10/31 13:16:32  lemmy
Rename Viewer -> Page
Constructors changed

Revision 1.9  2003/10/31 11:04:40  lemmy
translate todo

Revision 1.8  2003/10/31 10:42:47  lemmy
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.7  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.6  2003/10/29 16:56:21  lemmy
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.5  2003/10/28 11:07:32  lemmy
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.4  2003/10/26 11:07:33  lemmy
fixed zero width for description

Revision 1.3  2003/10/22 02:25:24  zet
+prefString

Revision 1.2  2003/10/22 01:37:55  zet
add column selector to server/search (might not be finished yet..)

Revision 1.1  2003/10/21 17:00:45  lemmy
class hierarchy for tableviewer

*/