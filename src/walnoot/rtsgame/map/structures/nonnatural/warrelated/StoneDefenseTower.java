package walnoot.rtsgame.map.structures.nonnatural.warrelated;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.corba.se.impl.oa.toa.TOA;
import com.sun.corba.se.impl.oa.toa.TOAFactory;

import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.map.Direction;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.players.Bow;
import walnoot.rtsgame.map.entities.players.Soldier;
import walnoot.rtsgame.map.projectiles.Arrow;
import walnoot.rtsgame.map.structures.BasicStructure;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.multiplayer.host.MPMapHost;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.rest.Util;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.MPGameScreen;
import walnoot.rtsgame.screen.SPGameScreen;

public class StoneDefenseTower extends DefenseTower {


	public StoneDefenseTower(Map map, GameScreen screen, int xPos, int yPos, Direction front) {
		super(map, screen, xPos, yPos, 4, 0, ID, front);
	}
	
	public StoneDefenseTower(Map map, GameScreen screen, int xPos, int yPos, int health, Direction front) {
		super(map, screen, xPos, yPos, 4, 0, ID, front);
		this.health = health;
	}

	public int getHeadSpace() {
		return 2;
	}

	public int getMaxHealth() {
		return 300;
	}

	public String getName() {
		return "Primary defense Tower";
	}

	public HashMap<String, Integer> getCosts() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put("gold", 20);
		costs.put("wood", 5);
		costs.put("stone", 25);
		return costs;
	}
}
