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

import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.view.viewers.GSorter;

import org.eclipse.jface.viewers.Viewer;


/**
 * ResultTableSorter
 *
 * @version $Id: ResultTableSorter.java,v 1.17 2003/12/01 16:39:25 zet Exp $
 *
 */
public class ResultTableSorter extends GSorter {
    public ResultTableSorter(ResultTableView rTableViewer) {
        super(rTableViewer);
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GSorter#sortOrder(int)
     */
    public boolean sortOrder(int columnIndex) {
        switch (cViewer.getColumnIDs()[ columnIndex ]) {
        case ResultTableView.NETWORK:
        case ResultTableView.NAME:
            return true;

        default:
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public int compare(Viewer viewer, Object obj1, Object obj2) {
        ResultInfo result1 = (ResultInfo) obj1;
        ResultInfo result2 = (ResultInfo) obj2;

        switch (cViewer.getColumnIDs()[ columnIndex ]) {
        case ResultTableView.NETWORK:
            return compareStrings(result1.getNetwork().getNetworkName(),
                result2.getNetwork().getNetworkName());

        case ResultTableView.NAME:
            return compareStrings(result1.getName(), result2.getName());

        case ResultTableView.SIZE:
            return compareLongs(result1.getSize(), result2.getSize());

        case ResultTableView.FORMAT:
            return compareStrings(result1.getFormat(), result2.getFormat());

        case ResultTableView.MEDIA:
            return compareStrings(result1.getType(), result2.getType());

        case ResultTableView.AVAILABILITY:
            return compareIntegers(result1.getAvail(), result2.getAvail());

        default:
            return 0;
        }
    }
}


/*
$Log: ResultTableSorter.java,v $
Revision 1.17  2003/12/01 16:39:25  zet
set default sort order for specific columns

Revision 1.16  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.15  2003/10/31 13:16:33  lemmster
Rename Viewer -> Page
Constructors changed

Revision 1.14  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.13  2003/10/22 14:38:32  dek
removed malformed UTF-8 char gcj complains about (was only in comment)

Revision 1.12  2003/10/22 01:37:45  zet
add column selector to server/search (might not be finished yet..)

Revision 1.11  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

Revision 1.10  2003/09/18 10:39:21  lemmster
checkstyle

Revision 1.9  2003/09/17 20:07:44  lemmster
avoid NPEs in search

Revision 1.8  2003/08/23 15:21:37  zet
remove @author

Revision 1.7  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: zet $

Revision 1.6  2003/08/16 20:59:09  dek
searching works now without errors AGAIN ;-)

Revision 1.5  2003/08/15 22:51:50  dek
searching works now without errors again :-)

Revision 1.4  2003/07/31 11:55:39  zet
sort with empty strings at bottom

Revision 1.3  2003/07/31 04:11:10  zet
searchresult changes

Revision 1.2  2003/07/27 18:45:47  lemmstercvs01
lots of changes

Revision 1.1  2003/07/25 22:34:51  lemmstercvs01
lots of changes

*/
