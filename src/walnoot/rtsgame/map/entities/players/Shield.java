package walnoot.rtsgame.map.entities.players;

import java.awt.Graphics;
import java.awt.Image;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.map.entities.Entity;

public class Shield extends SoldierComponent {
	private int protection = 0;
	private int health = 0;
	public static int ID = 601;
	private Image image;
	/**
	 * @param owner
	 * @param protection Number between 0 - 100, 0 is no protection , 100 is full
	 */
	public Shield(Soldier owner, int protection, int health) {
		super(owner, ID);
		this.protection = protection;
		this.health = health;
		image = Images.smallButtons[0][0];
		
	}

	public void render(Graphics g) {
		g.drawImage(image,owner.getScreenX(), owner.getScreenY(), null);
	}
	
	public void renderSelected(Graphics g){
		render(g);
	}

	public void update() {
		
	}
	
	public void damage(int damage){
		health -= damage;
		if(health <= 0){
			owner.removeSoldierComponent(this);
		}
	}

	public void activate() {}

	public int getProtection() {
		return protection;
	}

	public void activate(Entity target) {
	}

}
