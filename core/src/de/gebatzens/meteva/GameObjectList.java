/*
 * This file is part of Galaxy Scout.
 *
 * Galaxy Scout is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Galaxy Scout is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Galaxy Scout.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package de.gebatzens.meteva;

import java.util.Vector;

public class GameObjectList extends Vector<GameObject> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Vector<GameObject> newObjects = new Vector<GameObject>();
	public Vector<GameObject> oldObjects = new Vector<GameObject>();
	
	public void addNewObjects() {
		for(GameObject g : newObjects)
			add(g);
		newObjects.clear();
	}
	
	public void removeOldObjects() {
		for(GameObject g : oldObjects)
			remove(g);
		oldObjects.clear();
		
	}

}
