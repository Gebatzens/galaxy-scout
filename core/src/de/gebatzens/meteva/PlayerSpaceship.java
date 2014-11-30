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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;

import de.gebatzens.meteva.Meteor.MeteorType;

public class PlayerSpaceship extends Spaceship {
	
	float  miningLaser = 1, ice, noAttack, nextLeben, nextIceLeben, mx, rotTarget, laserRissMult, iceAlpha, fireAlpha;
	Meteor miningLaserTarget;
	
	TextureRegion rnormal, reis;
	
	ParticleEffectPool bombEffectPool;
	Array<PooledEffect> effects = new Array<PooledEffect>();
	float highMin, highMax, lowMin, lowMax;
	float vhighMin, vhighMax, vlowMin, vlowMax;
	float nextMExpl;
	private TextureRegion firetex;
	boolean iceDamage = false;
	
	public PlayerSpaceship(double x, double y) {
		super(GScout.getRegion("raumschiff"), x, y);
		
		width *= 0.7f;

		scaleToScreenSize();
		
		updateRect();
		
		rnormal = texture;
		reis = GScout.getRegion("raumschifffrost");
		firetex = GScout.getRegion("raumschiffglut");
		
		ParticleEffect bombEffect = new ParticleEffect();
		bombEffect.loadEmitters(Gdx.files.internal("particles/partikeltest"));
		bombEffect.loadEmitterImages(Gdx.files.internal("particles"));
		bombEffectPool = new ParticleEffectPool(bombEffect, 1, 2);
		PooledEffect effect = bombEffectPool.obtain();
		effects.add(effect);
		ScaledNumericValue value = effect.getEmitters().get(0).getScale();
	    highMax = value.getHighMax();
	    highMin = value.getHighMin();
	    lowMax = value.getLowMax();
	    lowMin = value.getLowMin();
	    
	    value = effect.getEmitters().get(0).getVelocity();
	    vhighMax = value.getHighMax();
	    vhighMin = value.getHighMin();
	    vlowMax = value.getLowMax();
	    vlowMin = value.getLowMin();
	    
		
	}
	
	public void scaleParticles() {
		ParticleEmitter e = effects.get(0).getEmitters().get(0);
		float scale = width / (texture.getRegionWidth() * 0.7f);
	    e.getScale().setLow(lowMin * scale, lowMax * scale);
	    e.getScale().setHigh(highMin * scale, highMax * scale);
	    
	    e.getVelocity().setLow(vlowMin * scale, vlowMax * scale);
	    e.getVelocity().setHigh(vhighMin * scale, vhighMax * scale);
	}
	
	public boolean laser(float tx, float ty) {
		if(miningLaser == 0)
			return false;
		if(ice != 0)
			return false;
		if(miningLaserTarget != null)
			return false;
		
		for(GameObject g : state.objects)
			if(g instanceof Meteor) {
				Meteor m = (Meteor) g;
				if(m.getLaserRect().contains((int) tx, (int) ty)) {
					miningLaserTarget = m;
					miningLaserTarget.color = new Color(0.8f, 0.8f, 1f, 1f);
					return true;
				
				}
			}

		return false;
		
	}

	@Override
	public void updateRect() {
		if(lwidth != width || lheight != height) {
			lwidth = width;
			lheight = height;
			poly = GScout.createPolygon(width * 0.8f, height * 0.8f);
		}
		poly.setPosition((float) (x + width / 2), (float) (y + height / 2));
		poly.setRotation(rot);
	}
	
