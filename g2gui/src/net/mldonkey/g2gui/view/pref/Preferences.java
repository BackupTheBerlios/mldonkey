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
package net.mldonkey.g2gui.view.pref;

import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.OptionsInfo;
import net.mldonkey.g2gui.model.OptionsInfoMap;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.widgets.Shell;

/**
 * OptionTree2
 *
 *
 * @version $Id: Preferences.java,v 1.37 2003/10/15 22:06:13 zet Exp $
 *
 */
public class Preferences extends PreferenceManager {
    private boolean connected = false;
    private PreferenceDialog prefdialog;
    private PreferenceStore preferenceStore;

    /**
     * @param preferenceStore where to store the values at
     */
    public Preferences( PreferenceStore preferenceStore ) {
        this.preferenceStore = preferenceStore;

		/* main page */
		PreferencePage g2gui = new G2GuiPref( "G2Gui", FieldEditorPreferencePage.GRID );
        g2gui.setPreferenceStore( preferenceStore );
        PreferenceNode g2GuiRootNode = new PreferenceNode( "G2gui", g2gui );

		/* display page */
        if ( PreferenceLoader.loadBoolean( "advancedMode" ) ) {
            PreferencePage preferencePage = new G2GuiDisplay( "Display", FieldEditorPreferencePage.GRID );
            preferencePage.setPreferenceStore( preferenceStore );
		   
		    PreferenceNode g2guiDisplayNode = new PreferenceNode( "Display", preferencePage );
		
				preferencePage = new G2GuiDisplayConsole( "Console", FieldEditorPreferencePage.GRID );
				preferencePage.setPreferenceStore( preferenceStore );
				g2guiDisplayNode.add( new PreferenceNode( "Console" , preferencePage ) );
          
       			preferencePage = new G2GuiDisplayDownloads( "Downloads", FieldEditorPreferencePage.GRID );
        		preferencePage.setPreferenceStore( preferenceStore );
         		g2guiDisplayNode.add( new PreferenceNode( "Downloads", preferencePage ) );
            
			g2GuiRootNode.add( g2guiDisplayNode );
        }
        
        /* news page */
        PreferencePage g2guiNews = new G2GuiNews( "News", FieldEditorPreferencePage.GRID );
		g2guiNews.setPreferenceStore( preferenceStore );
		g2GuiRootNode.add( new PreferenceNode( "News", g2guiNews ) );

		/* advanced page */
		PreferencePage g2guiAdvanced = new G2GuiAdvanced( "Advanced", FieldEditorPreferencePage.GRID );
        g2guiAdvanced.setPreferenceStore( preferenceStore );
        g2GuiRootNode.add( new PreferenceNode( "Advanced", g2guiAdvanced ) );

		/* add all of this to the root node */
        addToRoot( g2GuiRootNode );
    }

    /**
     * @param shell the parent shell, where this pref-window has to be opened
     * @param mldonkey the Core we want to configure
     */
    public void open( Shell shell, CoreCommunication mldonkey ) {
        try {
            initialize( preferenceStore );
        }
        catch ( IOException e ) {
            System.out.println( "initalizing Preferences Dialog failed due to IOException" );
        }
        prefdialog = new PreferenceDialog( shell, this );
        PreferenceDialog.setDefaultImage( G2GuiResources.getImage( "ProgramIcon" ) );
        if ( ( mldonkey != null ) && ( mldonkey.isConnected() ) ) {
            this.connected = true;
            createMLDonkeyOptions( connected, mldonkey );
        }

        //myprefs.addToRoot( new PreferenceNode
        //		( "mldonkey", new General( preferenceStore, connected, mldonkey ) ) );
        //		
        //myprefs.addToRoot( new PreferenceNode
        //		( "eDonkey", new Edonkey( preferenceStore, connected ) ) );	
        prefdialog.open();
    }

