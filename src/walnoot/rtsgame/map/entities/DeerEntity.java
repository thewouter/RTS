package walnoot.rtsgame.map.entities;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.Util;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.Screen;

public class DeerEntity extends MovingEntity {
	public static final int WALK_RANGE = 5, WALK_CHANGE = 5, ID = 103;
	
	public DeerEntity(Map map, int xPos, int yPos){
		super(map, xPos, yPos, ID);
	}
	
	public DeerEntity(Map map, int xPos, int yPos, int health){
		super(map,xPos, yPos, ID);
		this.health = health;
	}
	
	public void update(){
		super.update();
		if(!isMoving() && Util.RANDOM.nextInt(1000) < WALK_CHANGE) moveRandomLocation(WALK_RANGE);
	}
	
	public void onRightClick(GameScreen screen, InputHandler input){
		
		//screen.setPopup(new TextPopup(input, this, "allemaal", "zinnen", "hier...", "vier totaal"));
	}
	
	public void render(Graphics g){
		g.setColor(Color.BLACK);
		Screen.font.drawLine(g, getName(), getScreenX(), getScreenY());
		
	}
	
	public int getMaxHealth(){
		return 4;
	}
	
	public String getName(){
		return "Deer";
	}

	protected double getTravelTime(){
		return 200;
	}

	
	public int getSelectedOption() {return -1;}

	public void setSelectedOption(int indexSelected) {}
	
	public int getExtraOne() {
		return 0;
	}

	public boolean isMovable() {
		return false;
	}
}
