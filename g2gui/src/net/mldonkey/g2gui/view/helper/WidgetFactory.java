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
package net.mldonkey.g2gui.view.helper;

import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


/**
 * WidgetFactory
 *
 * @version $Id: WidgetFactory.java,v 1.3 2003/12/03 22:19:45 lemmy Exp $
 *
 */
public class WidgetFactory {
    /**
     * @param parent
     * @return ViewForm
     */
    public static ViewForm createViewForm(Composite parent) {
        return new ViewForm(parent,
            SWT.BORDER | (PreferenceLoader.loadBoolean("flatInterface") ? SWT.FLAT : SWT.NONE));
    }

    /**
     * @param numColumns
     * @param marginWidth
     * @param marginHeight
     * @param horizontalSpacing
     * @param verticalSpacing
     * @param makeColumnsEqualWidth
     * @return GridLayout
     */
    public static GridLayout createGridLayout(int numColumns, int marginWidth, int marginHeight,
        int horizontalSpacing, int verticalSpacing, boolean makeColumnsEqualWidth) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = numColumns;
        gridLayout.marginWidth = marginWidth;
        gridLayout.marginHeight = marginHeight;
        gridLayout.horizontalSpacing = horizontalSpacing;
        gridLayout.verticalSpacing = verticalSpacing;
        gridLayout.makeColumnsEqualWidth = makeColumnsEqualWidth;

        return gridLayout;
    }

    /**
     * @param parent
     * @param text
     * @param image
     * @return CLabel
     */
    public static CLabel createCLabel(ViewForm parent, String text, String image) {
        // GTK/SWT3-M4: (<unknown>:5346): GLib-GObject-CRITICAL **: file gtype.c: line 1942 (g_type_add_interface_static): assertion `g_type_parent (interface_type) == G_TYPE_INTERFACE' failed
        CLabel cLabel = new CLabel(parent, SWT.LEFT);
        cLabel.setText(G2GuiResources.getString(text));

        cLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        cLabel.setImage(G2GuiResources.getImage(image));

        if (PreferenceLoader.loadBoolean("useGradient")) {
            cLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
            cLabel.setBackground(new Color[] {
                    parent.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND),
                    parent.getBackground()
                }, new int[] { 100 });
        }

        return cLabel;
    }

    /**
	 * Create a sashForm, restoring its preferences
     * @param parent
     * @param prefString
     * @return SashForm
     */
    public static SashForm createSashForm(Composite parent, String prefString) {
        final String orientationPrefString = prefString + "Orientation";

        int orientation = PreferenceLoader.loadInteger(orientationPrefString);

        if ((orientation != SWT.HORIZONTAL) && (orientation != SWT.VERTICAL)) {
            orientation = PreferenceLoader.getDefaultInt(orientationPrefString);
        }

        final SashForm sashForm = new SashForm(parent, orientation);
        sashForm.setData("prefString", prefString);

        sashForm.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                	PreferenceLoader.setValue(orientationPrefString, sashForm.getOrientation());
                }
            });

        return sashForm;
    }

    /**
     * Load the weights & maximized control # of a sash (call after the sash has children)
     * @param sashForm
     * @param prefString
     */
    public static void loadSashForm(SashForm sashForm, String prefString) {
        final String sashChildPrefString = prefString + "Child";
        int maximizeControl = PreferenceLoader.loadInteger(prefString + "Maximized");

        // First set the sash weights if available
        if (sashPrefsExist(sashForm, prefString)) {
            int[] weights = new int[ sashForm.getChildren().length ];

            for (int i = 0; i < sashForm.getChildren().length; i++) {
                Rectangle bounds = PreferenceLoader.loadRectangle(sashChildPrefString + i);
                weights[ i ] = (sashForm.getOrientation() == SWT.HORIZONTAL) ? bounds.width
                                                                             : bounds.height;
            }

			// Weights can not be all 0
            for (int i = 0; i < weights.length; i++) {
                if (weights[ i ] > 0) {
                    sashForm.setWeights(weights);

                    break;
                }
            }
        }

        // Then check for maximize (weights still in effect)
        if ((maximizeControl > -1) && (maximizeControl <= sashForm.getChildren().length)) {
            sashForm.setMaximizedControl(sashForm.getChildren()[ maximizeControl ]);
        }

        // Save the control size
        for (int i = 0; i < sashForm.getChildren().length; i++) {
            final Control control = sashForm.getChildren()[ i ];
            final int childNumber = i;
            control.addControlListener(new ControlAdapter() {
                    public void controlResized(ControlEvent e) {
                        Control aControl = (Control) e.widget;

                        if ((aControl.getBounds().width > 0) && (aControl.getBounds().height > 0)) {
                            PreferenceConverter.setValue(PreferenceLoader.getPreferenceStore(), sashChildPrefString + childNumber,
                                aControl.getBounds());
                        }
                    }
                });
        }
    }

    /**
     * @param sashForm
     * @param prefString
     * @return true if all sashChildren preferences exist
     */
    public static boolean sashPrefsExist(SashForm sashForm, String prefString) {
        for (int i = 0; i < sashForm.getChildren().length; i++) {
            if (!PreferenceLoader.contains(prefString + "Child" + i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Save the maximized control # of a sash 
     * @param sashForm
     * @param control
     */
    public static void setMaximizedSashFormControl(SashForm sashForm, Control control) {
        String maximizedPrefString = null;

        if (sashForm.getData("prefString") != null) {
            maximizedPrefString = (String) sashForm.getData("prefString") + "Maximized";
        }

        if (sashForm.getMaximizedControl() == null) {
            sashForm.setMaximizedControl(control);

            if (maximizedPrefString != null) {
                for (int i = 0; i < sashForm.getChildren().length; i++) {
                    if (control == sashForm.getChildren()[ i ]) {
                        PreferenceLoader.setValue(maximizedPrefString, i);

                        break;
                    }
                }
            }
        } else {
            sashForm.setMaximizedControl(null);

            if (maximizedPrefString != null) {
                PreferenceLoader.setValue(maximizedPrefString, -1);
            }
        }
    }
}


/*
$Log: WidgetFactory.java,v $
Revision 1.3  2003/12/03 22:19:45  lemmy
store g2gui.pref in ~/.g2gui/g2gui.pref instead of the program directory

Revision 1.2  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.1  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

*/
