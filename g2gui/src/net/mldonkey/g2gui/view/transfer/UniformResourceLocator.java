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

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * UniformResourceLocator - Helper class for windows URL drag&drop
 *
 * @version $Id: UniformResourceLocator.java,v 1.1 2003/09/26 04:19:51 zet Exp $ 
 */
public class UniformResourceLocator extends ByteArrayTransfer {

    private static final String TYPENAME1 = "UniformResourceLocator";
    private static final String TYPENAME2 = "text/x-moz-url-data"; // testing..
    private static final int TYPEID1 = registerType(TYPENAME1);
    private static final int TYPEID2 = registerType(TYPENAME2);
    private static UniformResourceLocator _instance = new UniformResourceLocator();

    /**
     * @return UniformResourceLocator
     */
    public static UniformResourceLocator getInstance() {
        return _instance;
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.dnd.Transfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
     */
    public Object nativeToJava(TransferData transferData) {

        if (isSupportedType(transferData)) {
            byte[] buffer = (byte[]) super.nativeToJava(transferData);
            if (buffer == null) return null;

            int nullAt = 0;
            for (int i = 0; i < buffer.length && buffer[i] != 0; i++) {
                nullAt++;
            }
            return new String(buffer, 0, nullAt);
        }

        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
     * requires 2+ or gtk crashes
     */
    protected String[] getTypeNames() {
        return new String[] { TYPENAME1, TYPENAME2 };
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
     */
    protected int[] getTypeIds() {
        return new int[] { TYPEID1, TYPEID2 };
    }
}

/*
$Log: UniformResourceLocator.java,v $
Revision 1.1  2003/09/26 04:19:51  zet
initial commit

*/