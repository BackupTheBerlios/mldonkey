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
package net.mldonkey.g2gui.view.transfer;

import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.downloadTable.DownloadTableTreeViewer;

import org.eclipse.jface.preference.PreferenceStore;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;


/**
 * ColumnSelector
 *
 * @version $Id: ColumnSelector.java,v 1.2 2003/10/15 18:25:00 zet Exp $
 *
 */
public class ColumnSelector {
    private DownloadTableTreeViewer downloadTableTreeViewer;
    private Shell shell;
    private Display desktop = Display.getCurrent();
    private String[] columnLegend;
    private String allColumnIDs;
    private String leftColumnIDs;
    private String rightColumnIDs;
    private List leftList;
    private List rightList;
    private String prefOption;
    int width = 300;
    int height = 450;

    public ColumnSelector( String[] columnLegend, String allColumnIDs, String prefOption, DownloadTableTreeViewer downloadTableTreeViewer ) {
        this.columnLegend = columnLegend;
        this.allColumnIDs = allColumnIDs;
        this.prefOption = prefOption;
        this.downloadTableTreeViewer = downloadTableTreeViewer;

        String aString = PreferenceLoader.loadString( prefOption );
        rightColumnIDs = ( ( !aString.equals( "" ) ) ? aString : allColumnIDs );
        leftColumnIDs = "";

        for ( int i = 0; i < allColumnIDs.length(); i++ ) {
            if ( rightColumnIDs.indexOf( allColumnIDs.charAt( i ) ) == -1 ) {
                leftColumnIDs += allColumnIDs.charAt( i );
            }
        }

        createContents();
    }

    public void createContents() {
        shell = new Shell( SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL );
        shell.setBounds( ( desktop.getBounds().width - width ) / 2, ( desktop.getBounds().height - height ) / 2, width, height );
        shell.setImage( G2GuiResources.getImage( "table" ) );
        shell.setText( G2GuiResources.getString( "TT_ColumnSelector" ) );
        shell.setLayout( new FillLayout() );

        Composite parent = new Composite( shell, SWT.NONE );
        parent.setLayout( CGridLayout.createGL( 3, 5, 5, 0, 5, false ) );

        createHeader( parent );
        createLists( parent );
        createButtons( parent );
        refreshLists();
        shell.open();
    }

    public void createHeader( Composite parent ) {
        Label aLabel = new Label( parent, SWT.NONE );
        aLabel.setText( "Available" );

        Label bLabel = new Label( parent, SWT.NONE );

        Label cLabel = new Label( parent, SWT.NONE );
        cLabel.setText( "In Use" );
    }

    public void createLists( Composite parent ) {
        leftList = new List( parent, SWT.BORDER | SWT.MULTI );
        leftList.setLayoutData( new GridData( GridData.FILL_BOTH ) );

        Composite arrowComposite = new Composite( parent, SWT.NONE );
        arrowComposite.setLayout( CGridLayout.createGL( 1, 5, 5, 0, 5, false ) );

        Button moveRight = new Button( arrowComposite, SWT.NONE );
        moveRight.setText( " ---> " );
        moveRight.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        moveRight.addSelectionListener( new ArrowSelectionAdapter( true ) );

        Button moveLeft = new Button( arrowComposite, SWT.NONE );
        moveLeft.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        moveLeft.setText( " <--- " );
        moveLeft.addSelectionListener( new ArrowSelectionAdapter( false ) );

        Button restore = new Button( arrowComposite, SWT.NONE );
        restore.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        restore.setText( G2GuiResources.getString( "BTN_DEFAULT" ) );
        restore.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent s ) {
                    rightColumnIDs = allColumnIDs;
                    leftColumnIDs = "";
                    refreshLists();
                }
            } );

        rightList = new List( parent, SWT.BORDER | SWT.MULTI );
        rightList.setLayoutData( new GridData( GridData.FILL_BOTH ) );
    }

    public void createButtons( Composite parent ) {
        Composite buttonComposite = new Composite( parent, SWT.NONE );
        buttonComposite.setLayout( CGridLayout.createGL( 2, 5, 5, 5, 5, false ) );

        GridData gd = new GridData( GridData.FILL_HORIZONTAL );
        gd.horizontalSpan = 3;
        buttonComposite.setLayoutData( gd );

        Button cancelButton = new Button( buttonComposite, SWT.NONE );
        cancelButton.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END ) );
        cancelButton.setText( G2GuiResources.getString( "BTN_CANCEL" ) );
        cancelButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent s ) {
                    shell.close();
                }
            } );

        Button okButton = new Button( buttonComposite, SWT.NONE );
        okButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
        okButton.setText( G2GuiResources.getString( "BTN_OK" ) );
        okButton.setFocus();
        okButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent s ) {
                    if ( rightColumnIDs.length() > 1 ) {
                        PreferenceStore p = PreferenceLoader.getPreferenceStore();
                        p.setValue( prefOption, rightColumnIDs );
                        downloadTableTreeViewer.resetColumns();
                    }

                    shell.close();
                }
            } );
    }

    public void refreshLists() {
        refreshList( leftColumnIDs, leftList );
        refreshList( rightColumnIDs, rightList );
    }

    public void refreshList( String string, List list ) {
        list.removeAll();

        for ( int i = 0; i < string.length(); i++ ) {
            int n = string.charAt( i ) - 65;
            list.add( G2GuiResources.getString( columnLegend[ n ] ) );
        }
    }

    private class ArrowSelectionAdapter extends SelectionAdapter {
        private boolean b;

        public ArrowSelectionAdapter( boolean b ) {
            this.b = b;
        }

        public void widgetSelected( SelectionEvent s ) {
            List list = ( b ? leftList : rightList );
            String myString = ( b ? leftColumnIDs : rightColumnIDs );
            String otherString = ( b ? rightColumnIDs : leftColumnIDs );

            if ( list.getSelectionCount() > 0 ) {
                int[] ind = list.getSelectionIndices();

                String newString = "";
                boolean found;

                for ( int i = 0; i < myString.length(); i++ ) {
                    found = false;

                    for ( int j = 0; j < ind.length; j++ ) {
                        if ( i == ind[ j ] ) {
                            otherString += myString.charAt( i );
                            found = true;

                            break;
                        }
                    }

                    if ( !found ) {
                        newString += myString.charAt( i );
                    }
                }

                myString = newString;

                leftColumnIDs = ( b ? myString : otherString );
                rightColumnIDs = ( b ? otherString : myString );

                refreshLists();
            }
        }
    }
}


/*
$Log: ColumnSelector.java,v $
Revision 1.2  2003/10/15 18:25:00  zet
icons

Revision 1.1  2003/10/12 15:58:30  zet
rewrite downloads table & more..

*/
