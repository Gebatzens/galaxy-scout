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

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.I18NBundle;

public class GScout extends ApplicationAdapter implements InputProcessor, GestureListener {
	
	public static SpriteBatch batch;
	Texture img;
	public static TextureAtlas atlas;
	public static HashMap<String, AtlasRegion> map = new HashMap<String, AtlasRegion>();
	public static SpaceBackground sbg;
	public static State state;
	public static Random rand = new Random();
	public static BitmapFont survivant;
	public static Settings settings;
	public static int lastHSR = -1;
	public static Highscore lastHighscore = null;
	public static Highscore highscoren, highscoref;
	public static Texture whiteTexture;
	public static OrthographicCamera camera;
	public static int width, height;
	public static ShaderProgram fontShader;
	public static Locale locale;
	public static I18NBundle bundle;
	public static AssetManager manager;
	
	public static Sound[] meteorExpl = new Sound[4];
	public static Sound playerExpl, laser;
	public static Music rush;
	public static SoundButton musicb, soundb;
	
	public static ConfirmDialog confDia;
	
	public static MarketProfile mprof;
	public static Color guiColor = new Color(0, 102f / 255f, 188f / 255f, 200f / 255f);
	private boolean trace;
	public TraceInterface tracei;
	
	public final static int version = 5;
	
	@Override
	public void create () {
		
		//For testing
		trace = false;
		
		if(trace)
			tracei.beginTrace("initmeteva");
		batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("all.pack"));
		sbg = new SpaceBackground();
		sbg.initBackground();
		settings = new Settings();
		settings.load();
		highscoren = new Highscore();
		highscoren.load(false);
		highscoref = new Highscore();
		highscoref.load(true);
		mprof = new MarketProfile();
		mprof.load();
		mprof.reset();
		Gdx.app.setLogLevel(Application.LOG_ERROR);
		
		manager = new AssetManager();
		locale = Locale.getDefault();
		bundle = I18NBundle.createBundle(Gdx.files.internal("lang/bundle"), locale);
		Gdx.app.debug("Meteva", "using locale " + locale);
		
