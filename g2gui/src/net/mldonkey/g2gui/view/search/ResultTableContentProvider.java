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
package net.mldonkey.g2gui.view.search;

import java.util.List;

import net.mldonkey.g2gui.view.viewers.table.GTableContentProvider;


/**
 * ResultTableContentProvider
 *
 *
 * @version $Id: ResultTableContentProvider.java,v 1.7 2003/10/31 13:16:33 lemmster Exp $
 *
 */
public class ResultTableContentProvider extends GTableContentProvider {
    public ResultTableContentProvider(ResultTablePage rTableViewer) {
        super(rTableViewer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#
     * getElements(java.lang.Object)
     */
    public Object[] getElements(Object arg0) {
        List aList = (List) arg0;

        return aList.toArray();
    }
}


/*
$Log: ResultTableContentProvider.java,v $
Revision 1.7  2003/10/31 13:16:33  lemmster
Rename Viewer -> Page
Constructors changed

Revision 1.6  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.5  2003/10/22 01:37:45  zet
add column selector to server/search (might not be finished yet..)

Revision 1.4  2003/08/23 15:21:37  zet
remove @author

Revision 1.3  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: lemmster $

Revision 1.2  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/
