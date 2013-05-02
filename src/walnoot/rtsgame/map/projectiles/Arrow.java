package walnoot.rtsgame.map.projectiles;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.Bow;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.StoneDefenseTower;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.MPGameScreen;

public class Arrow extends Projectile{
	private final double slope;
	private static int LENGTH_ARROW = 20;

	public Arrow(Map map, Entity owner, int xStart, int yStart, double horSpeed, int distance, int direction, int startHeight, int maxDistance) {
		super(map, owner, xStart, yStart, horSpeed, distance, direction, startHeight, maxDistance);
		new Sound("/res/Sounds/bowshot.mp3").play();
		//int approxLifetimeInTicks = (int) ((TICKS_PER_SECOND * (distance + 1))/Math.sqrt(horSpeedX * horSpeedX + horSpeedY * horSpeedY));
		//System.out.println("approx. lifetime: " + approxLifetimeInTicks);
		slope = - ((double)startHeight / ((double) distance - 1));
		System.out.println("slope arrow: " + slope + "; distance is: " + distance + " and startHeight is: " + startHeight);
		//new Exception().printStackTrace(); to open call hierachy
	}

	public void render(Graphics g){
		g.setColor(Color.BLACK);
		g.drawLine((int)xScreen, (int)yScreen/* - (int)height * Tile.HEIGHT*/, (int)xScreen + (int)horSpeedX / 2, (int)yScreen + (int)horSpeedY / 2  /*- (int)(height * Tile.HEIGHT)*/);
	}
	
	public void updateHeight(){
		height += (slope * horSpeed)/ TICKS_PER_SECOND;
		System.out.println("height : " + height);
	}

	public void onImpact(Entity e) {
		e.damage(1);
	}
	
}