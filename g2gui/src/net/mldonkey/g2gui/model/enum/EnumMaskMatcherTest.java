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
package net.mldonkey.g2gui.model.enum;

import net.mldonkey.g2gui.model.enum.Enum.MaskMatcher;
import junit.framework.TestCase;

/**
 * EnumMaskMatcher
 *
 * @version $Id: EnumMaskMatcherTest.java,v 1.1 2003/10/31 11:01:16 lemmster Exp $ 
 *
 */
public class EnumMaskMatcherTest extends TestCase {
	private MaskMatcher aMatcher;

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(EnumMaskMatcherTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		aMatcher = new MaskMatcher();
	}

	public void testMatches1() {
		assertTrue( aMatcher.count() == 0 );
		assertFalse( aMatcher.matches( EnumNetwork.DONKEY ) );

		aMatcher.add( EnumNetwork.DONKEY );
		assertFalse( aMatcher.add( EnumNetwork.DONKEY ) );
		assertTrue( aMatcher.matches( EnumNetwork.DONKEY ) );
		aMatcher.remove( EnumNetwork.DONKEY );
		assertFalse( aMatcher.remove( EnumNetwork.DONKEY ) );
		assertFalse( aMatcher.matches( EnumNetwork.DONKEY ) );

		aMatcher.add( EnumNetwork.BT );
		assertTrue( aMatcher.matches( EnumNetwork.BT ) );
		aMatcher.remove( EnumNetwork.BT );
		assertFalse( aMatcher.matches( EnumNetwork.BT ) );

		aMatcher.add( EnumNetwork.DONKEY );
		aMatcher.add( EnumNetwork.BT );
		assertTrue( aMatcher.matches( EnumNetwork.BT ) );
		assertFalse( aMatcher.matches( EnumNetwork.DC ) );
		assertTrue( aMatcher.matches( EnumNetwork.DONKEY ) );
		assertFalse( aMatcher.matches( EnumNetwork.DC ) );
		assertFalse( aMatcher.matches( EnumNetwork.FT ) );
		assertFalse( aMatcher.matches( EnumNetwork.GNUT2 ) );
		
		aMatcher.remove( EnumNetwork.BT );
		assertFalse( aMatcher.matches( EnumNetwork.BT ) );

		aMatcher.remove( EnumNetwork.DONKEY );
		assertFalse( aMatcher.matches( EnumNetwork.DONKEY ) );

		assertTrue( aMatcher.count() == 0 );
	}

}

/*
$Log: EnumMaskMatcherTest.java,v $
Revision 1.1  2003/10/31 11:01:16  lemmster
TestCase for EnumMaskMatcher

*/