    /**
     * @param connected are we connected to the Core
     * @param mldonkey the Core were i get all my options from
     */
    private void createMLDonkeyOptions( boolean connected, CoreCommunication mldonkey ) {
        OptionsInfoMap options = mldonkey.getOptionsInfoMap();
        OptionsPreferenceStore optionsStore = new OptionsPreferenceStore();
        optionsStore.setInput( options );
        Map sections = new HashMap();
        Map plugins = new HashMap();
        MLDonkeyOptions advanced = null;
        /*now we iterate over the whole thing and create the preferencePages*/
        Iterator it = options.keySet().iterator();
        while ( it.hasNext() ) {
            OptionsInfo option = ( OptionsInfo ) options.get( it.next() );
            String section = option.getSectionToAppear();
            String plugin = option.getPluginToAppear();
            if ( ( section == null ) && ( plugin == null ) && showOption( option ) ) {
                if ( preferenceStore.getBoolean( "advancedMode" ) ) {
                    if ( advanced == null ) {
                        advanced = new MLDonkeyOptions( "Advanced ", FieldEditorPreferencePage.GRID );
                        advanced.setPreferenceStore( optionsStore );
                    }
                    advanced.addOption( option );
                }
            }
            else if ( ( section != null ) && section.equalsIgnoreCase( "other" ) && showOption( option ) ) {
                if ( preferenceStore.getBoolean( "advancedMode" ) ) {
                    if ( advanced == null ) {
                        advanced = new MLDonkeyOptions( "Advanced ", FieldEditorPreferencePage.GRID );
                        advanced.setPreferenceStore( optionsStore );
                    }
                    advanced.addOption( option );
                }
            }
            else if ( ( section != null ) && showOption( option ) ) {
                /* create the section, or if already done, only add the option */
                if ( !sections.containsKey( section ) ) {
                    MLDonkeyOptions temp = new MLDonkeyOptions( section, FieldEditorPreferencePage.GRID );

                    //myprefs.addToRoot( new PreferenceNode ( section, temp ) );
                    sections.put( section, temp );
                    temp.setPreferenceStore( optionsStore );
                }
                ( ( MLDonkeyOptions ) sections.get( section ) ).addOption( option );
            }
            else if ( ( plugin != null ) && showOption( option ) ) {
                /* create the pluginSection, or if already done, only add the option */
                if ( !plugins.containsKey( plugin ) ) {
                    /*only create the plugin, if it is possible at all...*/
                    MLDonkeyOptions temp = new MLDonkeyOptions( plugin, FieldEditorPreferencePage.GRID );
                    plugins.put( plugin, temp );
                    temp.setPreferenceStore( optionsStore );
                }
                ( ( MLDonkeyOptions ) plugins.get( plugin ) ).addOption( option );
            }
        }

        /*Now we create the tree-structure, since we received all options*/
        /*
         * first the sections:
         */
        it = sections.keySet().iterator();
        while ( it.hasNext() ) {
            String key = ( String ) it.next();
            MLDonkeyOptions page = ( MLDonkeyOptions ) sections.get( key );
            addToRoot( ( new PreferenceNode( key, page ) ) );
        }
        /*
         * and now the Plugins: first try to get the PrefPage "Networks", where all the "enabled"
         * options are, if this doesn't exist, create it. And then put all the plugins below this one
         */
        if ( plugins.size() != 0 ) {
            IPreferenceNode pluginOptions = find( "Networks" );
            if ( pluginOptions == null ) {
                MLDonkeyOptions emptyItem = new MLDonkeyOptions( "Networks", FieldEditorPreferencePage.FLAT );
                pluginOptions = new PreferenceNode( "Networks", emptyItem );
                emptyItem.isEmpty( true );
                addToRoot( pluginOptions );
            }
            it = plugins.keySet().iterator();
            while ( it.hasNext() ) {
                String key = ( String ) it.next();
                MLDonkeyOptions page = ( MLDonkeyOptions ) plugins.get( key );
                pluginOptions.add( ( new PreferenceNode( key, page ) ) );
            }
        }

        /*and now add the advanced-field at the very bottom of the list*/
        if ( advanced != null )
            addToRoot( ( new PreferenceNode( "Advanced", advanced ) ) );
    }

    /**
     * DOCUMENT ME!
     *
     * @param option DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean showOption( OptionsInfo option ) {
        if ( preferenceStore.getBoolean( "advancedMode" ) )
            return true;
        else if ( option.isAdvanced() )
            return false;
        return true;
    }

    /**
     * Initializes a preference Store. It creates the corresponding file , so that we can write and
     * read from it without throwing wild exceptions around.
     * @param preferenceStore the preferneceStore we want to initialize
     * @throws IOException some nice IO-Exception if the initialization failed
     */
    public void initialize( PreferenceStore preferenceStore )
        throws IOException {
        try {
            preferenceStore.load();
        }
        catch ( IOException e ) {
            preferenceStore.save();
            preferenceStore.load();
        }
    }

