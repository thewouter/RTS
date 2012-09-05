package walnoot.rtsgame.map.entities;

import java.awt.Graphics;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.screen.GameScreen;

public abstract class Entity implements Cloneable {
	protected final Map map;
	private boolean removed;
	public int xPos;
	public int yPos;
	protected int health;
	public int ID;
	public GameScreen screen;
	
	public Entity(Map map, int xPos, int yPos, int ID, GameScreen screen){
		this.map = map;
		this.xPos = xPos;
		this.yPos = yPos;
		this.ID = ID;
		this.screen = screen;
		health = getMaxHealth();
	}
	
	public void setID(int ID){
		this.ID = ID;
	}
	
	public abstract void update();
	public abstract void render(Graphics g);
	public abstract int getMaxHealth();
	public abstract String getName();
	public abstract int getCosts();
	
	/**
	 * @return extra additional information for save
	 */
	public abstract int getExtraOne();
	
	/**
	 * @param entityClicked Entity that is right-clicked
	 * @param screen
	 * @param input
	 * @return whether this Entity can move after onRightClick()
	 */
	public boolean onRightClick(Entity entityClicked, GameScreen screen, InputHandler input){
		return true;
	}
	public int getHealth(){
		return health;
	}
	
	public void damage(int damage){
		health -= damage;
		if(health <=0) map.removeEntity(this);
	}
	
	public int getxPos(){
		return xPos;
	}
	
	public int getyPos(){
		return yPos;
	}
	
	public int getScreenX(){
		return (xPos - yPos) * (-Tile.WIDTH / 2);
	}
	
	public int getScreenY(){
		return (xPos + yPos) * (Tile.HEIGHT / 2);
	}
	
	public boolean isSolid(int x, int y){
		return false;
	}
	
	public boolean isRemoved(){
		if(health <= 0) return true;
		return removed;
	}
	
	public void remove(){
		removed = true;
	}
	
	public Entity clone() {
        try {
            final Entity result = (Entity) super.clone();
            return result;
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError();
        }
	}
}
