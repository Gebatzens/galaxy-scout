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
import com.badlogic.gdx.math.Rectangle;

import de.gebatzens.meteva.MainState.MMState;

public class LevelState extends State {

	int speed, meteors;

	public GameObjectList objects = new GameObjectList(),
			vgobjects = new GameObjectList(), bgobjects = new GameObjectList();

	SpaceBackground background = new SpaceBackground();

	int points;
	float nextLevel = 10f, gameoverTimeout = 4f, nextBlackHole = 10, levelDelay = 10f;
	boolean pause, gameover;

	boolean keyEsc;

	PlayerSpaceship spaceship;
	int blackholeWait = 40;
	int removeMeteors = 0;
	TextureRegion pausetex, weitertex, lasertex, lebentex, laserBg;
	Rectangle pauserect;
	int tut;
	double start;

	Button pauseRestart, pauseMain;

	public LevelState(boolean tutorial) {
		speed = GScout.settings.isFastModeEnabled() ? 20 : 13;
		meteors = GScout.settings.isFastModeEnabled() ? 6 : 4;
		tut = tutorial ? 2 : 0;
		start = 0;
	}

	@Override
	public void init() {
		// Meteva.rand.setSeed(1000);
		background.initBackground();

		/* objects.add(( */spaceship = new PlayerSpaceship(
				-Gdx.graphics.getWidth() * -0.1f, Gdx.graphics.getHeight() / 2)/*
																				 * )
																				 * )
																				 */;
		for (int i = 0; i < meteors; i++) {
			objects.add(new Meteor(Gdx.graphics.getWidth()
					+ GScout.rand.nextInt(Gdx.graphics.getWidth()), GScout.rand
					.nextInt(Gdx.graphics.getHeight())));
		}

		lasertex = GScout.getRegion("batterie");
		lebentex = GScout.getRegion("leben");
		laserBg = GScout.getRegion("laserrahmen");

		pausetex = GScout.getRegion("pause");
		weitertex = GScout.getRegion("weiter");
		pauserect = new Rectangle((int) (Gdx.graphics.getWidth() * 0.874),
				(int) (Gdx.graphics.getHeight() * 0.899),
				Gdx.graphics.getWidth() * 0.05f,
				Gdx.graphics.getWidth() * 0.05f);
		pauseRestart = new Button(GScout.width * 0.15, GScout.height * 0.3,
				new Color(0, 102f / 255f, 188f / 255f, 200f / 255f),
				GScout.getString("restart_button"),
				GScout.getRegion("button2"),
				(int) (Gdx.graphics.getWidth() * 0.3f));
		pauseMain = new Button(GScout.width * 0.55, GScout.height * 0.3,
				new Color(0, 102f / 255f, 188f / 255f, 200f / 255f),
				GScout.getString("main_button"), GScout.getRegion("button2"),
				(int) (Gdx.graphics.getWidth() * 0.3f));
		/*
		 * MetevaSurface.addHandler(new TouchHandler() {
		 * 
		 * @Override public void touchDown(float x, float y) { }
		 * 
		 * @Override public void touchUp(float x, float y, long downTime) {
		 * if(pauserect.contains((int) x, (int) y) && !gameover) { pause =
		 * !pause; }
		 * 
		 * }
		 * 
		 * });
		 */

		// if(tut)
		// objects.add(new TextMessage("Weiche den Meteoriten aus", 1000, 500));

	}

