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
package net.mldonkey.g2gui.model;

import gnu.regexp.RE;
import gnu.regexp.REException;

import java.util.Set;
import java.util.TreeSet;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumExtension;
import net.mldonkey.g2gui.model.enum.EnumNetwork;
import net.mldonkey.g2gui.model.enum.EnumRating;
import net.mldonkey.g2gui.view.G2Gui;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.program.Program;

/**
 * ResultInfo
 *
 *
 * @version $Id: ResultInfo.java,v 1.40 2004/11/20 23:39:25 lemmy Exp $
 *
 */
public class ResultInfo extends Parent {
    /**
     * The Result Comment
     */
    private String comment = "";

    /**
     * Result ID
     */
    private int resultID;

    /**
     * The Network this result comes from
     */
    private NetworkInfo network;

    /**
     * Possible names
     */
    private String[] names;

    /**
     * MD4
     */
    
    protected String md4 = "";

    /**
     * Size
     */
    protected long size;

    /**
     * The size rounded with metric unit
     */
    private String stringSize = "";

    /**
     * Format
     */
    private String format = "";

    /**
     * Type
     */
    private String type = "";

    /**
     * Metatags
     */
    private Tag[] tags;

    /**
     * true = Normal, false = Already Downloaded
     */
    private boolean history;

    /**
     * have we downloaded this file already
     * (just for the gui, core not involved)
     */
    private boolean downloading;

	/**
	 * The rating for this result
	 */
	private Enum rating;

    /**
     * true if this obj contains profanity
     */
    private boolean containsProfanity = false;

    /**
     * true if this obj contains pronography
     */
    private boolean containsPornography = false;

    /**
     * true if this obj contains "fake" string
     */
    private boolean containsFake = false;
    
    /**
     * The Profanity Filter
     */
    private static RE profanityFilterRE;

    /**
     * The Pornography Filter
     */
    private static RE pornographyFilterRE;
    
    /**
     * The fake RE
     */
    private static RE fakeRE;

    /**
     * Create ones this Filters
     */
    static {
        // who knows how to filter this garbage properly...
        try {
            profanityFilterRE = new RE( "fuck|shit", RE.REG_ICASE );
        }
        catch ( REException e ) {
            profanityFilterRE = null;
        }
        try {
            pornographyFilterRE =
                new RE( "fuck|shit|porn|pr0n|pussy|xxx|sex|erotic|anal|lolita|sluts|fetish"
                        + "|naked|incest|bondage|masturbat|blow.*job|barely.*legal", RE.REG_ICASE );
        }
        catch ( REException e ) {
            pornographyFilterRE = null;
        }
        
        try {
            fakeRE = new RE( "fake", RE.REG_ICASE );
    	}  
		catch ( REException e ) {
		    fakeRE = null;
		 }
        
    }
    
    /**
     * @return true if this obj contains the string "fake"
     */
    public boolean containsFake() {
        return containsFake;
    }

    /**
     * @return does this obj contains pornography
     */
    public boolean containsPornography() {
        return containsPornography;
    }

    /**
     * @return does this obj contains profanity
     */
    public boolean containsProfanity() {
        return containsProfanity;
    }

    /**
     * Creates a new ResultInfo
     * @param core The parent
     */
    ResultInfo( CoreCommunication core ) {
        super( core );
    }

