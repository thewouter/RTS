package walnoot.rtsgame.map.entities.players;

public class Sword extends Weapon {
	int damage;

	public Sword(Soldier owner, int damage) {
		super(owner);
		LOAD_TIME = 30;
		this.damage = damage;
	}

	public void activate() {
		owner.target.damage(damage);
	}

}
