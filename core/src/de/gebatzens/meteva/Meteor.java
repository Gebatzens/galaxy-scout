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

import com.badlogic.gdx.math.Rectangle;

public class Meteor extends GameObject {

	public static enum MeteorType { NORMAL, GOLD, IRON, ICE, DIAMOND, FIRE }
	
	float speed, speed2 = 1, lastAttack;
	MeteorType type;
	float scale, lspeed, lscale;
	float health;
	double tmstart;
	TextMessage msg;
	boolean rotNeg;
	
	public Meteor(double x, double y) {
		super("m" + (GScout.rand.nextInt(5) + 1), x, y);
		
		type = MeteorType.NORMAL;
	
		if(GScout.rand.nextInt(10) == 0) {
			texture = GScout.getRegion("ironmeteorit");
			width = texture.getRegionWidth();
			height = texture.getRegionHeight();
			type = MeteorType.IRON;
			
			if(state.points > 30000 && GScout.rand.nextInt(3) == 0) {
				texture = GScout.getRegion("eismeteorit");
				width =texture.getRegionWidth();
				height = texture.getRegionHeight();
				type = MeteorType.ICE;
				this.y = state.spaceship.y;
			} else if(GScout.rand.nextInt(5) == 0) {
				if(GScout.rand.nextInt(3) == 0) {
					texture = GScout.getRegion("dmete");
					width = texture.getRegionWidth();
					height = texture.getRegionHeight();
					type = MeteorType.DIAMOND;
				} else {
					texture = GScout.getRegion("goldm");
					width =texture.getRegionWidth();
					height = texture.getRegionHeight();
					type = MeteorType.GOLD;
				}
			}
		} else if(GScout.rand.nextInt(35) == 0) {
			texture = GScout.getRegion("feufeumeteor");
			width = texture.getRegionWidth();
			height = texture.getRegionHeight();
			type = MeteorType.FIRE;
		}
			
		
		speed = 0.3f + GScout.rand.nextFloat() * (type == MeteorType.ICE ? 0.3f : 0.5f);
		lspeed = state.speed;
		
		scale = 0.5f + GScout.rand.nextFloat() * 0.5f;
		width *= scale;
		
		rotNeg = GScout.rand.nextBoolean();
		
		health = width * height * 0.05f;
		if(health > 900)
			health = 900;
		
		scaleToScreenSize();
		//poly = Meteva.createCirclePolygon(Math.min(width, height));
		updateRect();
		
		if(!(type == MeteorType.NORMAL || type == MeteorType.FIRE || type == MeteorType.ICE) && state.tut > 0) {
			msg = new TextMessage(GScout.getString("meteorTap"), x, y + height * 1.5, height * 0.4);
			msg.x = x + width / 2 - msg.width / 2;
			msg.lifetime = 4;
			tmstart = 0.3;
			state.tut--;
			if(state.tut == 0) {
				GScout.settings.setTutorialEnabled(false);
				GScout.settings.save();
			}
		}
	}
	
	@Override
	public void updateRect() {
		if(lwidth != width || lheight != height) {
			lwidth = width;
			lheight = height;
			poly = GScout.createPolygon(width * 0.8f, height * 0.8f);
		}
		poly.setPosition((float) x + width / 2, (float) y + height / 2);
		poly.setRotation(rot);
	}
	
	@Override
	public void update(float delta) {
		if(scale != lscale) {
			lscale = scale;
			width = (int) (texture.getRegionWidth() * scale);
			scaleToScreenSize();
		}
		x -= delta * speed * lspeed * 50f * speed2 * GScout.getDensityScale();
		rot += speed * delta * 40 * (rotNeg ? -1 : 1);
		if(type == MeteorType.ICE)
			speed += 0.005f;
		updateRect();
		if(x < -width * 3) {
			kill();

		}
		
		lastAttack += delta;
		if(type == MeteorType.FIRE && state.spaceship != null) {
			double tx = state.spaceship.x + state.spaceship.width / 2f, ty = state.spaceship.y + state.spaceship.height / 2f;
			double sx = x + width / 2, sy = y + height / 2;
			double dist = Math.sqrt(Math.pow(sx - tx, 2) + Math.pow(sy - ty, 2));
			if(dist < width * 3) {
				if(state.spaceship.fireAlpha < 1) {
					state.spaceship.fireAlpha += delta;
					if(state.spaceship.fireAlpha > 1)
						state.spaceship.fireAlpha = 1;
				}
				if(GScout.rand.nextInt(10) == 0) {
					state.spaceship.leben--;
					state.spaceship.ice = 0;
					state.spaceship.noAttack = 0;
					//if(0 == Meteva.rand.nextInt(10)) {
					Animation a = new Animation(GScout.getTexture("explosion.png"), state.spaceship.x + GScout.rand.nextInt(state.spaceship.width), 
							state.spaceship.y + GScout.rand.nextInt(state.spaceship.height), 5, 3, 3);
					state.vgobjects.newObjects.add(a);
					lastAttack = 0;
				}
			}
		}
		
		if(msg != null && x <= GScout.width) {
			tmstart -= delta;
			if(tmstart <= 0 && !msg.killed) {
				if(state.spaceship != null && state.spaceship.miningLaserTarget == this) {
					msg.kill();
				}
				msg.ox = x + width / 2 - msg.width / 2;
				msg.update(delta);
			}
		}
	}
	
	public Rectangle getLaserRect() {
		return new Rectangle((int) (x - width / 2f), (int) (y - height / 2f), (int) (width * 2f), (int) (height * 2f));
	}
	
	@Override
	public void kill() {
		super.kill();
		if(state.removeMeteors == 0) {
			//state.objects.newObjects.add(new Meteor(Gdx.graphics.getWidth() + Meteva.rand.nextInt(Gdx.graphics.getWidth()), 
			//		(Meteva.settings.isFastModeEnabled() && Meteva.rand.nextBoolean()) ? (Meteva.rand.nextBoolean() ? 10 + Meteva.rand.nextInt(100) : Gdx.graphics.getHeight() - 100 - Meteva.rand.nextInt(100)) : Meteva.rand.nextInt(Gdx.graphics.getHeight())));
			Meteor m = new Meteor(GScout.width + GScout.rand.nextInt(GScout.width),
					GScout.rand.nextInt(GScout.height) - GScout.height * 0.05);
		
			if(state.points < 50000 && GScout.rand.nextInt(10) == 0) {
				m.y = state.spaceship.y + (GScout.rand.nextBoolean() ? GScout.rand.nextFloat() * 10 : -GScout.rand.nextFloat() * 10);
				m.updateRect();
			}
			
			state.objects.newObjects.add(m);
		}
			
		else
			state.removeMeteors--;
	}
	
	@Override
	public void render() {
		super.render();
		if(msg != null && tmstart <= 0 && !msg.killed)
			msg.render();
	}
	
	/*@Override
	public void updateRect() {
		if(lwidth != width || lheight != height) {
			lwidth = width;
			lheight = height;
			poly = Meteva.createCirclePolygon(Math.min(width, height));
		}
		float r = Math.min(width,  height) / 2;
		poly.setPosition((float) x + r, (float) y + r);
		poly.setRotation(rot);
	}*/

}
