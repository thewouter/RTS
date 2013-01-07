package walnoot.rtsgame.multiplayer.host;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.map.entities.SheepEntity;
import walnoot.rtsgame.map.entities.players.PlayerEntity;
import walnoot.rtsgame.map.structures.Structure;
import walnoot.rtsgame.rest.Inventory;
import walnoot.rtsgame.rest.Util;

public class MPMapHost extends Map implements Cloneable{
	public MPHost host;

	public MPMapHost(int mapSize, MPHost host) {
		super(mapSize, null);
		this.host = host;
	}
	
	public void update(int translationX, int translationY, int screenWidth, int screenHeight){
		for(Entity e: getEntities()){
			if(e.xPos + e.yPos + 2 > - ((translationY) / 8) && e.xPos + e.yPos - 1 < - ((translationY - screenHeight - 128)/ 8) && e.xPos - e.yPos - 3 < ((translationX) / 16) && e.xPos - e.yPos + 1 > ((translationX - screenWidth) / 16)) {
				e.update();
			}else if(e instanceof MovingEntity){
				e.update();
			}
		}
			
		double fraction = ((double)amountSheepGroups/((double)SheepEntity.APROX_LIFETIME_IN_TICKS));
		if(Util.RANDOM.nextDouble() <= fraction){
			addSheepGroup(null);
		}
		for(Entity e: toBeRemoved){
			host.entityRemoved(getEntities().indexOf(e));
		}
		getEntities().removeAll(toBeRemoved);
		toBeRemoved.clear();
		getEntities().addAll(toBeAdded);
		toBeAdded.clear();
	}
	
	public void render(Graphics g){
		render(g, new Point(0,0), new Dimension(1000, 1000), 1000, 1000);
	}
	
	public void addEntity(Entity u){
		super.addEntity(u);
		if(host != null) host.entityAdded(u, (Player)u.screen);
	}
	
	public void addEntity(LinkedList<Entity> entities){
		for(Entity e:entities){
			addEntity(e);
		}
	}
	
	public void removeEntity(Entity e){
		super.removeEntity(e);
		if(host != null) host.entityRemoved(e.uniqueNumber);
	}
	
	
}
