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
package net.mldonkey.g2gui.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.WeakHashMap;


/**
 * ObjectWeakMap
 *
 * @version $Id: ObjectWeakMap.java,v 1.2 2003/11/26 15:48:09 zet Exp $
 *
 */
public class ObjectWeakMap extends Observable {
    public static final int ADDED = 0;
    public static final int UPDATED = 1;
    public static final int REMOVED = 2;
    public static final Integer ADDED_OBJECT = new Integer(ADDED);
    public static final Integer UPDATED_OBJECT = new Integer(UPDATED);
    public static final Integer REMOVED_OBJECT = new Integer(REMOVED);
    private Map weakMap;
    private List addedList;
    private List removedList;
    private List updatedList;

    public ObjectWeakMap() {
        weakMap = Collections.synchronizedMap(new WeakHashMap());
        addedList = Collections.synchronizedList(new ArrayList());
        removedList = Collections.synchronizedList(new ArrayList());
        updatedList = Collections.synchronizedList(new ArrayList());
    }

    public boolean add(Object object) {
        synchronized (this) {
            if (!weakMap.containsKey(object)) {
                weakMap.put(object, null);
                addedList.add(object);
                this.setChanged();
                this.notifyObservers(ADDED_OBJECT);

                return true;
            }
        }

        return false;
    }

    public void remove(Object object) {
        synchronized (this) {
            if (weakMap.containsKey(object)) {
                weakMap.remove(object);
                removedList.add(object);
                this.setChanged();
                this.notifyObservers(REMOVED_OBJECT);
            }
        }
    }

    public void addOrUpdate(Object object) {
        if (!add(object)) {
            updatedList.add(object);
            this.setChanged();
            this.notifyObservers(UPDATED_OBJECT);
        }
    }

    public Map getWeakMap() {
        return weakMap;
    }

    public List getAddedList() {
        return addedList;
    }

    public List getRemovedList() {
        return removedList;
    }

    public List getUpdatedList() {
        return updatedList;
    }

    public void clearAddedList() {
        addedList.clear();
    }

    public void clearRemovedList() {
        removedList.clear();
    }

    public void clearUpdatedList() {
        updatedList.clear();
    }

    public void clearAllLists() {
        synchronized (this) {
            addedList.clear();
            removedList.clear();
            updatedList.clear();
        }
    }
    
    public Set getKeySet() {
		return weakMap.keySet();
    }
}


/*
$Log: ObjectWeakMap.java,v $
Revision 1.2  2003/11/26 15:48:09  zet
minor

Revision 1.1  2003/11/26 07:41:56  zet
initial

*/
