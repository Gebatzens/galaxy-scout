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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ETC1;
import com.badlogic.gdx.graphics.glutils.ETC1TextureData;

public class SpaceBackground {
	
	Texture tex1/*, tex2*/;
	float value;
	
	public void renderBackground() {
		GScout.batch.setColor(Color.WHITE);
		GScout.batch.draw(tex1, -value, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
		GScout.batch.draw(tex1, -value + Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
		GScout.batch.draw(tex1, -value + Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
		/*Meteva.batch.draw(tex2, Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());*/
		
		
	}
	
	public void updateBackground(float delta) {
		if(!(GScout.state instanceof LevelState))
			return;
		value += delta * ((LevelState)GScout.state).speed;
		if(value > Gdx.graphics.getWidth() / 2)
			value = 0;
	}

	public void initBackground() {
		ETC1TextureData data1 = new ETC1TextureData(new ETC1.ETC1Data(Gdx.files.internal("stars1.pkm")), false);
	//	ETC1TextureData data2 = new ETC1TextureData(new ETC1.ETC1Data(Gdx.files.internal("stars2.pkm")), false);
		tex1 = new Texture(data1);
	//	tex2 = new Texture(data2);
	}

}
