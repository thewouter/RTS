package walnoot.rtsgame.screen.menus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.screen.Screen;

/** Een button voor op een MenuScreen */
public class MenuButton {
	private int xOffset, yPos, width, height;
	private MenuScreen screen;
	private String text;
	private boolean selected;
	private Point mouse;
	
	private boolean mouseDown = false;
	
	private static BufferedImage buttonImage;
	private static BufferedImage buttonImageDown;
	
	/**
	 * @param xOffset hoeveel hij van het midden verwijdert is
	 * @param width Als je -1 of lager invoert neemt hij de breedte van de
	 * standaard image
	 * @param height Als je -1 of lager invoert neemt hij de hoogte van de
	 * standaard image
	 */
	public MenuButton(String text, int xOffset, int yPos, int width, int height, MenuScreen s){
		try{
			if(buttonImage == null){
				BufferedImage imgSource = ImageIO.read(this.getClass().getResource("/res/Pictures/button.png"));
				
				buttonImage = imgSource.getSubimage(0, 0, imgSource.getWidth(), imgSource.getHeight() / 2);
				buttonImageDown = imgSource.getSubimage(0, imgSource.getHeight() / 2, imgSource.getWidth(), imgSource.getHeight() / 2);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		this.text = text;
		this.xOffset = xOffset;
		this.yPos = yPos;
		if(width > 0) this.width = width;
		else this.width = buttonImage.getWidth();
		if(height > 0) this.height = height;
		else this.height = buttonImage.getHeight();
		screen = s;
	}
	
	
	public void setMouseLocation(Point m, boolean mouseTapped){
		this.mouse = m;
		if(mouseTapped && isInRect()){
			screen.buttonPressed(this);
		}
	}
	
	private boolean isInRect(){
		
		if(mouse == null) return false;
		
		int x = mouse.x;
		int y = mouse.y;
		
		if(x > getxPos() && x < (getxPos() + width)){
			if(y > yPos && y < (yPos + height)){
				return true;
			}
		}
		
		return false;
	}
	
	public void select(){
		
	}
	
	public void render(Graphics g){
		
		selected = isInRect();
		
		g.setColor(Color.BLACK);
		
		BufferedImage image; //the button
		
		if(mouseDown) image = buttonImageDown;
		else image = buttonImage;
		
		g.drawImage(image, getxPos(), getyPos(), null);
		
		g.setColor(new Color(0x6B6B6B));
		Screen.font.drawLine(g, text, getxPos() + 11, yPos + (height / 2));
		Screen.font.drawLine(g, text, getxPos() + 10, yPos + (height / 2) + 1);
		
		g.setColor(new Color(0x4C4C4C));
		Screen.font.drawLine(g, text, getxPos() + 9, yPos + (height / 2));
		Screen.font.drawLine(g, text, getxPos() + 10, yPos + (height / 2) - 1);
		
		g.setColor(new Color(0x5B5B5B));
		Screen.font.drawLine(g, text, getxPos() + 10, yPos + (height / 2));
		
		g.setColor(new Color(128, 128, 255, 32));
		
		if(selected) g.fillRect(getxPos(), getyPos(), width, height);
	}
	
	public int getxPos(){
		return (((screen.component.getWidth() / RTSComponent.SCALE) / 2) - (width / 2)) + xOffset;
	}
	
	public int getyPos(){
		return yPos;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
}
