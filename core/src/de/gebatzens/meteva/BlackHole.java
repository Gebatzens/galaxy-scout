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

public class BlackHole extends GameObject {

	//Texture tex1;
	boolean add = false;
	int lspeed;
	float colorM = 1;
	boolean addColorM;
	Color color1, color2;
	
	public BlackHole(double x, double y) {
		super(GScout.getRegion("sw1"), x, y);
		//tex1 = texture;
		lspeed = state.speed;
		
		color1 = new Color(0x1d / (float) 0xff, 0x47 / (float) 0xff, 0x7e / (float) 0xff, 1);
		color2 = new Color(0x36 / (float) 0xff, 0xe4 / (float) 0xff, 0xcc / (float) 0xff, 1);
		
		width = (int) (texture.getRegionWidth() * (GScout.rand.nextFloat() * 0.8f + 0.7f));
		
		scaleToScreenSize();
	}
	
	@Override
	public void update(float delta) {
		
		x -= delta * lspeed * 8f;
		if(x < -500) {
			kill();
			return;
		}
		
		if(addColorM) {
			colorM += delta * 0.3;
			if(colorM >= 1) {
				addColorM = false;
				colorM = 1;
			}
		} else {
			colorM -= delta * 0.3;
			if(colorM <= 0) {
				addColorM = true;
				colorM = 0;
			}
		}
		color = color1.cpy().mul(colorM).add(color2.cpy().mul(1f - colorM));
		color.a = 1;
		
		if(state.spaceship == null)
			return;
		
		double tx = state.spaceship.x + state.spaceship.width / 2f, ty = state.spaceship.y + state.spaceship.height / 2f;
		double sx = x + width / 2, sy = y + height / 2; 
		double dist = Math.sqrt(Math.pow(sx - tx, 2) + Math.pow(sy - ty, 2));
		Vec2 dif = new Vec2(sx - tx, sy - ty);
		dif.normalize();
		
		if(dist < width * 2) { //radius of black hole
			state.spaceship.x += dif.x * 5 * GScout.getDensityScale();
			state.spaceship.y += dif.y * 5 * GScout.getDensityScale();
		}
		
		double mult = (dist / (float) (width * 2));
		if(mult > 1)
			mult = 1;
		
		if(dist < width * 2) { 
			state.spaceship.width = (int) (state.spaceship.texture.getRegionWidth() * 0.7f * mult * ((float) Gdx.graphics.getWidth() / 1920f));
			state.spaceship.height = 
					(int) (((float) state.spaceship.width / (float) state.spaceship.texture.getRegionWidth()) * (float) state.spaceship.texture.getRegionHeight());
			state.spaceship.updateRect();
		}
		if(dist < 10) {
			state.spaceship.kill(true);
			state.gameover();
		}
		
		//Calculating speed and size of objects
		for(GameObject g : state.objects) {
			if(g instanceof Meteor) {
				double mx = g.x + g.width / 2f, my = g.y + g.height / 2f;
				dist = Math.sqrt(Math.pow(sx - mx, 2) + Math.pow(sy - my, 2));
				
				if(g instanceof Meteor) {
					if(dist < Gdx.graphics.getWidth() * 0.2f)
						((Meteor) g).speed2 = (float) (dist / (Gdx.graphics.getWidth() * 0.2f)) + 0.1f;
					else
						((Meteor) g).speed2 = 1;
					
					if(((Meteor)g).speed2 > 1)
						((Meteor)g).speed2 = 1;
				}
				
				mult = (dist / (Gdx.graphics.getWidth() * 0.2f));
				if(mult > 1)
					mult = 1;
				g.width = (int) (g.texture.getRegionWidth() * (g instanceof Meteor ? ((Meteor)g).scale : 0.5f) * mult * ((float) Gdx.graphics.getWidth() / 1920f));
				g.height = (int) (g.texture.getRegionHeight() * (g instanceof Meteor ? ((Meteor)g).scale : 0.5f) * mult * ((float) Gdx.graphics.getWidth() / 1920f));
			}
		}
	}
	

}
