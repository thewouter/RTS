package walnoot.rtsgame.popups.entitypopup;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.rest.RTSFont;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.Screen;

public class EntityOptionsPopup extends EntityPopup {
	private final ArrayList<Option> options = new ArrayList<Option>();
	private int width, height = 0;
	int indexSelected = -1;
	int indexHighlighted = -1;
	int screenX = 0, screenY = 0;
	int longestLine = 0;
	private boolean dimensionsSet = false;

	public EntityOptionsPopup(Entity owner, GameScreen screen, Option...options){
		super(owner, screen);
		for(int i = 0; i < options.length; i++){
			this.options.add(options[i]);
		}
	}


	public void render(Graphics g){
		if(!dimensionsSet) setDimensions();
		dimensionsSet = true;
		
		g.setColor(Color.BLACK);
		
		drawBox(g, width, height, screenX ,screenY);
		
		for(int i = 0; i < options.size(); i++){
			if(i == indexSelected){
				options.get(i).renderInColor(g, i, Color.BLUE);
			}else if(i == indexHighlighted){
				options.get(i).renderInColor(g, i, Color.RED);
			}else{
				options.get(i).render(g, i);
			}
		}
	}
	
	private void setDimensions(){
		for(int i = 0; i < options.size(); i++){
			int lineWidth = Screen.font.getLineWidth(options.get(i).getName());
			if(lineWidth > width) width = lineWidth;
		}
		
		height = RTSFont.HEIGHT * (options.size())+EMPTY_SPACE;
	}


	public void update(int mouseX, int mouseY){
		
		screenX = owner.getScreenX() + screen.translationX;
		screenY = owner.getScreenY() + screen.translationY;
		
		if(isInPopup(mouseX, mouseY)){
			indexHighlighted = (mouseY  - 16 - screenY)/RTSFont.HEIGHT;
		}else{
			indexHighlighted = -1;
		}
		
		
	}
	
	public void addOption(Option option){
		options.add(option);
		int lineWidth = Screen.font.getLineWidth(option.getName());
		if(lineWidth > longestLine) {
			longestLine = lineWidth;
			width = longestLine + EMPTY_SPACE;
		}
		dimensionsSet = false;
	}
	
	public boolean isInPopup(int mouseX, int mouseY){
		if(mouseX > screenX && mouseX < screenX + width && mouseY > screenY && mouseY < screenY + height){
			return true;
		}
		return false;
	}
	
	public Option getOption(int index){
		if(index < options.size() && index >= 0) return options.get(index);
		else return null;
	}
	
	public void onLeftClick(int mouseX, int mouseY) {
		if(isInPopup(mouseX, mouseY)){
			indexSelected = (mouseY - 16 - screenY)/RTSFont.HEIGHT;
			if(getOption(indexSelected) != null){	
				getOption(indexSelected).onClick();
			}
			if(owner instanceof MovingEntity)
				((MovingEntity) owner).setSelectedOption(indexSelected);
		}else{
			indexSelected = -1;
		}
	}
}