    /** (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = new String();
        result += ( "ID: " + resultID );
        result += ( "\n Network: " + network );
        result += "\n filenames: ";
        for ( int i = 0; i < names.length; i++ )
            result += ( names[ i ] + " " );
        result += ( "\n Md4: " + md4 );
        result += ( "\n FileSize: " + size );
        result += ( "\n File-Format: " + format );
        result += ( "\n File-Typ: " + type );
        result += "\n MetaData: ";
        for ( int i = 0; i < tags.length; i++ )
            result += ( tags[ i ].getName() + " - " + tags[ i ].getValue() );
        result += ( "\n comment: " + comment );
        result += ( "\n status: " + history );
        return result;
    }

    /**
     * Reads a ResulfInfo from a MessageBuffer
     * @param messageBuffer The MessageBuffer to read from
     */
    public void readStream( MessageBuffer messageBuffer ) {
        /*
         * int32                Result Identifier
         * int32                Result Network Identifier
         * List of         String        Possible File Names
         * char[16]        File Md4 (binary)
         * int32                File Size
         * String                File Format
         * String                File Type
         * List of Tag        File Metadata
         * String                Comment
         * int8                        0 = Normal, 1 = Already Downloaded
         */
        this.resultID = messageBuffer.readInt32();
        this.setNetworkID( messageBuffer.readInt32() );
        this.names = messageBuffer.readStringList();
        readMD4(messageBuffer);       
        setSize(messageBuffer);        
        this.format = messageBuffer.readString();
        this.type = messageBuffer.readString();
        this.tags = messageBuffer.readTagList();
        this.comment = messageBuffer.readString();
        this.setHistory( messageBuffer.readByte() );
        readDate(messageBuffer);
        this.stringSize = RegExp.calcStringSize( this.getSize() );
        
        for ( int i = 0; i < names.length; i++ ) {
            if ( ( profanityFilterRE != null ) && ( profanityFilterRE.getMatch( names[ i ] ) != null ) ) {
                containsProfanity = true;
                if ( containsPornography )
                    break;
            }
            if ( ( pornographyFilterRE != null ) && ( pornographyFilterRE.getMatch( names[ i ] ) != null ) ) {
                containsPornography = true;
                if ( containsProfanity )
                    break;
            }
            if ( (fakeRE != null ) && ( fakeRE.getMatch ( names[ i ] ) != null ) ) {
                containsFake = true;
            }
        }
        
        if ( !containsFake && fakeRE != null ) {
            if ( fakeRE.getMatch( this.comment ) != null ) 
                containsFake = true;
        }
        
        this.setRating();
    }

    /**
	 * @param messageBuffer
	 */
    protected void readDate(MessageBuffer messageBuffer) {
		//Do nothing here, needed proto >26		
	}

	/**
	 * @param messageBuffer
	 */
	protected void readMD4(MessageBuffer messageBuffer) {
		this.md4 = messageBuffer.readBinary( 16 );
		
	}

	/**
	 * @param messageBuffer
	 */
	protected void setSize(MessageBuffer messageBuffer) {
		this.size = messageBuffer.readInt32();
		
	}

	/**
     * @param string file comment
     */
    private void setComment( String string ) {
        this.comment = string;
    }

    /**
     * @return The format
     */
    public String getFormat() {
        if ( format != null && !format.equals(G2Gui.emptyString) )
        	return format;
        else {
        	int index = this.getName().lastIndexOf( "." );
        	if ( index != -1 )
        		return this.getName().substring( index + 1 ).toLowerCase();
        	else
        		return G2Gui.emptyString;
        }
    }

    /**
     * @return The History (true = normal, false = alread downloaded)
     */
    public boolean getHistory() {
        return history;
    }

    /**
     * @return The MD4
     */
    public String getMd4() {
        return md4;
    }

    /**
     * @return The first possible Name
     */
    public String getName() {
    	return this.getName( 0 );
    }

    private String getName( int index ) {
    	// is the index vaild?
    	if ( names.length < index - 1 )
    		return G2Gui.emptyString;

    	String result = names[ index ]; 
    	if ( result == null )
    		return G2Gui.emptyString;
    	return result;
    }
    
    /**
     * @return Possible Names
     */
    public String[] getNames() {
        if ( names == null )
        	return new String[ 0 ];
       	return names;
    }
    
    /**
     * @return The length of the longest String in this array
     */
    public int getMaxNamesLength() {
    	int result = 0;
    	for ( int i = 0; i < getNames().length; i++ ) {
    		if ( getNames()[ i ].length() > result )
    			result = getNames()[ i ].length();
    	}
    	return result;
    }
    
    /**
     * 
     * @return The alternative names alphabetic ordered
     */
    public String[] getSortedNames() {
    	Set set = new TreeSet();
    	for ( int i = 0; i < getNames().length; i++ ) {
    		set.add( getNames()[ i ] );
    	}
    	Object[] obj = set.toArray();
    	String[] result = new String[ obj.length ];
    	for ( int j = 0; j < obj.length; j++ ) {
    		result[ j ] = (String) obj[ j ];
    	}
    	return result;
    }
    
