package walnoot.rtsgame.map;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.players.Bow;
import walnoot.rtsgame.map.structures.nonnatural.warrelated.DefenseTower;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;

public class Arrow{
	double xScreen = 0;
	double yScreen = 0;
	double horSpeedX;
	double horSpeedY;
	int lifetime = 0;
	final int TICKS_PER_SECOND = (int)(1000 / RTSComponent.MS_PER_TICK);
	Entity owner;
	int distance = 0;
	int startX, startY;
	private Map map;
	
	
	/**
	 * @param xStart 		x position at spawn in map coordinats
	 * @param yStart		y position at spawn in map coordinats
	 * @param horSpeed		horizontal speed in blocks*s^-1
	 * @param vertSpeed		vertical Speed in blocks*s^-1
	 * @param gravity  		gravity in m/s^2
	 * @param direction		direction in degrees
	 */
	public Arrow(Map map, Entity owner, int xStart, int yStart, double horSpeed, int distance, int direction){
		this.map = map;
		this.owner = owner;
		xScreen = Util.getScreenX(xStart, yStart) + Tile.getWidth() / 2;// 
		yScreen = Util.getScreenY(xStart, yStart) + Tile.getHeight() / 2;// + owner.screen.translationY;
		startX = xStart;
		startY = yStart;
		
		direction %= 360;	
		if(direction <=90 ){														// set correct hor. directions
			horSpeedX = (int) (horSpeed * Math.cos((direction * Math.PI) / 180));
			horSpeedY = (int) (horSpeed * Math.sin((direction * Math.PI) / 180));
			horSpeedY *=-1;
		}else if(direction <= 180){
			direction = 180 - direction;
			horSpeedX = (int) (horSpeed * Math.cos((direction * Math.PI) / 180));
			horSpeedY = (int) (horSpeed * Math.sin((direction * Math.PI) / 180));
		}else if(direction <= 270){
			direction = 270 - direction;
			horSpeedX = (int) (horSpeed * Math.cos((direction * Math.PI) / 180));
			horSpeedY = (int) (horSpeed * Math.sin((direction * Math.PI) / 180));
			horSpeedX *=-1;
		}else if(direction <= 360){
			direction = 360 - direction;
			horSpeedX = (int) (horSpeed * Math.cos((direction * Math.PI) / 180));
			horSpeedY = (int) (horSpeed * Math.sin((direction * Math.PI) / 180));
			horSpeedY *=-1;
			horSpeedX *=-1;
		}
		this.distance = distance;
		
		new Sound("/res/Sounds/bowshot.mp3").play();
		
	}
	
	public void update(){
		xScreen += horSpeedX / TICKS_PER_SECOND;
		yScreen += horSpeedY / TICKS_PER_SECOND;
		
		if(Util.getDistance(startX, startY, Util.getMapX((int)xScreen, (int)yScreen), Util.getMapY((int)xScreen, (int)yScreen)) >= distance){
			stop();
		}
		
		if(map.getEntities(Util.getMapX((int)xScreen, (int)yScreen), Util.getMapY((int)xScreen, (int)yScreen))!= null){
			for (Entity e:map.getEntities(Util.getMapX((int)xScreen, (int)yScreen), Util.getMapY((int)xScreen, (int)yScreen))){
				if(e == owner) continue;
				
				e.damage(1);
				stop();
			}
		}
		
	}
	
	public void render(Graphics g){
		g.setColor(Color.BLACK);
		g.drawLine((int)xScreen, (int)yScreen, (int)xScreen + (int)horSpeedX / 64, (int)yScreen + (int)horSpeedY / 64 );
	}
	
	public void stop(){
		map.removeArrow(this);
	}
}