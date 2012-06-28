package walnoot.rtsgame.popups;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.map.entities.Entity;
import walnoot.rtsgame.map.entities.MovingEntity;
import walnoot.rtsgame.screen.RTSFont;
import walnoot.rtsgame.screen.Screen;

public class OptionsPopup extends Popup {
	private final ArrayList<Option> options = new ArrayList<Option>();
	private int width, height = 0;
	int indexSelected = -1;
	int indexHighlighted = -1;
	int screenX = 0, screenY = 0;
	int longestLine = 0;
	Popup subPopup;
	private boolean dimensionsSet = false;

	public OptionsPopup(Entity owner, Option...options){
		super(owner);
		
		/*if(owner instanceof MovingEntity){
			if (((MovingEntity) owner).getSelectedOption() != -1){
				indexSelected = ((MovingEntity) owner).getSelectedOption();
			}
		}*/
		
		for(int i = 0; i < options.length; i++){
			this.options.add(options[i]);
		}
		
	}


	public void render(Graphics g){
		if(!dimensionsSet) setDimensions();
		dimensionsSet = true;
		
		g.setColor(Color.BLACK);
		
		drawBox(g, width, height);
		
		for(int i = 0; i < options.size(); i++){
			if(i == indexSelected){
				options.get(i).renderInColor(g, this, i, Color.BLUE);
			}else if(i == indexHighlighted){
				options.get(i).renderInColor(g, this, i, Color.RED);
			}else{
				options.get(i).render(g,this, i);
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


	public void update(int translationX, int translationY, int mouseX, int mouseY){
		
		screenX = getScreenX() + translationX;
		screenY = getScreenY() + translationY;
		
		if(isInPopup(mouseX, mouseY)){
			indexHighlighted = (mouseY  - 16- screenY)/RTSFont.HEIGHT;
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
		if(mouseY > screenY + Images.gui[0][0].getHeight() && mouseY < screenY + height*RTSFont.HEIGHT/ + 2 * Images.gui[0][0].getHeight() && mouseX >= screenX && mouseX < screenX + width + 2* Images.gui[0][0].getWidth()){
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
