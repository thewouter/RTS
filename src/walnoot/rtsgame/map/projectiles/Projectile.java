package walnoot.rtsgame.map.projectiles;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.MPGameScreen;

public abstract class Projectile {

	double xScreen = 0;
	double yScreen = 0;
	double horSpeedX;
	double horSpeedY;
	double horSpeed;
	int lifetime = 0;
	final int TICKS_PER_SECOND = (int)(1000 / RTSComponent.MS_PER_TICK);
	Entity owner;
	int distance = 0;
	int startX, startY;
	private Map map;
	double height;
	int xMap,yMap;
	private int maxDistance;
	
	/**
	 * @param xStart 		x position at spawn in map coordinats
	 * @param yStart		y position at spawn in map coordinats
	 * @param horSpeed		horizontal speed in blocks*s^-1
	 * @param vertSpeed		vertical Speed in blocks*s^-1
	 * @param gravity  		gravity in m/s^2
	 * @param direction		direction in degrees
	 */
	public Projectile(Map map, Entity owner, int xStart, int yStart, double horSpeed, int distance, int direction, int startHeight, int maxDistance){
		this.map = map;
		this.owner = owner;
		xScreen = Util.getScreenX(xStart, yStart) + Tile.getWidth()/2; 
		yScreen = Util.getScreenY(xStart, yStart) + Tile.getHeight()/2;
		startX = xStart;
		startY = yStart;
		height = startHeight;
		xMap = xStart;
		yMap = yStart;
		this.distance = distance;
		this.horSpeed = horSpeed;
		this.maxDistance = maxDistance;
		
		direction %= 360;	
		if(direction <=90 ){														// set correct hor. directions
			horSpeedX = (horSpeed * (Math.cos((direction * Math.PI) / 180)));
			horSpeedY = (horSpeed * (Math.sin((direction * Math.PI) / 180)));
			horSpeedY *=-1;
		}else if(direction <= 180){
			direction = 180 - direction;
			horSpeedX = (horSpeed * (Math.cos((direction * Math.PI) / 180)));
			horSpeedY = (horSpeed * (Math.sin((direction * Math.PI) / 180)));
		}else if(direction <= 270){
			direction = 270 - direction;
			horSpeedX = (horSpeed * (Math.cos((direction * Math.PI) / 180)));
			horSpeedY = (horSpeed * (Math.sin((direction * Math.PI) / 180)));
			horSpeedX *=-1;
		}else if(direction <= 360){
			direction = 360 - direction;
			horSpeedX = (horSpeed * (Math.cos((direction * Math.PI) / 180)));
			horSpeedY = (horSpeed * (Math.sin((direction * Math.PI) / 180)));
			horSpeedY *=-1;
			horSpeedX *=-1;
		}
		System.out.println("horspeedX is: " + horSpeedX + " horspeedY is: " + horSpeedY);
		
	}

	public abstract void updateHeight();
	public abstract void render(Graphics g);
	public abstract void onImpact(Entity e);
	
	public void update(){
		updateHeight();
		
		xScreen += (horSpeedX * Tile.WIDTH)/ TICKS_PER_SECOND;
		yScreen += (horSpeedY * Tile.WIDTH)/ TICKS_PER_SECOND;
		
		System.out.println(xScreen + " " + yScreen);
		
		int x = Util.getMapX((int)xScreen,(int) yScreen);
		int y =  Util.getMapY((int) xScreen, (int) yScreen);
		if(/*x != xMap ||y != yMap*/true){ // if on a new Tile
			xMap = x;
			yMap = y;
			
			if(Util.getDistance(startX, startY, Util.getMapX((int)xScreen, (int)yScreen), Util.getMapY((int)xScreen, (int)yScreen)) >= maxDistance || height < 0){
				stop();
			}
			
			if(!map.getEntities(x, y).isEmpty()){
				System.out.println("entities in range");
				for (Entity e:map.getEntities(x, y)){
					System.out.println("this one: " + e);
					if(e != owner && e.getHeadSpace() > height){
						System.out.println("its not the owner and it is at the correct height");
						stop();
						if(!(owner.screen instanceof MPGameScreen)){
							onImpact(e);
						}
					}else{
						System.out.println("entity is " + e.getHeadSpace() + " heigh and arrow is at " + height);
					}
				}
			}
		}
		
	}
	
	public void stop(){
		map.removeProjectile(this);
	}
}
