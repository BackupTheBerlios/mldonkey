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

import java.io.IOException;
import java.io.InputStream;

/**
 * Information
 *
 * @author markus
 * @version $Id: Information.java,v 1.1 2003/06/12 22:23:06 lemmstercvs01 Exp $ 
 *
 */
public interface Information {
	public void readStream( InputStream inputStream ) throws IOException;
}

/*
$Log: Information.java,v $
Revision 1.1  2003/06/12 22:23:06  lemmstercvs01
lots of changes

*/