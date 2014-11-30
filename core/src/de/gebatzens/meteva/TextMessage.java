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

import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Displays a message with a animation
 * 
 *
 */
public class TextMessage extends GameObject {

	String msg;
	double lifetime = 4;
	double scale = 2;
	double ox, oy;
	double fs;
	
	public TextMessage(String regname, double x, double y, double fs) {
		super(new TextureRegion(GScout.whiteTexture), x, y);
		msg = regname;
		ox = x;
		oy = y;
		this.fs = fs;
		GScout.setFontSize(fs);
		TextBounds bounds = GScout.survivant.getBounds(regname);
		width = (int) bounds.width;
		height = (int) bounds.height;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		x = ox;
		y = oy;
		if(scale == 1) {
			lifetime -= delta;
			if(lifetime < 1.0) {
				color.a = (float) lifetime;
			}
			if(lifetime <= 0)
				kill();
		} else {
			scale -= delta * 4;
			if(scale < 1)
				scale = 1;
			color.a = 1f - ((float) scale - 1f);
			GScout.setFontSize(fs);
			TextBounds b1 = GScout.survivant.getBounds(msg);
			float w1 = b1.width;
			float h1 = b1.height;
			GScout.setFontSize(fs * scale);
			TextBounds bounds = GScout.survivant.getBounds(msg);
			x = ox + w1 * 0.5 - bounds.width * 0.5;
			y = oy + h1 * 0.5 - bounds.height * 0.5;
		}
	}
	
	@Override
	public void render() {
		GScout.setFontSize(fs * scale);
		GScout.survivant.setColor(color);
		GScout.drawText(msg, (float) x, (float) y, true); 
	}

}
