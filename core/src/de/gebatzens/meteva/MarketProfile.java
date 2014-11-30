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

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.badlogic.gdx.Gdx;

public class MarketProfile {
	public String name = "mprof";
	public int money = 0;
	private HashMap<String, Integer> values = new HashMap<String, Integer>();
	
	public void save() {
		Properties p = new Properties();

		p.put("laserb", String.valueOf(get("laserb")));
		p.put("lasere", String.valueOf(get("lasere")));
		p.put("leben", String.valueOf(get("leben")));
		p.put("spaceship", String.valueOf(get("spaceship")));
		p.put("shield", String.valueOf(get("shield")));
		p.put("money", String.valueOf(money));
		try {
			p.store(Gdx.files.local("market_" + name).write(false), "Version " + GScout.version);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int get(String key) {
		Integer i = values.get(key);
		if(i == null)
			return 0;
		return i;
	}
	
	public void set(String key, int v) {
		values.put(key, v);
	}
	
	public void reset() {
		set("laserb", 1);
		set("lasere", 1);
		set("leben", 1);
		set("spaceship", 0);
		set("shield", 0);
	}
	
	public void load() {
		Properties p = new Properties();
		try {
			p.load(Gdx.files.local("market_" + name).read());
		} catch (Exception e) {
			set("laserb", 100);
			set("lasere", 1);
			set("leben", 100);
			set("spaceship", 0);
			set("shield", 0);
			save();
			return;
		}
		set("laserb", Integer.parseInt((String) p.get("laserb")));
		set("lasere", Integer.parseInt((String) p.get("lasere")));
		set("leben", Integer.parseInt((String) p.get("leben")));
		set("spaceship", Integer.parseInt((String) p.get("spaceship")));
		set("shield", Integer.parseInt((String) p.get("shield")));
		money = Integer.parseInt((String) p.get("money"));
	}

}
