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
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;

public class SoundButton {

	boolean activated = false;
	AtlasRegion tex1, tex2;
	double x, y;
	float width, height;
	Rectangle rect;
	
	public SoundButton(double x, double y, AtlasRegion tex, AtlasRegion tex2, float width) {
		this.x = x;
		this.y = y;
		this.tex1 = tex;
		this.tex2 = tex2;
		this.width = width;
		this.height = width;
		rect = new Rectangle((float) x, (float) y, (float) (width), (float) (height));
	}
	
	public void render() {
		GScout.batch.setColor(Color.WHITE);
		GScout.batch.draw(activated ? tex2 : tex1, (float) x, (float) y, width, height);
	}
	
	public void tap(float x, float y) {
		if(rect.contains(x, y)) {
			activated = !activated;
			GScout.state.actionPerformed(this);
		}
	}

}
