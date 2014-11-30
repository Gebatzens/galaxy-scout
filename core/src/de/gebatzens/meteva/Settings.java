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

import java.util.Properties;

import com.badlogic.gdx.Gdx;

public class Settings {

	Properties p = new Properties();
	
	public void setName(String name) {
		p.setProperty("name", name);
	}
	
	public String getName() {
		return p.getProperty("name");
	}
	
	public boolean tutorialEnabled() {
		String s = p.getProperty("tut");
		if(s == null)
			return true;
		return s.equals("true");
	}
	
	public void setTutorialEnabled(boolean e) {
		p.setProperty("tut", e ? "true" : "false");
	}
	
	public boolean musicEnabled() {
		String s = p.getProperty("music");
		if(s == null)
			return true;
		return s.equals("true");
	}
	
	public void setMusicEnabled(boolean e) {
		p.setProperty("music", e ? "true" : "false");
	}
	
	public boolean isFastModeEnabled() {
		String s = p.getProperty("fast");
		if(s == null)
			return false;
		return s.equals("true");
	}
	
	public void setFastModeEnabled(boolean e) {
		p.setProperty("fast", e ? "true" : "false");
	}
	
	public boolean soundsEnabled() {
		String s = p.getProperty("sound");
		if(s == null)
			return true;
		return s.equals("true");
	}
	
	public void setSoundsEnabled(boolean e) {
		p.setProperty("sound", e ? "true" : "false");
	}
	
	public void load() {
		try {
			p.load(Gdx.files.local("settings").read());
		} catch (Exception e) {
			p.setProperty("name", "Unbekannt");
			p.setProperty("fast", "false");
			p.setProperty("tut", "true");
			p.setProperty("music", "true");
			p.setProperty("sound", "true");
		}
	}
	
	public void save() {
		try {
			p.store(Gdx.files.local("settings").write(false), "You should not see this...    GS Settings; Time=" + System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}