	@Override
	public void update(float delta) {
		double my = Math.sin(Math.toRadians(rot)) * ((float) Gdx.graphics.getWidth() / 1920f);
		
		y += my * delta * 500;
		//x += mx;
		x += (Gdx.graphics.getWidth() * 0.2f - x) * delta;
		updateRect();
		scaleParticles();
		
		noAttack += delta;
		
		/*if(noAttack > 10 && leben < 100) {
			nextLeben -= delta;
			if(nextLeben <= 0) {
				nextLeben = 1;
				leben++;
			}
		}
		*/
		
		/*if(miningLaserCooldown != 0 && miningLaser == 0) {
			miningLaserCooldown -= delta;
			if(miningLaserCooldown < 0)
				miningLaserCooldown = 0;
		}*/
		
		if(miningLaserTarget == null && miningLaser < 1) {
			miningLaser += delta * 0.1f;
			if(miningLaser > 1)
				miningLaser = 1;
		}
		
		if(nextIceLeben != 0) {
			nextIceLeben -= delta;
			if(nextIceLeben < 0)
				nextIceLeben = 0;
		}
		
		iceDamage = false;
		if(ice != 0) {
			if(nextIceLeben == 0) {
				leben--;
				nextIceLeben = 0.5f;
				noAttack = 0;
				iceDamage = true;
			}
			
			ice -= delta;
			if(ice <= 0) {
				ice = 0;
			}
			
			if(GScout.rand.nextInt(10) == 0) {
				Animation a = new Animation(GScout.getTexture("eisexpl4.png"), state.spaceship.x + GScout.rand.nextInt(state.spaceship.width), 
						state.spaceship.y + GScout.rand.nextInt(state.spaceship.height), 5, 3, 3);
				state.vgobjects.newObjects.add(a);
			}
		}
		
		if(ice == 0 && iceAlpha > 0) {
			iceAlpha -= delta;
			if(iceAlpha < 0)
				iceAlpha = 0;
		}
		if(fireAlpha > 0) {
			ice = 0;
			if(iceAlpha > 0) {
				iceAlpha -= delta * 3;
				if(iceAlpha < 0)
					iceAlpha = 0;
			}
			fireAlpha -= delta * 0.4;
			if(fireAlpha < 0)//ohmann ich hab keine lust auf den scheissg (fgffgfgff (ls))
				fireAlpha = 0;
		}
		
		if(x < -width || x > Gdx.graphics.getWidth() || y > Gdx.graphics.getHeight() + width || y < -height * 2) {
			kill(true);
			state.gameover();
			
			return;
		}
		
		/*if(MouseHelper.buttonReleased(0)) {
			LaserSchuss l = new LaserSchuss(x + width / 2, y + height / 2 + 10, new Vector2f((float) (Mouse.getX() - (x + width / 2)), 
					(float) ((Display.getHeight() - Mouse.getY()) - (y + height / 2f))).normalise(null), this);
			state.objects.newObjects.add(l);
		}*/
		
		if(miningLaser != 0 && miningLaserTarget != null) { //Laser aktiv
			miningLaser -= delta * 0.5;
			miningLaserTarget.health -= delta * 500;
			miningLaserTarget.scale *= 0.99;
			laserRissMult = 0.3f + GScout.rand.nextFloat() * 0.4f * ((float) Gdx.graphics.getWidth() / 1920f);
			
			//TODO Soll Geld werden
			switch(miningLaserTarget.type) {
			case NORMAL:
				state.points += 200 * delta;
				break;
			case GOLD:
				state.points += 4000 * delta;
				break;
			case IRON:
				state.points += 2000 * delta;
				break;
			case ICE:
				state.points += 2500 * delta;
				break;
			case DIAMOND:
				state.points += 7000 * delta;
				break;
			case FIRE:
				state.points += 3500 * delta;
				break;
				
			}
			
			
			if(miningLaserTarget.health <= 0) {
				miningLaserTarget.kill();
				
				Animation a = new Animation(GScout.getTexture("explosion.png"), miningLaserTarget.x + miningLaserTarget.width / 2f, 
						miningLaserTarget.y + miningLaserTarget.height / 2f, 1, 3, 3);
				a.mx = miningLaserTarget.speed *- miningLaserTarget.speed2 * miningLaserTarget.lspeed * 50.0;
				state.objects.newObjects.add(a);
				
				if(GScout.settings.soundsEnabled()) {
					GScout.meteorExpl[GScout.rand.nextInt(4)].play();
					GScout.laser.stop();
				}
				
				
			}

			if(miningLaser <= 0 || miningLaserTarget.killed) {
				if(GScout.settings.soundsEnabled())
					GScout.laser.stop();
				miningLaserTarget = null;
			}
		}
		
		boolean c = false;
		
		/*if(FullscreenActivity.isRotVectorAvailable) {
			rotTarget = FullscreenActivity.pitch * (FullscreenActivity.getDeviceOrientation() == Surface.ROTATION_270 ? 1.5f : -1.5f);
			if(rotTarget < -35)
				rotTarget = -35;
			if(rotTarget > 35)
				rotTarget = 35;
			if(((y < Gdx.graphics.getHeight() / 10 && rotTarget < 0) || (y > (float) Gdx.graphics.getHeight() * 0.9f && rotTarget > 0)))
				rotTarget = 0;
			rot += (rotTarget - rot) / (ice == 0 ? 10f : 200f);
			c = true;
		} else */
		
		if(Gdx.input.isTouched()) {
				double wert = -Gdx.input.getDeltaY() * (ice == 0 ? 1.05f: 0.08) * (1920.0 / (double)Gdx.graphics.getWidth()) * 0.1;
				if(!((y < Gdx.graphics.getHeight() / 8 && wert < 0) || (y > (float) Gdx.graphics.getHeight() * 0.8f && wert > 0))) {
					rot += wert;
					if(rot > 45)
						rot = 45;
					if(rot < -45)
						rot = -45; 
						
					c = true;
				}
				
		} else {
			double wert = (Gdx.input.isKeyPressed(Keys.W) ? 10 : (Gdx.input.isKeyPressed(Keys.S) ? -10 : 0)) * (ice == 0 ? 1.05f: 0.08) * (1920.0 / (double)Gdx.graphics.getWidth()) * 0.1;
			if(wert != 0 /*&& !((y < Gdx.graphics.getHeight() / 8 && wert < 0) || (y > (float) Gdx.graphics.getHeight() * 0.8f && wert > 0))*/) {
				rot += wert;
				if(rot > 45)
					rot = 45;
				if(rot < -45)
					rot = -45; 
					
				c = true;
			}
		}
		
		for (int i = effects.size - 1; i >= 0; i--) {
		    PooledEffect effect = effects.get(i);
		    
		    double radius = Math.sqrt(width * width * 0.45 * 0.45 + height * height * 0.31 * 0.31);
		    Vec2 p1 = new Vec2(-width * 0.5, 0);
		    Vec2 targetp = new Vec2(-width * 0.5, -height * 0.19);
		    double angle = Math.atan2(targetp.y,targetp.x) - Math.atan2(p1.y,p1.x);
		    //ohman
		    
		    effect.setPosition((float) (x + width * 0.5 - Math.cos(Math.toRadians(rot) + angle) * radius), (float) (y + height * 0.5 - Math.sin(Math.toRadians(rot) + angle) * radius)); 
		    effect.getEmitters().get(0).getAngle().setLow(180 + rot);
		    
		    effect.update(delta);
		    if (effect.isComplete()) {
		    	effect.reset();
		    }
		}
		
		if(y < height * 0.2  && rot > -45)
			rot += delta * (ice == 0 ? 100 : 1);
		else if(y > Gdx.graphics.getHeight() - height * 1.2f && rot < 45)
			rot -= delta * (ice == 0 ? 100 : 1);
		
		if(!c) {
			rot += rot / -15f * (ice == 0 ? 1f : 0.05f);
		}
		//if(!c2)
		//	mx -= mx * 1.1f;

		
		for(GameObject g : state.objects) {
			if(g instanceof Meteor)
				if(Intersector.overlapConvexPolygons(poly, g.poly)) {
					if(((Meteor) g).type == MeteorType.ICE) {
						ice = 2;
						iceAlpha = 1;
						g.kill();
						Animation a = new Animation(GScout.getTexture("eisexpl4.png"), x + width / 2f, 
								y + height / 2f, 1, 3, 3);
						state.objects.newObjects.add(a);
					} else if (((Meteor)g).type == MeteorType.FIRE) {
						noAttack = 0;
						leben -= 90;
						g.kill();
						Animation a = new Animation(GScout.getTexture("explosion.png"), x + width / 2f, 
								y + height / 2f, 1, 3, 3);
						state.objects.newObjects.add(a);
						if(GScout.settings.soundsEnabled())
							GScout.meteorExpl[GScout.rand.nextInt(4)].play();
					} else {
						leben -= delta * 200;
						nextMExpl -= delta;
						if(nextMExpl <= 0) {
							nextMExpl = 0.05f;
							Animation a = new Animation(GScout.getTexture("explosion.png"), x + GScout.rand.nextInt(width), 
									y + GScout.rand.nextInt(height), 3, 3, 3);
							state.vgobjects.newObjects.add(a);
						}
						noAttack = 0;
					}
			
				}
		}
		
		if(Math.round(leben) <= 0) {
			leben = 0;
			kill();
			state.gameover();
			return;
		}
		
		
	}
	