    /**
     * @return is our nice gui connected to a remot mldonkey, so that we can get remote-options?
     */
    public boolean isConnected() {
        return connected;
    }
}

/*
$Log: Preferences.java,v $
Revision 1.37  2003/10/15 22:06:13  zet
Split Console/Downloads pref pages.

Revision 1.36  2003/10/01 20:56:27  lemmster
add class hierarchy

Revision 1.35  2003/09/18 10:23:48  lemmster
checkstyle

Revision 1.34  2003/09/15 22:06:19  zet
split preferences

Revision 1.33  2003/09/15 14:47:44  dek
Preferences: renamed "General" / "Other" to "Advanced" and put it on the end of the list (only visible in advanced-mode)

Revision 1.32  2003/08/30 12:09:04  dek
Label added, when empty prefPage (networks with old core)

Revision 1.31  2003/08/29 20:24:42  dek
icon for preferences, and simple frame (group)

Revision 1.30  2003/08/26 14:12:01  zet
decrease inputfieldlength

Revision 1.29  2003/08/26 09:21:52  dek
pref-dialog is now resizable again, since the problem with the huge pages is gone.
And maybe some people don't like fixed-size dialogs ;-)

Revision 1.28  2003/08/25 13:14:12  dek
now integegerFields have the same width as StringInputFields

Revision 1.27  2003/08/24 18:41:46  zet
try to remove horizontal scrollbars from prefs

Revision 1.26  2003/08/24 14:45:22  dek
put plugins below Networks-treeItem, seems to be more logic to me

Revision 1.25  2003/08/24 11:33:19  dek
removed cancelpsressed handler, which is not needed anymore, because
we use this superb FieldEditorPrefPages ;-)

Revision 1.24  2003/08/24 11:30:57  dek
prefDialog is not resizable any more, and we have IntEditors for int-values

Revision 1.23  2003/08/23 15:21:37  zet
remove @author

Revision 1.22  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: zet $

Revision 1.21  2003/08/20 11:51:52  dek
renamed pref.g2gui to pref.g2guiPref for not having 2 classes with same name

Revision 1.20  2003/08/19 20:58:45  dek
only 1 general by now

Revision 1.19  2003/08/19 18:09:31  dek
fixed advanced / pro with new devel-core

Revision 1.18  2003/08/19 13:08:03  dek
advanced-Options included

Revision 1.17  2003/08/18 14:54:01  dek
*** empty log message ***

Revision 1.16  2003/08/18 14:51:58  dek
some more jface-work

Revision 1.15  2003/08/18 13:55:22  dek
plugins-crash fixed

Revision 1.14  2003/08/18 12:22:28  dek
g2gui-pref-page is now fully JFace-approved ;- )

Revision 1.13  2003/08/17 21:22:34  dek
reworked options, finally, it makes full use of the jFace framework ;- )

Revision 1.12  2003/07/25 02:41:22  zet
console window colour config in prefs / try different fontfieldeditor / pref page  ( any worse? )

Revision 1.11  2003/07/10 19:27:28  dek
some idle-race cleanup

Revision 1.10  2003/07/09 09:16:05  dek
general Options

Revision 1.9  2003/07/08 16:59:23  dek
now the booleanValues are checkBoxes

Revision 1.8  2003/07/08 15:03:01  dek
*** empty log message ***

Revision 1.7  2003/07/07 18:31:08  dek
saving options now also works

Revision 1.6  2003/07/07 17:38:14  dek
now, one can take a look at all Core-options, not saving yet, but is in work

Revision 1.5  2003/07/02 16:16:47  dek
extensive Checkstyle applying

Revision 1.4  2003/06/27 18:05:46  dek
Client name is now an option, not saveable yet, but it's displayed ;- )

Revision 1.3  2003/06/26 12:04:44  dek
pref-dialog accessible in main-window

Revision 1.2  2003/06/25 10:42:36  dek
peferenences dialog at first start

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/23 18:57:05  dek
jface

*/