    /**
     * @return A preview of the alternative names (sorted)
     */
    public String getSortedNamesPreview() {
    	StringBuffer result = new StringBuffer();
    	String[] strings = getSortedNames();
    	for ( int i = 0; i < strings.length; i++ ) {
    		if ( i == 3 ) {
    			result.append( "\n" );
    			result.append( "..." );
    			break;
    		}
    		result.append( "\n" );
    		result.append( " -" );
    		result.append( strings[ i ] );
    	}
    	// add the title if we contain at least 1 char
    	if ( result.length() != 0 )
    		result.insert( 0, "Alt. names:" );

    	return result.toString();
    }
    
    /**
     * @return The Network id
     */
    public NetworkInfo getNetwork() {
        return network;
    }

    /**
     * @return The result id
     */
    public int getResultID() {
        return resultID;
    }

    /**
     * @return The size
     */
    public long getSize() {
        // convert to long
        long result = ( size & 0xFFFFFFFFL );
        return result;
    }

    /**
     * @return The Metadata
     */
    public Tag[] getTags() {
        if ( tags.length == 0 )
            return null;
        else
            return tags;
    }

    /**
     * @return The Availibility
     */
    public int getAvail() {
        if ( ( this.getTags() != null ) && ( this.getTags().length > 0 ) )
            for ( int i = 0; i < this.tags.length; i++ ) {
                String aString = this.tags[ i ].getName();
                if ( aString.equals( "availability" ) )
                    return this.tags[ i ].getValue();
            }
        return 0;
    }

    /**
     * @return The mp3 bitrate or "" if no audio
     */
    public String getBitrate() {
        if ( this.type.equals( "Audio" ) ) {
            for ( int i = 0; i < this.tags.length; i++ ) {
                String aString = this.tags[ i ].getName();
                if ( aString.equals( "bitrate" ) )
                    return new Integer( this.tags[ i ].getValue() ).toString() + "kb";
            }
            return G2Gui.emptyString;
        }
        return G2Gui.emptyString;
    }

    /**
     * @return The mp3 lenght or "" if no audio
     */
    public String getLength() {
        if ( this.type.equals( "Audio" ) ) {
            for ( int i = 0; i < this.tags.length; i++ ) {
                String aString = this.tags[ i ].getName();
                if ( aString.equals( "length" ) )
                    return this.tags[ i ].getSValue();
            }
            return G2Gui.emptyString;
        }
        return G2Gui.emptyString;
    }

    /**
     * @return The Type
     */
    public String getType() {
        if ( type != null && !type.equals( G2Gui.emptyString ) )
        	return type;
        else {
        	Enum aEnum = (Enum) FILE_TYPES.get( getFormat() );
        	if ( aEnum != null )
        		return ( (EnumExtension) aEnum ).toString();
        	else
        		return EnumExtension.UNKNOWN.toString();
        }
    }
    
    /**
     * @return The Comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param b The History as a byte
     */
    private void setHistory( byte b ) {
        if ( b == 0 )
            history = true;
        else
            history = false;
    }

    /**
     * translate the int to networkinfo
     * @param i the int
     */
    private void setNetworkID( int i ) {
        this.network = ( NetworkInfo ) this.parent.getNetworkInfoMap().get( i );
    }

    /**
     * @param info The networkinfo 
     */
    public void setNetwork( NetworkInfo info ) {
        network = info;
    }

    /**
     * @return The size rounded with metric unit
     */
    public String getStringSize() {
        return stringSize;
    }

    /**
     * @return This result as a link corresponding to the network
     */
    public String getLink() {
        EnumNetwork type = this.getNetwork().getNetworkType();
        //fixing Core-Bug not sending the correct EnumNetwork type
        //just remove EnumNetwork.NONE when core bug has been fixed
        if ( type == EnumNetwork.DONKEY || type == EnumNetwork.NONE ) {
            return "ed2k://|file|" + this.getNames()[ 0 ] + "|" + this.getSize() + "|" + this.getMd4()
            + "|/";
        }
        else {
            return G2Gui.emptyString;
        }
    }

    /**
     * @return do we already have downloaded this result
     */
    public boolean isDownloading() {
        return downloading;
    }

    /**
     * set this download to downloaded
     * 
     * @param bool download or not downloading
     */
    public void setDownloading( boolean bool ) {
        downloading = bool;
    }
    
