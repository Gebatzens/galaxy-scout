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

public class GamemodeButton extends Button {

	boolean activated = false;
	
	public GamemodeButton(double x, double y, String text, int w) {
		super(x, y, new Color(0, 102.0f / 255.0f, 188f / 255f, 200f / 255f), text, GScout.getRegion("button2"), w);
	}
	
	public GamemodeButton(double x, double y, String text) {
		this(x, y, text, (int) (GScout.width * 0.25f));
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		if(activated)
			color = color2;
		else
			color = color1;
	}
	
	@Override
	public void render() {
		super.render();
		GScout.batch.setColor(Color.WHITE);
		if(activated)
			GScout.batch.draw(GScout.whiteTexture, (float) (x + width * 0.25f), (float) (y + height * 0.24f), width / 2, height * 0.05f);
	}

}
