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
package net.mldonkey.g2gui.view.search;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.helper.WordFilter;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.GTableViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;


/**
 * ResultTableViewer
 *
 * @version $Id: ResultTableViewer.java,v 1.4 2003/10/28 00:36:06 zet Exp $
 *
 */
public class ResultTableViewer extends GTableViewer {
    private CTabItem cTabItem;
    private MouseListener aMouseListener;
    
    public static final int NETWORK = 0;
    public static final int NAME = 1;
    public static final int SIZE = 2;
    public static final int FORMAT = 3;
    public static final int MEDIA = 4;
    public static final int AVAILABILITY = 5;

    public ResultTableViewer(Composite parent, CoreCommunication aCore, CTabItem aCTabItem, MouseListener aMouseListener) {
        super(parent, aCore);
        this.cTabItem = aCTabItem;
        this.aMouseListener = aMouseListener;
        this.cTabItem.setData("gTableViewer", this);
        
        preferenceString = "result";
        columnLabels = new String[] { "SR_NETWORK", "SR_NAME", "SR_SIZE", "SR_FORMAT", "SR_MEDIA", "SR_AVAIL" };

        columnDefaultWidths = new int[] { 58, 300, 65, 45, 50, 70 };

        columnAlignment = new int[] { SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.LEFT, SWT.LEFT, SWT.LEFT };

        //	this.swtLayout = SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI;
        tableContentProvider =  new ResultTableContentProvider( this );
        tableLabelProvider = new ResultTableLabelProvider( this );
        tableSorter = new ResultTableSorter( this );
        tableMenuListener = new ResultTableMenuListener( this );

        this.createContents(parent);
    }

    public void setInput( Object object ) {
        
        tableViewer.setInput( object );
    }
    
    public CTabItem getCTabItem() {
        return cTabItem;
    }
    
    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.helper.OurTableViewer#create()
     */
    protected void createContents(Composite parent) {
        super.createContents(parent);
		tableViewer.addSelectionChangedListener((ResultTableMenuListener) tableMenuListener);
		
        // add optional filters
        if (PreferenceLoader.loadBoolean("searchFilterPornography")) {
            tableViewer.addFilter(new WordFilter(WordFilter.PORNOGRAPHY_FILTER_TYPE));
        } else if (PreferenceLoader.loadBoolean("searchFilterProfanity")) {
            tableViewer.addFilter(new WordFilter(WordFilter.PROFANITY_FILTER_TYPE));
        }

        /* add a menuListener to make the first menu item bold */
        addMenuListener();
        /* add a mouse-listener to catch double-clicks */
        tableViewer.getTable().addMouseListener(aMouseListener);

        /* just show tooltip on user request */
        if (PreferenceLoader.loadBoolean("showSearchTooltip")) {
            final ToolTipHandler tooltip = new ToolTipHandler(tableViewer.getTable().getShell());
            tooltip.activateHoverHelp(tableViewer.getTable());
        }
    }

    public ResultTableMenuListener getMenuListener() {
        return (ResultTableMenuListener) tableMenuListener;
    }

    /**
     * Emulated tooltip handler
     * Notice that we could display anything in a tooltip besides text and images.
     * For instance, it might make sense to embed large tables of data or buttons linking
     * data under inspection to material elsewhere, or perform dynamic lookup for creating
     * tooltip text on the fly.
     */
    private static class ToolTipHandler {
        private Shell tipShell;
        private CLabel tipLabelImage;
        private Label tipLabelText;
        private Widget tipWidget; // widget this tooltip is hovering over
        private Point tipPosition; // the position being hovered over

        /**
         * Creates a new tooltip handler
         *
         * @param parent the parent Shell
         */
        public ToolTipHandler(Composite parent) {
            final Display display = parent.getDisplay();
            tipShell = new Shell(parent.getShell(), SWT.ON_TOP);

            GridLayout gridLayout = CGridLayout.createGL(1, 2, 2, 0, 0, false);
            tipShell.setLayout(gridLayout);
            tipShell.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
            tipLabelImage = new CLabel(tipShell, SWT.NONE);
            tipLabelImage.setAlignment(SWT.LEFT);
            tipLabelImage.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
            tipLabelImage.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
            tipLabelImage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
            tipLabelText = new Label(tipShell, SWT.NONE);
            tipLabelText.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
            tipLabelText.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
            tipLabelText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
        }

