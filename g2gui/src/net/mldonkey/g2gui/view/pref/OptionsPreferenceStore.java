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

import net.mldonkey.g2gui.model.OptionsInfo;
import net.mldonkey.g2gui.model.OptionsInfoMap;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;


/**
 * OptionsPreferenceStore<br>
 * All the stubs are not used in this context, as we only make use of string, or boolean
 * of course, we could introduce some intEditors for the int-values from core, but we don't need this, right?
 * as mldonkey itself cares for wrong values... 
 *
 *
 * @version $Id: OptionsPreferenceStore.java,v 1.11 2003/08/26 08:41:17 dek Exp $ 
 *
 */
public class OptionsPreferenceStore implements IPreferenceStore {
	

	private boolean debug = false;

	private OptionsInfoMap input;

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#addPropertyChangeListener( org.eclipse.jface.util.IPropertyChangeListener )
	 */
	public void addPropertyChangeListener( IPropertyChangeListener listener ) {
		//  Auto-generated method stub
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#contains( java.lang.String )
	 */
	public boolean contains( String name ) {		
		return input.keySet().contains( name );
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#firePropertyChangeEvent( java.lang.String, java.lang.Object, java.lang.Object )
	 */
	public void firePropertyChangeEvent( String name, Object oldValue, Object newValue ) {
		if ( debug )  System.out.println( "firePropertyChangeEvent" );
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#getBoolean( java.lang.String )
	 */
	public boolean getBoolean( String name ) {
		if ( contains( name ) )		
			return new Boolean ( ( ( OptionsInfo )input
									.get( name ) )
									.getValue() ).booleanValue();
		else return false;
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultBoolean( java.lang.String )
	 */
	public boolean getDefaultBoolean( String name ) {
		if ( debug )  System.out.println( "getDefaultBoolean: " + name );
		return new Boolean ( ( ( OptionsInfo )input
							.get( name ) )
							.getDefaultValue() ).booleanValue();
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultDouble( java.lang.String )
	 */
	public double getDefaultDouble( String name ) {
		if ( debug )  System.out.println( "getDefaultDouble" );
		return 0;
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultFloat( java.lang.String )
	 */
	public float getDefaultFloat( String name ) {
		if ( debug )  System.out.println( "getDefaultFloat" );
		return 0;
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultInt( java.lang.String )
	 */
	public int getDefaultInt( String name ) {
		if ( debug )  System.out.println( "getDefaultInt: " + name );
		String stringValue = ( ( OptionsInfo )input.get( name ) ) .getDefaultValue();
		return new Integer( stringValue ).intValue();
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultLong( java.lang.String )
	 */
	public long getDefaultLong( String name ) {
		if ( debug )  System.out.println( "getDefaultLong: " + name );
		return 0;
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultString( java.lang.String )
	 */
	public String getDefaultString( String name ) {
		if ( debug )  System.out.println( "getDefaultString: " + name );
		return  ( ( OptionsInfo )input
					.get( name ) )
					.getDefaultValue();
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDouble( java.lang.String )
	 */
	public double getDouble( String name ) {		
		return 0;
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#getFloat( java.lang.String )
	 */
	public float getFloat( String name ) {		
		return 0;
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#getInt( java.lang.String )
	 */
	public int getInt( String name ) {		
		String stringValue = ( ( OptionsInfo )input.get( name ) ) .getValue();
		return new Integer( stringValue ).intValue();		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#getLong( java.lang.String )
	 */
	public long getLong( String name ) {		
		return 0;
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#getString( java.lang.String )
	 */
	public String getString( String name ) {
		return  ( ( OptionsInfo )input
							.get( name ) )
							.getValue();
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#isDefault( java.lang.String )
	 */
	public boolean isDefault( String name ) {
		if ( debug )  System.out.println( "isDefault ??" );
		return false;
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#needsSaving()
	 */
	public boolean needsSaving() {		
		return false;
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#putValue( java.lang.String, java.lang.String )
	 */
	public void putValue( String name, String value ) {
		if ( debug )  System.out.println( "putStringValue" );
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#removePropertyChangeListener( org.eclipse.jface.util.IPropertyChangeListener )
	 */
	public void removePropertyChangeListener( IPropertyChangeListener listener ) {
		if ( debug )  System.out.println( "removePropertyChangeListener" );
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault( java.lang.String, double )
	 */
	public void setDefault( String name, double value ) {
		if ( debug )  System.out.println( "setDoubleDefault" );
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault( java.lang.String, float )
	 */
	public void setDefault( String name, float value ) {
		if ( debug )  System.out.println( "setFloatDefault" );
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault( java.lang.String, int )
	 */
	public void setDefault( String name, int value ) {
		if ( debug )  System.out.println( "setIntDefault" );
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault( java.lang.String, long )
	 */
	public void setDefault( String name, long value ) {
		if ( debug )  System.out.println( "setLongDefault" );
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault( java.lang.String, java.lang.String )
	 */
	public void setDefault( String name, String defaultObject ) {
		if ( debug )  System.out.println( "setDefault: defaultObject:" + defaultObject );
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault( java.lang.String, boolean )
	 */
	public void setDefault( String name, boolean value ) {
		if ( debug )  System.out.println( "setDefault" );
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setToDefault( java.lang.String )
	 */
	public void setToDefault( String name ) {
		if ( debug )  System.out.println( "setToDefault:" + name );
		setValue( name, getDefaultString( name ) );
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue( java.lang.String, double )
	 */
	public void setValue( String name, double value ) {
		if ( debug )  System.out.println( "setDoubleValue" );
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue( java.lang.String, float )
	 */
	public void setValue( String name, float value ) {
		if ( debug )  System.out.println( "setFloatValue" );
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue( java.lang.String, int )
	 */
	public void setValue( String name, int value ) {
		setValue( name, Integer.toString( value ) );
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue( java.lang.String, long )
	 */
	public void setValue( String name, long value ) {
		if ( debug )  System.out.println( "setValue" );
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue( java.lang.String, java.lang.String )
	 */
	public void setValue( String name, String value ) {
		String oldValue = getString( name );
		if ( oldValue == null || !oldValue.equals( value ) ) {
			( ( OptionsInfo )input.get( name ) ).setValue( value );
			if ( debug )  System.out.println( "setting value: "+name+" : "+value );
		}
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue( java.lang.String, boolean )
	 */
	public void setValue( String name, boolean value ) {
		String temp;	
		boolean oldValue = getBoolean( name );		
		if ( value ) temp = "true";
		else temp = "false";
		setValue( name, temp );		
	}

	/**
	 * @param options
	 */
	public void setInput( OptionsInfoMap options ) {
		this.input = options;
		
	}
}

/*
$Log: OptionsPreferenceStore.java,v $
Revision 1.11  2003/08/26 08:41:17  dek
some cleaning up

Revision 1.10  2003/08/24 14:05:11  dek
getInt() returns now value instead of "0"

Revision 1.9  2003/08/24 11:30:57  dek
prefDialog is not resizable any more, and we have IntEditors for int-values

Revision 1.8  2003/08/23 15:21:37  zet
remove @author

Revision 1.7  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: dek $

Revision 1.6  2003/08/19 13:08:54  dek
minor fix

Revision 1.5  2003/08/19 13:08:03  dek
advanced-Options included

Revision 1.4  2003/08/18 12:43:07  dek
removed extendendFontFieldEditor

Revision 1.3  2003/08/18 12:22:28  dek
g2gui-pref-page is now fully JFace-approved ;- )

Revision 1.2  2003/08/17 21:29:51  dek
removed TO-DO's, as we don't really have to do them, look at top javadoc-comment

Revision 1.1  2003/08/17 21:22:21  dek
reworked options, finally, it makes full use of the jFace framework ;- )

*/