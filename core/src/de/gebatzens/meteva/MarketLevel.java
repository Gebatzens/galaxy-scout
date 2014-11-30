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

import com.badlogic.gdx.graphics.Color;

public class MarketLevel {

	String id;
	float x, y;
	int mlevel;
	Color color;
	
	public MarketLevel(float x, float y, String id, int mlevel) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.mlevel = mlevel;
		color = new Color(252f / 255f, 70f / 255f, 10f / 255f, 0.35f);
		//color = GScout.guiColor.cpy();
		//color.a = 0.3f;
		
	}
	
	public void render() {
		Color nc = new Color(1, 1, 1, 1).sub(color);
		nc.a = 0.6f;
		GScout.drawBoxBorder(x, y, GScout.width * 0.25f, GScout.height * 0.08f,
				GScout.width * 0.005f, color, color.cpy().mul(new Color(0.5f, 0.5f, 0.5f, 1f)));
		GScout.batch.setColor(nc);
		GScout.drawBoxBorder(x, y, 
				GScout.width * 0.25f * ((float) GScout.mprof.get(id) / (float) mlevel), GScout.height * 0.08f, 
				GScout.width * 0.005f, nc, nc.cpy().mul(new Color(0.5f, 0.5f, 0.5f, 1f)));
		
		GScout.survivant.setColor(Color.BLACK);
		GScout.setFontSize(GScout.width * 0.04f);
		GScout.drawText(GScout.mprof.get(id) + "", x, y + GScout.height * 0.1f, true);
		
	}
	
}