		for(int i = 0; i < 4; i++) {
			meteorExpl[i] = Gdx.audio.newSound(Gdx.files.internal("sounds/expl" + (i+1) + ".ogg"));
		}
		playerExpl = Gdx.audio.newSound(Gdx.files.internal("sounds/raumschiffexpl.ogg"));
		laser = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.ogg"));
		rush = Gdx.audio.newMusic(Gdx.files.internal("sounds/rush.mp3"));
		rush.setLooping(true);
		if(settings.musicEnabled())
			rush.play();
		
		
		Texture tex = new Texture(Gdx.files.internal("survivant.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		survivant = new BitmapFont(Gdx.files.internal("survivant.fnt"), new TextureRegion(tex), false);
		fontShader = new ShaderProgram(Gdx.files.internal("font.vert"), Gdx.files.internal("font.frag"));
		
		if (!fontShader.isCompiled()) {
		    Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
		}
		
		Pixmap p = new Pixmap(1, 1, Format.RGBA8888);
		p.setColor(Color.WHITE);
		p.drawPixel(0, 0);
		whiteTexture = new Texture(p);
		p.dispose();
		
	//	camera = new OrthographicCamera(1920, 1080);
	//	camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	//	camera.setToOrtho(true);
		
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(this);
		
		GestureDetector gd = new GestureDetector(this);
		im.addProcessor(gd);
		
		Gdx.input.setInputProcessor(im);
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		musicb = new SoundButton(GScout.width * 0.022f, GScout.height * 0.02f, GScout.getRegion("ton"), GScout.getRegion("tonaus"), 
				GScout.width * 0.065f);
		musicb.activated = !settings.musicEnabled();
		soundb = new SoundButton(GScout.width * 0.022f + 0.07f * GScout.width, GScout.height * 0.02f, GScout.getRegion("spielton"), GScout.getRegion("spieltonaus"), 
				GScout.width * 0.065f);
		soundb.activated = !settings.soundsEnabled();
		
		state = new MainState();
		state.init();
		
		if(trace)
			tracei.endTrace();
		
	}

	@Override
	public void dispose() {
		survivant.dispose();
		batch.dispose();
		rush.dispose();
	}
	
	public static AtlasRegion getRegion(String name) {
		AtlasRegion s = map.get(name);
		if(s == null) {
			s = atlas.findRegion(name);
			map.put(name, s);
		}
		
		return s;
	}
	
	public static void setFontSize(double fs) {
		survivant.setScale((float) (fs / 30.0));
	}
	
	public static String getString(String key, Object...strings) {
		try {
			return bundle.format(key, strings);
		} catch(Exception e) {
			return key;
		}
	}
		
	public static void drawOriginCenter(TextureRegion tex, float x, float y, float width, float height, float rot) {
		batch.draw(tex, x, y, width / 2f, height / 2f, width, height, 1, 1, rot);
	}
	
	public static void drawText(String text, float x, float y, boolean border) {
		batch.setShader(fontShader);
		fontShader.setUniformi("border", border ? 1 : 0);
		survivant.drawMultiLine(batch, text, x, y);
		batch.setShader(null);
	}
	
	@Override
	public void render () {
		if(trace)
			tracei.beginTrace("rendertrace");
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.setProjectionMatrix(camera.combined);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.enableBlending();
		batch.begin();
		state.render();
		state.update(Gdx.graphics.getDeltaTime());
		
		batch.end();
		if(trace) {
			tracei.endTrace();
			trace = false;
		}
	}
	
	public static double getDensityScale() {
		return (double) width / 1920d;
	}
	
	@Override
	public void pause() {
		if(state instanceof LevelState)
			((LevelState) state).pause = true;
		if(settings.musicEnabled())
			rush.pause();
		
	}
	
	public static void drawBoxBorder(float x, float y, float w, float h, float bw, Color in, Color out) {
		GScout.batch.setColor(out);
		float t = bw;
		GScout.batch.draw(GScout.whiteTexture, (float) (x - t), 
				(float) (y + h), (int) (w + 2*t), 
				(int) (t));
		GScout.batch.draw(GScout.whiteTexture, (float) (x + w),
				(float) (y), (int) t, 
				(int) (h));
		GScout.batch.draw(GScout.whiteTexture, (float) (x - t), 
				(float) (y - t), (int) (w + 2*t), 
				(int) (t+1));
		GScout.batch.draw(GScout.whiteTexture, (float) (x - t), 
				(float) (y), (int) t, 
				(int) (h));
		
		GScout.batch.setColor(in);
		GScout.batch.draw(GScout.whiteTexture, (float) (x), 
				(float) (y), (int) (w), 
				(int) (h));
	}
	
	@Override
	public void resume() {
		if(settings.musicEnabled())
			rush.play();
	}

	public static void setState(State s) {
		state = s;
		s.init();
		
	}
	
	public static Polygon createPolygon(float width, float height) {
		Polygon p = new Polygon(new float[]{0,0,
				width,0,
				width,height,
				0,height});
	    p.setOrigin(width/2, height/2);
	    return p;
	}
	
	public static Polygon createCirclePolygon(float radius) {
		float[] verts = new float[30];
		int index = 0;
		for(int i = 0; i < 360; i += 24) {
			verts[index] = (float) (Math.cos(Math.toRadians(i)) * radius + radius);
			verts[index + 1] = (float) (Math.sin(Math.toRadians(i)) * radius + radius);
			index += 2;
		}
		Polygon p = new Polygon(verts);
		p.setOrigin(radius, radius);
		return p;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		state.touchDown(screenX, Gdx.graphics.getHeight() - screenY, pointer);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		state.touchUp(screenX, Gdx.graphics.getHeight() - screenY, pointer);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		state.tap(x, Gdx.graphics.getHeight() - y, count);
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Loads a texture using a AssetManager
	 */
	public static Texture getTexture(String string) {
		Texture tex = null;
		try {
			tex = manager.get(string);
		} catch(Exception e) {
			manager.load(string, Texture.class);
			manager.finishLoading();
			tex = manager.get(string);
		}
		
		return tex;
	}
	
	public static Sound getSound(String string) {
		Sound s = null;
		try {
			s = manager.get(string);
		} catch(Exception e) {
			manager.load(string, Sound.class);
			manager.finishLoading();
			s = manager.get(string);
		}
		
		return s;
	}
}