        /**
         * Enables customized hover help for a specified control
         *
         * @control the control on which to enable hoverhelp
         */
        public void activateHoverHelp(final Control control) {
            /*
             * Get out of the way if we attempt to activate the control underneath the tooltip
             */
            control.addMouseListener(new MouseAdapter() {
                    public void mouseDown(MouseEvent e) {
                        if (tipShell.isVisible()) {
                            tipShell.setVisible(false);
                        }
                    }
                });

            /*
             * Trap hover events to pop-up tooltip
             */
            control.addMouseTrackListener(new MouseTrackAdapter() {
                    public void mouseExit(MouseEvent e) {
                        if (tipShell.isVisible()) {
                            tipShell.setVisible(false);
                        }

                        tipWidget = null;
                    }

                    public void mouseHover(MouseEvent event) {
                        if (event.widget == null) {
                            return;
                        }

                        Point pt = new Point(event.x, event.y);
                        Widget widget = event.widget;

                        /* get the selected item from the table */
                        Table w = (Table) widget;
                        widget = w.getItem(pt);

                        if (widget == null) {
                            tipShell.setVisible(false);
                            tipWidget = null;
                        }

                        if (widget == tipWidget) {
                            return;
                        }

                        tipWidget = widget;

                        // Create the tooltip on demand
                        if (widget instanceof TableItem) {
                            TableItem tableItem = (TableItem) widget;
                            ResultInfo aResult = (ResultInfo) tableItem.getData();
                            Image image = null;
                            Program p;

                            if (!aResult.getFormat().equals("")) {
                                p = Program.findProgram(aResult.getFormat());
                            } else {
                                String temp = aResult.getName();
                                int index = temp.lastIndexOf(".");

                                try {
                                    temp = temp.substring(index);
                                } catch (Exception e) {
                                    p = null;
                                }

                                p = Program.findProgram(temp);
                            }

                            if (p != null) {
                                ImageData data = p.getImageData();

                                if (data != null) {
                                    if (G2GuiResources.getImage(p.getName()) == null) {
                                        G2GuiResources.getImageRegistry().put(p.getName(), new Image(null, data));
                                    }
                                }
                            }

                            String imageText = aResult.getName();
                            String aString = "";

                            if (!aResult.getFormat().equals("")) {
                                aString += (G2GuiResources.getString("ST_TT_FORMAT") + aResult.getFormat() + "\n");
                            }

                            aString += (G2GuiResources.getString("ST_TT_LINK") + aResult.getLink() + "\n");
                            aString += (G2GuiResources.getString("ST_TT_NETWORK") + aResult.getNetwork().getNetworkName() + "\n");
                            aString += (G2GuiResources.getString("ST_TT_SIZE") + aResult.getStringSize() + "\n");
                            aString += (G2GuiResources.getString("ST_TT_AVAIL") + aResult.getAvail() + " " +
                            G2GuiResources.getString("ST_TT_SOURCES") + "\n");

                            if (aResult.getType().equals("Audio")) {
                                if (!aResult.getBitrate().equals("")) {
                                    aString += (G2GuiResources.getString("ST_TT_BITRATE") + aResult.getBitrate() + "\n");
                                }

                                if (!aResult.getLength().equals("")) {
                                    aString += (G2GuiResources.getString("ST_TT_LENGTH") + aResult.getLength());
                                }
                            }

                            if (!aResult.getHistory()) {
                                aString = aString + "\n" + G2GuiResources.getString("ST_TT_DOWNLOADED");
                            }

                            // set the text/image for the tooltip 
                            if (aString != null) {
                                tipLabelText.setText(aString);
                            } else {
                                tipLabelText.setText("");
                            }

                            /* load the image only if we have a associated programm */
                            if (p != null) {
                                tipLabelImage.setImage(G2GuiResources.getImage(p.getName()));
                            } else {
                                tipLabelImage.setImage(null);
                            }

                            tipLabelImage.setText(imageText);

                            /* pack/layout the tooltip */
                            tipShell.pack();
                            tipPosition = control.toDisplay(pt);
                            setHoverLocation(tipShell, tipPosition);
                            tipShell.setVisible(true);
                        }
                    }
                });
        }

        /**
         * Sets the location for a hovering shell
         * @param shell the object that is to hover
         * @param position the position of a widget to hover over
         * @return the top-left location for a hovering box
         */
        private void setHoverLocation(Shell shell, Point position) {
            Rectangle displayBounds = shell.getDisplay().getBounds();
            Rectangle shellBounds = shell.getBounds();
            shellBounds.x = Math.max(Math.min(position.x, displayBounds.width - shellBounds.width), 0);
            shellBounds.y = Math.max(Math.min(position.y + 16, displayBounds.height - shellBounds.height), 0);
            shell.setBounds(shellBounds);
        }
    }
}


/*
$Log: ResultTableViewer.java,v $
Revision 1.4  2003/10/28 00:36:06  zet
move columnselector into the pane

Revision 1.3  2003/10/22 02:25:10  zet
+prefString

Revision 1.2  2003/10/22 01:37:45  zet
add column selector to server/search (might not be finished yet..)

Revision 1.1  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

*/
