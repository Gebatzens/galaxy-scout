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
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class MainState extends State {

	public static enum MMState {
		MAIN, SCORE, MARKET
	}
	
	AtlasRegion title;
	float[] mx = new float[5], my = new float[5], mmx = new float[5], mmy = new float[5], rot = new float[5];
	AtlasRegion[] tex = new AtlasRegion[5];
	Button end, start, highscore;
	Button setName;
	boolean dialogOpened = false;
	MMState mstate = MMState.MAIN;
	GamemodeButton normalGM, fastGM;
	ScoreState sstate;
	MarketState market;
	
	@Override
	public void init() {
		title = GScout.getRegion("titel");
		
		
		for(int i = 0; i < 5; i++) {
			tex[i] = GScout.getRegion("m" + (GScout.rand.nextInt(5) + 1));
			createNew(i, true);
		
		}
		
		end = new Button(0, 0, GScout.getString("market_button"), 
				(int) (Gdx.graphics.getWidth() * 0.4f));
		start = new Button(0, 0, GScout.getString("start_button"),
				(int) (Gdx.graphics.getWidth() * 0.4f));
		highscore = new Button(0, 0, GScout.getString("highscore_button"), 
				(int) (Gdx.graphics.getWidth() * 0.4f)); 
		setName = new Button(0, 0, GScout.getString("change_name_button"), 
				(int) (Gdx.graphics.getWidth() * 0.18f)); 
		
		end.x = start.x = highscore.x = Gdx.graphics.getWidth() / 2f - end.width / 2f;
		start.y = Gdx.graphics.getHeight() * 0.45f;
		highscore.y = Gdx.graphics.getHeight() * 0.45f - end.height * 1.1f;
		end.y = Gdx.graphics.getHeight() * 0.45f - end.height * 2.2f;
		setName.x = end.x + end.width * 1.05f;
		setName.y = highscore.y + highscore.height * 0.8;
		
		end.updateRect();
		start.updateRect();
		highscore.updateRect();
		setName.updateRect();
		
		normalGM = new GamemodeButton(Gdx.graphics.getWidth() * 0.022f, setName.y, GScout.getString("normal_gm"));
		fastGM = new GamemodeButton(Gdx.graphics.getWidth() * 0.022f, setName.y - normalGM.height * 1.1f, GScout.getString("fast_gm"));
		normalGM.activated = !GScout.settings.isFastModeEnabled();
		fastGM.activated = !normalGM.activated;
		
		sstate = new ScoreState();
		sstate.init();
		
		market = new MarketState();
		market.init();
		
	}

	@Override
	public void render() {
		GScout.sbg.renderBackground();
		GScout.survivant.setColor(Color.WHITE);
		GScout.batch.setColor(Color.WHITE);

		if(mstate == MMState.MAIN) {
			float titleWidth = Gdx.graphics.getWidth() * 0.5f;
			GScout.batch.draw(title, (float) (Gdx.graphics.getWidth() * 0.26), (float) (Gdx.graphics.getHeight() * 0.65), (int) titleWidth, (int) ((titleWidth / (float) title.getRegionWidth()) * (float) title.getRegionHeight()));
			end.render();
			start.render();
			highscore.render();
			setName.render();
			normalGM.render();
			fastGM.render();
			GScout.soundb.render();
			GScout.musicb.render();
			GScout.setFontSize(Gdx.graphics.getWidth() * 0.02f);
			GScout.drawText(GScout.settings.getName(), (float) setName.x, (float) (setName.y - Gdx.graphics.getWidth() * 0.03f), true);
		}
			
		for(int i = 0; i < 5; i++) {
			GScout.batch.setColor(Color.WHITE);
			
			GScout.drawOriginCenter(tex[i], mx[i], my[i], (int) (tex[i].getRegionWidth() * ((double) Gdx.graphics.getWidth() / 1920.0)), (int) (tex[i].getRegionHeight() * ((float) Gdx.graphics.getWidth() / 1920f)), rot[i]);
		}
		
		if(mstate == MMState.SCORE) {
			sstate.render();
			return;
		} else if(mstate == MMState.MARKET) {
			market.render();
			return;
		}
		
		String s = GScout.getString("version_string");
		
		int fs = (int) (Gdx.graphics.getWidth() * 0.014);
		GScout.setFontSize(fs);
		TextBounds bounds = GScout.survivant.getBounds(s);
		GScout.drawText(s, (float) (Gdx.graphics.getWidth() - bounds.width - GScout.width * 0.15), Gdx.graphics.getHeight() * 0.04f, true);
		
		
	}
	public String getRandomTexture() {
		int i = GScout.rand.nextInt(6);
		switch(i) {
		case 0:
			return "ironmeteorit";
		default:
			return "m" + i;
		}
	}

	public void createNew(int i, boolean in) {
		boolean b = GScout.rand.nextBoolean();
		mx[i] = Gdx.graphics.getWidth() + (b ? GScout.rand.nextInt(200) : Gdx.graphics.getWidth() + GScout.rand.nextInt(200));
		if(in)
			mx[i] = GScout.rand.nextInt(GScout.width);
		
		my[i] = GScout.rand.nextInt(Gdx.graphics.getHeight());
		mmx[i] = b ? -GScout.rand.nextFloat() : GScout.rand.nextFloat();
		
		mmy[i] = GScout.rand.nextFloat();
		if(GScout.rand.nextBoolean())
			mmy[i] = -mmy[i];
		tex[i] = GScout.getRegion("m" + (GScout.rand.nextInt(5) + 1));
	}
	
	@Override
	public void update(float delta) {
		for(int i = 0; i < 5; i++) {
			mx[i] += mmx[i] * delta * 20;
			my[i] += mmy[i] * delta * 20;
			rot[i] += mmx[i] * delta * 20;
			if(mx[i] < -400 && mx[i] > Gdx.graphics.getWidth()) 
				createNew(i, false);
			if(my[i] > Gdx.graphics.getHeight())
				createNew(i, false);
			if(my[i] < -400)
				createNew(i, false);
		}

		if(mstate == MMState.SCORE) {
			sstate.update(delta);
			return;
		} else if(mstate == MMState.MARKET) {
			market.update(delta);
			return;
		}
		
		end.update(delta);
		start.update(delta);
		highscore.update(delta);
		setName.update(delta);
		normalGM.update(delta);
		fastGM.update(delta);
		
		
	}
	
	@Override
	public void actionPerformed(Object o) {
		if(mstate == MMState.SCORE) {
			sstate.actionPerformed(o);
			return;
		} else if(mstate == MMState.MARKET){
			market.actionPerformed(o);
			return;
		}
		if(o == end) {
			mstate = MMState.MARKET;
		} else if(o == setName) {
			Gdx.input.getPlaceholderTextInput(new TextInputListener() {

				@Override
				public void input(String text) {
					text = text.trim();
					if(!text.isEmpty()) {
						GScout.settings.setName(text);
						GScout.settings.save();
					}
					
				}

				@Override
				public void canceled() {
					
					
				}
				
			}, GScout.getString("enter_name"), GScout.getString("name"));
		} else if(o == start) {
			LevelState level = new LevelState(GScout.settings.tutorialEnabled());
			GScout.setState(level);
		} else if(o == highscore) {
			mstate = MMState.SCORE;
			
			//apply the mode to the highscore
			if(GScout.settings.isFastModeEnabled()) {
				sstate.fast.activated = true;
				sstate.normal.activated = false;
			} else {
				sstate.normal.activated = true;
				sstate.fast.activated = false;
			}
			
		} else if(o == normalGM) {
			normalGM.activated = true;
			fastGM.activated = false;
			GScout.settings.setFastModeEnabled(false);
			GScout.settings.save();
		} else if(o == fastGM) {
			normalGM.activated = false;
			fastGM.activated = true;
			GScout.settings.setFastModeEnabled(true);
			GScout.settings.save();
		} else if(o == GScout.musicb) {
			if(GScout.musicb.activated) {
				//ja, das stimmt
				GScout.settings.setMusicEnabled(false);
				GScout.rush.stop();
				
			} else {
				GScout.settings.setMusicEnabled(true);
				GScout.rush.play();
			}
			GScout.settings.save();
		} else if(o == GScout.soundb) {
			if(GScout.soundb.activated) {
				GScout.settings.setSoundsEnabled(false);
				
			} else {
				GScout.settings.setSoundsEnabled(true);
			}
			GScout.settings.save();
		}

	}
	
	@Override
	public void touchDown(float x, float y, int pointer) {
		
	}
	
	@Override
	public void tap(float x, float y, int count) {
		if(mstate == MMState.SCORE) {
			sstate.tap(x, y, count);
			return;
		} else if(mstate == MMState.MARKET) {
			market.tap(x, y, count);
			return;
		}
		end.tap(x, y);
		start.tap(x, y);
		highscore.tap(x, y);
		normalGM.tap(x, y);
		fastGM.tap(x, y);
		setName.tap(x, y);
		GScout.musicb.tap(x, y);
		GScout.soundb.tap(x, y);
	}
}
