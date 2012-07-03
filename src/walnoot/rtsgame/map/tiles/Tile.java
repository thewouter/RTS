package walnoot.rtsgame.map.tiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import walnoot.rtsgame.Images;

public class Tile {
	public static final int WIDTH = 32, HEIGHT = 16;
	
	public static Tile[] tiles = new Tile[256];
	
	public static Tile grass1 = new Tile(1, false);
	public static Tile grass2 = new Tile(0, false);
	public static Tile sand1 = new Tile(17, false);
	public static Tile water1 = new Tile(2, true);
	
	protected final boolean solid;
	protected final byte id;
	
	protected Tile(int id, boolean solid){
		this.id = (byte) id;
		this.solid = solid;
		
		if(tiles[id] != null) throw new IllegalArgumentException("dublicate tile id!");
		tiles[id] = this;
	}
	
	public static Tile getTile(int ID){
		switch(ID){
		case 0:
			return grass2;
		case 1:
			return grass1;
		case 2:
			return water1;
		case 17:
			return sand1;
		default:
			return sand1;	
		}
	}
	
	public void render(Graphics g, Dimension screenSize, Point translation, Point position){
		BufferedImage image = getImage();
		
		int x = getPointOnScreen(position).x;
		int y = getPointOnScreen(position).y;
		
		if((x + image.getWidth() + translation.x) < 0 || (y + image.getHeight() + translation.y) < 0) return;
		if((x + translation.x) > screenSize.width || (y + translation.y) > screenSize.height) return;
		
		g.setColor(Color.BLACK);
		g.drawImage(image, x, y, null);
	}
	
	public Point getPointOnScreen(Point coordinats){
		int x = (coordinats.x - coordinats.y) * (-Tile.WIDTH / 2);
		int y = (coordinats.x + coordinats.y) * (Tile.HEIGHT / 2);
		
		return new Point(x, y);
	}
	
	public BufferedImage getImage(){
		return getImage(id);
	}
	
	public BufferedImage getImage(int id){
		return getImage(id % 16, id / 16);
	}
	
	public BufferedImage getImage(int x, int y){
		return Images.terrain[x][y];
	}
	
	public boolean isSolid(){
		return solid;
	}
	
	public int getID(){
		return (int)id;
	}
	
	public static int getHeight(){
		return HEIGHT;
	}
	
	public static int getWidth(){
		return WIDTH;
	}
}