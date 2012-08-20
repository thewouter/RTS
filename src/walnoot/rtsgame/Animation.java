package walnoot.rtsgame;

import java.awt.Image;
import java.util.ArrayList;

public class Animation {
	private final int ticksPerFrame;
	private ArrayList<Image> frames = new ArrayList<Image>();
	private int totalTime = 0, currentTime = 0;
	
	public Animation(int ticksPerFrame){
		this.ticksPerFrame = ticksPerFrame;
	}
	
	public void addScene(Image i){
		frames.add(i);
		totalTime += ticksPerFrame;
	}
	
	public void update(){
		currentTime++;
		
		currentTime = currentTime % totalTime;
	}
	
	public Image getImage(){
		return frames.get(currentTime / ticksPerFrame);
	}
	
	public Image getImage(int index){
		return frames.get(index);
	}
}