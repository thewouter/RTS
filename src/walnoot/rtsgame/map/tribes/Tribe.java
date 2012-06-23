package walnoot.rtsgame.map.tribes;

import java.awt.Color;

public class Tribe {
	private final String tribeName;
	private final Color color;
	
	public Tribe(String tribeName, Color color){
		this.tribeName = tribeName;
		this.color = color;
	}
	
	public String getName(){
		return tribeName;
	}
	
	public Color getColor(){
		return color;
	}
}
