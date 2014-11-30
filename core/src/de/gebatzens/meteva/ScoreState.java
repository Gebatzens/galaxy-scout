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
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.gebatzens.meteva.ConfirmDialog.ConfirmHandler;
import de.gebatzens.meteva.MainState.MMState;

public class ScoreState extends State {

	Button back;
	TextureRegion highscoretitle;
	GamemodeButton fast, normal;
	float blink;
	Color blinkColor1 = Color.RED, blinkColor2 = new Color(1, 201f / 255f, 0, 1), blinkColor = Color.RED;
	DeleteButton db;
	
	@Override
	public void init() {
		back = new Button(0, 0, new Color(0, 102f / 255f, 188f / 255f, 200f / 255f), GScout.getString("back_button"), GScout.getRegion("button2"),
				(int) (0.2f * Gdx.graphics.getWidth())); 
		back.x = Gdx.graphics.getWidth() / 2f - back.width / 2f;
		back.y = Gdx.graphics.getHeight() * 0.04f;
		back.updateRect();
		highscoretitle = GScout.getRegion("highscoretitle");
		fast = new GamemodeButton(GScout.width * 0.1, GScout.height * 0.03, GScout.getString("fast_gm"));
		normal = new GamemodeButton(GScout.width * 0.65, GScout.height * 0.03, GScout.getString("normal_gm"));
		db = new DeleteButton(GScout.width * 0.03f, GScout.height * 0.03f, GScout.getRegion("trash"), GScout.width * 0.06f);
	}

	@Override
	public void render() {
	//	Meteva.sbg.renderBackground();
		GScout.survivant.setColor(Color.WHITE);
		GScout.batch.setColor(Color.WHITE);
		
		float width = Gdx.graphics.getWidth() * 0.45f;
		if(width > highscoretitle.getRegionWidth())
			width = highscoretitle.getRegionWidth();
		GScout.batch.draw(highscoretitle, Gdx.graphics.getWidth() / 2f - width / 2f, Gdx.graphics.getHeight() * 0.88f, (int) width, (int) (((float) width / (float) highscoretitle.getRegionWidth()) * (float) highscoretitle.getRegionHeight()));
		
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
		
		for(int i = 0; i < 11; i++) {
			Highscore h = fast.activated ? GScout.highscoref : GScout.highscoren;
			String player = h.players[i];
			if(player == null)
				break;
			int score = h.scores[i];
			
			if(GScout.lastHSR == i && h == GScout.lastHighscore)
				if(i == 0) 
					GScout.survivant.setColor(blinkColor);
				else
					GScout.survivant.setColor(Color.RED);
			else if(i == 0)
				GScout.survivant.setColor(1, 201f / 255f, 0, 1);
			else/* if(i % 2 == 0)*/
				GScout.survivant.setColor(0.5f, 0.5f, 0.5f, 1);
			/*else
				Meteva.survivant.setColor(0x1a / (float) 0xff, 0x73 / (float) 0xff, 0xa3 / (float) 0xff, 1);
			*/
			
			if(i % 2 == 0) {
				if(i == 0)
					GScout.batch.setColor(0xff / (float) 0xff, 0x40 / (float) 0xff, 0x1e / (float) 0xff, 0.11f);
				else
					GScout.batch.setColor(0x00 / (float) 0xff, 0xa6 / (float) 0xff, 0xff / (float) 0xff, 0.11f);
				GScout.batch.draw(GScout.whiteTexture, GScout.width * 0.14f, GScout.height * 0.83325f - (i + 1) * GScout.width * 0.034f, (int) (GScout.width * 0.72f), (int) (GScout.width * 0.0325f));
			}
				
			GScout.setFontSize(Gdx.graphics.getWidth() * 0.028);
			
			GScout.drawText(player, 
					(float) (Gdx.graphics.getWidth() * 0.2), 
					(float) (Gdx.graphics.getHeight() * 0.84 - i * Gdx.graphics.getWidth() * 0.034), true);
			if(i == 0) 
				GScout.survivant.setColor(0xfc / (float) 0xff, 0x81 / (float) 0xff, 0x06 / (float) 0xff, 0.7f);
			else
				GScout.survivant.setColor(0x1a / (float) 0xff, 0x6b / (float) 0xff, 0xdd / (float) 0xff, 0.7f);
			TextBounds bounds = GScout.survivant.getBounds((i + 1) + ".");
			GScout.drawText((i + 1) + ".", 
					(float) (Gdx.graphics.getWidth() * 0.193 - bounds.width), 
					(float) (Gdx.graphics.getHeight() * 0.84 - i * Gdx.graphics.getWidth() * 0.034), true);
			if(i == 0) 
				GScout.survivant.setColor(0xfc / (float) 0xff, 0x81 / (float) 0xff, 0x06 / (float) 0xff, 0.6f);
			else
				GScout.survivant.setColor(0x53 / (float) 0xff, 0xd0 / (float) 0xff, 0xe3 / (float) 0xff, 0.6f);
			bounds = GScout.survivant.getBounds("" + score);
			GScout.drawText("" + score, (float) (Gdx.graphics.getWidth() * 0.84 - bounds.width), 
					(float) (Gdx.graphics.getHeight() * 0.84 - i * Gdx.graphics.getWidth() * 0.034), true);
		
		}
	
		back.render();
		fast.render();
		normal.render();
		db.render();
		
		GScout.survivant.setColor(Color.WHITE);
	
		
	}

	@Override
	public void update(float delta) {
		back.update(delta);
		fast.update(delta);
		normal.update(delta);
		blink -= delta;
		if(blink <= 0) {
			blink = 0.5f;
			if(blinkColor == blinkColor1)
				blinkColor = blinkColor2;
			else
				blinkColor = blinkColor1;
		}
		
	}
	
	@Override
	public void tap(float x, float y, int p) {
		back.tap(x, y);
		fast.tap(x, y);
		normal.tap(x, y);
		db.tap(x, y);
	}
	
	@Override
	public void actionPerformed(Object o) {
		if(o == back) {
			((MainState)GScout.state).mstate = MMState.MAIN;
			GScout.lastHSR = -1;
		} else if(o == fast) {
			fast.activated = true;
			normal.activated = false;
		} else if(o == normal) {
			normal.activated = true;
			fast.activated = false;
		} else if(o == db && (fast.activated ? GScout.highscoref : GScout.highscoren).players[0] != null) {
			GScout.confDia.startConfirmProcess(new ConfirmHandler() {

				@Override
				public void yes() {
					(fast.activated ? GScout.highscoref : GScout.highscoren).clear();
				}
				
			});
			
		}
	}

}
