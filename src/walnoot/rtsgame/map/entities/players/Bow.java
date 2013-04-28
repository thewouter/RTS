package walnoot.rtsgame.map.entities.players;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.LinkedList;

import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.projectiles.Arrow;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;

public class Bow extends Weapon {
	private static int ID = 633;
	
	public Bow(Soldier owner) {
		super(owner, ID);
		MIN_HIT_RANGE = 10;
		MAX_HIT_RANGE = 12;
		LOAD_TIME = 150;
		owner.addSoldierComponent(new AlertComponent(owner, MAX_HIT_RANGE));
	}
	
	public void activate() {
		owner.map.shootArrow(owner, owner.target, true, 15, MAX_HIT_RANGE);
	}
}
