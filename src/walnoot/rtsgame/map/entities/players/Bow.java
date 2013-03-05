package walnoot.rtsgame.map.entities.players;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.LinkedList;

import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Arrow;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;

public class Bow extends Weapon {
	private static int ID = 603;
	
	public Bow(Soldier owner) {
		super(owner, ID);
		MIN_HIT_RANGE = 4;
		MAX_HIT_RANGE = 6;
		LOAD_TIME = 60;
		owner.addSoldierComponent(new AlertComponent(owner, MAX_HIT_RANGE));
	}
	
	public void update(){
		super.update();
	}

	public void activate() {
		owner.map.shootArrow(owner, owner.xPos - owner.getHeadSpace(), owner.yPos - owner.getHeadSpace() , 500.0, MAX_HIT_RANGE, Util.getDirectionInDegrees(owner, owner.target, true));
	}
}
