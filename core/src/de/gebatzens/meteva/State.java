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

public abstract class State {
	
	public abstract void init();
	public abstract void render();
	public abstract void update(float delta);
	
	public void touchUp(float x, float y, int pointer) {
		
	}
	
	public void touchDown(float x, float y, int pointer) {
		
	}
	
	public void tap(float x, float y, int count) {
		
	}
	
	public void actionPerformed(Object o) {
		
	}

}
