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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;

public class Highscore {
	
	public String[] players = new String[11];
	public int[] scores = new int[11];
	boolean fast;
	
	public void load(boolean fast) {
		this.fast = fast;
		InputStream fin = null;
		try {
			fin = Gdx.files.local(fast ? "highscorefast" : "highscore").read();
		} catch (Exception e) {
			return;
		}
		
		Scanner scan = new Scanner(fin);
		int index = 0;
		while(scan.hasNextLine()) {
			String next = scan.nextLine();
			String[] parts = next.split("\t");
			players[index] = parts[0];
			scores[index] = Integer.parseInt(parts[1]);
			index++;
		}
		scan.close();
	}
	
	public int add(String player, int score) {
		int ret = -1;
		for(int i = 0; i < 11; i++) {
			if(score > scores[i]) {
				for(int u = 9; u >= i; u--) {
					players[u+1] = players[u];
					scores[u+1] = scores[u];
				}
				players[i] = player;
				scores[i] = score;
				ret = i;
				break;
			}
		}
		save();
		return ret;
	}
	
	public void clear() {
		Gdx.files.local(fast ? "highscorefast" : "highscore").delete();
		for(int i = 0; i < 11; i++) {
			scores[i] = 0;
			players[i] = null;
		}
	}
	
	public void save() {
		try {
			OutputStream fos = Gdx.files.local(fast ? "highscorefast" : "highscore").write(false);
			PrintStream print = new PrintStream(fos);
			for(int i = 0; i < 11; i++) {
				String s = players[i];
				if(s == null) {
					print.close();
					return;
				}
				
				print.println(s + "\t" + scores[i]);
			}
			print.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
