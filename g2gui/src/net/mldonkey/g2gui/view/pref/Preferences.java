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
package net.mldonkey.g2gui.view.pref;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.OptionsInfo;
import net.mldonkey.g2gui.model.OptionsInfoMap;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.widgets.Shell;

/**
 * OptionTree2
 *
 * @author $user$
 * @version $Id: Preferences.java,v 1.11 2003/07/10 19:27:28 dek Exp $ 
 *
 */
public class Preferences extends PreferenceManager {	

	private boolean connected = false;
	private PreferenceManager myprefs;
	private PreferenceDialog prefdialog;
	private PreferenceStore preferenceStore;
	private String[] wantedSections = {
							"Identification",
							"Bandwidth",
							"Ports"
							};
	private String[] wantedPlugins = {
							"Overnet",
							"eDonkey",
							"Fasttrack",
							"Gnutella",
							"BitTorrent"
							};

	/**
	 * @param preferenceStore where to store the values at
	 */
	public Preferences( PreferenceStore preferenceStore ) {	
		this.preferenceStore = 	preferenceStore;
		myprefs = new PreferenceManager();
		myprefs.addToRoot( new PreferenceNode( "G2gui", new G2Gui( preferenceStore, connected ) ) );		
	}
	
	/**
	 * @param shell the parent shell, where this pref-window has to be opened
	 * @param mldonkey the Core we want to configure
	 */
	public void open( Shell shell, CoreCommunication mldonkey ) {
		try {
				initialize( preferenceStore );
			} catch ( IOException e ) {
				System.out.println( "initalizing Preferences Dialog failed due to IOException" );
			}
		prefdialog = new PreferenceDialog( shell, myprefs ) {
				/* (non-Javadoc)
				 * @see org.eclipse.jface.preference.PreferenceDialog#cancelPressed()
				 */
				protected void cancelPressed() {				
					prefdialog.close();
				}
				};
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
		
		Map sections = new HashMap();
		Map plugins = new HashMap();
		
		/*now we iterate over the whole thing and create the preferencePages*/
		Iterator it = options.keySet().iterator();
		while ( it.hasNext() ) {		
			OptionsInfo option = ( OptionsInfo ) options.get( it.next() );			
			String section = option.getSectionToAppear();
			String plugin = option.getPluginToAppear();
			
			if ( ( section == null ) && ( plugin == null ) ) {				
				/* create the General-section, or if already done, only add the option */
				if ( !sections.containsKey( "General" ) ) {
					MLDonkeyOptions temp = new MLDonkeyOptions( "General" );
					myprefs.addToRoot( new PreferenceNode ( "General", temp ) );
					sections.put( "General", temp );					
					//temp.setVisible(false);					
					}
			
			/*commented out the following, as it produces ton's of options in this tab
			 * which made it unreadable	
			 */
			( ( MLDonkeyOptions  )sections.get( "General" ) ).addOption( option );
				
			}
			else if ( section != null ) {				
			/* create the section, or if already done, only add the option */
				if ( !sections.containsKey( section ) ) {
					MLDonkeyOptions temp = new MLDonkeyOptions( section );
					//myprefs.addToRoot( new PreferenceNode ( section, temp ) );
					sections.put( section, temp );
					}
				( ( MLDonkeyOptions  )sections.get( section ) ).addOption( option );
			}

			else if ( plugin != null ) {				
			/* create the pluginSection, or if already done, only add the option */
				if ( !plugins.containsKey( plugin ) ) {
					/*only create the plugin, if it is possible at all...*/					
					MLDonkeyOptions temp = new MLDonkeyOptions( plugin );					
					plugins.put( plugin, temp );					
					}
				( ( MLDonkeyOptions  )plugins.get( plugin ) ).addOption( option );			
				//create the plugin, or if already done, only add the option
				}
		}
		/*Now we create the tree-structure, since we received all options*/
		
		
		 /*
		  * first the sections:
		  */
//this creates only the sections defined in wantedSections[]		 
		 for ( int i = 0; i < wantedSections.length; i++ ) {
			if ( sections.containsKey( wantedSections[ i ] ) ) {
				MLDonkeyOptions optionspage = ( MLDonkeyOptions ) sections.get( wantedSections[ i ] );
				   myprefs.addToRoot( new PreferenceNode ( wantedSections[ i ], optionspage ) );
			}				
		}
		 
			
// this creates sections for _all_ the options we received, do we really want this?
   		/*
		 sections.remove( "General" ); //remove the General-page, as it is already created
		 it = sections.keySet().iterator();
		 while ( it.hasNext() ) {
		 	String sectionName = ( String ) it.next();
		 	 MLDonkeyOptions optionspage = ( MLDonkeyOptions ) sections.get( sectionName );
				myprefs.addToRoot( new PreferenceNode ( sectionName, optionspage ) );
		 }
		 */
		 
		 
		 
		 /*
		  * and now the Plugins:
		  */
		PreferenceNode pluginOptions = new PreferenceNode( "plugins", new MLDonkeyOptions( "Plugins" ) );
			myprefs.addToRoot( pluginOptions );
			
			
//this creates only the sections defined in wantedPlugins[]		 
			   for ( int i = 0; i < wantedPlugins.length; i++ ) {
				  if ( plugins.containsKey( wantedPlugins[ i ] ) ) {
					  MLDonkeyOptions optionspage = ( MLDonkeyOptions ) plugins.get( wantedPlugins[ i ] );
						 pluginOptions.add( new PreferenceNode ( wantedPlugins[ i ], optionspage ) );
				  }				
			  }		  
		  
//this creates sections for _all_ the pluginsInfos we received, do we really want this?
		/*
		it = plugins.keySet().iterator();
		while ( it.hasNext() ) {
		   String sectionName = ( String ) it.next();
			MLDonkeyOptions optionspage = ( MLDonkeyOptions ) plugins.get( sectionName );
				pluginOptions.add( new PreferenceNode ( sectionName, optionspage ) );
		}
		*/
	}
		
		
	

	/**
	 * Initializes a preference Store. It creates the corresponding file , so that we can write and
	 * read from it without throwing wild exceptions around.
	 * @param preferenceStore the preferneceStore we want to initialize
	 * @throws IOException some nice IO-Exception if the initialization failed
	 */
	public void initialize( PreferenceStore preferenceStore ) throws IOException {
		try {			
			preferenceStore.load();
		} catch ( IOException e ) {
			preferenceStore.save();
			preferenceStore.load();
		}
	}	


	/**
	 * @return this classes preferenceManager
	 */
	public PreferenceManager getMyprefs() {
		return myprefs;
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
Client name is now an option, not saveable yet, but it's displayed ;-)

Revision 1.3  2003/06/26 12:04:44  dek
pref-dialog accessible in main-window

Revision 1.2  2003/06/25 10:42:36  dek
peferenences dialog at first start

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/23 18:57:05  dek
jface

*/