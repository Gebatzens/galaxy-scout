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

public class Scrap extends GameObject {

	double mx, my, rotadd;
	
	public Scrap(String tex, double x, double y, double mx, double my, double rotadd) {
		super(tex, x, y);
		this.mx = mx;
		this.my = my;
		this.rotadd = rotadd;
		rot = GScout.rand.nextInt(360);
		
		scaleToScreenSize();
	}
	
	@Override
	public void update(float delta) {
		x += mx * delta * 200;
		y += my * delta * 200;
		rot += rotadd * delta * 250;
	}

}
