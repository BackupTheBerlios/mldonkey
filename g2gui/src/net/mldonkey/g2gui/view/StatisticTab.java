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
 * ( at your option ) any later version.
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

import java.util.Observable;

import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.helper.CCLabel;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.statistic.GraphControl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Statistic Tab
 *
 * @version $Id: StatisticTab.java,v 1.26 2003/09/18 09:44:57 lemmster Exp $
 */
public class StatisticTab extends GuiTab {
    private GraphControl uploadsGraphControl;
    private String uploadsGraphName = "Uploads";
    private Color uploadsGraphColor1 = new Color( null, 255, 0, 0 );
    private Color uploadsGraphColor2 = new Color( null, 125, 0, 0 );
    private GraphControl downloadsGraphControl;
    private String downloadsGraphName = "Downloads";
    private Color downloadsGraphColor1 = new Color( null, 0, 0, 255 );
    private Color downloadsGraphColor2 = new Color( null, 0, 0, 125 );

    /**
     * default constructor
     * @param gui The GUI-Objekt representing the top-level gui-layer
     */
    public StatisticTab( MainTab gui ) {
        super( gui );
        createButton( "StatisticsButton", G2GuiResources.getString( "TT_StatisticsButton" ),
                      G2GuiResources.getString( "TT_StatisticsButtonToolTip" ) );
        createContents( this.subContent );
        gui.getCore().getClientStats().addObserver( this );
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    protected void createContents( Composite parent ) {
        final SashForm mainSash = new SashForm( parent, SWT.VERTICAL );
        mainSash.setLayout( new FillLayout() );

        /* Top composite for other stats */
        ViewForm statsViewForm =
            new ViewForm( mainSash,
                          SWT.BORDER
                          | ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );
        statsViewForm.setLayoutData( new GridData( GridData.FILL_BOTH ) );

        CLabel statsCLabel =
            CCLabel.createCL( statsViewForm, "TT_StatisticsButton", "StatisticsButtonSmallTitlebar" );

        Composite statsComposite = new Composite( statsViewForm, SWT.NONE );
        statsComposite.setLayout( new FillLayout() );

        Button b = new Button( statsComposite, SWT.NONE );
        b.setText( "<gui protocol needs more stats>" );
        b.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent s ) {
                    mainSash.setWeights( new int[] { 0, 100 } );
                }
            } );

        statsViewForm.setTopLeft( statsCLabel );
        statsViewForm.setContent( statsComposite );

        /* Bottom graph for Sash */
        SashForm graphSash = new SashForm( mainSash, SWT.HORIZONTAL );

        ViewForm downloadsGraphViewForm =
            new ViewForm( graphSash,
                          SWT.BORDER
                          | ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );

        ViewForm uploadsGraphViewForm =
            new ViewForm( graphSash,
                          SWT.BORDER
                          | ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );

        CLabel downloadsCLabel =
            CCLabel.createCL( downloadsGraphViewForm, "TT_Downloads", "StatisticsButtonSmallTitlebar" );
        CLabel uploadsCLabel =
            CCLabel.createCL( uploadsGraphViewForm, "TT_Uploads", "StatisticsButtonSmallTitlebar" );

        downloadsGraphControl =
            new GraphControl( downloadsGraphViewForm, downloadsGraphName, downloadsGraphColor1,
                              downloadsGraphColor2 );
        uploadsGraphControl =
            new GraphControl( uploadsGraphViewForm, uploadsGraphName, 
            				  uploadsGraphColor1, uploadsGraphColor2 );

        downloadsGraphViewForm.setTopLeft( downloadsCLabel );
        downloadsGraphViewForm.setContent( downloadsGraphControl );
        uploadsGraphViewForm.setTopLeft( uploadsCLabel );
        uploadsGraphViewForm.setContent( uploadsGraphControl );

        /* the both clabes */
        uploadsCLabel.addMouseListener( new MaximizeSashMouseAdapter( graphSash, uploadsGraphViewForm ) );
        downloadsCLabel.addMouseListener( new MaximizeSashMouseAdapter( graphSash, downloadsGraphViewForm ) );

        /* Until top composite has stats */
        mainSash.setWeights( new int[] { 0, 10 } );
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     * @param receivedInfo DOCUMENT ME!
     */
    public void update( Observable arg0, Object receivedInfo ) {
        ClientStats clientInfo = ( ClientStats ) receivedInfo;
        uploadsGraphControl.addPointToGraph( clientInfo.getTcpUpRate() );
        downloadsGraphControl.addPointToGraph( clientInfo.getTcpDownRate() );
        uploadsGraphControl.redraw();
        downloadsGraphControl.redraw();
    }

    /**
     * DOCUMENT ME!
     *
     * @param removeObserver DOCUMENT ME!
     */
    public void setInActive( boolean removeObserver ) {
        // Do not remove Observer
        super.setInActive();
    }

    /**
     * DOCUMENT ME!
     */
    public void dispose() {
        super.dispose();
        uploadsGraphColor1.dispose();
        uploadsGraphColor2.dispose();
        downloadsGraphColor1.dispose();
        downloadsGraphColor2.dispose();
    }

    public class MaximizeSashMouseAdapter extends MouseAdapter {
        private SashForm sashForm;
        private Control control;
		
		/**
		 * DOCUMENT ME!
		 * 
		 * @param sashForm DOCUMENT ME!
		 * @param control DOCUMENT ME!
		 */
        public MaximizeSashMouseAdapter( SashForm sashForm, Control control ) {
            this.sashForm = sashForm;
            this.control = control;
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void mouseDoubleClick( MouseEvent e ) {
            if ( sashForm.getMaximizedControl() == null )
                sashForm.setMaximizedControl( control );
            else
                sashForm.setMaximizedControl( null );
        }
    }
}

/*
$Log: StatisticTab.java,v $
Revision 1.26  2003/09/18 09:44:57  lemmster
checkstyle

Revision 1.25  2003/09/16 01:58:25  zet
maximize on dblclick

Revision 1.24  2003/09/13 22:24:42  zet
int[] array

Revision 1.23  2003/08/29 22:13:44  zet
fix icon

Revision 1.22  2003/08/29 22:11:47  zet
add CCLabel helper class

Revision 1.21  2003/08/29 21:42:11  zet
add shadow

Revision 1.20  2003/08/25 22:20:25  zet
*** empty log message ***

Revision 1.19  2003/08/23 15:21:37  zet
remove @author

Revision 1.18  2003/08/23 01:12:43  zet
remove todos

Revision 1.17  2003/08/22 21:06:48  lemmster
replace $user$ with $Author: lemmster $

Revision 1.16  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.15  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging


*/
