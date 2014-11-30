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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Animation extends GameObject {

	protected float next;
	protected int img = 0;
	protected int rows, cols;
	protected float div;
	protected boolean drawInCenter = true;
	public boolean light = false;
	public double mx, my;

	public Animation(Texture tex, double x, double y, float div, int rows, int cols) {
		super(new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight()), x, y);
		this.div = div;
		next = 0.05f;
		this.rows = rows;
		this.cols = cols;

	}
	
	public void update(float delta) {
		x += mx * delta;
		y += my * delta;

		next -= delta;
		if (img != rows * cols && next <= 0) {
			img++;
			next = 0.05f;
			if (img == rows * cols)
				kill();
		}
	}

	@Override
	public void render() {
		GScout.batch.setColor(color);
		float width = texture.getRegionWidth() / cols, height = texture.getRegionHeight() / rows;
		float scale = Gdx.graphics.getWidth() / 1920f;
		float rx = (float) (x - (width * scale) / (div * 2));
		float ry = (float) (y - (height * scale) / (div * 2)); 
		float rwidth = (width * scale / div);
		float rheight = height * scale / div;
		int u = (int) ((img % cols) * width);
		int v = (int) ((img / cols) * width);
		int tw = (int) width, th = (int) height; 
		GScout.batch.draw(texture.getTexture(), rx, ry, rwidth, rheight, u, v, tw, th, false, false);

	}

}
