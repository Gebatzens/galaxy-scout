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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class TouchUtil {
	
	/**
	 * Checks if any finger is touching the screen on rect
	 */
	public static boolean isTouched(Rectangle rect) {
		for(int i = 0; i < 5; i++) {
			if(Gdx.input.isTouched(i))
				if(rect.contains(Gdx.input.getX(i), Gdx.graphics.getHeight() - Gdx.input.getY(i)))
					return true;
		}
		return false;
	}

}
