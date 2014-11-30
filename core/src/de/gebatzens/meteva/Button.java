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
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;

public class Button {
	
	public Color color2, color1;
	String text;
	boolean pressed;
	double x, y;
	int width, height;
	AtlasRegion tex;
	Color color;
	Rectangle rect;
	boolean lastUpdateDown = false;
	Button inst = this;
	public boolean enabled = true;
	
	public Button(double x, double y, Color color, String text, AtlasRegion tex, int width) {
		this.tex = tex;
		color1 = color;
		color2 = new Color(1, 1, 1, 2).sub(color);
		this.color = color1;
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = (int) (((float) width / (float) tex.getRegionWidth()) * (float) tex.getRegionHeight());
		rect = new Rectangle((float) x, (float) y, (float) (width), (float) (height));
		
	}
	
	public Button(double x, double y, String text, int w) {
		this(x, y, GScout.guiColor, text, GScout.getRegion("button2"), w);
	}
	
	public void updateRect() {
		rect = new Rectangle((int) x, (int) y, (int) (width), (int) (height));
	}
	
	public void update(float delta) {
		if(TouchUtil.isTouched(rect)) {
			color = color2;
			pressed = true;
			
		} else {
			color = color1;
			pressed = false;
		}

	}
	
	public void render() {
		GScout.batch.setColor(color);
		GScout.batch.draw(tex, (float) x, (float) y, width, height);
		
		//int fs = Meteva.fontSurvivant.getFontsize(text, (int) (width * 0.8f), 5, 80, 7);
		GScout.setFontSize(height / 2);
		TextBounds bounds = GScout.survivant.getBounds(text);
		
		Color c = Color.WHITE;
		GScout.survivant.setColor(c);
		GScout.drawText(text, (float) (x + width / 2 - bounds.width / 2), (float) (y + height / 2 + bounds.height / 2 + height / 9), true);
		GScout.survivant.setColor(Color.WHITE);
	}
	
	public void tap(float x, float y) {
		if(rect.contains(x, y)) {
			GScout.state.actionPerformed(this);
		}
	}

}
