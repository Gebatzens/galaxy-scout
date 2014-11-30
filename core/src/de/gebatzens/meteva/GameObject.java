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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;

public class GameObject {

	TextureRegion texture;
	Polygon poly;
	Color color;
	double x, y;
	int width, height;
	boolean verts, horzs;
	float rot;
	LevelState state;
	boolean killed = false;
	protected int lwidth;
	protected int lheight;

	public GameObject(TextureRegion tex, double x, double y) {
		texture = tex;
		this.x = x;
		this.y = y;
		color = Color.WHITE.cpy();
		state = (LevelState) GScout.state;
		lwidth = width = texture.getRegionWidth();
		lheight = height = texture.getRegionHeight();
		poly = GScout.createPolygon(width * 0.8f, height * 0.8f);
		poly.setPosition((float) (x + width / 2), (float) (y + height / 2)); 
	}
	
	public GameObject(String regname, double x, double y) {
		this(GScout.getRegion(regname), x, y);
	}
	
	public void render() {
		GScout.batch.setColor(color);
		GScout.batch.draw(texture.getTexture(), (float) x, (float) y, (float) width / 2f, (float) height / 2f, 
				width, height, 1, 1, rot, texture.getRegionX(), texture.getRegionY(), texture.getRegionWidth(), texture.getRegionHeight(), verts, horzs);
		
	}
	
	public void scaleToScreenSize() {
		width *= ((float) Gdx.graphics.getWidth() / 1920f);
		height = (int) (((float) width / (float) texture.getRegionWidth()) * (float) texture.getRegionHeight());
	}
	
	public void update(float delta) {
		
	}
	
	public void kill() {
		if(state.objects.contains(this))
			state.objects.oldObjects.add(this);
		if(state.vgobjects.contains(this))
			state.vgobjects.oldObjects.add(this);
		if(state.bgobjects.contains(this))
			state.bgobjects.oldObjects.add(this);
		killed = true;
	}
	
	public void updateRect() {
		if(lwidth != width || lheight != height) {
			lwidth = width;
			lheight = height;
			poly = GScout.createPolygon(width, height);
		}
		poly.setPosition((float) x + width / 2, (float) y + height / 2);
		poly.setRotation(rot);
	}
	
}
