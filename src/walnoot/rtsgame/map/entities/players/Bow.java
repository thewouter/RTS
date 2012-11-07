package walnoot.rtsgame.map.entities.players;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.LinkedList;

import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.rest.Util;

public class Bow extends Weapon {
	public static int HIT_RANGE = 5;
	LinkedList<Arrow> arrows = new LinkedList<Arrow>();
	private LinkedList<Arrow> toBeRemoved = new LinkedList<Arrow>();
	
	public Bow(Soldier owner) {
		super(owner);
		for(SoldierComponent c: owner.comp){
			if( c instanceof AlertComponent){
				((AlertComponent)c).setRadius(HIT_RANGE);
			}
		}
	}
	
	public void update(){
		for(Arrow a : arrows){
			a.update();
		}
		arrows.removeAll(toBeRemoved);
		toBeRemoved.clear();
	}

	public void activate() {
		arrows.add(new Arrow(this, owner.xPos , owner.yPos , 500.0, 10.0, 9.81, 45 + 2 * 90));
	}
	
	public void removeArrow(Arrow a){
		toBeRemoved .add(a);
	}
	
	public void render(Graphics g){
		for(Arrow a : arrows){
			a.render(g);
		}
	}
	
	private class Arrow{
		double xScreen = 0;
		double yScreen = 0;
		double horSpeedX;
		double horSpeedY;
		double vertSpeed;
		final int maxlifetime;
		int lifetime = 0;
		final int TICKS_PER_SECOND = (int)(1000 / RTSComponent.MS_PER_TICK);
		Bow ownerr;
		
		
		/**
		 * @param xStart 		x position at spawn in map coordinats
		 * @param yStart		y position at spawn in map coordinats
		 * @param horSpeed		horizontal speed in blocks*s^-1
		 * @param vertSpeed		vertical Speed in blocks*s^-1
		 * @param gravity  		gravity in m/s^2
		 * @param direction		direction in degrees
		 */
		public Arrow(Bow ownerr, int xStart, int yStart, double horSpeed, double vertSpeed, double gravity, int direction){
			xScreen = Util.getScreenX(xStart, yStart);// 
			yScreen = Util.getScreenY(xStart, yStart);// + owner.screen.translationY;
			
			System.out.println(xScreen + "   " + yScreen);
			
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
			maxlifetime = (int) (( 2 * vertSpeed ) / gravity) * TICKS_PER_SECOND ;
			this.ownerr = ownerr;
			System.out.println("maxLife: " + maxlifetime + " xspeed: " + horSpeedX + " yspeed: " + horSpeedY); 
		}
		
		public void update(){
			//System.out.println(xScreen + "  " + yScreen);
			xScreen += horSpeedX / TICKS_PER_SECOND;
			yScreen += horSpeedY / TICKS_PER_SECOND;
			if(lifetime >= maxlifetime) stop();
			lifetime++;
			if(owner.map.getEntity(owner.screen.getMapX((int)xScreen , (int)yScreen), owner.screen.getMapY((int)xScreen, (int)yScreen)) != null){
				hit(owner.map.getEntity(owner.screen.getMapX((int)xScreen , (int)yScreen), owner.screen.getMapY((int)xScreen, (int)yScreen)));
			}
			
		}
		
		public void hit(Entity e){
			
		}
		
		public void render(Graphics g){
			g.setColor(Color.BLACK);
			g.drawLine((int)xScreen, (int)yScreen, (int)xScreen + (int)horSpeedX / 32, (int)yScreen + (int)horSpeedY / 32 );
		}
		
		public void stop(){
			ownerr.removeArrow(this);
		}
	}

}