	@Override
	public void render() {
		background.renderBackground();

		for (GameObject g : bgobjects)
			g.render();
		/*
		 * for(GameObject g : objects) if(g instanceof BlackHole) g.render();
		 */
		for (GameObject g : objects)
			if (g instanceof Meteor)
				g.render();
		/*
		 * for(GameObject g : objects) if(g instanceof LaserSchuss) g.render();
		 */
		for (GameObject g : objects)
			if (!(g instanceof Meteor/*
									 * || g instanceof BlackHole || g instanceof
									 * LaserSchuss
									 */))
				g.render();

		if (spaceship != null)
			spaceship.render();

		for (GameObject g : vgobjects)
			g.render();

		GScout.batch.setColor(Color.WHITE);

		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		int fs = (int) Math.round(width * 0.03);
		// int fsub = Math.round(width / 400f);
		int balkenRand = (int) (width * 0.004);
	
		GScout.survivant.setColor(new Color(0x3b / 255f, 0xa3 / 255f, 0xd7 / (float) 0xff, 1));
		GScout.setFontSize(fs);
		GScout.drawText("" + points, (float) (width * 0.015),
				(float) (height * 0.96), true);

		double laserPer = spaceship == null ? 0f : (spaceship.miningLaser);
		Color color = new Color((float) (1.0 - laserPer), (float) laserPer, 0,
				1);

		GScout.batch.setColor(color);
		GScout.batch.draw(laserBg, (float) (width * 0.170),
				(float) (height * 0.869), (int) (width * 0.04),
				(int) (width * 0.06));
		GScout.batch.setColor(Color.WHITE);
		GScout.batch
				.draw(lasertex,
						(float) (width * 0.175),
						(float) (height * 0.875),
						(int) (width * 0.03),
						(int) (lasertex.getRegionHeight() * ((width * 0.03) / (float) lasertex
								.getRegionWidth())));
		GScout.batch.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
		GScout.batch
				.draw(GScout.whiteTexture, (float) (width * 0.22),
						(float) (height * 0.885), balkenRand * 2
								+ (int) (width * 0.25 * laserPer),
						(int) (width * 0.04));
		GScout.batch.setColor(color);
		GScout.batch.draw(GScout.whiteTexture,
				(float) (width * 0.22 + balkenRand),
				(float) (height * 0.885 + balkenRand),
				(int) (width * 0.25 * laserPer), (int) (width * 0.04)
						- balkenRand * 2);
		GScout.batch.setColor(new Color((1f - (float) laserPer) * 0.6f,
				(float) laserPer * 0.6f, 0, 1));
		String text = (int) (laserPer * 100.0) + "";
		GScout.setFontSize(fs * 0.8);
		TextBounds bounds = GScout.survivant.getBounds(text);
		GScout.fontShader.setUniformi("border", 0);
		GScout.survivant.setColor(color.mul(0.5f, 0.5f, 052f, 1).clamp());
		GScout.drawText(text, (float) (width * 0.35 - bounds.width / 2f),
				(float) (height * 0.95), false);

		float lebenPer = spaceship == null ? 0f : (spaceship.leben / 100f);
		color = new Color(1f - lebenPer, lebenPer, 0, 1);

		GScout.batch.setColor(lebenPer, 0, 0, 1);
		GScout.batch.draw(lebentex, (float) (width * 0.505),
				(float) (height * 0.87), (int) (width * 0.06),
				(int) (width * 0.06));

		GScout.batch.setColor(color);

		GScout.batch.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
		GScout.batch
				.draw(GScout.whiteTexture, (float) (width * 0.58),
						(float) (height * 0.885), balkenRand * 2
								+ (int) (width * 0.25 * lebenPer),
						(int) (width * 0.04));
		GScout.batch.setColor(color);
		GScout.batch.draw(GScout.whiteTexture,
				(float) (width * 0.58 + balkenRand),
				(float) (height * 0.885 + balkenRand),
				(int) (width * 0.25 * lebenPer), (int) (width * 0.04)
						- balkenRand * 2);
		GScout.batch.setColor(new Color((1f - lebenPer) * 0.6f,
				lebenPer * 0.6f, 0, 1));
		GScout.survivant.setColor(color.mul(0.5f, 0.5f, 0.5f, 1).clamp());
		text = (int) (lebenPer * 100f) + "";
		GScout.setFontSize(fs * 0.8);
		bounds = GScout.survivant.getBounds(text);
		GScout.drawText(text, (float) (0.71 * width - bounds.width / 2f),
				(float) (height * 0.95), false);

		GScout.batch.setColor(Color.WHITE);

		GScout.batch
				.draw(pause ? weitertex : pausetex,
						Gdx.graphics.getWidth() * 0.875f,
						Gdx.graphics.getHeight() * 0.875f,
						(int) (Gdx.graphics.getWidth() * 0.05f),
						(int) (pausetex.getRegionHeight() * (Gdx.graphics
								.getWidth() * 0.05f / pausetex.getRegionWidth())));

		if (pause) {
			String s = GScout.getString("pause_string");
			GScout.setFontSize(Gdx.graphics.getWidth() * 0.1f);
			bounds = GScout.survivant.getBounds(s);
			GScout.survivant.setColor(Color.WHITE);
			GScout.drawText(s, Gdx.graphics.getWidth() / 2f - bounds.width
					* 0.4f, Gdx.graphics.getHeight() * 0.75f, true);
			pauseRestart.render();
			pauseMain.render();
		}

		if (gameover) {
			String s = GScout.getString("game_over_string");
			GScout.setFontSize(Gdx.graphics.getWidth() * 0.1f);
			bounds = GScout.survivant.getBounds(s);
			GScout.survivant.setColor(new Color(1f, 0, 0,
					1f - (gameoverTimeout / 4f)));
			GScout.drawText(s,
					Gdx.graphics.getWidth() / 2f - bounds.width / 2f,
					Gdx.graphics.getHeight() * 0.65f, true);
			GScout.batch.setColor(Color.WHITE);
		}

	}

