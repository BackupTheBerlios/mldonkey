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

import java.text.DecimalFormat;

import org.eclipse.swt.graphics.Image;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumRating;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

/**
 * ResultInfo
 *
 *
 * @version $Id: ResultInfo.java,v 1.26 2003/11/10 08:35:13 lemmster Exp $
 *
 */
public class ResultInfo extends Parent {
    /**
     * The Result Comment
     */
    private String comment;

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
    private String md4;

    /**
     * Size
     */
    private int size;

    /**
     * The size rounded with metric unit
     */
    private String stringSize;

    /**
     * Format
     */
    private String format;

    /**
     * Type
     */
    private String type;

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
    public ResultInfo( CoreCommunication core ) {
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
        this.md4 = messageBuffer.readBinary( 16 );
        this.size = messageBuffer.readInt32();
        this.format = messageBuffer.readString();
        this.type = messageBuffer.readString();
        this.tags = messageBuffer.readTagList();
        this.comment = messageBuffer.readString();
        this.setHistory( messageBuffer.readByte() );
        this.stringSize = this.calcStringSize( this.getSize() );
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
        
    }

    /**
     * creates a String from the size
     * @param size The size
     * @return a string represantation of this size
     */
    private String calcStringSize( long size ) {
        float k = 1024f;
        float m = k * k;
        float g = m * k;
        float t = g * k;
        float fsize = ( float ) size;
        DecimalFormat df = new DecimalFormat( "0.##" );
        if ( fsize > t )
            return new String( df.format( fsize / t ) + " TB" );
        else if ( fsize > g )
            return new String( df.format( fsize / g ) + " GB" );
        else if ( fsize > m )
            return new String( df.format( fsize / m ) + " MB" );
        else if ( fsize > k )
            return new String( df.format( fsize / k ) + " KB" );
        else
            return new String( size + "" );
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
        return format;
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
        if ( this.getNames().length != 0 )
            return this.names[ 0 ];
        return "";
    }

    /**
     * @return Possible Names
     */
    public String[] getNames() {
        return names;
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
            return "";
        }
        return "";
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
            return "";
        }
        return "";
    }

    /**
     * @return The Type
     */
    public String getType() {
        return type;
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
        this.network = ( NetworkInfo ) this.parent.getNetworkInfoMap().infoIntMap.get( i );
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
        if ( this.getNetwork().getNetworkName().equals( "Donkey" ) )
            return "ed2k://|file|" + this.getNames()[ 0 ] + "|" + this.getSize() + "|" + this.getMd4()
                   + "|/";
        else
            return "";
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

		if (this.getRating() == EnumRating.EXCELLENT)
			result.append( G2GuiResources.getString("RTLP_EXCELLENT") );
		else if (this.getRating() == EnumRating.VERYHIGH)
			result.append( G2GuiResources.getString("RTLP_VERYHIGH") );
		else if (this.getRating() == EnumRating.HIGH)
			result.append( G2GuiResources.getString("RTLP_HIGH") );
		else if (this.getRating() == EnumRating.NORMAL)
			result.append( G2GuiResources.getString("RTLP_NORMAL") );
		else
			result.append( G2GuiResources.getString("RTLP_LOW") );

		// append the exact avail
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
     * The rating for this resultinfo
     * @return
     */
    public Enum getRating() {
		if (this.getAvail() > 100)
			return EnumRating.EXCELLENT;
		else if (this.getAvail() > 50)
			return EnumRating.VERYHIGH;
		else if (this.getAvail() > 10)
			return EnumRating.HIGH;
		else if (this.getAvail() > 5)
			return EnumRating.NORMAL;
		else
			return EnumRating.LOW;
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
Revision 1.26  2003/11/10 08:35:13  lemmster
move getRating... into ResultInfo

Revision 1.25  2003/10/22 23:42:57  zet
fake regexp

Revision 1.24  2003/10/13 19:54:32  zet
remove high ascii

Revision 1.23  2003/09/18 09:16:47  lemmster
checkstyle

Revision 1.22  2003/09/17 20:07:44  lemmster
avoid NPEs in search

Revision 1.21  2003/09/15 15:32:09  lemmster
reset state of canceled downloads from search [bug #908]

Revision 1.20  2003/09/08 12:38:00  lemmster
show bitrate/length for audio files in tooltip

Revision 1.19  2003/08/31 12:22:27  lemmster
added boolean downloading

Revision 1.18  2003/08/28 12:41:04  lemmster
initialize with false

Revision 1.17  2003/08/28 11:54:41  lemmster
use getter methode for profanity/pornogaphic

Revision 1.16  2003/08/28 00:12:10  zet
chk for null

Revision 1.15  2003/08/26 22:44:03  zet
basic filtering

Revision 1.14  2003/08/23 15:21:37  zet
remove @author

Revision 1.13  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: lemmster $

Revision 1.12  2003/08/14 12:45:46  dek
searching works now without errors

Revision 1.11  2003/07/31 04:07:43  zet
size is now a long

Revision 1.10  2003/07/25 22:34:52  lemmstercvs01
lots of changes

Revision 1.9  2003/07/24 16:07:05  lemmstercvs01
renamed getNetworkID() to getNetwork()

Revision 1.8  2003/07/23 23:49:22  lemmstercvs01
stringSize for better display design added

Revision 1.7  2003/07/23 17:01:24  lemmstercvs01
added getComment() and setNetworkID(Networkinfo info)

Revision 1.6  2003/07/06 08:49:33  lemmstercvs01
better oo added

Revision 1.5  2003/07/06 07:45:26  lemmstercvs01
checkstyle applied

Revision 1.4  2003/07/06 07:29:47  lemmstercvs01
javadoc improved

Revision 1.3  2003/07/05 13:14:50  dek
*** empty log message ***

Revision 1.2  2003/07/05 12:52:19  lemmstercvs01
bugfix in readStream()

Revision 1.1  2003/06/30 07:19:47  lemmstercvs01
initial commit (untested)

*/
