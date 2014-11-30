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

public class Vec2 {
	
	double x, y;
	
	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}
	
	public Vec2 sub(Vec2 v) {
		return new Vec2(x - v.x, y - v.y);
	}
	
	public Vec2 add(Vec2 v) {
		return new Vec2(x + v.x, y + v.y);
	}
	
	public Vec2 mul(double d) {
		return new Vec2(x * d, y * d);
	}
	
	public double dot(Vec2 o) {
		return x * o.x + y * o.y;
	}
	
	public void normalize() {
		double l = getLength();
		if(l == 0)
			return;
		x /= l;
		y /= l;
	}

}
