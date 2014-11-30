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

import de.gebatzens.meteva.MainState.MMState;

public class MarketState extends State {

	public static enum ShopState {
		LASER, LEBEN, SPACESHIPS
	}
	
	GamemodeButton laser, leben, spaceships;
	Button back;
	ShopState state = ShopState.LASER;
	TextureRegion shop;
	MarketLevel laserb;
	
	@Override
	public void init() {
		int w = (int) (GScout.width * 0.2f);
		laser = new GamemodeButton(GScout.width * 0.16f, GScout.height * 0.75f, "Laser", w);
		leben = new GamemodeButton(GScout.width * 0.41f, GScout.height * 0.75f, "Leben", w);
		spaceships = new GamemodeButton(GScout.width * 0.645f, GScout.height * 0.75f, "Raumschiffe", w);
		laser.activated = true;
		
		back = new Button(0, 0, GScout.getString("back_button"), (int) (0.2f * Gdx.graphics.getWidth())); 
		back.x = Gdx.graphics.getWidth() / 2f - back.width / 2f;
		back.y = Gdx.graphics.getHeight() * 0.04f;
		back.updateRect();
		
		shop = GScout.getRegion("shop");
		
		float x = GScout.width * 0.17f;
		float by = GScout.height * 0.55f;
		laserb = new MarketLevel(x, by, "laserb", 10);
	}

	@Override
	public void render() {
		GScout.batch.setColor(1, 1, 1, 0.1f);
		float t = GScout.width * 0.005f;
		GScout.batch.draw(GScout.whiteTexture, (float) (Gdx.graphics.getWidth() * 0.14f - t), 
				(float) (Gdx.graphics.getHeight() * 0.85f), (int) (Gdx.graphics.getWidth() * 0.72f + 2*t), 
				(int) (t));
		GScout.batch.draw(GScout.whiteTexture, (float) (Gdx.graphics.getWidth() * 0.86f), 
				(float) (Gdx.graphics.getHeight() * 0.15f), (int) t, 
				(int) (Gdx.graphics.getHeight() * 0.7f));
		GScout.batch.draw(GScout.whiteTexture, (float) (Gdx.graphics.getWidth() * 0.14f - t), 
				(float) (Gdx.graphics.getHeight() * 0.15f - t), (int) (Gdx.graphics.getWidth() * 0.72f + 2*t), 
				(int) (t));
		GScout.batch.draw(GScout.whiteTexture, (float) (Gdx.graphics.getWidth() * 0.14f -t), 
				(float) (Gdx.graphics.getHeight() * 0.15f), (int) t, 
				(int) (Gdx.graphics.getHeight() * 0.7f));
		
		GScout.batch.setColor(0.7f, 0.7f, 0.7f, 0.3f);
		GScout.batch.draw(GScout.whiteTexture, (float) (Gdx.graphics.getWidth() * 0.14f), 
				(float) (Gdx.graphics.getHeight() * 0.15f), (int) (Gdx.graphics.getWidth() * 0.72f), 
				(int) (Gdx.graphics.getHeight() * 0.7f));
		
		GScout.batch.setColor(new Color(1, 1, 1, 0.1f));
		GScout.batch.draw(GScout.whiteTexture, Gdx.graphics.getWidth() * 0.14f, Gdx.graphics.getHeight() * 0.15f,
				Gdx.graphics.getWidth() * 0.72f, GScout.height * 0.55f);
		
		laser.render();
		leben.render();
		spaceships.render();
		back.render();
		switch(state) {
		case LASER:
			laserb.render();
			break;
		case LEBEN:
			break;
		case SPACESHIPS:
			break;
		}
		
		GScout.batch.setColor(Color.WHITE);
		float titleWidth = Gdx.graphics.getWidth() * 0.23f;
		GScout.batch.draw(shop, (float) (Gdx.graphics.getWidth() * 0.40), 
				(float) (Gdx.graphics.getHeight() * 0.87), (int) titleWidth, 
				(int) ((titleWidth / (float) shop.getRegionWidth()) * (float) shop.getRegionHeight()));
		
	}

	@Override
	public void update(float delta) {
		laser.update(delta);
		leben.update(delta);
		spaceships.update(delta);
		back.update(delta);
		
	}
	
	public void setState(ShopState state) {
		this.state = state;
		switch(state) {
		case LASER:
			laser.activated = true;
			leben.activated = spaceships.activated = false;
			break;
		case LEBEN:
			leben.activated = true;
			laser.activated = spaceships.activated = false;
			break;
		case SPACESHIPS:
			spaceships.activated = true;
			laser.activated = leben.activated = false;
			break;
		}
	}
	
	@Override
	public void actionPerformed(Object o) {
		if(o == back) {
			((MainState)GScout.state).mstate = MMState.MAIN;
			GScout.mprof.save();
		} else if(o == laser) {
			setState(ShopState.LASER);
		} else if(o == leben) {
			setState(ShopState.LEBEN);
		} else if(o == spaceships) {
			setState(ShopState.SPACESHIPS);
		}
		
	}
	
	@Override
	public void tap(float x, float y, int count) {
		back.tap(x, y);
		laser.tap(x, y);
		leben.tap(x, y);
		spaceships.tap(x, y);
	}

}
