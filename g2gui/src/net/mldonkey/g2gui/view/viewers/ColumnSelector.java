/*
 * Copyright 2003
 * g2gui Team
 *
 *
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view.viewers;

import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.helper.VersionCheck;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;


/**
 * ColumnSelector
 *
 * @version $Id: ColumnSelector.java,v 1.11 2004/02/05 20:44:43 psy Exp $
 *
 */
public class ColumnSelector extends Dialog {
    public static final int MAGIC_NUMBER = 65;
    private String[] columnLegend;
    private String allColumnIDs;
    private String leftColumnIDs;
    private String rightColumnIDs;
    private List leftList;
    private List rightList;
    private String prefOption;
    private char bogusColumn;

    /**
     * @param parentShell
     * @param columnLegend
     * @param allColumnIDs
     * @param prefOption
     */
    public ColumnSelector(Shell parentShell, String[] columnLegend, String allColumnIDs,
        String prefOption) {
        super(parentShell);
        
        /* how many columns in total do we have? */
        int totalColumns = allColumnIDs.length();
        System.out.println("All columns: " + totalColumns);
        
        /* remember the gtk bogus column if we use gtk */
        if (VersionCheck.isGtk()) {
	        bogusColumn = allColumnIDs.charAt( allColumnIDs.length() - 1);
	        
	        /* cut the bogus column from the string */
	        allColumnIDs = allColumnIDs.substring(0, allColumnIDs.length() - 1);
	        
	        /* reduce the amount of columns by one */
	        totalColumns -= 1;
        }
        
        System.out.println("allColumnIDs: " + allColumnIDs + ", bogus cut: " + bogusColumn);
        
        /* copy over the ColumnLegends into our array */
        this.columnLegend = new String[totalColumns];
        for (int i = 0; i < totalColumns; i++) {
        	System.out.println("Copying: " + columnLegend[i]);
        	this.columnLegend[i] = columnLegend[i];
        }
        
        this.allColumnIDs = allColumnIDs;
        this.prefOption = prefOption + "TableColumns";
        
        /* check if we have a previously saved column-setup,
         * use all available columns if nothing was saved 
         */ 
        String aString = PreferenceLoader.loadString(this.prefOption);
        
        leftColumnIDs = "";
        rightColumnIDs = ((!aString.equals("")) ? aString : allColumnIDs);
        
        /* check for a gtk bogus column and remove it,
         *  we do not want to have it in our list */
        if ( rightColumnIDs.charAt(rightColumnIDs.length()-1) == 
        	(char) (ColumnSelector.MAGIC_NUMBER + totalColumns) )
        	rightColumnIDs = rightColumnIDs.substring(0, rightColumnIDs.length() - 1); 
        
        //System.out.println("Right: " + rightColumnIDs);
		
        /* check which columns are unused and put them into the left list */
        for (int i = 0; i < totalColumns; i++) {
        	System.out.println("checking: " + allColumnIDs.charAt(i));
        	if (rightColumnIDs.indexOf(allColumnIDs.charAt(i)) == -1) {
                leftColumnIDs += allColumnIDs.charAt(i);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    public Control createDialogArea(Composite oldParent) {
        Composite parent = (Composite) super.createDialogArea(oldParent);

        parent.setLayout(WidgetFactory.createGridLayout(3, 5, 5, 0, 5, false));

        createHeader(parent);
        createLists(parent);
        refreshLists();

        return parent;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(G2GuiResources.getString("TT_ColumnSelector"));
        shell.setImage(G2GuiResources.getImage("table"));
    }

    /**
     * @param parent
     */
    public void createHeader(Composite parent) {
        Label aLabel = new Label(parent, SWT.NONE);
        aLabel.setText("Available");

        new Label(parent, SWT.NONE);

        Label cLabel = new Label(parent, SWT.NONE);
        cLabel.setText("In Use");
    }

    /**
     * @param parent
     */
    public void createLists(Composite parent) {
        int max_characters = 0;
        int tmpi;

        for (int i = 0; i < columnLegend.length; i++) {
            if ((tmpi = G2GuiResources.getString(columnLegend[ i ]).length()) > max_characters) {
                max_characters = tmpi;
            }
        }

        // calculate width/height based on font size
        GC gc = new GC(parent);
        GridData gd = new GridData(GridData.FILL_BOTH);
        int heightHint = (gc.getFontMetrics().getHeight() + 7) * allColumnIDs.length();
        int widthHint = (gc.getFontMetrics().getAverageCharWidth() * max_characters) + 15;
        gc.dispose();

        gd.heightHint = heightHint;
        gd.widthHint = widthHint;

        leftList = new List(parent, SWT.BORDER | SWT.MULTI);
        leftList.setLayoutData(gd);

        Composite arrowComposite = new Composite(parent, SWT.NONE);
        arrowComposite.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 0, 5, false));

        Button moveRight = new Button(arrowComposite, SWT.NONE);
        moveRight.setText(" ---> ");
        moveRight.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        moveRight.addSelectionListener(new ArrowSelectionAdapter(true));

        Button moveLeft = new Button(arrowComposite, SWT.NONE);
        moveLeft.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        moveLeft.setText(" <--- ");
        moveLeft.addSelectionListener(new ArrowSelectionAdapter(false));

        Button restore = new Button(arrowComposite, SWT.NONE);
        restore.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        restore.setText(G2GuiResources.getString("BTN_DEFAULT"));
        restore.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    rightColumnIDs = allColumnIDs;
                    leftColumnIDs = "";
                    refreshLists();
                }
            });

        rightList = new List(parent, SWT.BORDER | SWT.MULTI);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = heightHint;
        gd.widthHint = widthHint;

        rightList.setLayoutData(gd);

        setDragDrop(leftList);
        setDragDrop(rightList);
    }

    /**
     * savePrefs
     */
    public void savePrefs() {
        if (rightColumnIDs.length() > 1) {
            /* add our gtk bogus column before saving */
        	if (VersionCheck.isGtk()) rightColumnIDs += bogusColumn;
        	
        	PreferenceLoader.setValue(prefOption, rightColumnIDs);
            PreferenceLoader.saveStore();
        }
    }

    /**
     * refreshLists
     */
    public void refreshLists() {
        refreshList(leftColumnIDs, leftList);
        refreshList(rightColumnIDs, rightList);
    }

    /**
     * @param string
     * @param list
     */
    public void refreshList(String columnIDs, List list) {
        list.removeAll();
        //System.out.println("Refresh: columnIDs (" + columnIDs.length() + ")");
        //System.out.println("Refresh: columnLegend " + columnLegend.length);
        for (int i = 0; i < columnIDs.length(); i++) {
            int n = columnIDs.charAt(i) - MAGIC_NUMBER;
            list.add(G2GuiResources.getString(columnLegend[ n ]));
        }
    }

    /**
     * @param b
     */
    public void moveItems(boolean b) {
        List list = (b ? leftList : rightList);
        String myString = (b ? leftColumnIDs : rightColumnIDs);
        String otherString = (b ? rightColumnIDs : leftColumnIDs);

        if (list.getSelectionCount() > 0) {
            int[] ind = list.getSelectionIndices();

            String newString = "";
            boolean found;

            for (int i = 0; i < myString.length(); i++) {
                found = false;

                for (int j = 0; j < ind.length; j++) {
                    if (i == ind[ j ]) {
                        otherString += myString.charAt(i);
                        found = true;

                        break;
                    }
                }

                if (!found) {
                    newString += myString.charAt(i);
                }
            }

            myString = newString;

            leftColumnIDs = (b ? myString : otherString);
            rightColumnIDs = (b ? otherString : myString);

            refreshLists();
        }
    }

    /**
     * @param list
     */
    public void setDragDrop(final List list) {
        Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
        int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

        final DragSource source = new DragSource(list, operations);
        source.setTransfer(types);
        source.addDragListener(new DragSourceListener() {
                public void dragStart(DragSourceEvent event) {
                    event.doit = (list.getSelectionCount() != 0);
                }

                public void dragSetData(DragSourceEvent event) {
                    event.data = "DND";
                }

                public void dragFinished(DragSourceEvent event) {
                }
            });

        DropTarget target = new DropTarget(list, operations);
        target.setTransfer(types);
        target.addDropListener(new DropTargetAdapter() {
                public void drop(DropTargetEvent event) {
                    if (!event.data.equals("DND")) {
                        event.detail = DND.DROP_NONE;

                        return;
                    }

                    moveItems(list != leftList);
                }
            });
    }

    /**
     * ArrowSelectionAdapter
     */
    private class ArrowSelectionAdapter extends SelectionAdapter {
        private boolean b;

        public ArrowSelectionAdapter(boolean b) {
            this.b = b;
        }

        public void widgetSelected(SelectionEvent s) {
            moveItems(b);
        }
    }
}


/*
$Log: ColumnSelector.java,v $
Revision 1.11  2004/02/05 20:44:43  psy
hopefully fixed dynamic column behaviour under gtk by introducing a
bogus column.

Revision 1.10  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.9  2003/12/03 22:19:11  lemmy
store g2gui.pref in ~/.g2gui/g2gui.pref instead of the program directory

Revision 1.8  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.7  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.6  2003/11/02 22:27:32  zet
-

Revision 1.5  2003/11/02 22:26:25  zet
add drag&drop

Revision 1.4  2003/11/02 21:14:21  zet
setwidth

Revision 1.3  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.2  2003/10/24 22:49:04  zet
get fontHeight in pixels, not points

Revision 1.1  2003/10/22 01:36:59  zet
add column selector to server/search (might not be finished yet..)


*/