	@Override
	public void render() {
		
		for (int i = effects.size - 1; i >= 0; i--) {
		    PooledEffect effect = effects.get(i);
		    
		    effect.draw(GScout.batch);
		  
		}
		
		if(miningLaserTarget != null) {
			double x1 = x + width / 2f, y1 = y + (double) height * 0.3;
			double x2 = miningLaserTarget.x + miningLaserTarget.width / 2f, y2 = miningLaserTarget.y + miningLaserTarget.height / 2f;
			Vec2 dir = new Vec2(x2 - x1, y2 - y1);
			Vec2 dir2 = new Vec2(x2 - x1, y2 - y1);
			dir.normalize();
			
			GScout.batch.setColor((float) 0x69 / (float) 0xff, 0xb2 / (float) 0xff, 0xe0 / (float) 0xff, 1);
			
			float deg = (float) Math.toDegrees(Math.atan2(dir.y, dir.x));
			/*float[] matrix = new float[16];
			MetevaRenderer.setTexturesEnabled(false);
			Matrix.setIdentityM(matrix, 0);
			Matrix.translateM(matrix, 0, (float) (x1 + dir2.x / 2f), (float) (y1 + dir2.y / 2f), 0);
			Matrix.rotateM(matrix, 0, deg, 0, 0, 1);
			Matrix.scaleM(matrix, 0, (float) (dir2.mul(0.5f).getLength()), (float) (3) * ((float) Gdx.graphics.getWidth() / 1920f), 1);
			MetevaRenderer.setMatrix(matrix);*/ 
			
			GScout.batch.draw(new TextureRegion(GScout.whiteTexture, 0, 0, 1, 1), (float) x1, (float) y1, (float) 0, (float) 0, (int) dir2.getLength(), (int) (Gdx.graphics.getWidth() * 0.003), 1, 1, deg, true);
			
			
			TextureRegion tex = GScout.getRegion("riss");
			
			GScout.batch.draw(tex, (float) (x2 - tex.getRegionWidth() * laserRissMult / 2f), (float) (y2 - tex.getRegionHeight() * laserRissMult / 2f), (int) (tex.getRegionWidth() * laserRissMult), (int) (tex.getRegionHeight() * laserRissMult));
			
		}
		
		texture = rnormal;
		super.render();
		color.a = iceAlpha;
		texture = reis;
		super.render();
		color.a = fireAlpha;
		texture = firetex;
		super.render();
		color.a = 1;
	}
	
