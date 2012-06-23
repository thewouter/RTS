package walnoot.rtsgame.map.tiles;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class WaterTile extends Tile {
	protected WaterTile(int id, boolean solid){
		super(id, solid);
	}
	
	public void render(Graphics g, Dimension screenSize, Point translation, Point position){
		super.render(g, screenSize, translation, position);
	}
}