	/**
	 * Get the String depending on the availability
	 * @return
	 */
    public String getRatingString() {
    	// use StringBuffer, concat with "+" is slow and this method is called often
		StringBuffer result = new StringBuffer();

		result.append( this.getRating().toString() );
		result.append( "(" );
		result.append( this.getAvail() );
		result.append( ")" );
		
		return result.toString();
    }
    
    /**
     * Get the Image depending on the availability
     * @return an Image
     */
    public Image getRatingImage() {
    	if (this.containsFake())
    		return G2GuiResources.getImage("epRatingFake");
		else if (this.getRating() == EnumRating.EXCELLENT)
			return G2GuiResources.getImage("epRatingExcellent");
		else if (this.getRating() == EnumRating.VERYHIGH)
			return G2GuiResources.getImage("epRatingExcellent");
		else if (this.getRating() == EnumRating.HIGH)
			return G2GuiResources.getImage("epRatingGood");
		else if (this.getRating() == EnumRating.NORMAL)
			return G2GuiResources.getImage("epRatingFair");
		else
			return G2GuiResources.getImage("epRatingPoor");
    }
    
    /**
     * @return The detailed infos about this searchresult
     */
    public String getToolTipString() {
    	StringBuffer result = new StringBuffer();

    	if (this.getNetwork().getNetworkType() == EnumNetwork.DONKEY) {
    		result.append(G2GuiResources.getString("ST_TT_MD4"));
    		result.append(this.getMd4().toUpperCase());
    		result.append("\n");
    	}	

    	if (!this.getFormat().equals(G2Gui.emptyString)) {
    		result.append(G2GuiResources.getString("ST_TT_FORMAT"));
    		result.append(this.getFormat());
    		result.append("\n");
    	}	

    	result.append(G2GuiResources.getString("ST_TT_NETWORK"));
    	result.append(this.getNetwork().getNetworkName());
    	result.append("\n");

    	result.append(G2GuiResources.getString("ST_TT_SIZE"));
    	result.append(this.getStringSize());
    	result.append("\n");
    			
    	result.append(G2GuiResources.getString("ST_TT_AVAIL"));
    	result.append(this.getAvail());
    	result.append(" ");
    	result.append(G2GuiResources.getString("ST_TT_SOURCES"));
    	
    	if (this.getType().equals("Audio")) {
    		if (!this.getBitrate().equals(G2Gui.emptyString)) {
    			result.append("\n");
    			result.append(G2GuiResources.getString("ST_TT_BITRATE"));
    			result.append(this.getBitrate());
    		}
    		if (!this.getLength().equals(G2Gui.emptyString)) {
    			result.append("\n");
    			result.append(G2GuiResources.getString("ST_TT_LENGTH"));
    			result.append(this.getLength());
    		}
   		}

    	if (!this.getHistory()) {
    		result.append("\n");
    		result.append(G2GuiResources.getString("ST_TT_DOWNLOADED"));
    	}
    	
    	return result.toString();
    }
    
    /**
     * @return Create the image of the associated program
     */
    public Image getToolTipImage() {
    	Image image = null;
    	
    	// Find the associated program
   		Program p = Program.findProgram( this.getFormat() );

    	if ( p != null ) {
    		// if the G2GuiResources didnt already contains the image
    		image = G2GuiResources.getImage( p.getName() );
    		if ( image == null ) {
    			ImageData data = p.getImageData();
    			if ( data != null ) {
    				image = new Image( null, data );
    				G2GuiResources.getImageRegistry().put( p.getName(), image );
    			}
    		}
    	}
    	return image;
    }
    
    /**
     * The rating for this resultinfo
     * @return
     */
    public Enum getRating() {
		return rating;
    }
    
    private void setRating() {
		if (this.getAvail() > 100)
			this.rating = EnumRating.EXCELLENT;
		else if (this.getAvail() > 50)
			this.rating = EnumRating.VERYHIGH;
		else if (this.getAvail() > 10)
			this.rating = EnumRating.HIGH;
		else if (this.getAvail() > 5)
			this.rating = EnumRating.NORMAL;
		else
			this.rating = EnumRating.LOW;
    }
    
