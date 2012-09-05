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
import walnoot.rtsgame.rest.Util;

public class MPMapHost extends Map implements Cloneable{
	public MPHost host;

	public MPMapHost(int mapSize, MPHost host) {
		super(mapSize);
		this.host = host;
		addEntity(new PlayerEntity(this, null, 10, 10));
	}
	
	public void update(int translationX, int translationY, int screenWidth, int screenHeight){
		for(Entity e: entities){
			if(e.xPos + e.yPos + 2 > - ((translationY) / 8) && e.xPos + e.yPos - 1 < - ((translationY - screenHeight - 128)/ 8) && e.xPos - e.yPos - 3 < ((translationX) / 16) && e.xPos - e.yPos + 1 > ((translationX - screenWidth) / 16)) {
				e.update();
			}else if(e instanceof MovingEntity){
				e.update();
			}
		}
			
		double fraction = ((double)amountSheepGroups/((double)SheepEntity.APROX_LIFETIME_IN_TICKS));
		if(Util.RANDOM.nextDouble() <= fraction){
			addSheepGroup();
		}
		for(Entity e: toBeRemoved){
			host.entityRemoved(entities.indexOf(e));
		}
		for(Entity e: toBeAdded){
			host.entityAdded(e.ID, e.xPos, e.yPos);
		}
		entities.removeAll(toBeRemoved);
		toBeRemoved.clear();
		entities.addAll(toBeAdded);
		toBeAdded.clear();
	}
	
	public void render(Graphics g){
		render(g, new Point(0,0), new Dimension(1000, 1000), 1000, 1000);
	}
	
	public void addEntity(Entity u){
		if(u == null)return;
		if(!(u instanceof Structure) || ((Structure) u ).getSize() == 1){
			if(getEntity(u.xPos, u.yPos) instanceof MovingEntity || getEntity(u.xPos, u.yPos) == null){
				if(getTile(u.getxPos(), u.getyPos()) == null) return;
				if(!getTile(u.getxPos(), u.getyPos()).isSolid()){
					toBeAdded.add(u);
				}
			}
		}else{
			Structure structure = (Structure) u;
			for(int x = 0; x < structure.getSize(); x++){
				for(int y = 0; y < structure.getSize(); y++){
					if(getTile(u.xPos + x, u.yPos + y).isSolid()) return;
					if(getEntity(u.getxPos() + x, u.getyPos() + y) != null) return;
				}
			}
			toBeAdded.add(u);
			
		}
	}
	
	public ArrayList<Entity> getEntities(){
		ArrayList<Entity> copy = new ArrayList<Entity>();
		
		for(Entity e:entities){
			copy.add(e.clone());
		}
		
		return copy;
	}
	
	public void addEntity(LinkedList<Entity> entities){
		for(Entity e:entities){
			addEntity(e);
		}
	}
	
}
