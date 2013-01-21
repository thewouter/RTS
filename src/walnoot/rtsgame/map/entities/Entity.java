package walnoot.rtsgame.map.entities;

import java.awt.Color;
import java.awt.Graphics;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.Map;
import walnoot.rtsgame.map.tiles.Tile;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.MPGameScreen;

public abstract class Entity implements Cloneable {
	public final Map map;
	private boolean removed;
	public int xPos;
	public int yPos;
	protected int health;
	public int ID;
	public GameScreen screen;
	public int uniqueNumber;
	public boolean isOwnedByPlayer = true;
	
	public static int ENTITY_ID_COUNTER = 1;
	
	public Entity(Map map, int xPos, int yPos, int ID, GameScreen screen){
		this.map = map;
		this.xPos = xPos;
		this.yPos = yPos;
		this.ID = ID;
		this.screen = screen;
		health = getMaxHealth();
		uniqueNumber = ENTITY_ID_COUNTER;
		Entity.ENTITY_ID_COUNTER = ENTITY_ID_COUNTER + 1;
		
	}
	public void setOwned(Boolean b){
		isOwnedByPlayer = b;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}
	
	public abstract void update();
	public abstract void render(Graphics g);
	public abstract int getMaxHealth();
	public abstract String getName();
	public abstract int getCosts();
	public abstract int getHeadSpace();
	
	
	public void renderSelected(Graphics g){
		if(isOwnedByPlayer) g.setColor(screen.getColor());
		else g.setColor(Color.GREEN);
		g.drawOval(getScreenX(), getScreenY(), Tile.getWidth(), Tile.getHeight());
		render(g);
	}
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
		if(screen instanceof MPGameScreen){
			((MPGameScreen) screen).damageEntity(this, damage);
		}
		
		health -= damage;
		if(health <=0) map.removeEntity(this);
		onDestroying();
	}
	
	public void onDestroying() {}
	
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

	public void damageFromHost(int damage) {
		health -= damage;
		if(health <=0) map.removeEntity(this);
	}
	
	public boolean isOwnedByPlayer(){
		return isOwnedByPlayer;
	}
	
	public String getHealthInString() {
		return health + "";
	}
	
	public boolean isWalkable(int x, int y, Entity toPass) {
		return false;
	}
	
	
}