    /**
     * Compares this resultInfo to the specified object.
     * The result is <code>true</code> if and only if the argument is not
     * <code>null</code> and is a <code>FileInfo</code> object that represents
     * the same sequence of md4 characters/size as this object.
     *
     * @param aFileInfo the object to compare this <code>ResultInfo</code> against
     * @return <code>true</code> if the objs are equal;
     *          <code>false</code> otherwise.
     */
    public boolean equals( FileInfo aFileInfo ) {
        /* does the size match */
        if ( !( this.getSize() == aFileInfo.getSize() ) )
            return false;
        if ( !this.getName().equals( aFileInfo.getNames()[ 0 ] ) )
            return false;
        return true;

        //TODO use just the md4 to compare (when the core sends a correct one)
    }
}

/*
$Log: ResultInfo.java,v $
Revision 1.40  2004/11/20 23:39:25  lemmy
due to the Core-Bug not sending a correct identifier for the network,getLink() always return a empty string

Revision 1.39  2004/09/17 22:36:48  dek
update for gui-Protocol 29

Revision 1.38  2004/09/10 18:10:34  lemmy
use the get(int i) method of networkinfointmap instead of working on the TIntObjectMap directly

Revision 1.37  2004/09/10 17:59:09  dek
Work-around for core-bug

Revision 1.36  2004/03/29 14:51:44  dek
some mem-improvements

Revision 1.35  2004/03/21 21:23:53  dek
some proto25 was still missing !!

Revision 1.34  2004/03/21 21:20:26  dek
some proto25 was still missing

Revision 1.33  2003/12/17 13:06:05  lemmy
save all panelistener states correctly to the prefstore

Revision 1.32  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.31  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

Revision 1.30  2003/11/30 09:31:26  lemmy
ToolTip complete reworked (complete)

Revision 1.29  2003/11/29 13:03:54  lemmy
ToolTip complete reworked (to be continued)

Revision 1.28  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.27  2003/11/10 08:54:47  lemmy
rating filter in searchresult

Revision 1.26  2003/11/10 08:35:13  lemmy
move getRating... into ResultInfo

Revision 1.25  2003/10/22 23:42:57  zet
fake regexp

Revision 1.24  2003/10/13 19:54:32  zet
remove high ascii

Revision 1.23  2003/09/18 09:16:47  lemmy
checkstyle

Revision 1.22  2003/09/17 20:07:44  lemmy
avoid NPEs in search

Revision 1.21  2003/09/15 15:32:09  lemmy
reset state of canceled downloads from search [bug #908]

Revision 1.20  2003/09/08 12:38:00  lemmy
show bitrate/length for audio files in tooltip

Revision 1.19  2003/08/31 12:22:27  lemmy
added boolean downloading

Revision 1.18  2003/08/28 12:41:04  lemmy
initialize with false

Revision 1.17  2003/08/28 11:54:41  lemmy
use getter methode for profanity/pornogaphic

Revision 1.16  2003/08/28 00:12:10  zet
chk for null

Revision 1.15  2003/08/26 22:44:03  zet
basic filtering

Revision 1.14  2003/08/23 15:21:37  zet
remove @author

Revision 1.13  2003/08/22 21:03:15  lemmy
replace $user$ with $Author: lemmy $

Revision 1.12  2003/08/14 12:45:46  dek
searching works now without errors

Revision 1.11  2003/07/31 04:07:43  zet
size is now a long

Revision 1.10  2003/07/25 22:34:52  lemmy
lots of changes

Revision 1.9  2003/07/24 16:07:05  lemmy
renamed getNetworkID() to getNetwork()

Revision 1.8  2003/07/23 23:49:22  lemmy
stringSize for better display design added

Revision 1.7  2003/07/23 17:01:24  lemmy
added getComment() and setNetworkID(Networkinfo info)

Revision 1.6  2003/07/06 08:49:33  lemmy
better oo added

Revision 1.5  2003/07/06 07:45:26  lemmy
checkstyle applied

Revision 1.4  2003/07/06 07:29:47  lemmy
javadoc improved

Revision 1.3  2003/07/05 13:14:50  dek
*** empty log message ***

Revision 1.2  2003/07/05 12:52:19  lemmy
bugfix in readStream()

Revision 1.1  2003/06/30 07:19:47  lemmy
initial commit (untested)

*/