	public int meteorAmount() {
		int a = 0;
		for (GameObject g : objects)
			if (g instanceof Meteor)
				a++;
		return a;
	}

	public boolean blackHole() {
		for (GameObject g : objects) {
			if (g instanceof BlackHole)
				return true;
		}
		return false;
	}

	/*
	 * public boolean anyEnemies() { for(GameObject g : objects) { if(g
	 * instanceof Enemy) return true; } return false; }
	 */

	@Override
	public void update(float delta) {
		if (gameover)
			pause = false;
		if (!pause && start == 0) {
			if (speed != 0) {
				points += delta * 1000;
				nextLevel -= delta
						* (GScout.settings.isFastModeEnabled() ? 2 : 1);

			}

			if (nextLevel <= 0 && points <= 90000) {
				nextLevel = levelDelay;
				speed += 1;
				if(points > 90000)
					levelDelay += 10;
				
				if(points <= 90000)
					objects.add(new Meteor(Gdx.graphics.getWidth()
						+ GScout.rand.nextInt(Gdx.graphics.getWidth()),
						GScout.rand.nextInt(Gdx.graphics.getHeight())));

			}

			background.updateBackground(delta);

			if (spaceship != null)
				spaceship.update(delta);
			for (GameObject o : objects)
				o.update(delta);
			for (GameObject o : vgobjects)
				o.update(delta);
			for (GameObject o : bgobjects)
				o.update(delta);

			if (points > 50000) {
				if (nextBlackHole != 0) {
					nextBlackHole -= delta;
					if (nextBlackHole < 0)
						nextBlackHole = 0;
				} else if (!blackHole()) {
					nextBlackHole = blackholeWait;
					bgobjects
							.add(new BlackHole(Gdx.graphics.getWidth() + 200,
									GScout.rand.nextInt(Gdx.graphics
											.getHeight() - 100)));
					if (blackholeWait >= 10) {
						blackholeWait -= 2;
					}

				}
			}

		} else if (!pause) {
			background.updateBackground(delta);
			for (GameObject go : objects)
				if (!(go instanceof Meteor))
					go.update(delta);
			start -= delta;
			if (start < 0)
				start = 0;
		}
		if (pause) {
			pauseRestart.update(delta);
			pauseMain.update(delta);
		}

		if (gameover) {
			gameoverTimeout -= delta;
			if (gameoverTimeout < 0) {
				MainState state = new MainState();
				state.mstate = MMState.SCORE;
				state.init();
				state.sstate.fast.activated = GScout.settings
						.isFastModeEnabled();
				state.sstate.normal.activated = !GScout.settings
						.isFastModeEnabled();

				GScout.state = state;
				return;
			}
		}

		objects.addNewObjects();
		objects.removeOldObjects();
		vgobjects.addNewObjects();
		vgobjects.removeOldObjects();
		bgobjects.addNewObjects();
		bgobjects.removeOldObjects();

	}

	public void gameover() {
		speed = 0;
		GScout.lastHSR = (GScout.settings.isFastModeEnabled() ? GScout.highscoref
				: GScout.highscoren).add(GScout.settings.getName(), points);
		GScout.lastHighscore = GScout.settings.isFastModeEnabled() ? GScout.highscoref
				: GScout.highscoren;
		for (GameObject g : bgobjects)
			if (g instanceof BlackHole)
				((BlackHole) g).lspeed = 0;
		for (GameObject g : objects)
			if (g instanceof Meteor)
				((Meteor) g).lspeed = 0;
		gameover = true;
		spaceship = null;

	}

	@Override
	public void tap(float x, float y, int count) {
		if (pauserect.contains(x, y) && !gameover) {
			pause = !pause;
			if (pause)
				GScout.laser.pause();
			else
				GScout.laser.resume();
		} else if (!pause && spaceship != null) {
			for (GameObject g : objects)
				if (g instanceof Meteor)
					if (((Meteor) g).getLaserRect().contains(x, y))
						if (spaceship.miningLaserTarget == g) {
							spaceship.miningLaserTarget = null;
							if(GScout.settings.soundsEnabled())
								GScout.laser.stop();
						} else {
							spaceship.miningLaserTarget = (Meteor) g;
							if(GScout.settings.soundsEnabled()) {
								GScout.laser.stop();
								GScout.laser.play(0.5f);
							}
						}
		}
		if (pause) {
			pauseRestart.tap(x, y);
			pauseMain.tap(x, y);
		}

		if (gameover)
			gameoverTimeout = -1;
	}

	@Override
	public void actionPerformed(Object o) {
		if (o == pauseRestart) {
			GScout.setState(new LevelState(true));
		} else if (o == pauseMain) {
			GScout.setState(new MainState());
		}
	}

}