	public String[] getParts() {
		return new String[] { "rp1", "rp2", "rp3", "rp4", "rp5", "rp6"};
	}
	
	@Override
	public void kill() {
		if(GScout.settings.soundsEnabled())
			GScout.laser.stop();
		kill(false);
	}
	
	public void kill(boolean blackhole) {
		super.kill();
		
		if(blackhole)
			return;
		String[] parts = getParts();
		
		for(String s : parts) {
			float my = GScout.rand.nextFloat() * 0.1f * state.speed;
			if(GScout.rand.nextBoolean())
				my = -my;
			float mx = GScout.rand.nextFloat() * 0.1f * state.speed;
			if(GScout.rand.nextBoolean())
				mx = -mx;
			Scrap sc = new Scrap(s, x, y, mx, my, GScout.rand.nextFloat() * 0.1f);
			state.objects.newObjects.add(sc);
			
			
		}
		
		Animation a = new Animation(GScout.getTexture("eisexpl4.png"), x + width / 2f, y + height / 2f, 1, 3, 3);
		Animation a2 = new Animation(GScout.getTexture("explosion.png"), x + width / 2f, y + height / 2f, 1, 3, 3);
		state.objects.newObjects.add(a2);
		state.objects.newObjects.add(a);
		if(GScout.settings.soundsEnabled())
			GScout.playerExpl.play();
		
	}

}
