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
package net.mldonkey.g2gui.model;

/**
 * ModelFactory26.java
 *
 * @version $Id: ModelFactory27.java,v 1.1 2004/09/17 22:36:48 dek Exp $ 
 *
 */
public class ModelFactory27 extends ModelFactory25 {
	
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.model.ModelFactory#getResultInfo()
	 */
	public ResultInfo getResultInfo() {		
		return new ResultInfo27(core);
	}

}


/*
 $Log: ModelFactory27.java,v $
 Revision 1.1  2004/09/17 22:36:48  dek
 update for gui-Protocol 29


 */